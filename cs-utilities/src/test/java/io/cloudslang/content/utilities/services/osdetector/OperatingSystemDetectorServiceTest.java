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

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

/**
 * Created by Tirla Florin-Alin on 08/12/2017.
 **/
@RunWith(PowerMockRunner.class)
public class OperatingSystemDetectorServiceTest {
    @Mock
    private SshOsDetectorService sshOsDetectorService;

    @Mock
    private PowerShellOsDetectorService powershellOsDetectorService;

    @Mock
    private NmapOsDetectorService nmapOsDetectorService;

    @Mock
    private LocalOsDetectorService localOsDetectorService;

    @Mock
    private OsDetectorHelperService osDetectorHelperService;

    private OperatingSystemDetectorService operatingSystemDetectorService;

    @Before
    public void setUp() {
        operatingSystemDetectorService = new OperatingSystemDetectorService(sshOsDetectorService, powershellOsDetectorService, nmapOsDetectorService, localOsDetectorService, osDetectorHelperService);
    }

    @Test
    public void testDetectOsWithAllDetectorsFailed() {
        doReturn(false).when(osDetectorHelperService).foundOperatingSystem(any(OperatingSystemDetails.class));
        doReturn(new OperatingSystemDetails()).when(localOsDetectorService).detectOs(any(OsDetectorInputs.class));
        doReturn(new OperatingSystemDetails()).when(sshOsDetectorService).detectOs(any(OsDetectorInputs.class));
        doReturn(new OperatingSystemDetails()).when(powershellOsDetectorService).detectOs(any(OsDetectorInputs.class));
        doReturn(new OperatingSystemDetails()).when(nmapOsDetectorService).detectOs(any(OsDetectorInputs.class));

        OperatingSystemDetails actualOsDetails = operatingSystemDetectorService.detectOs(new OsDetectorInputs.Builder().build());

        assertEquals("", actualOsDetails.getName());
        assertEquals("", actualOsDetails.getVersion());
        assertEquals("", actualOsDetails.getFamily());
        assertEquals("", actualOsDetails.getArchitecture());
    }
}