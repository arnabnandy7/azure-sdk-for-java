// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.trafficmanager.fluent.inner;

import com.azure.core.annotation.Immutable;
import com.azure.core.util.logging.ClientLogger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/** The DeleteOperationResult model. */
@Immutable
public final class DeleteOperationResultInner {
    @JsonIgnore private final ClientLogger logger = new ClientLogger(DeleteOperationResultInner.class);

    /*
     * The result of the operation or request.
     */
    @JsonProperty(value = "boolean", access = JsonProperty.Access.WRITE_ONLY)
    private Boolean operationResult;

    /**
     * Get the operationResult property: The result of the operation or request.
     *
     * @return the operationResult value.
     */
    public Boolean operationResult() {
        return this.operationResult;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
    }
}
