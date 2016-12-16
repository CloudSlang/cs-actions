/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.aws;

import static org.apache.commons.lang3.StringUtils.isBlank;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
/**
 * Created by Mihai Tusa.
 * 9/15/2016.
 */
public enum InstanceInitiatedShutdownBehavior {
    STOP,
    TERMINATE;

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (InstanceInitiatedShutdownBehavior member : InstanceInitiatedShutdownBehavior.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException("Unrecognized Instance Initiated Shutdown Behavior value: [" + input + "]. " +
                "Valid values are: stop, terminate.");

    }
}
