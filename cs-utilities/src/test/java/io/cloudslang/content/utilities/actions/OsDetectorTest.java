/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.utilities.actions;

import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;
import io.cloudslang.content.utilities.services.osdetector.NmapOsDetectorService;
import io.cloudslang.content.utilities.services.osdetector.OperatingSystemDetectorService;
import io.cloudslang.content.utilities.services.osdetector.OsDetectorHelperService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Tirla Florin-Alin on 27/11/2017.
 **/
@RunWith(PowerMockRunner.class)
@PrepareForTest({OsDetector.class})
public class OsDetectorTest {
    private static final String HOST = "my-host.much.wow";
    @Mock
    private OperatingSystemDetectorService operatingSystemDetectorService;

    @Mock
    private OsDetectorHelperService osDetectorHelperService;

    @Mock
    private NmapOsDetectorService nmapOsDetectorService;

    private OsDetector osDetector = new OsDetector();

    @Before
    public void setUp() throws Exception {
        whenNew(OsDetectorHelperService.class).withNoArguments().thenReturn(osDetectorHelperService);
        whenNew(NmapOsDetectorService.class).withAnyArguments().thenReturn(nmapOsDetectorService);
        whenNew(OperatingSystemDetectorService.class).withAnyArguments().thenReturn(operatingSystemDetectorService);

        doCallRealMethod().when(osDetectorHelperService).formatOsCommandsOutput(Matchers.<Map<String, List<String>>>any());
        doCallRealMethod().when(osDetectorHelperService).validateNmapInputs(any(OsDetectorInputs.class), any(NmapOsDetectorService.class));
    }

    @Test
    public void testDefaultValues() throws Exception {
        doReturn(new OperatingSystemDetails()).when(operatingSystemDetectorService).detectOs(any(OsDetectorInputs.class));
        osDetector.execute(HOST, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "");

        verify(operatingSystemDetectorService).detectOs(getInputsWithDefault());
    }

    @Test
    public void testWithInvalidHost() throws Exception {
        Map<String, String> actualResult = osDetector.execute("!@#$%^&*()__+{>", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");

        performFailureChecks(actualResult, "The 'host' input must be an must be localhost or an internet domain name or an internet address.");
    }

    @Test
    public void testWithInvalidProxyPort() throws Exception {
        Map<String, String> actualResult = osDetector.execute(HOST, "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "mynmap", "", "", "");

        performFailureChecks(actualResult, "The 'nmapPath' input must be an absolute path or the string 'nmap'.");
    }

    @Test
    public void testWithInvalidNmapArguments() throws Exception {
        Map<String, String> actualResult = osDetector.execute(HOST, "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "!@#*^@$(#!^)", "", "");

        performFailureChecks(actualResult, "The 'nmapArguments' input contains illegal characters. To perform a weaker validation set the value 'permissive' for the input 'nmapValidator'.");
    }

    @Test
    public void testWithInvalidNmapValidatorLevel() throws Exception {
        Map<String, String> actualResult = osDetector.execute(HOST, "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "invalid", "");

        performFailureChecks(actualResult, "The value provided for 'nmapValidator' in invalid. Valid values are: restrictive, permissive.");
    }

    @Test
    public void testWithInvalidNmapValidatorLevel1() throws Exception {
        Map<String, String> actualResult = osDetector.execute(HOST, "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "!@&^FE*!H)!JA(", "permissive", "");

        performFailureChecks(actualResult, "The 'nmapArguments' input contains the following illegal characters: *&()!.");
    }

    @Test
    public void testWithNoOsDetected() throws Exception {
        OperatingSystemDetails returnedOsDetails = new OperatingSystemDetails();
        returnedOsDetails.setVersion("ignored");
        returnedOsDetails.addCommandOutput("some detector", singletonList("some output"));

        doReturn(returnedOsDetails).when(operatingSystemDetectorService).detectOs(any(OsDetectorInputs.class));

        Map<String, String> actualResult = osDetector.execute(HOST, "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");

        assertEquals("-1", actualResult.get("returnCode"));
        assertEquals("Unable to detect the operating system.", actualResult.get("returnResult"));
        assertNull(actualResult.get("osFamily"));
        assertNull(actualResult.get("osName"));
        assertNull(actualResult.get("osArchitecture"));
        assertNull(actualResult.get("osVersion"));
        assertThat(actualResult.get("osCommands"), containsString("some detector detection"));
        assertThat(actualResult.get("osCommands"), containsString("some output"));
        assertThat(actualResult.get("exception"), containsString("Unable to detect the operating system."));
    }

    @Test
    public void testWithOsDetected() throws Exception {
        OperatingSystemDetails returnedOsDetails = new OperatingSystemDetails();
        returnedOsDetails.setVersion("ultimate");
        returnedOsDetails.setName("b os");
        returnedOsDetails.setFamily("fam");
        returnedOsDetails.setArchitecture("xYZ");
        returnedOsDetails.addCommandOutput("some detector", singletonList("some output"));

        doReturn(returnedOsDetails).when(operatingSystemDetectorService).detectOs(any(OsDetectorInputs.class));
        doReturn(true).when(osDetectorHelperService).foundOperatingSystem(any(OperatingSystemDetails.class));

        Map<String, String> actualResult = osDetector.execute(HOST, "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");

        assertEquals("0", actualResult.get("returnCode"));
        assertEquals("Successfully detected the operating system.", actualResult.get("returnResult"));
        assertEquals("fam", actualResult.get("osFamily"));
        assertEquals("b os", actualResult.get("osName"));
        assertEquals("xYZ", actualResult.get("osArchitecture"));
        assertEquals("ultimate", actualResult.get("osVersion"));
        assertThat(actualResult.get("osCommands"), containsString("some detector detection"));
        assertThat(actualResult.get("osCommands"), containsString("some output"));
        assertNull(actualResult.get("exception"));
    }

    private void performFailureChecks(Map<String, String> actualResult, String expectedMessage) {
        assertEquals("-1", actualResult.get("returnCode"));
        assertEquals(expectedMessage, actualResult.get("returnResult"));
        assertNull(actualResult.get("osFamily"));
        assertNull(actualResult.get("osName"));
        assertNull(actualResult.get("osArchitecture"));
        assertNull(actualResult.get("osVersion"));
        assertNull(actualResult.get("osCommands"));
        assertThat(actualResult.get("exception"), containsString(expectedMessage));
    }

    private OsDetectorInputs getInputsWithDefault() {
        return new OsDetectorInputs.Builder()
                .withHost(HOST)
                .withPort("")
                .withUsername("")
                .withPassword("")
                .withSshTimeout("90000")
                .withPowerShellTimeout("60000")
                .withNmapTimeout("30000")
                .withSshConnectTimeout("10000")
                .withNmapPath("nmap")
                .withNmapArguments("-sS -sU -O -Pn --top-ports 20")
                .withNmapValidator("restrictive")
                .withPrivateKeyFile("")
                .withPrivateKeyData("")
                .withKnownHostsPolicy("strict")
                .withKnownHostsPath(Paths.get(System.getProperty("user.home"), new String[]{".ssh", "known_hosts"}).toString())
                .withAllowedCiphers("aes128-ctr,aes128-cbc,3des-ctr,3des-cbc,blowfish-cbc,aes192-ctr,aes192-cbc,aes256-ctr,aes256-cbc")
                .withAgentForwarding("false")
                .withProtocol("https")
                .withAuthType("basic")
                .withProxyHost("")
                .withProxyPort("8080")
                .withProxyUsername("")
                .withProxyPassword("")
                .withTrustAllRoots("false")
                .withX509HostnameVerifier("strict")
                .withTrustKeystore("")
                .withTrustPassword("")
                .withKerberosConfFile("")
                .withKerberosLoginConfFile("")
                .withKerberosSkipPortForLookup("")
                .withKeystore("")
                .withKeystorePassword("")
                .withWinrmLocale("en-US")
                .build();
    }
}