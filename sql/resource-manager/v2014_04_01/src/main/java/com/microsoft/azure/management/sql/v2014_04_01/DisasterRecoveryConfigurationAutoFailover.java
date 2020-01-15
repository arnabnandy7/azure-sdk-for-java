/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.sql.v2014_04_01;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.microsoft.rest.ExpandableStringEnum;

/**
 * Defines values for DisasterRecoveryConfigurationAutoFailover.
 */
public final class DisasterRecoveryConfigurationAutoFailover extends ExpandableStringEnum<DisasterRecoveryConfigurationAutoFailover> {
    /** Static value Off for DisasterRecoveryConfigurationAutoFailover. */
    public static final DisasterRecoveryConfigurationAutoFailover OFF = fromString("Off");

    /** Static value On for DisasterRecoveryConfigurationAutoFailover. */
    public static final DisasterRecoveryConfigurationAutoFailover ON = fromString("On");

    /**
     * Creates or finds a DisasterRecoveryConfigurationAutoFailover from its string representation.
     * @param name a name to look for
     * @return the corresponding DisasterRecoveryConfigurationAutoFailover
     */
    @JsonCreator
    public static DisasterRecoveryConfigurationAutoFailover fromString(String name) {
        return fromString(name, DisasterRecoveryConfigurationAutoFailover.class);
    }

    /**
     * @return known DisasterRecoveryConfigurationAutoFailover values
     */
    public static Collection<DisasterRecoveryConfigurationAutoFailover> values() {
        return values(DisasterRecoveryConfigurationAutoFailover.class);
    }
}