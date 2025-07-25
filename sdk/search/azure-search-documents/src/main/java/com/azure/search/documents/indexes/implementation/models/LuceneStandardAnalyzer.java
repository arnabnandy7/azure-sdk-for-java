// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.
// Changes may cause incorrect behavior and will be lost if the code is
// regenerated.

package com.azure.search.documents.indexes.implementation.models;

import com.azure.core.annotation.Fluent;
import com.azure.core.annotation.JsonFlatten;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;

/** The LuceneStandardAnalyzer model. */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@odata\\.type")
@JsonTypeName("#Microsoft.Azure.Search.StandardAnalyzer")
@JsonFlatten
@Fluent
public class LuceneStandardAnalyzer extends LexicalAnalyzer {
    /*
     * The maximum token length. Default is 255. Tokens longer than the maximum
     * length are split. The maximum token length that can be used is 300
     * characters.
     */
    @JsonProperty(value = "maxTokenLength")
    private Integer maxTokenLength;

    /*
     * A list of stopwords.
     */
    @JsonProperty(value = "stopwords")
    private List<String> stopwords;

    /**
     * Creates an instance of LuceneStandardAnalyzer class.
     *
     * @param name the name value to set.
     */
    @JsonCreator
    public LuceneStandardAnalyzer(@JsonProperty(value = "name", required = true) String name) {
        super(name);
    }

    /**
     * Get the maxTokenLength property: The maximum token length. Default is 255. Tokens longer than the maximum length
     * are split. The maximum token length that can be used is 300 characters.
     *
     * @return the maxTokenLength value.
     */
    public Integer getMaxTokenLength() {
        return this.maxTokenLength;
    }

    /**
     * Set the maxTokenLength property: The maximum token length. Default is 255. Tokens longer than the maximum length
     * are split. The maximum token length that can be used is 300 characters.
     *
     * @param maxTokenLength the maxTokenLength value to set.
     * @return the LuceneStandardAnalyzer object itself.
     */
    public LuceneStandardAnalyzer setMaxTokenLength(Integer maxTokenLength) {
        this.maxTokenLength = maxTokenLength;
        return this;
    }

    /**
     * Get the stopwords property: A list of stopwords.
     *
     * @return the stopwords value.
     */
    public List<String> getStopwords() {
        return this.stopwords;
    }

    /**
     * Set the stopwords property: A list of stopwords.
     *
     * @param stopwords the stopwords value to set.
     * @return the LuceneStandardAnalyzer object itself.
     */
    public LuceneStandardAnalyzer setStopwords(List<String> stopwords) {
        this.stopwords = stopwords;
        return this;
    }
}
