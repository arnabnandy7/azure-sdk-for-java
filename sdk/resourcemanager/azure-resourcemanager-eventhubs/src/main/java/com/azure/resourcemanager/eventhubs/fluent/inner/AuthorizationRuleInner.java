// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.eventhubs.fluent.inner;

import com.azure.core.annotation.Fluent;
import com.azure.core.annotation.JsonFlatten;
import com.azure.core.management.ProxyResource;
import com.azure.core.util.logging.ClientLogger;
import com.azure.resourcemanager.eventhubs.models.AccessRights;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The AuthorizationRule model. */
@JsonFlatten
@Fluent
public class AuthorizationRuleInner extends ProxyResource {
    @JsonIgnore private final ClientLogger logger = new ClientLogger(AuthorizationRuleInner.class);

    /*
     * The rights associated with the rule.
     */
    @JsonProperty(value = "properties.rights")
    private List<AccessRights> rights;

    /**
     * Get the rights property: The rights associated with the rule.
     *
     * @return the rights value.
     */
    public List<AccessRights> rights() {
        return this.rights;
    }

    /**
     * Set the rights property: The rights associated with the rule.
     *
     * @param rights the rights value to set.
     * @return the AuthorizationRuleInner object itself.
     */
    public AuthorizationRuleInner withRights(List<AccessRights> rights) {
        this.rights = rights;
        return this;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
    }
}
