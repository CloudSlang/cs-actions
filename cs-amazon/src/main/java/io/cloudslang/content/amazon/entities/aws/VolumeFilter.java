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
public enum VolumeFilter {
    ATTACHMENT_ATTACH_TIME("attachment.attach-time"),
    ATTACHMENT_DELETE_ON_TERMINATION("attachment.delete-on-termination"),
    ATTACHMENT_DEVICE("attachment.device"),
    ATTACHMENT_INSTANCE_ID("attachment.instance-id"),
    ATTACHMENT_STATUS("attachment.status"),
    AVAILABILITY_ZONE("availability-zone"),
    CREATE_TIME("create-time"),
    ENCRYPTED("encrypted"),
    SIZE("size"),
    SNAPSHOT_ID("snapshot-id"),
    STATUS("status"),
    TAG("tag"),
    TAG_KEY("tag-key"),
    TAG_VALUE("tag-value"),
    VOLUME_ID("volume-id"),
    VOLUME_TYPE("volume-type");

    private final String value;

    VolumeFilter(String value) {
        this.value = value;
    }

    public static String getVolumeFilter(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (VolumeFilter volumeFilter : VolumeFilter.values()) {
            if (volumeFilter.getValue().equalsIgnoreCase(input)) {
                return volumeFilter.getValue();
            }
        }

        throw new IllegalArgumentException("Invalid filter value: [" + input + "]. Valid values are: attachment.attach-time | " +
                "attachment.delete-on-termination | attachment.device | attachment.instance-id | attachment.status | " +
                "availability-zone | create-time | encrypted | size | snapshot-id | status | tag | tag-key | tag-value | " +
                "volume-id | volume-type ");
    }

    public String getValue() {
        return value;
    }
}
