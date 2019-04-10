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


package io.cloudslang.content.utilities.actions;

import io.cloudslang.content.utilities.entities.LocalPingInputs;
import io.cloudslang.content.utilities.services.localping.LocalPingService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_SENT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocalPing.class, InetAddress.class})
public class LocalPingTest {

    private static final String LOCALHOST = "localhost";
    private static final String COMMAND_OUTPUT = "Reply from 127.0.0.1: bytes=32 time=190ms TTL=122\n" +
            "Reply from 127.0.0.1: bytes=32 time=191ms TTL=122\n" +
            "Reply from 127.0.0.1: bytes=32 time=190ms TTL=122\n" +
            "Reply from 127.0.0.1: bytes=32 time=190ms TTL=122\n" +
            "\n" +
            "Ping statistics for 127.0.0.1:\n" +
            "    Packets: Sent = 4, Received = 4, Lost = 0 (0% loss),\n" +
            "Approximate round trip times in milli-seconds:\n" +
            "    Minimum = 190ms, Maximum = 191ms, Average = 190ms";
    private static final String INVALID_HOST = "invalidHost";

    @Mock
    private LocalPingService localPingServiceMock;

    @Rule
    private ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        whenNew(LocalPingService.class).withAnyArguments().thenReturn(localPingServiceMock);
    }

    @Test
    public void executePingCommandWithRequiredInputs() throws Exception {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put(RETURN_RESULT, COMMAND_OUTPUT);
        expectedMap.put(PACKETS_SENT, "4");

        doReturn(expectedMap).when(localPingServiceMock).executePingCommand(any(LocalPingInputs.class));

        Map<String, String> actualMap = new LocalPing().execute(LOCALHOST, "4", "", "", "");

        assertEquals(expectedMap.get(RETURN_RESULT), actualMap.get(RETURN_RESULT));
        assertEquals(expectedMap.get(PACKETS_SENT), actualMap.get(PACKETS_SENT));
    }
}