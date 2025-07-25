// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.redis.fluent.inner;

import com.azure.core.annotation.Fluent;
import com.azure.core.util.logging.ClientLogger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The RedisListResult model. */
@Fluent
public final class RedisListResultInner {
    @JsonIgnore private final ClientLogger logger = new ClientLogger(RedisListResultInner.class);

    /*
     * List of Redis cache instances.
     */
    @JsonProperty(value = "value")
    private List<RedisResourceInner> value;

    /*
     * Link for next page of results.
     */
    @JsonProperty(value = "nextLink", access = JsonProperty.Access.WRITE_ONLY)
    private String nextLink;

    /**
     * Get the value property: List of Redis cache instances.
     *
     * @return the value value.
     */
    public List<RedisResourceInner> value() {
        return this.value;
    }

    /**
     * Set the value property: List of Redis cache instances.
     *
     * @param value the value value to set.
     * @return the RedisListResultInner object itself.
     */
    public RedisListResultInner withValue(List<RedisResourceInner> value) {
        this.value = value;
        return this;
    }

    /**
     * Get the nextLink property: Link for next page of results.
     *
     * @return the nextLink value.
     */
    public String nextLink() {
        return this.nextLink;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
        if (value() != null) {
            value().forEach(e -> e.validate());
        }
    }
}
