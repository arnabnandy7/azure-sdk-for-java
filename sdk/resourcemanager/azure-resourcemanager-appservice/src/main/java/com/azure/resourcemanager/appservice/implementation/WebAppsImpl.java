// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.resourcemanager.appservice.implementation;

import com.azure.core.http.rest.PagedFlux;
import com.azure.core.http.rest.PagedIterable;
import com.azure.resourcemanager.appservice.AppServiceManager;
import com.azure.resourcemanager.appservice.fluent.WebAppsClient;
import com.azure.resourcemanager.appservice.fluent.inner.SiteConfigResourceInner;
import com.azure.resourcemanager.appservice.fluent.inner.SiteInner;
import com.azure.resourcemanager.appservice.fluent.inner.SiteLogsConfigInner;
import com.azure.resourcemanager.appservice.models.WebApp;
import com.azure.resourcemanager.appservice.models.WebAppBasic;
import com.azure.resourcemanager.appservice.models.WebApps;
import com.azure.resourcemanager.resources.fluentcore.arm.collection.SupportsBatchDeletion;
import com.azure.resourcemanager.resources.fluentcore.arm.collection.implementation.BatchDeletionImpl;
import com.azure.resourcemanager.resources.fluentcore.arm.collection.implementation.GroupableResourcesImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/** The implementation for WebApps. */
public class WebAppsImpl
    extends GroupableResourcesImpl<WebApp, WebAppImpl, SiteInner, WebAppsClient, AppServiceManager>
    implements WebApps, SupportsBatchDeletion {

    public WebAppsImpl(final AppServiceManager manager) {
        super(manager.inner().getWebApps(), manager);
    }

    @Override
    public Mono<WebApp> getByResourceGroupAsync(final String groupName, final String name) {
        final WebAppsImpl self = this;
        return this
            .getInnerAsync(groupName, name)
            .flatMap(
                siteInner ->
                    Mono
                        .zip(
                            self.inner().getConfigurationAsync(groupName, name),
                            self.inner().getDiagnosticLogsConfigurationAsync(groupName, name),
                            (SiteConfigResourceInner siteConfigResourceInner, SiteLogsConfigInner logsConfigInner) ->
                                wrapModel(siteInner, siteConfigResourceInner, logsConfigInner)));
    }

    @Override
    protected Mono<SiteInner> getInnerAsync(String resourceGroupName, String name) {
        return this.inner().getByResourceGroupAsync(resourceGroupName, name);
    }

    @Override
    protected Mono<Void> deleteInnerAsync(String resourceGroupName, String name) {
        return inner().deleteAsync(resourceGroupName, name).then();
    }

    @Override
    protected WebAppImpl wrapModel(String name) {
        return new WebAppImpl(name, new SiteInner().withKind("app"), null, null, this.manager());
    }

    protected WebAppImpl wrapModel(SiteInner inner, SiteConfigResourceInner siteConfig, SiteLogsConfigInner logConfig) {
        if (inner == null) {
            return null;
        }
        return new WebAppImpl(inner.name(), inner, siteConfig, logConfig, this.manager());
    }

    @Override
    protected WebAppImpl wrapModel(SiteInner inner) {
        return wrapModel(inner, null, null);
    }

    @Override
    public WebAppImpl define(String name) {
        return wrapModel(name);
    }

    @Override
    public Flux<String> deleteByIdsAsync(Collection<String> ids) {
        return BatchDeletionImpl.deleteByIdsAsync(ids, this::deleteInnerAsync);
    }

    @Override
    public Flux<String> deleteByIdsAsync(String... ids) {
        return this.deleteByIdsAsync(new ArrayList<>(Arrays.asList(ids)));
    }

    @Override
    public void deleteByIds(Collection<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            this.deleteByIdsAsync(ids).blockLast();
        }
    }

    @Override
    public void deleteByIds(String... ids) {
        this.deleteByIds(new ArrayList<>(Arrays.asList(ids)));
    }

    @Override
    public PagedIterable<WebAppBasic> listByResourceGroup(String resourceGroupName) {
        return new PagedIterable<>(this.listByResourceGroupAsync(resourceGroupName));
    }

    @Override
    public PagedFlux<WebAppBasic> listByResourceGroupAsync(String resourceGroupName) {
        return inner().listByResourceGroupAsync(resourceGroupName)
            .mapPage(inner -> new WebAppBasicImpl(inner, this.manager()));
    }

    @Override
    public PagedIterable<WebAppBasic> list() {
        return new PagedIterable<>(this.listAsync());
    }

    @Override
    public PagedFlux<WebAppBasic> listAsync() {
        return inner().listAsync()
            .mapPage(inner -> new WebAppBasicImpl(inner, this.manager()));
    }
}
