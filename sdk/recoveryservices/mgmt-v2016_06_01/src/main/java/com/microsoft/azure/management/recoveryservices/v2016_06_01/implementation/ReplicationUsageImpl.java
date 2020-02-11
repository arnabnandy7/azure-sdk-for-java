/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.recoveryservices.v2016_06_01.implementation;

import com.microsoft.azure.management.recoveryservices.v2016_06_01.ReplicationUsage;
import com.microsoft.azure.arm.model.implementation.WrapperImpl;
import rx.Observable;
import com.microsoft.azure.management.recoveryservices.v2016_06_01.JobsSummary;
import com.microsoft.azure.management.recoveryservices.v2016_06_01.MonitoringSummary;

class ReplicationUsageImpl extends WrapperImpl<ReplicationUsageInner> implements ReplicationUsage {
    private final RecoveryServicesManager manager;

    ReplicationUsageImpl(ReplicationUsageInner inner,  RecoveryServicesManager manager) {
        super(inner);
        this.manager = manager;
    }

    @Override
    public RecoveryServicesManager manager() {
        return this.manager;
    }



    @Override
    public JobsSummary jobsSummary() {
        return this.inner().jobsSummary();
    }

    @Override
    public MonitoringSummary monitoringSummary() {
        return this.inner().monitoringSummary();
    }

    @Override
    public Integer protectedItemCount() {
        return this.inner().protectedItemCount();
    }

    @Override
    public Integer recoveryPlanCount() {
        return this.inner().recoveryPlanCount();
    }

    @Override
    public Integer recoveryServicesProviderAuthType() {
        return this.inner().recoveryServicesProviderAuthType();
    }

    @Override
    public Integer registeredServersCount() {
        return this.inner().registeredServersCount();
    }

}