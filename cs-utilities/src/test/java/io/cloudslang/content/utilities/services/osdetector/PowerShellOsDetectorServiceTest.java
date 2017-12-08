package io.cloudslang.content.utilities.services.osdetector;

import io.cloudslang.content.entities.WSManRequestInputs;
import io.cloudslang.content.services.WSManRemoteShellService;
import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;
import io.cloudslang.content.utilities.util.OsDetectorUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

/**
 * Created by Tirla Florin-Alin on 08/12/2017.
 **/
@RunWith(PowerMockRunner.class)
public class PowerShellOsDetectorServiceTest {
    @Mock
    private OsDetectorUtils osDetectorUtils;

    @Mock
    private WSManRemoteShellService wsManRemoteShellService;

    private PowerShellOsDetectorService powerShellOsDetectorService;

    @Before
    public void setUp() {
        powerShellOsDetectorService = new PowerShellOsDetectorService(osDetectorUtils, wsManRemoteShellService);
    }

    @Test
    public void testPowerShellDetection() throws SAXException, InterruptedException, TransformerException, IOException,
            XPathExpressionException, TimeoutException, URISyntaxException, ParserConfigurationException {
        doReturn(new HashMap<String, String>()).when(wsManRemoteShellService).runCommand(any(WSManRequestInputs.class));
        OperatingSystemDetails toBeReturnedOsDetails = new OperatingSystemDetails();
        toBeReturnedOsDetails.setArchitecture("xYZ");
        toBeReturnedOsDetails.setFamily("b os fam");
        toBeReturnedOsDetails.setName("b os");
        toBeReturnedOsDetails.setVersion("ultimate");
        doReturn(toBeReturnedOsDetails).when(osDetectorUtils).processOutput(any(OperatingSystemDetails.class), anyMapOf(String.class, String.class), anyString());

        OperatingSystemDetails actualOsDetails = powerShellOsDetectorService.detect(new OsDetectorInputs.Builder().withPowerShellTimeout("007").build());

        assertEquals("b os", actualOsDetails.getName());
        assertEquals("xYZ", actualOsDetails.getArchitecture());
        assertEquals("b os fam", actualOsDetails.getFamily());
        assertEquals("ultimate", actualOsDetails.getVersion());
    }
}