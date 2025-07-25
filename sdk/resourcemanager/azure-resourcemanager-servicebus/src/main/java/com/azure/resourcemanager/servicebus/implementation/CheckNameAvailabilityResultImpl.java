// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.resourcemanager.servicebus.implementation;

import com.azure.resourcemanager.resources.fluentcore.model.implementation.WrapperImpl;
import com.azure.resourcemanager.servicebus.fluent.inner.CheckNameAvailabilityResultInner;
import com.azure.resourcemanager.servicebus.models.CheckNameAvailabilityResult;
import com.azure.resourcemanager.servicebus.models.UnavailableReason;

/**
 * Implementation for CheckNameAvailabilityResult.
 */
class CheckNameAvailabilityResultImpl
        extends WrapperImpl<CheckNameAvailabilityResultInner>
        implements CheckNameAvailabilityResult {
    /**
     * Creates an instance of the check name availability result object.
     *
     * @param inner the inner object
     */
    CheckNameAvailabilityResultImpl(CheckNameAvailabilityResultInner inner) {
        super(inner);
    }

    @Override
    public boolean isAvailable() {
        return inner().nameAvailable();
    }

    @Override
    public UnavailableReason unavailabilityReason() {
        return inner().reason();
    }

    @Override
    public String unavailabilityMessage() {
        return inner().message();
    }
}

