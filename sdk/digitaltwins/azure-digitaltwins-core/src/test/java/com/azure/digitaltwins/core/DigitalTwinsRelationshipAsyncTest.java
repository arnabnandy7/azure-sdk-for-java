// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.digitaltwins.core;

import com.azure.core.http.HttpClient;
import com.azure.core.util.logging.ClientLogger;
import com.azure.digitaltwins.core.models.BasicDigitalTwin;
import com.azure.digitaltwins.core.models.BasicRelationship;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.azure.digitaltwins.core.TestAssetDefaults.*;
import static com.azure.digitaltwins.core.TestAssetsHelper.*;
import static com.azure.digitaltwins.core.TestHelper.DISPLAY_NAME_WITH_ARGUMENTS;
import static com.azure.digitaltwins.core.TestHelper.assertRestException;
import static com.azure.digitaltwins.core.helpers.UniqueIdHelper.getUniqueDigitalTwinId;
import static com.azure.digitaltwins.core.helpers.UniqueIdHelper.getUniqueModelId;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_PRECON_FAILED;
import static javax.net.ssl.HttpsURLConnection.HTTP_NO_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class DigitalTwinsRelationshipAsyncTest extends DigitalTwinsRelationshipTestBase {
    private final ClientLogger logger = new ClientLogger(DigitalTwinsRelationshipAsyncTest.class);

    @ParameterizedTest(name = DISPLAY_NAME_WITH_ARGUMENTS)
    @MethodSource("com.azure.digitaltwins.core.TestHelper#getTestParameters")
    @Override
    public void relationshipLifecycleTest(HttpClient httpClient, DigitalTwinsServiceVersion serviceVersion) {
        DigitalTwinsAsyncClient asyncClient = getAsyncClient(httpClient, serviceVersion);

        String floorModelId = getUniqueModelId(FLOOR_MODEL_ID_PREFIX, asyncClient, randomIntegerStringGenerator);
        String roomModelId = getUniqueModelId(ROOM_MODEL_ID_PREFIX, asyncClient, randomIntegerStringGenerator);
        String hvacModelId = getUniqueModelId(HVAC_MODEL_ID_PREFIX, asyncClient, randomIntegerStringGenerator);

        String floorTwinId = getUniqueDigitalTwinId(FLOOR_TWIN_ID_PREFIX, asyncClient, randomIntegerStringGenerator);
        String roomTwinId = getUniqueDigitalTwinId(ROOM_TWIN_ID_PREFIX, asyncClient, randomIntegerStringGenerator);
        String hvacTwinId = getUniqueDigitalTwinId(HVAC_TWIN_ID_PREFIX, asyncClient, randomIntegerStringGenerator);

        try {
            // Create floor, room and hvac model
            createModelsRunner(
                floorModelId,
                roomModelId,
                hvacModelId,
                modelsList -> StepVerifier
                    .create(asyncClient.createModels(modelsList))
                    .assertNext(createResponseList -> logger.info("Created {} models successfully", createResponseList.size()))
                    .verifyComplete());

            // Create floor twin
            createFloorTwinRunner(
                floorTwinId,
                floorModelId,
                (twinId, twin) -> StepVerifier
                    .create(asyncClient.createDigitalTwin(twinId, twin, BasicDigitalTwin.class))
                    .assertNext(basicDigitalTwin -> logger.info("Created {} twin successfully", basicDigitalTwin.getId()))
                    .verifyComplete());

            // Create room twin
            createRoomTwinRunner(
                roomTwinId,
                roomModelId,
                (twinId, twin) -> StepVerifier
                    .create(asyncClient.createDigitalTwin(twinId, twin, BasicDigitalTwin.class))
                    .assertNext(basicDigitalTwin -> logger.info("Created {} twin successfully", basicDigitalTwin.getId()))
                    .verifyComplete());

            // Create hvac twin
            createHvacTwinRunner(
                hvacTwinId,
                hvacModelId,
                (twinId, twin) -> StepVerifier
                    .create(asyncClient.createDigitalTwin(twinId, twin, BasicDigitalTwin.class))
                    .assertNext(basicDigitalTwin -> logger.info("Created {} twin successfully", basicDigitalTwin.getId()))
                    .verifyComplete());

            // Connect the created twins via relationships
            String floorContainsRoomPayload = getRelationshipWithPropertyPayload(roomTwinId, CONTAINS_RELATIONSHIP, "isAccessRestricted", true);
            String floorTwinCoolsRelationshipPayload = getRelationshipPayload(floorTwinId, COOLS_RELATIONSHIP);
            String floorTwinContainedInRelationshipPayload = getRelationshipPayload(floorTwinId, CONTAINED_IN_RELATIONSHIP);
            String floorCooledByHvacPayload = getRelationshipPayload(hvacTwinId, COOLED_BY_RELATIONSHIP);
            List<Object> floorContainsRoomUpdatePayload = getRelationshipUpdatePayload("/isAccessRestricted", false);

            // Create relationship from Floor -> Room
            StepVerifier
                .create(asyncClient.createRelationship(floorTwinId, FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID, floorContainsRoomPayload, BasicRelationship.class))
                .assertNext(
                    basicRelationship -> {
                        assertThat(basicRelationship.getId())
                            .isEqualTo(FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID)
                            .as("Created relationship from floor -> room");
                        logger.info("Created {} relationship between source = {} and target = {}", basicRelationship.getId(), basicRelationship.getSourceId(), basicRelationship.getTargetId());
                    }
                )
                .verifyComplete();

            // Create relationship from Floor -> Hvac
            StepVerifier
                .create(asyncClient.createRelationship(floorTwinId, FLOOR_COOLED_BY_HVAC_RELATIONSHIP_ID, floorCooledByHvacPayload, BasicRelationship.class))
                .assertNext(
                    basicRelationship -> {
                        assertThat(basicRelationship.getId())
                            .isEqualTo(FLOOR_COOLED_BY_HVAC_RELATIONSHIP_ID)
                            .as("Created relationship from floor -> hvac");
                        logger.info("Created {} relationship between source = {} and target = {}", basicRelationship.getId(), basicRelationship.getSourceId(), basicRelationship.getTargetId());
                    }
                )
                .verifyComplete();

            // Create relationship from Hvac -> Floor
            StepVerifier
                .create(asyncClient.createRelationship(hvacTwinId, HVAC_COOLS_FLOOR_RELATIONSHIP_ID, floorTwinCoolsRelationshipPayload, BasicRelationship.class))
                .assertNext(
                    basicRelationship -> {
                        assertThat(basicRelationship.getId())
                            .isEqualTo(HVAC_COOLS_FLOOR_RELATIONSHIP_ID)
                            .as("Created relationship from hvac -> floor");
                        logger.info("Created {} relationship between source = {} and target = {}", basicRelationship.getId(), basicRelationship.getSourceId(), basicRelationship.getTargetId());
                    }
                )
                .verifyComplete();

            // Create relationship from Room -> Floor
            StepVerifier
                .create(asyncClient.createRelationship(roomTwinId, ROOM_CONTAINED_IN_FLOOR_RELATIONSHIP_ID, floorTwinContainedInRelationshipPayload, BasicRelationship.class))
                .assertNext(
                    basicRelationship -> {
                        assertThat(basicRelationship.getId())
                            .isEqualTo(ROOM_CONTAINED_IN_FLOOR_RELATIONSHIP_ID)
                            .as("Created relationship from room -> floor");
                        logger.info("Created {} relationship between source = {} and target = {}", basicRelationship.getId(), basicRelationship.getSourceId(), basicRelationship.getTargetId());
                    }
                )
                .verifyComplete();

            // Create a relation which already exists - should return status code 409 (Conflict).
            StepVerifier.create(asyncClient.createRelationship(roomTwinId, ROOM_CONTAINED_IN_FLOOR_RELATIONSHIP_ID, floorTwinContainedInRelationshipPayload))
                .verifyErrorSatisfies(ex -> assertRestException(ex, HTTP_PRECON_FAILED));

            // Update relationships

            // Create relationship from Floor -> Room
            StepVerifier
                .create(asyncClient.updateRelationshipWithResponse(floorTwinId, FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID, floorContainsRoomUpdatePayload, null))
                .assertNext(
                    voidDigitalTwinsResponse -> {
                        assertThat(voidDigitalTwinsResponse.getStatusCode())
                            .as("Updated relationship floor -> room")
                            .isEqualTo(HTTP_NO_CONTENT);
                        logger.info("Updated {} relationship successfully in source {}", FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID, floorTwinId);
                    }
                )
                .verifyComplete();

            // GET relationship
            StepVerifier
                .create(asyncClient.getRelationship(floorTwinId, FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID, BasicRelationship.class))
                .assertNext(basicRelationship -> {
                    assertThat(basicRelationship.getId())
                        .isEqualTo(FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID)
                        .as("Retrieved floor -> room relationship");
                    logger.info("Retrieved {} relationship under source {}", basicRelationship.getId(), basicRelationship.getSourceId());
                })
                .verifyComplete();

            // LIST incoming relationships
            List<String> incomingRelationshipsSourceIds = new ArrayList<>();
            StepVerifier
                .create(asyncClient.listIncomingRelationships(floorTwinId))
                .assertNext(incomingRelationship -> incomingRelationshipsSourceIds.add(incomingRelationship.getSourceId()))
                .assertNext(incomingRelationship -> incomingRelationshipsSourceIds.add(incomingRelationship.getSourceId()))
                .expectComplete()
                .verify();
            assertThat(incomingRelationshipsSourceIds)
                .as("Floor has incoming relationships from room and hvac")
                .containsExactlyInAnyOrder(roomTwinId, hvacTwinId);
            logger.info("Retrieved incoming relationships for {}, found sources {}", floorTwinId, Arrays.toString(incomingRelationshipsSourceIds.toArray()));

            // LIST relationships
            List<String> relationshipsTargetIds = new ArrayList<>();
            StepVerifier
                .create(asyncClient.listRelationships(floorTwinId, BasicRelationship.class))
                .assertNext(basicRelationship -> relationshipsTargetIds.add(basicRelationship.getTargetId()))
                .assertNext(basicRelationship -> relationshipsTargetIds.add(basicRelationship.getTargetId()))
                .expectComplete()
                .verify();
            assertThat(relationshipsTargetIds)
                .as("Floor has a relationship to room and hvac")
                .containsExactlyInAnyOrder(roomTwinId, hvacTwinId);
            logger.info("Retrieved all relationships for {}, found targets {}", floorTwinId, Arrays.toString(relationshipsTargetIds.toArray()));

            // LIST relationship by name
            StepVerifier
                .create(asyncClient.listRelationships(roomTwinId, CONTAINED_IN_RELATIONSHIP, BasicRelationship.class))
                .assertNext(basicRelationship -> {
                    assertThat(basicRelationship.getName())
                        .isEqualTo(CONTAINED_IN_RELATIONSHIP)
                        .as("Room has only one containedIn relationship to floor");
                    assertThat(basicRelationship.getTargetId())
                        .isEqualTo(floorTwinId)
                        .as("Room has only one containedIn relationship to floor");
                    logger.info("Retrieved relationship {} for twin {}", basicRelationship.getId(), roomTwinId);
                })
                .expectComplete()
                .verify();

            // DELETE the created relationships
            StepVerifier
                .create(asyncClient.deleteRelationship(floorTwinId, FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID))
                .verifyComplete();
            logger.info("Deleted relationship {} for twin {}", FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID, floorTwinId);

            StepVerifier
                .create(asyncClient.deleteRelationship(roomTwinId, ROOM_CONTAINED_IN_FLOOR_RELATIONSHIP_ID))
                .verifyComplete();
            logger.info("Deleted relationship {} for twin {}", ROOM_CONTAINED_IN_FLOOR_RELATIONSHIP_ID, roomTwinId);

            StepVerifier
                .create(asyncClient.deleteRelationship(floorTwinId, FLOOR_COOLED_BY_HVAC_RELATIONSHIP_ID))
                .verifyComplete();
            logger.info("Deleted relationship {} for twin {}", FLOOR_COOLED_BY_HVAC_RELATIONSHIP_ID, floorTwinId);

            StepVerifier
                .create(asyncClient.deleteRelationship(hvacTwinId, HVAC_COOLS_FLOOR_RELATIONSHIP_ID))
                .verifyComplete();
            logger.info("Deleted relationship {} for twin {}", HVAC_COOLS_FLOOR_RELATIONSHIP_ID, hvacTwinId);

            // GET a relationship which doesn't exist - should return status code 404 (Not Found).
            StepVerifier
                .create(asyncClient.getRelationship(floorTwinId, FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID))
                .verifyErrorSatisfies(ex -> assertRestException(ex, HTTP_NOT_FOUND));

        } finally {
            // Clean up
            try {
                logger.info("Cleaning up test resources.");

                logger.info("Deleting created relationships.");
                // Delete the created relationships.
                List<BasicRelationship> relationships = new ArrayList<>();
                asyncClient.listRelationships(floorTwinId, BasicRelationship.class)
                    .doOnNext(relationships::add)
                    .blockLast();
                asyncClient.listRelationships(roomTwinId, BasicRelationship.class)
                    .doOnNext(relationships::add)
                    .blockLast();
                asyncClient.listRelationships(hvacTwinId, BasicRelationship.class)
                    .doOnNext(relationships::add)
                    .blockLast();
                relationships.forEach(basicRelationship -> asyncClient.deleteRelationship(basicRelationship.getSourceId(), basicRelationship.getId()).block());

                // Now the twins and models can be deleted.
                logger.info("Deleting created digital twins.");
                asyncClient.deleteDigitalTwin(floorTwinId).block();
                asyncClient.deleteDigitalTwin(roomTwinId).block();
                asyncClient.deleteDigitalTwin(hvacTwinId).block();

                logger.info("Deleting created models.");
                asyncClient.deleteModel(floorModelId).block();
                asyncClient.deleteModel(roomModelId).block();
                asyncClient.deleteModel(hvacModelId).block();
            }
            catch (Exception ex) {
                fail("Test cleanup failed", ex);
            }
        }
    }
}
