// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.trafficmanager.fluent;

import com.azure.core.annotation.Delete;
import com.azure.core.annotation.ExpectedResponses;
import com.azure.core.annotation.Get;
import com.azure.core.annotation.Headers;
import com.azure.core.annotation.Host;
import com.azure.core.annotation.HostParam;
import com.azure.core.annotation.PathParam;
import com.azure.core.annotation.Put;
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
import com.azure.resourcemanager.trafficmanager.fluent.inner.DeleteOperationResultInner;
import com.azure.resourcemanager.trafficmanager.fluent.inner.UserMetricsModelInner;
import reactor.core.publisher.Mono;

/** An instance of this class provides access to all the operations defined in TrafficManagerUserMetricsKeys. */
public final class TrafficManagerUserMetricsKeysClient {
    private final ClientLogger logger = new ClientLogger(TrafficManagerUserMetricsKeysClient.class);

    /** The proxy service used to perform REST calls. */
    private final TrafficManagerUserMetricsKeysService service;

    /** The service client containing this operation class. */
    private final TrafficManagerManagementClient client;

    /**
     * Initializes an instance of TrafficManagerUserMetricsKeysClient.
     *
     * @param client the instance of the service client containing this operation class.
     */
    public TrafficManagerUserMetricsKeysClient(TrafficManagerManagementClient client) {
        this.service =
            RestProxy
                .create(
                    TrafficManagerUserMetricsKeysService.class,
                    client.getHttpPipeline(),
                    client.getSerializerAdapter());
        this.client = client;
    }

    /**
     * The interface defining all the services for TrafficManagerManagementClientTrafficManagerUserMetricsKeys to be
     * used by the proxy service to perform REST calls.
     */
    @Host("{$host}")
    @ServiceInterface(name = "TrafficManagerManage")
    private interface TrafficManagerUserMetricsKeysService {
        @Headers({"Accept: application/json", "Content-Type: application/json"})
        @Get("/subscriptions/{subscriptionId}/providers/Microsoft.Network/trafficManagerUserMetricsKeys/default")
        @ExpectedResponses({200})
        @UnexpectedResponseExceptionType(ManagementException.class)
        Mono<Response<UserMetricsModelInner>> get(
            @HostParam("$host") String endpoint,
            @QueryParam("api-version") String apiVersion,
            @PathParam("subscriptionId") String subscriptionId,
            Context context);

        @Headers({"Accept: application/json", "Content-Type: application/json"})
        @Put("/subscriptions/{subscriptionId}/providers/Microsoft.Network/trafficManagerUserMetricsKeys/default")
        @ExpectedResponses({201})
        @UnexpectedResponseExceptionType(ManagementException.class)
        Mono<Response<UserMetricsModelInner>> createOrUpdate(
            @HostParam("$host") String endpoint,
            @QueryParam("api-version") String apiVersion,
            @PathParam("subscriptionId") String subscriptionId,
            Context context);

        @Headers({"Accept: application/json", "Content-Type: application/json"})
        @Delete("/subscriptions/{subscriptionId}/providers/Microsoft.Network/trafficManagerUserMetricsKeys/default")
        @ExpectedResponses({200})
        @UnexpectedResponseExceptionType(ManagementException.class)
        Mono<Response<DeleteOperationResultInner>> delete(
            @HostParam("$host") String endpoint,
            @QueryParam("api-version") String apiVersion,
            @PathParam("subscriptionId") String subscriptionId,
            Context context);
    }

    /**
     * Get the subscription-level key used for Real User Metrics collection.
     *
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the subscription-level key used for Real User Metrics collection.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<UserMetricsModelInner>> getWithResponseAsync() {
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
        return FluxUtil
            .withContext(
                context ->
                    service
                        .get(
                            this.client.getEndpoint(),
                            this.client.getApiVersion(),
                            this.client.getSubscriptionId(),
                            context))
            .subscriberContext(context -> context.putAll(FluxUtil.toReactorContext(this.client.getContext())));
    }

    /**
     * Get the subscription-level key used for Real User Metrics collection.
     *
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the subscription-level key used for Real User Metrics collection.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<UserMetricsModelInner>> getWithResponseAsync(Context context) {
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
        context = this.client.mergeContext(context);
        return service
            .get(this.client.getEndpoint(), this.client.getApiVersion(), this.client.getSubscriptionId(), context);
    }

    /**
     * Get the subscription-level key used for Real User Metrics collection.
     *
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the subscription-level key used for Real User Metrics collection.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<UserMetricsModelInner> getAsync() {
        return getWithResponseAsync()
            .flatMap(
                (Response<UserMetricsModelInner> res) -> {
                    if (res.getValue() != null) {
                        return Mono.just(res.getValue());
                    } else {
                        return Mono.empty();
                    }
                });
    }

    /**
     * Get the subscription-level key used for Real User Metrics collection.
     *
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the subscription-level key used for Real User Metrics collection.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<UserMetricsModelInner> getAsync(Context context) {
        return getWithResponseAsync(context)
            .flatMap(
                (Response<UserMetricsModelInner> res) -> {
                    if (res.getValue() != null) {
                        return Mono.just(res.getValue());
                    } else {
                        return Mono.empty();
                    }
                });
    }

    /**
     * Get the subscription-level key used for Real User Metrics collection.
     *
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the subscription-level key used for Real User Metrics collection.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public UserMetricsModelInner get() {
        return getAsync().block();
    }

    /**
     * Get the subscription-level key used for Real User Metrics collection.
     *
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the subscription-level key used for Real User Metrics collection.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Response<UserMetricsModelInner> getWithResponse(Context context) {
        return getWithResponseAsync(context).block();
    }

    /**
     * Create or update a subscription-level key used for Real User Metrics collection.
     *
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return class representing Traffic Manager User Metrics.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<UserMetricsModelInner>> createOrUpdateWithResponseAsync() {
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
        return FluxUtil
            .withContext(
                context ->
                    service
                        .createOrUpdate(
                            this.client.getEndpoint(),
                            this.client.getApiVersion(),
                            this.client.getSubscriptionId(),
                            context))
            .subscriberContext(context -> context.putAll(FluxUtil.toReactorContext(this.client.getContext())));
    }

    /**
     * Create or update a subscription-level key used for Real User Metrics collection.
     *
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return class representing Traffic Manager User Metrics.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<UserMetricsModelInner>> createOrUpdateWithResponseAsync(Context context) {
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
        context = this.client.mergeContext(context);
        return service
            .createOrUpdate(
                this.client.getEndpoint(), this.client.getApiVersion(), this.client.getSubscriptionId(), context);
    }

    /**
     * Create or update a subscription-level key used for Real User Metrics collection.
     *
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return class representing Traffic Manager User Metrics.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<UserMetricsModelInner> createOrUpdateAsync() {
        return createOrUpdateWithResponseAsync()
            .flatMap(
                (Response<UserMetricsModelInner> res) -> {
                    if (res.getValue() != null) {
                        return Mono.just(res.getValue());
                    } else {
                        return Mono.empty();
                    }
                });
    }

    /**
     * Create or update a subscription-level key used for Real User Metrics collection.
     *
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return class representing Traffic Manager User Metrics.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<UserMetricsModelInner> createOrUpdateAsync(Context context) {
        return createOrUpdateWithResponseAsync(context)
            .flatMap(
                (Response<UserMetricsModelInner> res) -> {
                    if (res.getValue() != null) {
                        return Mono.just(res.getValue());
                    } else {
                        return Mono.empty();
                    }
                });
    }

    /**
     * Create or update a subscription-level key used for Real User Metrics collection.
     *
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return class representing Traffic Manager User Metrics.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public UserMetricsModelInner createOrUpdate() {
        return createOrUpdateAsync().block();
    }

    /**
     * Create or update a subscription-level key used for Real User Metrics collection.
     *
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return class representing Traffic Manager User Metrics.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Response<UserMetricsModelInner> createOrUpdateWithResponse(Context context) {
        return createOrUpdateWithResponseAsync(context).block();
    }

    /**
     * Delete a subscription-level key used for Real User Metrics collection.
     *
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the result of the request or operation.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<DeleteOperationResultInner>> deleteWithResponseAsync() {
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
        return FluxUtil
            .withContext(
                context ->
                    service
                        .delete(
                            this.client.getEndpoint(),
                            this.client.getApiVersion(),
                            this.client.getSubscriptionId(),
                            context))
            .subscriberContext(context -> context.putAll(FluxUtil.toReactorContext(this.client.getContext())));
    }

    /**
     * Delete a subscription-level key used for Real User Metrics collection.
     *
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the result of the request or operation.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<DeleteOperationResultInner>> deleteWithResponseAsync(Context context) {
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
        context = this.client.mergeContext(context);
        return service
            .delete(this.client.getEndpoint(), this.client.getApiVersion(), this.client.getSubscriptionId(), context);
    }

    /**
     * Delete a subscription-level key used for Real User Metrics collection.
     *
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the result of the request or operation.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<DeleteOperationResultInner> deleteAsync() {
        return deleteWithResponseAsync()
            .flatMap(
                (Response<DeleteOperationResultInner> res) -> {
                    if (res.getValue() != null) {
                        return Mono.just(res.getValue());
                    } else {
                        return Mono.empty();
                    }
                });
    }

    /**
     * Delete a subscription-level key used for Real User Metrics collection.
     *
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the result of the request or operation.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<DeleteOperationResultInner> deleteAsync(Context context) {
        return deleteWithResponseAsync(context)
            .flatMap(
                (Response<DeleteOperationResultInner> res) -> {
                    if (res.getValue() != null) {
                        return Mono.just(res.getValue());
                    } else {
                        return Mono.empty();
                    }
                });
    }

    /**
     * Delete a subscription-level key used for Real User Metrics collection.
     *
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the result of the request or operation.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public DeleteOperationResultInner delete() {
        return deleteAsync().block();
    }

    /**
     * Delete a subscription-level key used for Real User Metrics collection.
     *
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the result of the request or operation.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Response<DeleteOperationResultInner> deleteWithResponse(Context context) {
        return deleteWithResponseAsync(context).block();
    }
}
