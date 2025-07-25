// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.authorization.fluent;

import com.azure.core.annotation.ServiceClient;
import com.azure.core.http.HttpPipeline;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.util.logging.ClientLogger;
import com.azure.core.util.serializer.SerializerAdapter;
import com.azure.resourcemanager.resources.fluentcore.AzureServiceClient;
import java.time.Duration;

/** Initializes a new instance of the AuthorizationManagementClient type. */
@ServiceClient(builder = AuthorizationManagementClientBuilder.class)
public final class AuthorizationManagementClient extends AzureServiceClient {
    private final ClientLogger logger = new ClientLogger(AuthorizationManagementClient.class);

    /** The ID of the target subscription. */
    private final String subscriptionId;

    /**
     * Gets The ID of the target subscription.
     *
     * @return the subscriptionId value.
     */
    public String getSubscriptionId() {
        return this.subscriptionId;
    }

    /** server parameter. */
    private final String endpoint;

    /**
     * Gets server parameter.
     *
     * @return the endpoint value.
     */
    public String getEndpoint() {
        return this.endpoint;
    }

    /** The HTTP pipeline to send requests through. */
    private final HttpPipeline httpPipeline;

    /**
     * Gets The HTTP pipeline to send requests through.
     *
     * @return the httpPipeline value.
     */
    public HttpPipeline getHttpPipeline() {
        return this.httpPipeline;
    }

    /** The serializer to serialize an object into a string. */
    private final SerializerAdapter serializerAdapter;

    /**
     * Gets The serializer to serialize an object into a string.
     *
     * @return the serializerAdapter value.
     */
    public SerializerAdapter getSerializerAdapter() {
        return this.serializerAdapter;
    }

    /** The default poll interval for long-running operation. */
    private final Duration defaultPollInterval;

    /**
     * Gets The default poll interval for long-running operation.
     *
     * @return the defaultPollInterval value.
     */
    public Duration getDefaultPollInterval() {
        return this.defaultPollInterval;
    }

    /** The ClassicAdministratorsClient object to access its operations. */
    private final ClassicAdministratorsClient classicAdministrators;

    /**
     * Gets the ClassicAdministratorsClient object to access its operations.
     *
     * @return the ClassicAdministratorsClient object.
     */
    public ClassicAdministratorsClient getClassicAdministrators() {
        return this.classicAdministrators;
    }

    /** The GlobalAdministratorsClient object to access its operations. */
    private final GlobalAdministratorsClient globalAdministrators;

    /**
     * Gets the GlobalAdministratorsClient object to access its operations.
     *
     * @return the GlobalAdministratorsClient object.
     */
    public GlobalAdministratorsClient getGlobalAdministrators() {
        return this.globalAdministrators;
    }

    /** The ProviderOperationsMetadatasClient object to access its operations. */
    private final ProviderOperationsMetadatasClient providerOperationsMetadatas;

    /**
     * Gets the ProviderOperationsMetadatasClient object to access its operations.
     *
     * @return the ProviderOperationsMetadatasClient object.
     */
    public ProviderOperationsMetadatasClient getProviderOperationsMetadatas() {
        return this.providerOperationsMetadatas;
    }

    /** The RoleAssignmentsClient object to access its operations. */
    private final RoleAssignmentsClient roleAssignments;

    /**
     * Gets the RoleAssignmentsClient object to access its operations.
     *
     * @return the RoleAssignmentsClient object.
     */
    public RoleAssignmentsClient getRoleAssignments() {
        return this.roleAssignments;
    }

    /** The PermissionsClient object to access its operations. */
    private final PermissionsClient permissions;

    /**
     * Gets the PermissionsClient object to access its operations.
     *
     * @return the PermissionsClient object.
     */
    public PermissionsClient getPermissions() {
        return this.permissions;
    }

    /** The RoleDefinitionsClient object to access its operations. */
    private final RoleDefinitionsClient roleDefinitions;

    /**
     * Gets the RoleDefinitionsClient object to access its operations.
     *
     * @return the RoleDefinitionsClient object.
     */
    public RoleDefinitionsClient getRoleDefinitions() {
        return this.roleDefinitions;
    }

    /**
     * Initializes an instance of AuthorizationManagementClient client.
     *
     * @param httpPipeline The HTTP pipeline to send requests through.
     * @param serializerAdapter The serializer to serialize an object into a string.
     * @param defaultPollInterval The default poll interval for long-running operation.
     * @param environment The Azure environment.
     */
    AuthorizationManagementClient(
        HttpPipeline httpPipeline,
        SerializerAdapter serializerAdapter,
        Duration defaultPollInterval,
        AzureEnvironment environment,
        String subscriptionId,
        String endpoint) {
        super(httpPipeline, serializerAdapter, environment);
        this.httpPipeline = httpPipeline;
        this.serializerAdapter = serializerAdapter;
        this.defaultPollInterval = defaultPollInterval;
        this.subscriptionId = subscriptionId;
        this.endpoint = endpoint;
        this.classicAdministrators = new ClassicAdministratorsClient(this);
        this.globalAdministrators = new GlobalAdministratorsClient(this);
        this.providerOperationsMetadatas = new ProviderOperationsMetadatasClient(this);
        this.roleAssignments = new RoleAssignmentsClient(this);
        this.permissions = new PermissionsClient(this);
        this.roleDefinitions = new RoleDefinitionsClient(this);
    }
}
