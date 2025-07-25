// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.keyvault.fluent;

import com.azure.core.annotation.ExpectedResponses;
import com.azure.core.annotation.Get;
import com.azure.core.annotation.Headers;
import com.azure.core.annotation.Host;
import com.azure.core.annotation.HostParam;
import com.azure.core.annotation.PathParam;
import com.azure.core.annotation.QueryParam;
import com.azure.core.annotation.ReturnType;
import com.azure.core.annotation.ServiceInterface;
import com.azure.core.annotation.ServiceMethod;
import com.azure.core.annotation.UnexpectedResponseExceptionType;
import com.azure.core.http.rest.Response;
import com.azure.core.http.rest.RestProxy;
import com.azure.core.management.exception.ManagementException;
import com.azure.core.util.Context;
import com.azure.core.util.FluxUtil;
import com.azure.core.util.logging.ClientLogger;
import com.azure.resourcemanager.keyvault.fluent.inner.PrivateLinkResourceListResultInner;
import reactor.core.publisher.Mono;

/** An instance of this class provides access to all the operations defined in PrivateLinkResources. */
public final class PrivateLinkResourcesClient {
    private final ClientLogger logger = new ClientLogger(PrivateLinkResourcesClient.class);

    /** The proxy service used to perform REST calls. */
    private final PrivateLinkResourcesService service;

    /** The service client containing this operation class. */
    private final KeyVaultManagementClient client;

    /**
     * Initializes an instance of PrivateLinkResourcesClient.
     *
     * @param client the instance of the service client containing this operation class.
     */
    public PrivateLinkResourcesClient(KeyVaultManagementClient client) {
        this.service =
            RestProxy
                .create(PrivateLinkResourcesService.class, client.getHttpPipeline(), client.getSerializerAdapter());
        this.client = client;
    }

    /**
     * The interface defining all the services for KeyVaultManagementClientPrivateLinkResources to be used by the proxy
     * service to perform REST calls.
     */
    @Host("{$host}")
    @ServiceInterface(name = "KeyVaultManagementCl")
    private interface PrivateLinkResourcesService {
        @Headers({"Accept: application/json", "Content-Type: application/json"})
        @Get(
            "/subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.KeyVault/vaults"
                + "/{vaultName}/privateLinkResources")
        @ExpectedResponses({200})
        @UnexpectedResponseExceptionType(ManagementException.class)
        Mono<Response<PrivateLinkResourceListResultInner>> listByVault(
            @HostParam("$host") String endpoint,
            @PathParam("subscriptionId") String subscriptionId,
            @PathParam("resourceGroupName") String resourceGroupName,
            @PathParam("vaultName") String vaultName,
            @QueryParam("api-version") String apiVersion,
            Context context);
    }

    /**
     * Gets the private link resources supported for the key vault.
     *
     * @param resourceGroupName Name of the resource group that contains the key vault.
     * @param vaultName The name of the key vault.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the private link resources supported for the key vault.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<PrivateLinkResourceListResultInner>> listByVaultWithResponseAsync(
        String resourceGroupName, String vaultName) {
        if (this.client.getEndpoint() == null) {
            return Mono
                .error(
                    new IllegalArgumentException(
                        "Parameter this.client.getEndpoint() is required and cannot be null."));
        }
        if (this.client.getSubscriptionId() == null) {
            return Mono
                .error(
                    new IllegalArgumentException(
                        "Parameter this.client.getSubscriptionId() is required and cannot be null."));
        }
        if (resourceGroupName == null) {
            return Mono
                .error(new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null."));
        }
        if (vaultName == null) {
            return Mono.error(new IllegalArgumentException("Parameter vaultName is required and cannot be null."));
        }
        return FluxUtil
            .withContext(
                context ->
                    service
                        .listByVault(
                            this.client.getEndpoint(),
                            this.client.getSubscriptionId(),
                            resourceGroupName,
                            vaultName,
                            this.client.getApiVersion(),
                            context))
            .subscriberContext(context -> context.putAll(FluxUtil.toReactorContext(this.client.getContext())));
    }

    /**
     * Gets the private link resources supported for the key vault.
     *
     * @param resourceGroupName Name of the resource group that contains the key vault.
     * @param vaultName The name of the key vault.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the private link resources supported for the key vault.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<PrivateLinkResourceListResultInner>> listByVaultWithResponseAsync(
        String resourceGroupName, String vaultName, Context context) {
        if (this.client.getEndpoint() == null) {
            return Mono
                .error(
                    new IllegalArgumentException(
                        "Parameter this.client.getEndpoint() is required and cannot be null."));
        }
        if (this.client.getSubscriptionId() == null) {
            return Mono
                .error(
                    new IllegalArgumentException(
                        "Parameter this.client.getSubscriptionId() is required and cannot be null."));
        }
        if (resourceGroupName == null) {
            return Mono
                .error(new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null."));
        }
        if (vaultName == null) {
            return Mono.error(new IllegalArgumentException("Parameter vaultName is required and cannot be null."));
        }
        context = this.client.mergeContext(context);
        return service
            .listByVault(
                this.client.getEndpoint(),
                this.client.getSubscriptionId(),
                resourceGroupName,
                vaultName,
                this.client.getApiVersion(),
                context);
    }

    /**
     * Gets the private link resources supported for the key vault.
     *
     * @param resourceGroupName Name of the resource group that contains the key vault.
     * @param vaultName The name of the key vault.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the private link resources supported for the key vault.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<PrivateLinkResourceListResultInner> listByVaultAsync(String resourceGroupName, String vaultName) {
        return listByVaultWithResponseAsync(resourceGroupName, vaultName)
            .flatMap(
                (Response<PrivateLinkResourceListResultInner> res) -> {
                    if (res.getValue() != null) {
                        return Mono.just(res.getValue());
                    } else {
                        return Mono.empty();
                    }
                });
    }

    /**
     * Gets the private link resources supported for the key vault.
     *
     * @param resourceGroupName Name of the resource group that contains the key vault.
     * @param vaultName The name of the key vault.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the private link resources supported for the key vault.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<PrivateLinkResourceListResultInner> listByVaultAsync(
        String resourceGroupName, String vaultName, Context context) {
        return listByVaultWithResponseAsync(resourceGroupName, vaultName, context)
            .flatMap(
                (Response<PrivateLinkResourceListResultInner> res) -> {
                    if (res.getValue() != null) {
                        return Mono.just(res.getValue());
                    } else {
                        return Mono.empty();
                    }
                });
    }

    /**
     * Gets the private link resources supported for the key vault.
     *
     * @param resourceGroupName Name of the resource group that contains the key vault.
     * @param vaultName The name of the key vault.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the private link resources supported for the key vault.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public PrivateLinkResourceListResultInner listByVault(String resourceGroupName, String vaultName) {
        return listByVaultAsync(resourceGroupName, vaultName).block();
    }

    /**
     * Gets the private link resources supported for the key vault.
     *
     * @param resourceGroupName Name of the resource group that contains the key vault.
     * @param vaultName The name of the key vault.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the private link resources supported for the key vault.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public PrivateLinkResourceListResultInner listByVault(String resourceGroupName, String vaultName, Context context) {
        return listByVaultAsync(resourceGroupName, vaultName, context).block();
    }
}
