// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.digitaltwins.core;

import com.azure.core.http.HttpClient;
import com.azure.core.http.rest.PagedIterable;
import com.azure.core.util.Context;
import com.azure.core.util.logging.ClientLogger;
import com.azure.digitaltwins.core.models.IncomingRelationship;
import com.azure.digitaltwins.core.models.ModelData;
import com.azure.digitaltwins.core.models.BasicDigitalTwin;
import com.azure.digitaltwins.core.models.BasicRelationship;
import com.azure.digitaltwins.core.models.DigitalTwinsResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.azure.digitaltwins.core.TestAssetDefaults.*;
import static com.azure.digitaltwins.core.TestAssetsHelper.*;
import static com.azure.digitaltwins.core.TestHelper.DISPLAY_NAME_WITH_ARGUMENTS;
import static com.azure.digitaltwins.core.TestHelper.assertRestException;
import static com.azure.digitaltwins.core.helpers.UniqueIdHelper.getUniqueDigitalTwinId;
import static com.azure.digitaltwins.core.helpers.UniqueIdHelper.getUniqueModelId;
import static java.net.HttpURLConnection.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class DigitalTwinsRelationshipTest extends DigitalTwinsRelationshipTestBase {
    private final ClientLogger logger = new ClientLogger(DigitalTwinsRelationshipTest.class);

    @ParameterizedTest(name = DISPLAY_NAME_WITH_ARGUMENTS)
    @MethodSource("com.azure.digitaltwins.core.TestHelper#getTestParameters")
    @Override
    public void relationshipLifecycleTest(HttpClient httpClient, DigitalTwinsServiceVersion serviceVersion) {
        DigitalTwinsClient client = getClient(httpClient, serviceVersion);

        String floorModelId = getUniqueModelId(FLOOR_MODEL_ID_PREFIX, client, randomIntegerStringGenerator);
        String roomModelId = getUniqueModelId(ROOM_MODEL_ID_PREFIX, client, randomIntegerStringGenerator);
        String hvacModelId = getUniqueModelId(HVAC_MODEL_ID_PREFIX, client, randomIntegerStringGenerator);

        String floorTwinId = getUniqueDigitalTwinId(FLOOR_TWIN_ID_PREFIX, client, randomIntegerStringGenerator);
        String roomTwinId = getUniqueDigitalTwinId(ROOM_TWIN_ID_PREFIX, client, randomIntegerStringGenerator);
        String hvacTwinId = getUniqueDigitalTwinId(HVAC_TWIN_ID_PREFIX, client, randomIntegerStringGenerator);

        try {
            // Create floor, room and hvac model
            createModelsRunner(
                floorModelId,
                roomModelId,
                hvacModelId,
                modelsList -> {
                    List<ModelData> createdModels = client.createModels(modelsList);
                    logger.info("Created {} models successfully", createdModels.size());
                }
            );

            // Create floor twin
            createFloorTwinRunner(
                floorTwinId,
                floorModelId,
                (twinId, twin) -> {
                    BasicDigitalTwin createdTwin = client.createDigitalTwin(twinId, twin, BasicDigitalTwin.class);
                    logger.info("Created {} twin successfully", createdTwin.getId());
                }
            );

            // Create room twin
            createRoomTwinRunner(
                roomTwinId,
                roomModelId,
                (twinId, twin) -> {
                    BasicDigitalTwin createdTwin = client.createDigitalTwin(twinId, twin, BasicDigitalTwin.class);
                    logger.info("Created {} twin successfully", createdTwin.getId());
                }
            );

            // Create hvac twin
            createHvacTwinRunner(
                hvacTwinId,
                hvacModelId,
                (twinId, twin) -> {
                    BasicDigitalTwin createdTwin = client.createDigitalTwin(twinId, twin, BasicDigitalTwin.class);
                    logger.info("Created {} twin successfully", createdTwin.getId());
                }
            );

            // Connect the created twins via relationships
            String floorContainsRoomPayload = getRelationshipWithPropertyPayload(roomTwinId, CONTAINS_RELATIONSHIP, "isAccessRestricted", true);
            String floorTwinCoolsRelationshipPayload = getRelationshipPayload(floorTwinId, COOLS_RELATIONSHIP);
            String floorTwinContainedInRelationshipPayload = getRelationshipPayload(floorTwinId, CONTAINED_IN_RELATIONSHIP);
            String floorCooledByHvacPayload = getRelationshipPayload(hvacTwinId, COOLED_BY_RELATIONSHIP);
            List<Object> floorContainsRoomUpdatePayload = getRelationshipUpdatePayload("/isAccessRestricted", false);

            // Create relationship from Floor -> Room
            BasicRelationship floorRoomRelationship = client.createRelationship(floorTwinId, FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID, floorContainsRoomPayload, BasicRelationship.class);
            assertThat(floorRoomRelationship.getId())
                .isEqualTo(FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID)
                .as("Created relationship from floor -> room");
            logger.info("Created {} relationship between source = {} and target = {}", floorRoomRelationship.getId(), floorRoomRelationship.getSourceId(), floorRoomRelationship.getTargetId());

            // Create relationship from Floor -> Hvac
            BasicRelationship floorHvacRelationship = client.createRelationship(floorTwinId, FLOOR_COOLED_BY_HVAC_RELATIONSHIP_ID, floorCooledByHvacPayload, BasicRelationship.class);
            assertThat(floorHvacRelationship.getId())
                .isEqualTo(FLOOR_COOLED_BY_HVAC_RELATIONSHIP_ID)
                .as("Created relationship from floor -> hvac");
            logger.info("Created {} relationship between source = {} and target = {}", floorHvacRelationship.getId(), floorHvacRelationship.getSourceId(), floorHvacRelationship.getTargetId());

            // Create relationship from Hvac -> Floor
            BasicRelationship hvacFloorRelationship = client.createRelationship(hvacTwinId, HVAC_COOLS_FLOOR_RELATIONSHIP_ID, floorTwinCoolsRelationshipPayload, BasicRelationship.class);
            assertThat(hvacFloorRelationship.getId())
                .isEqualTo(HVAC_COOLS_FLOOR_RELATIONSHIP_ID)
                .as("Created relationship from hvac -> floor");
            logger.info("Created {} relationship between source = {} and target = {}", hvacFloorRelationship.getId(), hvacFloorRelationship.getSourceId(), hvacFloorRelationship.getTargetId());

            // Create relationship from Room -> Floor
            BasicRelationship roomFloorRelationship = client.createRelationship(roomTwinId, ROOM_CONTAINED_IN_FLOOR_RELATIONSHIP_ID, floorTwinContainedInRelationshipPayload, BasicRelationship.class);
            assertThat(roomFloorRelationship.getId())
                .isEqualTo(ROOM_CONTAINED_IN_FLOOR_RELATIONSHIP_ID)
                .as("Created relationship from room -> floor");
            logger.info("Created {} relationship between source = {} and target = {}", roomFloorRelationship.getId(), roomFloorRelationship.getSourceId(), roomFloorRelationship.getTargetId());

            // Create a relation which already exists - should return status code 409 (Conflict).
            assertRestException(
                () -> client.createRelationship(roomTwinId, ROOM_CONTAINED_IN_FLOOR_RELATIONSHIP_ID, floorTwinContainedInRelationshipPayload),
                HTTP_PRECON_FAILED
            );

            // Update relationships

            // Create relationship from Floor -> Room
            DigitalTwinsResponse<Void> updateRelationshipResponse = client.updateRelationshipWithResponse(floorTwinId, FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID, floorContainsRoomUpdatePayload, null, Context.NONE);
            assertThat(updateRelationshipResponse.getStatusCode())
                .as("Updated relationship floor -> room")
                .isEqualTo(HTTP_NO_CONTENT);
            logger.info("Updated {} relationship successfully in source {}", FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID, floorTwinId);

            // GET relationship
            BasicRelationship floorContainsRoomRelationship = client.getRelationship(floorTwinId, FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID, BasicRelationship.class);
            assertThat(floorContainsRoomRelationship.getId())
                .isEqualTo(FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID)
                .as("Retrieved floor -> room relationship");
            logger.info("Retrieved {} relationship under source {}", floorContainsRoomRelationship.getId(), floorContainsRoomRelationship.getSourceId());

            // LIST incoming relationships
            List<String> incomingRelationshipsSourceIds = new ArrayList<>();
            PagedIterable<IncomingRelationship> listIncomingRelationships = client.listIncomingRelationships(floorTwinId);
            listIncomingRelationships.forEach(incomingRelationship -> incomingRelationshipsSourceIds.add(incomingRelationship.getSourceId()));
            assertThat(incomingRelationshipsSourceIds)
                .as("Floor has incoming relationships from room and hvac")
                .containsExactlyInAnyOrder(roomTwinId, hvacTwinId);
            logger.info("Retrieved incoming relationships for {}, found sources {}", floorTwinId, Arrays.toString(incomingRelationshipsSourceIds.toArray()));

            // LIST relationships
            List<String> relationshipsTargetIds = new ArrayList<>();
            PagedIterable<BasicRelationship> listRelationships = client.listRelationships(floorTwinId, BasicRelationship.class);
            listRelationships.forEach(basicRelationship -> relationshipsTargetIds.add(basicRelationship.getTargetId()));
            assertThat(relationshipsTargetIds)
                .as("Floor has a relationship to room and hvac")
                .containsExactlyInAnyOrder(roomTwinId, hvacTwinId);
            logger.info("Retrieved all relationships for {}, found targets {}", floorTwinId, Arrays.toString(relationshipsTargetIds.toArray()));

            // LIST relationship by name
            List<String> containedInRelationshipsTargetIds = new ArrayList<>();
            PagedIterable<BasicRelationship> listContainedInRelationship = client.listRelationships(roomTwinId, CONTAINED_IN_RELATIONSHIP, BasicRelationship.class, Context.NONE);
            listContainedInRelationship.forEach(basicRelationship -> {
                containedInRelationshipsTargetIds.add(basicRelationship.getTargetId());
                logger.info("Retrieved relationship {} for twin {}", basicRelationship.getId(), roomTwinId);
            });
            assertThat(containedInRelationshipsTargetIds.size())
                .as("Room has only one containedIn relationship to floor")
                .isEqualTo(1);

            // DELETE the created relationships
            client.deleteRelationship(floorTwinId, FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID);
            logger.info("Deleted relationship {} for twin {}", FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID, floorTwinId);

            client.deleteRelationship(roomTwinId, ROOM_CONTAINED_IN_FLOOR_RELATIONSHIP_ID);
            logger.info("Deleted relationship {} for twin {}", ROOM_CONTAINED_IN_FLOOR_RELATIONSHIP_ID, roomTwinId);

            client.deleteRelationship(floorTwinId, FLOOR_COOLED_BY_HVAC_RELATIONSHIP_ID);
            logger.info("Deleted relationship {} for twin {}", FLOOR_COOLED_BY_HVAC_RELATIONSHIP_ID, floorTwinId);

            client.deleteRelationship(hvacTwinId, HVAC_COOLS_FLOOR_RELATIONSHIP_ID);
            logger.info("Deleted relationship {} for twin {}", HVAC_COOLS_FLOOR_RELATIONSHIP_ID, hvacTwinId);

            // GET a relationship which doesn't exist - should return status code 404 (Not Found).
            assertRestException(
                () -> client.getRelationship(floorTwinId, FLOOR_CONTAINS_ROOM_RELATIONSHIP_ID),
                HTTP_NOT_FOUND
            );

        } finally {
            // Clean up
            try {
                logger.info("Cleaning up test resources.");

                logger.info("Deleting created relationships.");
                // Delete the created relationships.
                List<BasicRelationship> relationships = new ArrayList<>();
                client.listRelationships(floorTwinId, BasicRelationship.class)
                    .iterableByPage()
                    .forEach(basicRelationshipPagedResponse -> relationships.addAll(basicRelationshipPagedResponse.getValue()));
                client.listRelationships(roomTwinId, BasicRelationship.class)
                    .iterableByPage()
                    .forEach(basicRelationshipPagedResponse -> relationships.addAll(basicRelationshipPagedResponse.getValue()));
                client.listRelationships(hvacTwinId, BasicRelationship.class)
                    .iterableByPage()
                    .forEach(basicRelationshipPagedResponse -> relationships.addAll(basicRelationshipPagedResponse.getValue()));
                relationships.forEach(basicRelationship -> client.deleteRelationship(basicRelationship.getSourceId(), basicRelationship.getId()));

                // Now the twins and models can be deleted.
                logger.info("Deleting created digital twins.");
                client.deleteDigitalTwin(floorTwinId);
                client.deleteDigitalTwin(roomTwinId);
                client.deleteDigitalTwin(hvacTwinId);

                logger.info("Deleting created models.");
                client.deleteModel(floorModelId);
                client.deleteModel(roomModelId);
                client.deleteModel(hvacModelId);
            }
            catch (Exception ex) {
                fail("Test cleanup failed", ex);
            }
        }
    }
}
