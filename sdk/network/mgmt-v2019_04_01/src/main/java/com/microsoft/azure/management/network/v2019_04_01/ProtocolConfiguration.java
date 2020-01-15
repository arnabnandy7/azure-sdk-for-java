/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.network.v2019_04_01;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration of the protocol.
 */
public class ProtocolConfiguration {
    /**
     * HTTP configuration of the connectivity check.
     */
    @JsonProperty(value = "HTTPConfiguration")
    private HTTPConfiguration hTTPConfiguration;

    /**
     * Get hTTP configuration of the connectivity check.
     *
     * @return the hTTPConfiguration value
     */
    public HTTPConfiguration hTTPConfiguration() {
        return this.hTTPConfiguration;
    }

    /**
     * Set hTTP configuration of the connectivity check.
     *
     * @param hTTPConfiguration the hTTPConfiguration value to set
     * @return the ProtocolConfiguration object itself.
     */
    public ProtocolConfiguration withHTTPConfiguration(HTTPConfiguration hTTPConfiguration) {
        this.hTTPConfiguration = hTTPConfiguration;
        return this;
    }

}