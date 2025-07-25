// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.network.fluent;

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
import com.azure.core.http.rest.PagedFlux;
import com.azure.core.http.rest.PagedIterable;
import com.azure.core.http.rest.PagedResponse;
import com.azure.core.http.rest.PagedResponseBase;
import com.azure.core.http.rest.Response;
import com.azure.core.http.rest.RestProxy;
import com.azure.core.management.exception.ManagementException;
import com.azure.core.util.Context;
import com.azure.core.util.FluxUtil;
import com.azure.core.util.logging.ClientLogger;
import com.azure.resourcemanager.network.fluent.inner.ExpressRouteLinkInner;
import com.azure.resourcemanager.network.fluent.inner.ExpressRouteLinkListResultInner;
import reactor.core.publisher.Mono;

/** An instance of this class provides access to all the operations defined in ExpressRouteLinks. */
public final class ExpressRouteLinksClient {
    private final ClientLogger logger = new ClientLogger(ExpressRouteLinksClient.class);

    /** The proxy service used to perform REST calls. */
    private final ExpressRouteLinksService service;

    /** The service client containing this operation class. */
    private final NetworkManagementClient client;

    /**
     * Initializes an instance of ExpressRouteLinksClient.
     *
     * @param client the instance of the service client containing this operation class.
     */
    public ExpressRouteLinksClient(NetworkManagementClient client) {
        this.service =
            RestProxy.create(ExpressRouteLinksService.class, client.getHttpPipeline(), client.getSerializerAdapter());
        this.client = client;
    }

    /**
     * The interface defining all the services for NetworkManagementClientExpressRouteLinks to be used by the proxy
     * service to perform REST calls.
     */
    @Host("{$host}")
    @ServiceInterface(name = "NetworkManagementCli")
    private interface ExpressRouteLinksService {
        @Headers({"Accept: application/json", "Content-Type: application/json"})
        @Get(
            "/subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.Network"
                + "/ExpressRoutePorts/{expressRoutePortName}/links/{linkName}")
        @ExpectedResponses({200})
        @UnexpectedResponseExceptionType(ManagementException.class)
        Mono<Response<ExpressRouteLinkInner>> get(
            @HostParam("$host") String endpoint,
            @PathParam("subscriptionId") String subscriptionId,
            @QueryParam("api-version") String apiVersion,
            @PathParam("resourceGroupName") String resourceGroupName,
            @PathParam("expressRoutePortName") String expressRoutePortName,
            @PathParam("linkName") String linkName,
            Context context);

        @Headers({"Accept: application/json", "Content-Type: application/json"})
        @Get(
            "/subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.Network"
                + "/ExpressRoutePorts/{expressRoutePortName}/links")
        @ExpectedResponses({200})
        @UnexpectedResponseExceptionType(ManagementException.class)
        Mono<Response<ExpressRouteLinkListResultInner>> list(
            @HostParam("$host") String endpoint,
            @PathParam("subscriptionId") String subscriptionId,
            @QueryParam("api-version") String apiVersion,
            @PathParam("resourceGroupName") String resourceGroupName,
            @PathParam("expressRoutePortName") String expressRoutePortName,
            Context context);

        @Headers({"Accept: application/json", "Content-Type: application/json"})
        @Get("{nextLink}")
        @ExpectedResponses({200})
        @UnexpectedResponseExceptionType(ManagementException.class)
        Mono<Response<ExpressRouteLinkListResultInner>> listNext(
            @PathParam(value = "nextLink", encoded = true) String nextLink, Context context);
    }

    /**
     * Retrieves the specified ExpressRouteLink resource.
     *
     * @param resourceGroupName The name of the resource group.
     * @param expressRoutePortName The name of the ExpressRoutePort resource.
     * @param linkName The name of the ExpressRouteLink resource.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return expressRouteLink child resource definition.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<ExpressRouteLinkInner>> getWithResponseAsync(
        String resourceGroupName, String expressRoutePortName, String linkName) {
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
        if (expressRoutePortName == null) {
            return Mono
                .error(new IllegalArgumentException("Parameter expressRoutePortName is required and cannot be null."));
        }
        if (linkName == null) {
            return Mono.error(new IllegalArgumentException("Parameter linkName is required and cannot be null."));
        }
        final String apiVersion = "2020-05-01";
        return FluxUtil
            .withContext(
                context ->
                    service
                        .get(
                            this.client.getEndpoint(),
                            this.client.getSubscriptionId(),
                            apiVersion,
                            resourceGroupName,
                            expressRoutePortName,
                            linkName,
                            context))
            .subscriberContext(context -> context.putAll(FluxUtil.toReactorContext(this.client.getContext())));
    }

    /**
     * Retrieves the specified ExpressRouteLink resource.
     *
     * @param resourceGroupName The name of the resource group.
     * @param expressRoutePortName The name of the ExpressRoutePort resource.
     * @param linkName The name of the ExpressRouteLink resource.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return expressRouteLink child resource definition.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<ExpressRouteLinkInner>> getWithResponseAsync(
        String resourceGroupName, String expressRoutePortName, String linkName, Context context) {
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
        if (expressRoutePortName == null) {
            return Mono
                .error(new IllegalArgumentException("Parameter expressRoutePortName is required and cannot be null."));
        }
        if (linkName == null) {
            return Mono.error(new IllegalArgumentException("Parameter linkName is required and cannot be null."));
        }
        final String apiVersion = "2020-05-01";
        context = this.client.mergeContext(context);
        return service
            .get(
                this.client.getEndpoint(),
                this.client.getSubscriptionId(),
                apiVersion,
                resourceGroupName,
                expressRoutePortName,
                linkName,
                context);
    }

    /**
     * Retrieves the specified ExpressRouteLink resource.
     *
     * @param resourceGroupName The name of the resource group.
     * @param expressRoutePortName The name of the ExpressRoutePort resource.
     * @param linkName The name of the ExpressRouteLink resource.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return expressRouteLink child resource definition.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<ExpressRouteLinkInner> getAsync(
        String resourceGroupName, String expressRoutePortName, String linkName) {
        return getWithResponseAsync(resourceGroupName, expressRoutePortName, linkName)
            .flatMap(
                (Response<ExpressRouteLinkInner> res) -> {
                    if (res.getValue() != null) {
                        return Mono.just(res.getValue());
                    } else {
                        return Mono.empty();
                    }
                });
    }

    /**
     * Retrieves the specified ExpressRouteLink resource.
     *
     * @param resourceGroupName The name of the resource group.
     * @param expressRoutePortName The name of the ExpressRoutePort resource.
     * @param linkName The name of the ExpressRouteLink resource.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return expressRouteLink child resource definition.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<ExpressRouteLinkInner> getAsync(
        String resourceGroupName, String expressRoutePortName, String linkName, Context context) {
        return getWithResponseAsync(resourceGroupName, expressRoutePortName, linkName, context)
            .flatMap(
                (Response<ExpressRouteLinkInner> res) -> {
                    if (res.getValue() != null) {
                        return Mono.just(res.getValue());
                    } else {
                        return Mono.empty();
                    }
                });
    }

    /**
     * Retrieves the specified ExpressRouteLink resource.
     *
     * @param resourceGroupName The name of the resource group.
     * @param expressRoutePortName The name of the ExpressRoutePort resource.
     * @param linkName The name of the ExpressRouteLink resource.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return expressRouteLink child resource definition.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public ExpressRouteLinkInner get(String resourceGroupName, String expressRoutePortName, String linkName) {
        return getAsync(resourceGroupName, expressRoutePortName, linkName).block();
    }

    /**
     * Retrieves the specified ExpressRouteLink resource.
     *
     * @param resourceGroupName The name of the resource group.
     * @param expressRoutePortName The name of the ExpressRoutePort resource.
     * @param linkName The name of the ExpressRouteLink resource.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return expressRouteLink child resource definition.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public ExpressRouteLinkInner get(
        String resourceGroupName, String expressRoutePortName, String linkName, Context context) {
        return getAsync(resourceGroupName, expressRoutePortName, linkName, context).block();
    }

    /**
     * Retrieve the ExpressRouteLink sub-resources of the specified ExpressRoutePort resource.
     *
     * @param resourceGroupName The name of the resource group.
     * @param expressRoutePortName The name of the ExpressRoutePort resource.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return response for ListExpressRouteLinks API service call.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<PagedResponse<ExpressRouteLinkInner>> listSinglePageAsync(
        String resourceGroupName, String expressRoutePortName) {
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
        if (expressRoutePortName == null) {
            return Mono
                .error(new IllegalArgumentException("Parameter expressRoutePortName is required and cannot be null."));
        }
        final String apiVersion = "2020-05-01";
        return FluxUtil
            .withContext(
                context ->
                    service
                        .list(
                            this.client.getEndpoint(),
                            this.client.getSubscriptionId(),
                            apiVersion,
                            resourceGroupName,
                            expressRoutePortName,
                            context))
            .<PagedResponse<ExpressRouteLinkInner>>map(
                res ->
                    new PagedResponseBase<>(
                        res.getRequest(),
                        res.getStatusCode(),
                        res.getHeaders(),
                        res.getValue().value(),
                        res.getValue().nextLink(),
                        null))
            .subscriberContext(context -> context.putAll(FluxUtil.toReactorContext(this.client.getContext())));
    }

    /**
     * Retrieve the ExpressRouteLink sub-resources of the specified ExpressRoutePort resource.
     *
     * @param resourceGroupName The name of the resource group.
     * @param expressRoutePortName The name of the ExpressRoutePort resource.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return response for ListExpressRouteLinks API service call.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<PagedResponse<ExpressRouteLinkInner>> listSinglePageAsync(
        String resourceGroupName, String expressRoutePortName, Context context) {
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
        if (expressRoutePortName == null) {
            return Mono
                .error(new IllegalArgumentException("Parameter expressRoutePortName is required and cannot be null."));
        }
        final String apiVersion = "2020-05-01";
        context = this.client.mergeContext(context);
        return service
            .list(
                this.client.getEndpoint(),
                this.client.getSubscriptionId(),
                apiVersion,
                resourceGroupName,
                expressRoutePortName,
                context)
            .map(
                res ->
                    new PagedResponseBase<>(
                        res.getRequest(),
                        res.getStatusCode(),
                        res.getHeaders(),
                        res.getValue().value(),
                        res.getValue().nextLink(),
                        null));
    }

    /**
     * Retrieve the ExpressRouteLink sub-resources of the specified ExpressRoutePort resource.
     *
     * @param resourceGroupName The name of the resource group.
     * @param expressRoutePortName The name of the ExpressRoutePort resource.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return response for ListExpressRouteLinks API service call.
     */
    @ServiceMethod(returns = ReturnType.COLLECTION)
    public PagedFlux<ExpressRouteLinkInner> listAsync(String resourceGroupName, String expressRoutePortName) {
        return new PagedFlux<>(
            () -> listSinglePageAsync(resourceGroupName, expressRoutePortName),
            nextLink -> listNextSinglePageAsync(nextLink));
    }

    /**
     * Retrieve the ExpressRouteLink sub-resources of the specified ExpressRoutePort resource.
     *
     * @param resourceGroupName The name of the resource group.
     * @param expressRoutePortName The name of the ExpressRoutePort resource.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return response for ListExpressRouteLinks API service call.
     */
    @ServiceMethod(returns = ReturnType.COLLECTION)
    public PagedFlux<ExpressRouteLinkInner> listAsync(
        String resourceGroupName, String expressRoutePortName, Context context) {
        return new PagedFlux<>(
            () -> listSinglePageAsync(resourceGroupName, expressRoutePortName, context),
            nextLink -> listNextSinglePageAsync(nextLink, context));
    }

    /**
     * Retrieve the ExpressRouteLink sub-resources of the specified ExpressRoutePort resource.
     *
     * @param resourceGroupName The name of the resource group.
     * @param expressRoutePortName The name of the ExpressRoutePort resource.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return response for ListExpressRouteLinks API service call.
     */
    @ServiceMethod(returns = ReturnType.COLLECTION)
    public PagedIterable<ExpressRouteLinkInner> list(String resourceGroupName, String expressRoutePortName) {
        return new PagedIterable<>(listAsync(resourceGroupName, expressRoutePortName));
    }

    /**
     * Retrieve the ExpressRouteLink sub-resources of the specified ExpressRoutePort resource.
     *
     * @param resourceGroupName The name of the resource group.
     * @param expressRoutePortName The name of the ExpressRoutePort resource.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return response for ListExpressRouteLinks API service call.
     */
    @ServiceMethod(returns = ReturnType.COLLECTION)
    public PagedIterable<ExpressRouteLinkInner> list(
        String resourceGroupName, String expressRoutePortName, Context context) {
        return new PagedIterable<>(listAsync(resourceGroupName, expressRoutePortName, context));
    }

    /**
     * Get the next page of items.
     *
     * @param nextLink The nextLink parameter.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return response for ListExpressRouteLinks API service call.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<PagedResponse<ExpressRouteLinkInner>> listNextSinglePageAsync(String nextLink) {
        if (nextLink == null) {
            return Mono.error(new IllegalArgumentException("Parameter nextLink is required and cannot be null."));
        }
        return FluxUtil
            .withContext(context -> service.listNext(nextLink, context))
            .<PagedResponse<ExpressRouteLinkInner>>map(
                res ->
                    new PagedResponseBase<>(
                        res.getRequest(),
                        res.getStatusCode(),
                        res.getHeaders(),
                        res.getValue().value(),
                        res.getValue().nextLink(),
                        null))
            .subscriberContext(context -> context.putAll(FluxUtil.toReactorContext(this.client.getContext())));
    }

    /**
     * Get the next page of items.
     *
     * @param nextLink The nextLink parameter.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return response for ListExpressRouteLinks API service call.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<PagedResponse<ExpressRouteLinkInner>> listNextSinglePageAsync(String nextLink, Context context) {
        if (nextLink == null) {
            return Mono.error(new IllegalArgumentException("Parameter nextLink is required and cannot be null."));
        }
        context = this.client.mergeContext(context);
        return service
            .listNext(nextLink, context)
            .map(
                res ->
                    new PagedResponseBase<>(
                        res.getRequest(),
                        res.getStatusCode(),
                        res.getHeaders(),
                        res.getValue().value(),
                        res.getValue().nextLink(),
                        null));
    }
}
