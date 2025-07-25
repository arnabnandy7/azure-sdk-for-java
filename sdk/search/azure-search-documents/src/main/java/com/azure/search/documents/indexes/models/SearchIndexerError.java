// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.
// Changes may cause incorrect behavior and will be lost if the code is
// regenerated.

package com.azure.search.documents.indexes.models;

import com.azure.core.annotation.Immutable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** The SearchIndexerError model. */
@Immutable
public final class SearchIndexerError {
    /*
     * The key of the item for which indexing failed.
     */
    @JsonProperty(value = "key", access = JsonProperty.Access.WRITE_ONLY)
    private String key;

    /*
     * The message describing the error that occurred while processing the
     * item.
     */
    @JsonProperty(value = "errorMessage", required = true, access = JsonProperty.Access.WRITE_ONLY)
    private String errorMessage;

    /*
     * The status code indicating why the indexing operation failed. Possible
     * values include: 400 for a malformed input document, 404 for document not
     * found, 409 for a version conflict, 422 when the index is temporarily
     * unavailable, or 503 for when the service is too busy.
     */
    @JsonProperty(value = "statusCode", required = true, access = JsonProperty.Access.WRITE_ONLY)
    private int statusCode;

    /*
     * The name of the source at which the error originated. For example, this
     * could refer to a particular skill in the attached skillset. This may not
     * be always available.
     */
    @JsonProperty(value = "name", access = JsonProperty.Access.WRITE_ONLY)
    private String name;

    /*
     * Additional, verbose details about the error to assist in debugging the
     * indexer. This may not be always available.
     */
    @JsonProperty(value = "details", access = JsonProperty.Access.WRITE_ONLY)
    private String details;

    /*
     * A link to a troubleshooting guide for these classes of errors. This may
     * not be always available.
     */
    @JsonProperty(value = "documentationLink", access = JsonProperty.Access.WRITE_ONLY)
    private String documentationLink;

    /**
     * Creates an instance of SearchIndexerError class.
     *
     * @param errorMessage the errorMessage value to set.
     * @param statusCode the statusCode value to set.
     */
    @JsonCreator
    public SearchIndexerError(
            @JsonProperty(value = "errorMessage", required = true, access = JsonProperty.Access.WRITE_ONLY)
                    String errorMessage,
            @JsonProperty(value = "statusCode", required = true, access = JsonProperty.Access.WRITE_ONLY)
                    int statusCode) {
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }

    /**
     * Get the key property: The key of the item for which indexing failed.
     *
     * @return the key value.
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Get the errorMessage property: The message describing the error that occurred while processing the item.
     *
     * @return the errorMessage value.
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * Get the statusCode property: The status code indicating why the indexing operation failed. Possible values
     * include: 400 for a malformed input document, 404 for document not found, 409 for a version conflict, 422 when the
     * index is temporarily unavailable, or 503 for when the service is too busy.
     *
     * @return the statusCode value.
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * Get the name property: The name of the source at which the error originated. For example, this could refer to a
     * particular skill in the attached skillset. This may not be always available.
     *
     * @return the name value.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the details property: Additional, verbose details about the error to assist in debugging the indexer. This
     * may not be always available.
     *
     * @return the details value.
     */
    public String getDetails() {
        return this.details;
    }

    /**
     * Get the documentationLink property: A link to a troubleshooting guide for these classes of errors. This may not
     * be always available.
     *
     * @return the documentationLink value.
     */
    public String getDocumentationLink() {
        return this.documentationLink;
    }
}
