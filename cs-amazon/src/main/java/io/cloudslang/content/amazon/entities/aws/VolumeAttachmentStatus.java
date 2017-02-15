/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.aws;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by sandorr
 * 2/14/2017.
 */
public enum VolumeAttachmentStatus {
    ATTACHING,
    ATTACHED,
    DETACHING,
    DETACHED;

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (VolumeAttachmentStatus volumeAttachmentStatus : VolumeAttachmentStatus.values()) {
            if (volumeAttachmentStatus.name().equalsIgnoreCase(input)) {
                return volumeAttachmentStatus.name().toLowerCase();
            }
        }

        throw new RuntimeException("Invalid attachment.status value: [" + input + "]. Valid values: attaching, attached, " +
                "detaching, detached.");
    }
}
