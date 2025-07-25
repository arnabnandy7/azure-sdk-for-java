// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.resourcemanager.network;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.HttpClient;
import com.azure.core.http.HttpPipeline;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.core.http.policy.HttpPipelinePolicy;
import com.azure.core.http.policy.RetryPolicy;
import com.azure.resourcemanager.keyvault.KeyVaultManager;
import com.azure.resourcemanager.msi.MSIManager;
import com.azure.core.management.profile.AzureProfile;
import com.azure.resourcemanager.resources.ResourceManager;
import com.azure.resourcemanager.resources.fluentcore.utils.HttpPipelineProvider;
import com.azure.resourcemanager.resources.fluentcore.utils.SdkContext;
import com.azure.resourcemanager.test.ResourceManagerTestBase;
import com.azure.resourcemanager.test.utils.TestDelayProvider;
import com.azure.resourcemanager.test.utils.TestIdentifierProvider;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class NetworkManagementTest extends ResourceManagerTestBase {
    protected ResourceManager resourceManager;
    protected NetworkManager networkManager;
    protected KeyVaultManager keyVaultManager;
    protected MSIManager msiManager;
    protected String rgName = "";

    @Override
    protected HttpPipeline buildHttpPipeline(
        TokenCredential credential,
        AzureProfile profile,
        HttpLogOptions httpLogOptions,
        List<HttpPipelinePolicy> policies,
        HttpClient httpClient) {
        return HttpPipelineProvider.buildHttpPipeline(
            credential,
            profile,
            null,
            httpLogOptions,
            null,
            new RetryPolicy("Retry-After", ChronoUnit.SECONDS),
            policies,
            httpClient);
    }

    @Override
    protected void initializeClients(HttpPipeline httpPipeline, AzureProfile profile) {
        SdkContext.setDelayProvider(new TestDelayProvider(!isPlaybackMode()));
        SdkContext sdkContext = new SdkContext();
        sdkContext.setIdentifierFunction(name -> new TestIdentifierProvider(testResourceNamer));
        rgName = generateRandomResourceName("javanwmrg", 15);
        resourceManager =
            ResourceManager.authenticate(httpPipeline, profile).withDefaultSubscription();
        networkManager = NetworkManager.authenticate(httpPipeline, profile, sdkContext);
        keyVaultManager = KeyVaultManager.authenticate(httpPipeline, profile);
        msiManager = MSIManager.authenticate(httpPipeline, profile);
    }

    @Override
    protected void cleanUpResources() {
        resourceManager.resourceGroups().beginDeleteByName(rgName);
    }
}
