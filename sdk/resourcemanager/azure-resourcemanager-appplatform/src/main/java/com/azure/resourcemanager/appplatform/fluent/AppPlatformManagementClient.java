// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.appplatform.fluent;

import com.azure.core.annotation.ServiceClient;
import com.azure.core.http.HttpPipeline;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.util.logging.ClientLogger;
import com.azure.core.util.serializer.SerializerAdapter;
import com.azure.resourcemanager.resources.fluentcore.AzureServiceClient;
import java.time.Duration;

/** Initializes a new instance of the AppPlatformManagementClient type. */
@ServiceClient(builder = AppPlatformManagementClientBuilder.class)
public final class AppPlatformManagementClient extends AzureServiceClient {
    private final ClientLogger logger = new ClientLogger(AppPlatformManagementClient.class);

    /**
     * Gets subscription ID which uniquely identify the Microsoft Azure subscription. The subscription ID forms part of
     * the URI for every service call.
     */
    private final String subscriptionId;

    /**
     * Gets Gets subscription ID which uniquely identify the Microsoft Azure subscription. The subscription ID forms
     * part of the URI for every service call.
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

    /** Api Version. */
    private final String apiVersion;

    /**
     * Gets Api Version.
     *
     * @return the apiVersion value.
     */
    public String getApiVersion() {
        return this.apiVersion;
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

    /** The ServicesClient object to access its operations. */
    private final ServicesClient services;

    /**
     * Gets the ServicesClient object to access its operations.
     *
     * @return the ServicesClient object.
     */
    public ServicesClient getServices() {
        return this.services;
    }

    /** The ConfigServersClient object to access its operations. */
    private final ConfigServersClient configServers;

    /**
     * Gets the ConfigServersClient object to access its operations.
     *
     * @return the ConfigServersClient object.
     */
    public ConfigServersClient getConfigServers() {
        return this.configServers;
    }

    /** The MonitoringSettingsClient object to access its operations. */
    private final MonitoringSettingsClient monitoringSettings;

    /**
     * Gets the MonitoringSettingsClient object to access its operations.
     *
     * @return the MonitoringSettingsClient object.
     */
    public MonitoringSettingsClient getMonitoringSettings() {
        return this.monitoringSettings;
    }

    /** The AppsClient object to access its operations. */
    private final AppsClient apps;

    /**
     * Gets the AppsClient object to access its operations.
     *
     * @return the AppsClient object.
     */
    public AppsClient getApps() {
        return this.apps;
    }

    /** The BindingsClient object to access its operations. */
    private final BindingsClient bindings;

    /**
     * Gets the BindingsClient object to access its operations.
     *
     * @return the BindingsClient object.
     */
    public BindingsClient getBindings() {
        return this.bindings;
    }

    /** The CertificatesClient object to access its operations. */
    private final CertificatesClient certificates;

    /**
     * Gets the CertificatesClient object to access its operations.
     *
     * @return the CertificatesClient object.
     */
    public CertificatesClient getCertificates() {
        return this.certificates;
    }

    /** The CustomDomainsClient object to access its operations. */
    private final CustomDomainsClient customDomains;

    /**
     * Gets the CustomDomainsClient object to access its operations.
     *
     * @return the CustomDomainsClient object.
     */
    public CustomDomainsClient getCustomDomains() {
        return this.customDomains;
    }

    /** The DeploymentsClient object to access its operations. */
    private final DeploymentsClient deployments;

    /**
     * Gets the DeploymentsClient object to access its operations.
     *
     * @return the DeploymentsClient object.
     */
    public DeploymentsClient getDeployments() {
        return this.deployments;
    }

    /** The OperationsClient object to access its operations. */
    private final OperationsClient operations;

    /**
     * Gets the OperationsClient object to access its operations.
     *
     * @return the OperationsClient object.
     */
    public OperationsClient getOperations() {
        return this.operations;
    }

    /** The SkusClient object to access its operations. */
    private final SkusClient skus;

    /**
     * Gets the SkusClient object to access its operations.
     *
     * @return the SkusClient object.
     */
    public SkusClient getSkus() {
        return this.skus;
    }

    /**
     * Initializes an instance of AppPlatformManagementClient client.
     *
     * @param httpPipeline The HTTP pipeline to send requests through.
     * @param serializerAdapter The serializer to serialize an object into a string.
     * @param defaultPollInterval The default poll interval for long-running operation.
     * @param environment The Azure environment.
     */
    AppPlatformManagementClient(
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
        this.apiVersion = "2020-07-01";
        this.services = new ServicesClient(this);
        this.configServers = new ConfigServersClient(this);
        this.monitoringSettings = new MonitoringSettingsClient(this);
        this.apps = new AppsClient(this);
        this.bindings = new BindingsClient(this);
        this.certificates = new CertificatesClient(this);
        this.customDomains = new CustomDomainsClient(this);
        this.deployments = new DeploymentsClient(this);
        this.operations = new OperationsClient(this);
        this.skus = new SkusClient(this);
    }
}
