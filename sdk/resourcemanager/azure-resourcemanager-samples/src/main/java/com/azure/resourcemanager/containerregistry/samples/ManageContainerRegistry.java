// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.resourcemanager.containerregistry.samples;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.Azure;
import com.azure.resourcemanager.containerregistry.models.AccessKeyType;
import com.azure.resourcemanager.containerregistry.models.Registry;
import com.azure.resourcemanager.containerregistry.models.RegistryCredentials;
import com.azure.resourcemanager.resources.fluentcore.arm.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.resourcemanager.samples.DockerUtils;
import com.azure.resourcemanager.samples.Utils;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Azure Container Registry sample for managing container registry.
 *    - Create an Azure Container Registry to be used for holding the Docker images
 *    - If a local Docker engine cannot be found, create a Linux virtual machine that will host a Docker engine
 *        to be used for this sample
 *    - Use Docker Java to create a Docker client that will push/pull an image to/from Azure Container Registry
 *    - Pull a test image from the public Docker repo (hello-world:latest) to be used as a sample for pushing/pulling
 *        to/from an Azure Container Registry
 *    - Create a new Docker container from an image that was pulled from Azure Container Registry
 */
public class ManageContainerRegistry {

    /**
     * Main function which runs the actual sample.
     *
     * @param azure instance of the azure client
     * @return true if sample runs successfully
     */
    public static boolean runSample(Azure azure) throws IOException {
        final String rgName = azure.sdkContext().randomResourceName("rgACR", 15);
        final String acrName = azure.sdkContext().randomResourceName("acrsample", 20);
        final Region region = Region.US_EAST;
        final String dockerImageName = "hello-world";
        final String dockerImageTag = "latest";
        final String dockerContainerName = "sample-hello";

        try {
            //=============================================================
            // Create an Azure Container Registry to store and manage private Docker container images

            System.out.println("Creating an Azure Container Registry");

            Date t1 = new Date();

            Registry azureRegistry = azure.containerRegistries().define(acrName)
                    .withRegion(region)
                    .withNewResourceGroup(rgName)
                    .withBasicSku()
                    .withRegistryNameAsAdminUser()
                    .create();

            Date t2 = new Date();
            System.out.println("Created Azure Container Registry: (took " + ((t2.getTime() - t1.getTime()) / 1000) + " seconds) " + azureRegistry.id());
            Utils.print(azureRegistry);


            //=============================================================
            // Create a Docker client that will be used to push/pull images to/from the Azure Container Registry

            RegistryCredentials acrCredentials = azureRegistry.getCredentials();
            DockerClient dockerClient = DockerUtils.createDockerClient(azure, rgName, region,
                    azureRegistry.loginServerUrl(), acrCredentials.username(), acrCredentials.accessKeys().get(AccessKeyType.PRIMARY));

            //=============================================================
            // Pull a temp image from public Docker repo and create a temporary container from that image
            // These steps can be replaced and instead build a custom image using a Dockerfile and the app's JAR

            dockerClient.pullImageCmd(dockerImageName)
                    .withTag(dockerImageTag)
                    .withAuthConfig(new AuthConfig()) // anonymous
                    .exec(new PullImageResultCallback())
                    .awaitSuccess();
            System.out.println("List local Docker images:");
            List<Image> images = dockerClient.listImagesCmd().withShowAll(true).exec();
            for (Image image : images) {
                System.out.format("\tFound Docker image %s (%s)%n", image.getRepoTags()[0], image.getId());
            }

            CreateContainerResponse dockerContainerInstance = dockerClient.createContainerCmd(dockerImageName + ":" + dockerImageTag)
                    .withName(dockerContainerName)
                    .withCmd("/hello")
                    .exec();
            System.out.println("List Docker containers:");
            List<Container> dockerContainers = dockerClient.listContainersCmd()
                    .withShowAll(true)
                    .exec();
            for (Container container : dockerContainers) {
                System.out.format("\tFound Docker container %s (%s)%n", container.getImage(), container.getId());
            }

            //=============================================================
            // Commit the new container

            String privateRepoUrl = azureRegistry.loginServerUrl() + "/samples/" + dockerContainerName;
            dockerClient.commitCmd(dockerContainerInstance.getId())
                .withRepository(privateRepoUrl)
                .withTag("latest").exec();

            // We can now remove the temporary container instance
            dockerClient.removeContainerCmd(dockerContainerInstance.getId())
                    .withForce(true)
                    .exec();

            //=============================================================
            // Push the new Docker image to the Azure Container Registry

            dockerClient.pushImageCmd(privateRepoUrl)
                    .exec(new PushImageResultCallback()).awaitSuccess();

            // Remove the temp image from the local Docker host
            try {
                dockerClient.removeImageCmd(dockerImageName + ":" + dockerImageTag).withForce(true).exec();
            } catch (NotFoundException e) {
                // just ignore if not exist
            }

            //=============================================================
            // Verify that the image we saved in the Azure Container registry can be pulled and instantiated locally

            dockerClient.pullImageCmd(privateRepoUrl)
                    .withAuthConfig(dockerClient.authConfig())
                    .exec(new PullImageResultCallback()).awaitSuccess();
            System.out.println("List local Docker images after pulling sample image from the Azure Container Registry:");
            images = dockerClient.listImagesCmd()
                    .withShowAll(true)
                    .exec();
            for (Image image : images) {
                System.out.format("\tFound Docker image %s (%s)%n", image.getRepoTags()[0], image.getId());
            }
            dockerClient.createContainerCmd(privateRepoUrl)
                .withName(dockerContainerName + "-private")
                .withCmd("/hello").exec();
            System.out.println("List Docker containers after instantiating container from the Azure Container Registry sample image:");
            dockerContainers = dockerClient.listContainersCmd()
                    .withShowAll(true)
                    .exec();
            for (Container container : dockerContainers) {
                System.out.format("\tFound Docker container %s (%s)%n", container.getImage(), container.getId());
            }

            return true;
        } finally {
            try {
                System.out.println("Deleting Resource Group: " + rgName);
                azure.resourceGroups().beginDeleteByName(rgName);
                System.out.println("Deleted Resource Group: " + rgName);
            } catch (NullPointerException npe) {
                System.out.println("Did not create any resources in Azure. No clean up is necessary");
            } catch (Exception g) {
                g.printStackTrace();
            }
        }
    }

    /**
     * Main entry point.
     *
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
