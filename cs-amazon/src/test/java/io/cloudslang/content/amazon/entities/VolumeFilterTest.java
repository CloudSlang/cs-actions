/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities;

import io.cloudslang.content.amazon.entities.aws.VolumeFilter;
import org.junit.Test;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

/**
 * Created by sandorr
 * 2/14/2017.
 */
public class VolumeFilterTest {
    private static final String ATTACHMENT_ATTACH_TIME = "attachment.attach-time";
    private static final String RANDOM_STRING = "randomString";
    private static final String ATTACHMENT_DELETE_ON_TERMINATION = "attachment.delete-on-termination";
    private static final String ATTACHMENT_DEVICE = "attachment.device";
    private static final String ATTACHMENT_INSTANCE_ID = "attachment.instance-id";
    private static final String ATTACHMENT_STATUS = "attachment.status";
    private static final String AVAILABILITY_ZONE = "availability-zone";
    private static final String CREATE_TIME = "create-time";
    private static final String ENCRYPTED = "encrypted";
    private static final String SIZE = "size";
    private static final String SNAPSHOT_ID = "snapshot-id";
    private static final String STATUS = "status";
    private static final String TAG = "tag";
    private static final String TAG_KEY = "tag-key";
    private static final String TAG_VALUE = "tag-value";
    private static final String VOLUME_ID = "volume-id";
    private static final String VOLUME_TYPE = "volume-type";

    @Test
    public void volumeStatusTest() {
        assertEquals(ATTACHMENT_ATTACH_TIME, VolumeFilter.getVolumeFilter(ATTACHMENT_ATTACH_TIME));
        assertEquals(ATTACHMENT_DELETE_ON_TERMINATION, VolumeFilter.getVolumeFilter(ATTACHMENT_DELETE_ON_TERMINATION));
        assertEquals(ATTACHMENT_DEVICE, VolumeFilter.getVolumeFilter(ATTACHMENT_DEVICE));
        assertEquals(ATTACHMENT_INSTANCE_ID, VolumeFilter.getVolumeFilter(ATTACHMENT_INSTANCE_ID));
        assertEquals(ATTACHMENT_STATUS, VolumeFilter.getVolumeFilter(ATTACHMENT_STATUS));
        assertEquals(AVAILABILITY_ZONE, VolumeFilter.getVolumeFilter(AVAILABILITY_ZONE));
        assertEquals(CREATE_TIME, VolumeFilter.getVolumeFilter(CREATE_TIME));
        assertEquals(ENCRYPTED, VolumeFilter.getVolumeFilter(ENCRYPTED));
        assertEquals(SIZE, VolumeFilter.getVolumeFilter(SIZE));
        assertEquals(SNAPSHOT_ID, VolumeFilter.getVolumeFilter(SNAPSHOT_ID));
        assertEquals(STATUS, VolumeFilter.getVolumeFilter(STATUS));
        assertEquals(TAG, VolumeFilter.getVolumeFilter(TAG));
        assertEquals(TAG_KEY, VolumeFilter.getVolumeFilter(TAG_KEY));
        assertEquals(TAG_VALUE, VolumeFilter.getVolumeFilter(TAG_VALUE));
        assertEquals(VOLUME_ID, VolumeFilter.getVolumeFilter(VOLUME_ID));
        assertEquals(VOLUME_TYPE, VolumeFilter.getVolumeFilter(VOLUME_TYPE));

        assertEquals(NOT_RELEVANT, VolumeFilter.getVolumeFilter(EMPTY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void volumeStatusTestException() {
        VolumeFilter.getVolumeFilter(RANDOM_STRING);
    }
}
