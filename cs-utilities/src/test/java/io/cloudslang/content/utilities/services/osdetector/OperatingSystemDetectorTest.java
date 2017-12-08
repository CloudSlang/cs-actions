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
public class OperatingSystemDetectorTest {
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

    private OperatingSystemDetector operatingSystemDetector;

    @Before
    public void setUp() {
        operatingSystemDetector = new OperatingSystemDetector(sshOsDetectorService, powershellOsDetectorService, nmapOsDetectorService, localOsDetectorService, osDetectorHelperService);
    }

    @Test
    public void testDetectOsWithAllDetectorsFailed() {
        doReturn(false).when(osDetectorHelperService).foundOperatingSystem(any(OperatingSystemDetails.class));
        doReturn(new OperatingSystemDetails()).when(localOsDetectorService).detect(any(OsDetectorInputs.class));
        doReturn(new OperatingSystemDetails()).when(sshOsDetectorService).detect(any(OsDetectorInputs.class));
        doReturn(new OperatingSystemDetails()).when(powershellOsDetectorService).detect(any(OsDetectorInputs.class));
        doReturn(new OperatingSystemDetails()).when(nmapOsDetectorService).detect(any(OsDetectorInputs.class));

        OperatingSystemDetails actualOsDetails = operatingSystemDetector.detectOs(new OsDetectorInputs.Builder().build());

        assertEquals("", actualOsDetails.getName());
        assertEquals("", actualOsDetails.getVersion());
        assertEquals("", actualOsDetails.getFamily());
        assertEquals("", actualOsDetails.getArchitecture());
    }
}