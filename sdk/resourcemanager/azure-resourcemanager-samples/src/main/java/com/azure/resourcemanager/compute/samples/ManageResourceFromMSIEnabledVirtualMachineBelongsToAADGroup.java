// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.resourcemanager.compute.samples;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.Azure;
import com.azure.resourcemanager.compute.models.CachingTypes;
import com.azure.resourcemanager.compute.models.KnownLinuxVirtualMachineImage;
import com.azure.resourcemanager.compute.models.VirtualMachine;
import com.azure.resourcemanager.compute.models.VirtualMachineSizeTypes;
import com.azure.resourcemanager.authorization.models.ActiveDirectoryGroup;
import com.azure.resourcemanager.authorization.models.BuiltInRole;
import com.azure.resourcemanager.resources.models.ResourceGroup;
import com.azure.resourcemanager.resources.fluentcore.arm.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.resourcemanager.resources.fluentcore.utils.SdkContext;
import com.azure.resourcemanager.samples.Utils;
import com.azure.resourcemanager.storage.models.StorageAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Azure Compute sample for managing virtual machines -
 *   - Create a AAD security group
 *   - Assign AAD security group Contributor role at a resource group
 *   - Create a virtual machine with MSI enabled
 *   - Add virtual machine MSI service principal to the AAD group
 *   - Set custom script in the virtual machine that
 *          - install az cli in the virtual machine
 *          - uses az cli MSI credentials to create a storage account
 *   - Get storage account created through MSI credentials.
 */
public final class ManageResourceFromMSIEnabledVirtualMachineBelongsToAADGroup {
    /**
     * Main function which runs the actual sample.
     *
     * @param azure instance of the azure client
     * @return true if sample runs successfully
     */
    public static boolean runSample(Azure azure) {
        String groupName = azure.sdkContext().randomResourceName("group", 15);
        final String rgName = azure.sdkContext().randomResourceName("rgCOMV", 15);
        String roleAssignmentName = azure.sdkContext().randomUuid();
        final String linuxVMName = azure.sdkContext().randomResourceName("VM1", 15);
        final String pipName = azure.sdkContext().randomResourceName("pip1", 15);
        final String userName = "tirekicker";
        final String password = Utils.password();
        final Region region = Region.US_SOUTH_CENTRAL;

        final String installScript = "https://raw.githubusercontent.com/Azure/azure-libraries-for-java/master/azure-samples/src/main/resources/create_resources_with_msi.sh";
        String installCommand = "bash create_resources_with_msi.sh {stgName} {rgName} {location}";
        List<String> fileUris = new ArrayList<>();
        fileUris.add(installScript);

        try {

            //=============================================================
            // Create a AAD security group

            System.out.println("Creating a AAD security group");

            ActiveDirectoryGroup activeDirectoryGroup = azure.accessManagement()
                    .activeDirectoryGroups()
                    .define(groupName)
                        .withEmailAlias(groupName)
                        .create();

            //=============================================================
            // Assign AAD security group Contributor role at a resource group

            ResourceGroup resourceGroup = azure.resourceGroups()
                    .define(rgName)
                        .withRegion(region)
                        .create();

            SdkContext.sleep(45 * 1000);

            System.out.println("Assigning AAD security group Contributor role to the resource group");

            azure.accessManagement()
                    .roleAssignments()
                    .define(roleAssignmentName)
                        .forGroup(activeDirectoryGroup)
                        .withBuiltInRole(BuiltInRole.CONTRIBUTOR)
                        .withResourceGroupScope(resourceGroup)
                        .create();

            System.out.println("Assigned AAD security group Contributor role to the resource group");

            //=============================================================
            // Create a Linux VM with MSI enabled

            System.out.println("Creating a Linux VM with MSI enabled");

            VirtualMachine virtualMachine = azure.virtualMachines()
                    .define(linuxVMName)
                        .withRegion(region)
                        .withNewResourceGroup(rgName)
                        .withNewPrimaryNetwork("10.0.0.0/28")
                        .withPrimaryPrivateIPAddressDynamic()
                        .withNewPrimaryPublicIPAddress(pipName)
                        .withPopularLinuxImage(KnownLinuxVirtualMachineImage.UBUNTU_SERVER_16_04_LTS)
                        .withRootUsername(userName)
                        .withRootPassword(password)
                        .withSize(VirtualMachineSizeTypes.STANDARD_DS2_V2)
                        .withOSDiskCaching(CachingTypes.READ_WRITE)
                        .withSystemAssignedManagedServiceIdentity()
                        .create();

            System.out.println("Created virtual machine with MSI enabled");
            Utils.print(virtualMachine);

            //=============================================================
            // Add virtual machine MSI service principal to the AAD group

            System.out.println("Adding virtual machine MSI service principal to the AAD group");

            activeDirectoryGroup.update()
                    .withMember(virtualMachine.systemAssignedManagedServiceIdentityPrincipalId())
                    .apply();

            System.out.println("Added virtual machine MSI service principal to the AAD group");

            System.out.println("Waiting 15 minutes to MSI extension in the VM to refresh the token");

            SdkContext.sleep(10 * 60 * 1000);

            // Prepare custom script t install az cli that uses MSI to create a storage account
            //
            final String stgName = azure.sdkContext().randomResourceName("st44", 15);
            installCommand = installCommand.replace("{stgName}", stgName)
                    .replace("{rgName}", rgName)
                    .replace("{location}", region.name());

            // Update the VM by installing custom script extension.
            //
            System.out.println("Installing custom script extension to configure az cli in the virtual machine");
            System.out.println("az cli will use MSI credentials to create storage account");

            virtualMachine
                    .update()
                        .defineNewExtension("CustomScriptForLinux")
                            .withPublisher("Microsoft.OSTCExtensions")
                            .withType("CustomScriptForLinux")
                            .withVersion("1.4")
                            .withMinorVersionAutoUpgrade()
                            .withPublicSetting("fileUris", fileUris)
                            .withPublicSetting("commandToExecute", installCommand)
                            .attach()
                        .apply();

            // Retrieve the storage account created by az cli using MSI credentials
            //
            StorageAccount storageAccount = azure.storageAccounts()
                    .getByResourceGroup(rgName, stgName);

            System.out.println("Storage account created by az cli using MSI credential");
            Utils.print(storageAccount);
            return true;
        } finally {
            try {
                System.out.println("Deleting Resource Group: " + rgName);
                azure.resourceGroups().beginDeleteByName(rgName);
            } catch (NullPointerException npe) {
                System.out.println("Did not create any resources in Azure. No clean up is necessary");
            } catch (Exception g) {
                g.printStackTrace();
            }
        }
    }

    /**
     * Main entry point.
     * @param args the parameters
     */
    public static void main(String[] args) {
        try {
            //=============================================================
            // Authenticate

            final AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
            final TokenCredential credential = new DefaultAzureCredentialBuilder()
                .build();

            Azure azure = Azure
                .configure()
                .withLogLevel(HttpLogDetailLevel.BASIC)
                .authenticate(credential, profile)
                .withDefaultSubscription();

            // Print selected subscription
            System.out.println("Selected subscription: " + azure.subscriptionId());

            runSample(azure);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private ManageResourceFromMSIEnabledVirtualMachineBelongsToAADGroup() {
    }
}
