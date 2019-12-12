/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
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

import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Tirla Florin-Alin on 08/12/2017.
 **/
@RunWith(PowerMockRunner.class)
public class OsDetectorHelperServiceTest {
    private OsDetectorHelperService osDetectorHelperService;

    @Before
    public void setUp() {
        osDetectorHelperService = new OsDetectorHelperService();
    }

    @Test
    public void testCropValueWithSuccess() {
        assertEquals("expected", osDetectorHelperService.cropValue("aksjd SEARCHED=expectedEND asd", "SEARCHED=", "END"));
    }

    @Test
    public void testCropValueWithNoResultForBegin() {
        assertEquals("", osDetectorHelperService.cropValue("aksjd SEARCHED=expectedEND asd", "non-existent=", "END"));
    }

    @Test
    public void testCropValueWithNoResultForEnd() {
        assertEquals("", osDetectorHelperService.cropValue("aksjd SEARCHED=expectedEND asd", "SEARCHED=", "non-existent"));
    }

    @Test
    public void testCropValueWithResult() {
        assertEquals("expected", osDetectorHelperService.cropValue("SEARCHED=expectedEND", "SEARCHED=", "END"));
    }

    @Test
    public void testFoundOperatingSystemWithName() {
        OperatingSystemDetails os = new OperatingSystemDetails();
        os.setName("b os");
        assertTrue(osDetectorHelperService.foundOperatingSystem(os));
    }

    @Test
    public void testFoundOperatingSystemWithFamily() {
        OperatingSystemDetails os = new OperatingSystemDetails();
        os.setFamily("b os fam");
        assertTrue(osDetectorHelperService.foundOperatingSystem(os));
    }

    @Test
    public void testFoundOperatingSystemWithBothNameAndFamily() {
        OperatingSystemDetails os = new OperatingSystemDetails();
        os.setName("b os");
        os.setFamily("b os fam");
        assertTrue(osDetectorHelperService.foundOperatingSystem(os));
    }

    @Test
    public void testFoundOperatingSystemDummyDetails() {
        assertFalse(osDetectorHelperService.foundOperatingSystem(new OperatingSystemDetails()));
    }

    @Test
    public void testFoundOperatingSystemWithNoNameAndFamily() {
        OperatingSystemDetails os = new OperatingSystemDetails();
        os.setVersion("ultimate");
        os.setArchitecture("xYZ");
        os.addCommandOutput("dummy", singletonList("dummy"));
        assertFalse(osDetectorHelperService.foundOperatingSystem(os));
    }

    @Test
    public void testResolveOsFamily() {
        assertEquals("Other", osDetectorHelperService.resolveOsFamily("dummy"));
        assertEquals("Linux", osDetectorHelperService.resolveOsFamily("linux"));
        assertEquals("Linux", osDetectorHelperService.resolveOsFamily("Linux"));
        assertEquals("Windows", osDetectorHelperService.resolveOsFamily("windows"));
        assertEquals("Windows", osDetectorHelperService.resolveOsFamily("windOWS"));
        assertEquals("SunOS", osDetectorHelperService.resolveOsFamily("soLAris"));
        assertEquals("FreeBSD", osDetectorHelperService.resolveOsFamily("freebsd"));
    }

    @Test
    public void testProcessOutputForNoResult() {
        OperatingSystemDetails actualOsDetails = osDetectorHelperService.processOutput(new OperatingSystemDetails(),
                new HashMap<String, String>() {{put("returnResult", "msg");}}, "det option");
        assertEquals("", actualOsDetails.getName());
        assertEquals("", actualOsDetails.getVersion());
        assertEquals("", actualOsDetails.getFamily());
        assertEquals("", actualOsDetails.getArchitecture());
        assertEquals(singletonList("msg"), actualOsDetails.getCommandsOutput().get("det option"));
    }

    @Test
    public void testProcessOutputForWindows() {
        final String windowsOsCmdOutput = "Host Name:                 hostname\n" +
                "OS Name:                   Microsoft Windows 10 Enterprise\n" +
                "OS Version:                10\n" +
                "OS Manufacturer:           Microsoft Corporation\n" +
                "OS Configuration:          Member Workstation\n" +
                "OS Build Type:             Multiprocessor Free\n" +
                "Registered Owner:          Owner\n" +
                "Registered Organization:   Organisation\n" +
                "Product ID:                00329-00000-00003-AA434\n" +
                "Original Install Date:     04/09/2017, 10:12:27\n" +
                "System Boot Time:          05/12/2017, 09:57:09\n" +
                "System Manufacturer:       Hewlett-Packard\n" +
                "System Model:              HP ZBook 15 G2\n" +
                "System Type:               x64-based PC\n";
        OperatingSystemDetails actualOsDetails = osDetectorHelperService.processOutput(new OperatingSystemDetails(),
                new HashMap<String, String>() {{
                    put("returnResult", windowsOsCmdOutput);
                    put("returnCode", "0");
        }}, "det option");
        assertEquals("Microsoft Windows 10 Enterprise", actualOsDetails.getName());
        assertEquals("10", actualOsDetails.getVersion());
        assertEquals("Windows", actualOsDetails.getFamily());
        assertEquals("x64-based PC", actualOsDetails.getArchitecture());
        assertEquals(singletonList(windowsOsCmdOutput), actualOsDetails.getCommandsOutput().get("det option"));
    }
}