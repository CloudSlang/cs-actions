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

import org.junit.Test;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

/**
 * Created by sandorr
 * 2/14/2017.
 */
public class VolumeAttachmentStatusTest {

    private static final String ATTACHING = "attaching";
    private static final String ATTACHED = "attached";
    private static final String DETACHING = "detaching";
    private static final String DETACHED = "detached";
    private static final String WRONG_VALUE = "wrongValue";

    @Test
    public void volumeAttachmentStatusTest() {
        assertEquals(ATTACHING, VolumeAttachmentStatus.getValue(ATTACHING));
        assertEquals(ATTACHED, VolumeAttachmentStatus.getValue(ATTACHED));
        assertEquals(DETACHING, VolumeAttachmentStatus.getValue(DETACHING));
        assertEquals(DETACHED, VolumeAttachmentStatus.getValue(DETACHED));

        assertEquals(NOT_RELEVANT, VolumeAttachmentStatus.getValue(EMPTY));
    }

    @Test(expected = RuntimeException.class)
    public void volumeAttachmentStatusTestException() {
        VolumeAttachmentStatus.getValue(WRONG_VALUE);
    }
}
