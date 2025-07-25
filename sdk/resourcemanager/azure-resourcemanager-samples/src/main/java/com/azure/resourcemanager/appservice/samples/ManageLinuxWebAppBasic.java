// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.resourcemanager.appservice.samples;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.Azure;
import com.azure.resourcemanager.appservice.models.AppServicePlan;
import com.azure.resourcemanager.appservice.models.JavaVersion;
import com.azure.resourcemanager.appservice.models.PricingTier;
import com.azure.resourcemanager.appservice.models.RuntimeStack;
import com.azure.resourcemanager.appservice.models.WebApp;
import com.azure.resourcemanager.appservice.models.WebAppBasic;
import com.azure.resourcemanager.appservice.models.WebContainer;
import com.azure.resourcemanager.resources.fluentcore.arm.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.resourcemanager.samples.Utils;
import com.azure.core.http.policy.HttpLogDetailLevel;

/**
 * Azure App Service basic sample for managing web apps.
 *  - Create 3 linux web apps under the same new app service plan:
 *    - 1, 2 are in the same resource group, 3 in a different one
 *    - Stop and start 1, restart 2
 *    - Add Java support to app 3
 *  - List web apps
 *  - Delete a web app
 */
public final class ManageLinuxWebAppBasic {

    /**
     * Main function which runs the actual sample.
     * @param azure instance of the azure client
     * @return true if sample runs successfully
     */
    public static boolean runSample(Azure azure) {
        // New resources
        final String app1Name       = azure.sdkContext().randomResourceName("webapp1-", 20);
        final String app2Name       = azure.sdkContext().randomResourceName("webapp2-", 20);
        final String app3Name       = azure.sdkContext().randomResourceName("webapp3-", 20);
        final String rg1Name        = azure.sdkContext().randomResourceName("rg1NEMV_", 24);
        final String rg2Name        = azure.sdkContext().randomResourceName("rg2NEMV_", 24);

        try {


            //============================================================
            // Create a web app with a new app service plan

            System.out.println("Creating web app " + app1Name + " in resource group " + rg1Name + "...");

            WebApp app1 = azure.webApps()
                    .define(app1Name)
                    .withRegion(Region.US_WEST)
                    .withNewResourceGroup(rg1Name)
                    .withNewLinuxPlan(PricingTier.STANDARD_S1)
                    .withBuiltInImage(RuntimeStack.NODEJS_6_9)
                    .create();

            System.out.println("Created web app " + app1.name());
            Utils.print(app1);

            //============================================================
            // Create a second web app with the same app service plan

            System.out.println("Creating another web app " + app2Name + " in resource group " + rg1Name + "...");
            AppServicePlan plan = azure.appServicePlans().getById(app1.appServicePlanId());
            WebApp app2 = azure.webApps()
                    .define(app2Name)
                    .withExistingLinuxPlan(plan)
                    .withExistingResourceGroup(rg1Name)
                    .withBuiltInImage(RuntimeStack.NODEJS_6_9)
                    .create();

            System.out.println("Created web app " + app2.name());
            Utils.print(app2);

            //============================================================
            // Create a third web app with the same app service plan, but
            // in a different resource group

            System.out.println("Creating another web app " + app3Name + " in resource group " + rg2Name + "...");
            WebApp app3 = azure.webApps()
                    .define(app3Name)
                    .withExistingLinuxPlan(plan)
                    .withNewResourceGroup(rg2Name)
                    .withBuiltInImage(RuntimeStack.NODEJS_6_9)
                    .create();

            System.out.println("Created web app " + app3.name());
            Utils.print(app3);

            //============================================================
            // stop and start app1, restart app 2
            System.out.println("Stopping web app " + app1.name());
            app1.stop();
            System.out.println("Stopped web app " + app1.name());
            Utils.print(app1);
            System.out.println("Starting web app " + app1.name());
            app1.start();
            System.out.println("Started web app " + app1.name());
            Utils.print(app1);
            System.out.println("Restarting web app " + app2.name());
            app2.restart();
            System.out.println("Restarted web app " + app2.name());
            Utils.print(app2);

            //============================================================
            // Configure app 3 to have Java 8 enabled
            System.out.println("Adding Java support to web app " + app3Name + "...");
            app3.update()
                    .withJavaVersion(JavaVersion.JAVA_8_NEWEST)
                    .withWebContainer(WebContainer.TOMCAT_8_0_NEWEST)
                    .apply();
            System.out.println("Java supported on web app " + app3Name + "...");

            //=============================================================
            // List web apps

            System.out.println("Printing list of web apps in resource group " + rg1Name + "...");

            for (WebAppBasic webApp : azure.webApps().listByResourceGroup(rg1Name)) {
                Utils.print(webApp);
            }

            System.out.println("Printing list of web apps in resource group " + rg2Name + "...");

            for (WebAppBasic webApp : azure.webApps().listByResourceGroup(rg2Name)) {
                Utils.print(webApp);
            }

            //=============================================================
            // Delete a web app

            System.out.println("Deleting web app " + app1Name + "...");
            azure.webApps().deleteByResourceGroup(rg1Name, app1Name);
            System.out.println("Deleted web app " + app1Name + "...");

            System.out.println("Printing list of web apps in resource group " + rg1Name + " again...");
            for (WebAppBasic webApp : azure.webApps().listByResourceGroup(rg1Name)) {
                Utils.print(webApp);
            }
            return true;
        } finally {
            try {
                System.out.println("Deleting Resource Group: " + rg1Name);
                azure.resourceGroups().beginDeleteByName(rg1Name);
                System.out.println("Deleted Resource Group: " + rg1Name);
                System.out.println("Deleting Resource Group: " + rg2Name);
                azure.resourceGroups().beginDeleteByName(rg2Name);
                System.out.println("Deleted Resource Group: " + rg2Name);
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
}
