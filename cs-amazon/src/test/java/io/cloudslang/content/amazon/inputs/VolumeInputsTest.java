/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.amazon.inputs;

import io.cloudslang.content.amazon.entities.inputs.VolumeInputs;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by sandorr
 * 2/14/2017.
 */
public class VolumeInputsTest {

    private static final String DESCRIPTION = "description";
    private static final String DEVICE_NAME = "deviceName";
    private static final int MAX_RESULTS = 10;
    private static final int SIZE = 1024;

    @Test
    public void volumeInputsBuilderTest() {
        final VolumeInputs volumeInputs = new VolumeInputs.Builder()
                .withDescription(DESCRIPTION)
                .withDeviceName(DEVICE_NAME)
                .withEncrypted(BooleanUtils.toStringTrueFalse(true))
                .withForce(BooleanUtils.toStringTrueFalse(false))
                .withIops(EMPTY)
                .withMaxResults(String.valueOf(MAX_RESULTS))
                .withSize(String.valueOf(SIZE))
                .withNextToken(EMPTY)
                .build();

        assertEquals(DESCRIPTION, volumeInputs.getDescription());
        assertEquals(DEVICE_NAME, volumeInputs.getDeviceName());
        assertEquals(NOT_RELEVANT, volumeInputs.getIops());
        assertEquals(EMPTY, volumeInputs.getNextToken());

        assertEquals(String.valueOf(MAX_RESULTS), volumeInputs.getMaxResults());
        assertEquals(String.valueOf(SIZE), volumeInputs.getSize());

        assertTrue(volumeInputs.isEncrypted());
        assertFalse(volumeInputs.isForce());
    }
}
