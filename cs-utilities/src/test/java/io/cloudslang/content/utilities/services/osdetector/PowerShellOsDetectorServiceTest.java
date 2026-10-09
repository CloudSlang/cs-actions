/*
 * Copyright 2022-2024 Open Text
 * This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */





package io.cloudslang.content.utilities.services.osdetector;

import io.cloudslang.content.entities.WSManRequestInputs;
import io.cloudslang.content.services.WSManRemoteShellService;
import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

/**
 * Created by Tirla Florin-Alin on 08/12/2017.
 **/
@ExtendWith(MockitoExtension.class)
public class PowerShellOsDetectorServiceTest {
    @Mock
    private OsDetectorHelperService osDetectorHelperService;

    @Mock
    private WSManRemoteShellService wsManRemoteShellService;

    private PowerShellOsDetectorService powerShellOsDetectorService;

    @BeforeEach
    public void setUp() {
        powerShellOsDetectorService = new PowerShellOsDetectorService(osDetectorHelperService, wsManRemoteShellService);
    }

    @Test
    public void testPowerShellDetection() throws Exception {
        doReturn(new HashMap<String, String>()).when(wsManRemoteShellService).runCommand(any(WSManRequestInputs.class));
        OperatingSystemDetails toBeReturnedOsDetails = new OperatingSystemDetails();
        toBeReturnedOsDetails.setArchitecture("xYZ");
        toBeReturnedOsDetails.setFamily("b os fam");
        toBeReturnedOsDetails.setName("b os");
        toBeReturnedOsDetails.setVersion("ultimate");
        doReturn(toBeReturnedOsDetails).when(osDetectorHelperService).processOutput(any(OperatingSystemDetails.class), anyMap(), anyString());

        OperatingSystemDetails actualOsDetails = powerShellOsDetectorService.detectOs(new OsDetectorInputs.Builder().withPowerShellTimeout("007").build());

        assertEquals("b os", actualOsDetails.getName());
        assertEquals("xYZ", actualOsDetails.getArchitecture());
        assertEquals("b os fam", actualOsDetails.getFamily());
        assertEquals("ultimate", actualOsDetails.getVersion());
    }
}