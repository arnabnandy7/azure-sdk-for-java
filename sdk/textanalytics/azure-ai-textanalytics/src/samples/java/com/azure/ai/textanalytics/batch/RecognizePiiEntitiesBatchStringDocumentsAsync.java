// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.ai.textanalytics.batch;

import com.azure.ai.textanalytics.TextAnalyticsAsyncClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.PiiEntityCollection;
import com.azure.ai.textanalytics.models.RecognizePiiEntitiesResult;
import com.azure.ai.textanalytics.models.RecognizePiiEntityOptions;
import com.azure.ai.textanalytics.models.TextDocumentBatchStatistics;
import com.azure.core.credential.AzureKeyCredential;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sample demonstrates how to recognize the PII(Personally Identifiable Information) entities of {@code String} documents.
 */
public class RecognizePiiEntitiesBatchStringDocumentsAsync {
    /**
     * Main method to invoke this demo about how to recognize the Personally Identifiable Information entities of
     * documents.
     *
     * @param args Unused arguments to the program.
     */
    public static void main(String[] args) {
        // Instantiate a client that will be used to call the service.
        TextAnalyticsAsyncClient client = new TextAnalyticsClientBuilder()
            .credential(new AzureKeyCredential("{key}"))
            .endpoint("{endpoint}")
            .buildAsyncClient();

        // The texts that need be analyzed.
        List<String> documents = Arrays.asList(
            "My SSN is 859-98-0987",
            "Visa card 4111 1111 1111 1111"
        );

        // Show statistics and model version
        RecognizePiiEntityOptions options = new RecognizePiiEntityOptions().setIncludeStatistics(true).setModelVersion("latest");

        // Recognizing Personally Identifiable Information entities for each document in a batch of documents
        AtomicInteger counter = new AtomicInteger();
        client.recognizePiiEntitiesBatch(documents, "en", options).subscribe(
            recognizePiiEntitiesResultCollection -> {
                // Model version
                System.out.printf("Results of Azure Text Analytics \"Personally Identifiable Information Entities Recognition\" Model, version: %s%n", recognizePiiEntitiesResultCollection.getModelVersion());

                // Batch statistics
                TextDocumentBatchStatistics batchStatistics = recognizePiiEntitiesResultCollection.getStatistics();
                System.out.printf("Documents statistics: document count = %s, erroneous document count = %s, transaction count = %s, valid document count = %s.%n",
                    batchStatistics.getDocumentCount(), batchStatistics.getInvalidDocumentCount(), batchStatistics.getTransactionCount(), batchStatistics.getValidDocumentCount());

                for (RecognizePiiEntitiesResult entitiesResult : recognizePiiEntitiesResultCollection) {
                    System.out.printf("%nText = %s%n", documents.get(counter.getAndIncrement()));
                    if (entitiesResult.isError()) {
                        // Erroneous document
                        System.out.printf("Cannot recognize Personally Identifiable Information entities. Error: %s%n", entitiesResult.getError().getMessage());
                    } else {
                        // Valid document
                        PiiEntityCollection piiEntityCollection = entitiesResult.getEntities();
                        System.out.printf("Redacted Text: %s%n", piiEntityCollection.getRedactedText());
                        piiEntityCollection.forEach(entity -> System.out.printf(
                            "Recognized Personally Identifiable Information entity: %s, entity category: %s, entity subcategory: %s, offset: %s, length: %s, confidence score: %f.%n",
                            entity.getText(), entity.getCategory(), entity.getSubcategory(), entity.getOffset(), entity.getLength(), entity.getConfidenceScore()));
                    }
                }
            },
            error -> System.err.println("There was an error recognizing Personally Identifiable Information entities of the documents." + error),
            () -> System.out.println("Batch of Personally Identifiable Information entities recognized."));

        // The .subscribe() creation and assignment is not a blocking call. For the purpose of this example, we sleep
        // the thread so the program does not end before the send operation is complete. Using .block() instead of
        // .subscribe() will turn this into a synchronous call.
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ignored) {
        }
    }
}
