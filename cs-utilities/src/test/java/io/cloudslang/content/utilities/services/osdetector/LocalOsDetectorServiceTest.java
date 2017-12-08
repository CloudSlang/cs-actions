/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.utilities.services.osdetector;

import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.apache.commons.lang3.SystemUtils.OS_ARCH;
import static org.apache.commons.lang3.SystemUtils.OS_NAME;
import static org.apache.commons.lang3.SystemUtils.OS_VERSION;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

/**
 * Created by Tirla Florin-Alin on 08/12/2017.
 **/
@RunWith(PowerMockRunner.class)
public class LocalOsDetectorServiceTest {
    @Mock
    private OsDetectorHelperService osDetectorHelperService;

    private LocalOsDetectorService localOsDetectorService;

    @Before
    public void setUp() {
        localOsDetectorService = new LocalOsDetectorService(osDetectorHelperService);
        doReturn("fam").when(osDetectorHelperService).resolveOsFamily(anyString());
    }

    @Test
    public void testDetectWithLocalhost() {
        OperatingSystemDetails detectedOs = localOsDetectorService.detect(new OsDetectorInputs.Builder().withHost("localhost").build());

        performDetectionSuccessChecks(detectedOs);
    }

    private void performDetectionSuccessChecks(OperatingSystemDetails detectedOs) {
        assertEquals(OS_NAME, detectedOs.getName());
        assertEquals(OS_VERSION, detectedOs.getVersion());
        assertEquals(OS_ARCH, detectedOs.getArchitecture());
        assertEquals("fam", detectedOs.getFamily());
    }

    @Test
    public void testDetectWithLocalIpHost() {
        OperatingSystemDetails detectedOs = localOsDetectorService.detect(new OsDetectorInputs.Builder().withHost("127.0.0.1").build());

        performDetectionSuccessChecks(detectedOs);
    }

    @Test
    public void testDetectWithInvalidHost() {
        OperatingSystemDetails detectedOs = localOsDetectorService.detect(new OsDetectorInputs.Builder().withHost("!&(DA").build());

        performDetectionFailChecks(detectedOs);
    }

    private void performDetectionFailChecks(OperatingSystemDetails detectedOs) {
        assertEquals("", detectedOs.getName());
        assertEquals("", detectedOs.getVersion());
        assertEquals("", detectedOs.getArchitecture());
        assertEquals("", detectedOs.getFamily());
    }

    @Test
    public void testDetectWithExternalHost() {
        OperatingSystemDetails detectedOs = localOsDetectorService.detect(new OsDetectorInputs.Builder().withHost("!&(DA").build());

        performDetectionFailChecks(detectedOs);
    }
}