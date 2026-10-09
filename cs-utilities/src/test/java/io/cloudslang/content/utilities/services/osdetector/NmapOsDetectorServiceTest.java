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

import com.google.common.collect.ImmutableList;
import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;
import io.cloudslang.content.utilities.util.ProcessExecutor;
import io.cloudslang.content.utilities.entities.ProcessResponseEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockConstruction;

/**
 * Created by Tirla Florin-Alin on 08/12/2017.
 **/
public class NmapOsDetectorServiceTest {
    private OsDetectorHelperService osDetectorHelperService;

    private NmapOsDetectorService nmapOsDetectorService;
    private MockedConstruction<ProcessExecutor> processExecutorConstruction;
    private ProcessResponseEntity processResponse;
    private Exception processException;

    @BeforeEach
    public void setUp() throws Exception {
        osDetectorHelperService = Mockito.mock(OsDetectorHelperService.class);
        nmapOsDetectorService = new NmapOsDetectorService(osDetectorHelperService);
        processExecutorConstruction = mockConstruction(ProcessExecutor.class, (mock, context) ->
                doAnswer(invocation -> {
                    if (processException != null) {
                        throw processException;
                    }
                    return processResponse;
                }).when(mock).execute(anyString(), anyInt()));
    }

    @AfterEach
    public void tearDown() {
        processExecutorConstruction.close();
    }

    @Test
    public void testProxyArgAppenderWithSuccess() {
        String actualNmapArg = nmapOsDetectorService.appendProxyArgument("--existing-arguments", "http://some-proxy.host", "8080");

        assertEquals("--existing-arguments --proxies http://some-proxy.host:8080", actualNmapArg);
    }

    @Test
    public void testProxyArgAppenderWithInvalidPort() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> nmapOsDetectorService.appendProxyArgument("--existing-arguments", "http://some-proxy.host", "invalid"));
        assertEquals("The 'proxyPort' input does not contain a valid port.", exception.getMessage());
    }

    @Test
    public void testProxyArgAppenderWithInvalidHost() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> nmapOsDetectorService.appendProxyArgument("--existing-arguments", "invalid", "8080"));
        assertEquals("The 'proxyHost' input does not contain a valid URL: no protocol: invalid.", exception.getMessage());
    }

    @Test
    public void testNmapDetectionWithTimeout() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        processResponse = new ProcessResponseEntity("stdout", "stderr", -1, true);
        OperatingSystemDetails actualOsDetails = nmapOsDetectorService.detectOs(new OsDetectorInputs.Builder().withNmapTimeout("007").build());

        performFailureChecks(actualOsDetails, "The nmap command timed out");

    }

    @Test
    public void testNmapDetectionWithIOException() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        processException = new IOException("error msg");
        OperatingSystemDetails actualOsDetails = nmapOsDetectorService.detectOs(new OsDetectorInputs.Builder().withNmapTimeout("007").build());

        performFailureChecks(actualOsDetails, "Failed to run Nmap command: error msg");

    }

    @Test
    public void testNmapDetectionWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        processException = new TimeoutException("timeout msg");
        OperatingSystemDetails actualOsDetails = nmapOsDetectorService.detectOs(new OsDetectorInputs.Builder().withNmapTimeout("007").build());

        performFailureChecks(actualOsDetails, "The nmap command timed out");
    }

    @Test
    public void testNmapDetectionWithInterruptedException() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        processException = new InterruptedException("error msg");
        OperatingSystemDetails actualOsDetails = nmapOsDetectorService.detectOs(new OsDetectorInputs.Builder().withNmapTimeout("007").build());

        performFailureChecks(actualOsDetails, "Execution of Nmap command was canceled.");
    }

    @Test
    public void testNmapDetectionWithExecutionException() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        processException = new ExecutionException(null);
        OperatingSystemDetails actualOsDetails = nmapOsDetectorService.detectOs(new OsDetectorInputs.Builder().withNmapTimeout("007").build());

        performFailureChecks(actualOsDetails, "An exception occurred while running the Nmap command: null");
    }

    @Test
    public void testNmapDetectionWithSuccess() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        processResponse = new ProcessResponseEntity("stdout", "stderr", 0, false);
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