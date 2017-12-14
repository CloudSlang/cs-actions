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
package io.cloudslang.content.utilities.services.osdetector;

import com.google.common.collect.ImmutableList;
import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;
import io.cloudslang.content.utilities.util.ProcessExecutor;
import io.cloudslang.content.utilities.entities.ProcessResponseEntity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Tirla Florin-Alin on 08/12/2017.
 **/
@RunWith(PowerMockRunner.class)
@PrepareForTest({NmapOsDetectorService.class})
public class NmapOsDetectorServiceTest {
    @Mock
    private OsDetectorHelperService osDetectorHelperService;

    @Mock
    private ProcessExecutor processExecutor;

    private NmapOsDetectorService nmapOsDetectorService;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        nmapOsDetectorService = new NmapOsDetectorService(osDetectorHelperService);
        whenNew(ProcessExecutor.class).withNoArguments().thenReturn(processExecutor);
    }

    @Test
    public void testProxyArgAppenderWithSuccess() {
        String actualNmapArg = nmapOsDetectorService.appendProxyArgument("--existing-arguments", "http://some-proxy.host", "8080");

        assertEquals("--existing-arguments --proxies http://some-proxy.host:8080", actualNmapArg);
    }

    @Test
    public void testProxyArgAppenderWithInvalidPort() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The 'proxyPort' input does not contain a valid port.");

        nmapOsDetectorService.appendProxyArgument("--existing-arguments", "http://some-proxy.host", "invalid");
    }

    @Test
    public void testProxyArgAppenderWithInvalidHost() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The 'proxyHost' input does not contain a valid URL: no protocol: invalid.");

        nmapOsDetectorService.appendProxyArgument("--existing-arguments", "invalid", "8080");
    }

    @Test
    public void testNmapDetectionWithTimeout() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        doReturn(new ProcessResponseEntity("stdout", "stderr", -1, true)).when(processExecutor).execute(anyString(), anyInt());
        OperatingSystemDetails actualOsDetails = nmapOsDetectorService.detectOs(new OsDetectorInputs.Builder().withNmapTimeout("007").build());

        performFailureChecks(actualOsDetails, "The nmap command timed out");

    }

    @Test
    public void testNmapDetectionWithIOException() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        doThrow(new IOException("error msg")).when(processExecutor).execute(anyString(), anyInt());
        OperatingSystemDetails actualOsDetails = nmapOsDetectorService.detectOs(new OsDetectorInputs.Builder().withNmapTimeout("007").build());

        performFailureChecks(actualOsDetails, "Failed to run Nmap command: error msg");

    }

    @Test
    public void testNmapDetectionWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        doThrow(new TimeoutException("timeout msg")).when(processExecutor).execute(anyString(), anyInt());
        OperatingSystemDetails actualOsDetails = nmapOsDetectorService.detectOs(new OsDetectorInputs.Builder().withNmapTimeout("007").build());

        performFailureChecks(actualOsDetails, "The nmap command timed out");
    }

    @Test
    public void testNmapDetectionWithInterruptedException() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        doThrow(new InterruptedException("error msg")).when(processExecutor).execute(anyString(), anyInt());
        OperatingSystemDetails actualOsDetails = nmapOsDetectorService.detectOs(new OsDetectorInputs.Builder().withNmapTimeout("007").build());

        performFailureChecks(actualOsDetails, "Execution of Nmap command was canceled.");
    }

    @Test
    public void testNmapDetectionWithExecutionException() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        doThrow(new ExecutionException(null)).when(processExecutor).execute(anyString(), anyInt());
        OperatingSystemDetails actualOsDetails = nmapOsDetectorService.detectOs(new OsDetectorInputs.Builder().withNmapTimeout("007").build());

        performFailureChecks(actualOsDetails, "An exception occurred while running the Nmap command: null");
    }

    @Test
    public void testNmapDetectionWithSuccess() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        doReturn(new ProcessResponseEntity("stdout", "stderr", 0, false)).when(processExecutor).execute(anyString(), anyInt());
        doReturn("b os").when(osDetectorHelperService).cropValue(anyString(), anyString(), anyString());
        doReturn("b os fam").when(osDetectorHelperService).resolveOsFamily(anyString());
        OperatingSystemDetails actualOsDetails = nmapOsDetectorService.detectOs(new OsDetectorInputs.Builder().withNmapTimeout("007").build());

        assertEquals("b os", actualOsDetails.getName());
        assertEquals("b os fam", actualOsDetails.getFamily());
        assertEquals("", actualOsDetails.getArchitecture());
        assertEquals("", actualOsDetails.getVersion());
        assertEquals(ImmutableList.of("stdout", "stderr", "Execution of Nmap command had exit code: 0"), actualOsDetails.getCommandsOutput().get("Nmap"));
    }

    private void performFailureChecks(OperatingSystemDetails actualOsDetails, String expectedCmdMsg) {
        assertEquals("", actualOsDetails.getName());
        assertEquals("", actualOsDetails.getVersion());
        assertEquals("", actualOsDetails.getArchitecture());
        assertEquals("", actualOsDetails.getFamily());
        assertEquals(singletonList(expectedCmdMsg), actualOsDetails.getCommandsOutput().get("Nmap"));
    }
}