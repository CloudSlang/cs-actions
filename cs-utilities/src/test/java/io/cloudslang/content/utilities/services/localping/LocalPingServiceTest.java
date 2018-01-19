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
package io.cloudslang.content.utilities.services.localping;

import io.cloudslang.content.utilities.entities.LocalPingInputs;
import io.cloudslang.content.utilities.util.CommandExecutor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_RECEIVED;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_SENT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PERCENTAGE_PACKETS_LOST;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_AVG;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_MAX;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_MIN;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.UNABLE_TO_DETECT_LOCAL_OPERATING_SYSTEM;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.UNSUPPORTED_OPERATING_SYSTEM_S;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.WINDOWS;
import static io.cloudslang.content.utilities.util.CommandExecutor.executeCommand;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocalPingService.class, CommandExecutor.class})
public class LocalPingServiceTest {

    private static final String DUMMY_IP = "10.14.15.220";
    private static final String INVALID_IP = "1000.0.0.1";
    private static final String COMMAND_OUTPUT = "Reply from 127.0.0.1: bytes=32 time=190ms TTL=122\n" +
            "Reply from 127.0.0.1: bytes=32 time=191ms TTL=122\n" +
            "Reply from 127.0.0.1: bytes=32 time=190ms TTL=122\n" +
            "Reply from 127.0.0.1: bytes=32 time=190ms TTL=122\n" +
            "\n" +
            "Ping statistics for 127.0.0.1:\n" +
            "    Packets: Sent = 4, Received = 4, Lost = 0 (0% loss),\n" +
            "Approximate round trip times in milli-seconds:\n" +
            "    Minimum = 190ms, Maximum = 191ms, Average = 190ms";
    private static final String INVALID_COMMAND_OUTPUT = "Ping request could not find host 1000.0.0.1." +
            " Please check the name and try again.";
    private static final String EMPTY_STRING = "";
    private static final String OTHER = "Other";
    private static final String MAC_OS = "MacOS";

    @Spy
    private LocalPingService localPingServiceSpy = new LocalPingService();

    @Rule
    private ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLocalPingServiceSuccess() throws IOException {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost(DUMMY_IP)
                .build();

        mockStatic(CommandExecutor.class);

        when(executeCommand(anyString())).thenReturn(COMMAND_OUTPUT);
        doReturn(WINDOWS).when(localPingServiceSpy).detectLocalOsFamily();

        Map<String, String> resultsMap = localPingServiceSpy.executePingCommand(localPingInputs);

        assertEquals(COMMAND_OUTPUT, resultsMap.get(RETURN_RESULT));
    }

    @Test
    public void testLocalPingServiceInvalidIp() throws IOException {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost(INVALID_IP)
                .build();

        mockStatic(CommandExecutor.class);

        when(executeCommand(anyString())).thenReturn(INVALID_COMMAND_OUTPUT);
        doReturn(WINDOWS).when(localPingServiceSpy).detectLocalOsFamily();

        Map<String, String> resultsMap = localPingServiceSpy.executePingCommand(localPingInputs);

        assertEquals(INVALID_COMMAND_OUTPUT, resultsMap.get(RETURN_RESULT));
        assertEquals(EMPTY_STRING, resultsMap.get(PACKETS_SENT));
        assertEquals(EMPTY_STRING, resultsMap.get(PACKETS_RECEIVED));
        assertEquals(EMPTY_STRING, resultsMap.get(PERCENTAGE_PACKETS_LOST));
        assertEquals(EMPTY_STRING, resultsMap.get(TRANSMISSION_TIME_MIN));
        assertEquals(EMPTY_STRING, resultsMap.get(TRANSMISSION_TIME_AVG));
        assertEquals(EMPTY_STRING, resultsMap.get(TRANSMISSION_TIME_MAX));
    }

    @Test
    public void testLocalPingServiceUnrecognizedOs() throws IOException {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost(DUMMY_IP)
                .build();

        mockStatic(CommandExecutor.class);

        when(executeCommand(anyString())).thenReturn(COMMAND_OUTPUT);
        doReturn(OTHER).when(localPingServiceSpy).detectLocalOsFamily();

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(UNABLE_TO_DETECT_LOCAL_OPERATING_SYSTEM);

        localPingServiceSpy.executePingCommand(localPingInputs);
    }

    @Test
    public void testLocalPingServiceUnsupportedOs() throws IOException {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost(DUMMY_IP)
                .build();

        mockStatic(CommandExecutor.class);

        when(executeCommand(anyString())).thenReturn(COMMAND_OUTPUT);
        doReturn(MAC_OS).when(localPingServiceSpy).detectLocalOsFamily();

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(format(UNSUPPORTED_OPERATING_SYSTEM_S, MAC_OS));

        localPingServiceSpy.executePingCommand(localPingInputs);
    }
}