package com.azure.digitaltwins.core.models;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Properties on a component that adhere to a specific model.
 */
@Fluent
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ModelProperties {

    /**
     * Information about the model a component conforms to. This field is present on every digital twin.
     */
    @JsonProperty(value = "$metadata", required = true)
    private ComponentMetadata metadata = new ComponentMetadata();

    /**
     * The additional properties of the model. This field will contain any properties of the digital twin that are not already defined by the other strong types of this class.
     */
    private final Map<String, Object> customProperties = new HashMap<>();

    /**
     * Gets the metadata about the model.
     * @return The model metadata.
     */
    public ComponentMetadata getMetadata() {
        return metadata;
    }

    /**
     * Sets the model metadata.
     * @param metadata Model metadata.
     * @return The ModelProperties object itself.
     */
    public ModelProperties setMetadata(ComponentMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Gets the custom properties
     * @return The custom properties
     */
    @JsonAnyGetter
    public Map<String, Object> getCustomProperties() {
        return customProperties;
    }

    /**
     * Sets the custom properties
     * @param key The key of the additional property to be added to the digital twin.
     * @param value The value of the additional property to be added to the digital twin.
     * @return The ModelProperties object itself.
     */
    @JsonAnySetter
    public ModelProperties setCustomProperties(String key, Object value) {
        this.customProperties.put(key, value);
        return this;
    }
}
