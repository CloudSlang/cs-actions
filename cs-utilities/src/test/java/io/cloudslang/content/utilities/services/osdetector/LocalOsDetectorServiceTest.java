package io.cloudslang.content.utilities.services.osdetector;

import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;
import io.cloudslang.content.utilities.util.OsDetectorUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
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
@PrepareForTest({LocalOsDetectorService.class})
public class LocalOsDetectorServiceTest {
    @Mock
    private OsDetectorUtils osDetectorUtils;

    private LocalOsDetectorService localOsDetectorService;// = new LocalOsDetectorService(osDetectorUtils);

    @Before
    public void setUp() {
        localOsDetectorService = new LocalOsDetectorService(osDetectorUtils);
        doReturn("fam").when(osDetectorUtils).resolveOsFamily(anyString());
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