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


package io.cloudslang.content.utilities.actions;

import io.cloudslang.content.utilities.entities.LocalPingInputs;
import io.cloudslang.content.utilities.services.localping.LocalPingService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_SENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockConstruction;

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

    @Test
    public void executePingCommandWithRequiredInputs() throws Exception {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put(RETURN_RESULT, COMMAND_OUTPUT);
        expectedMap.put(PACKETS_SENT, "4");

        try (MockedConstruction<LocalPingService> mockedService = mockConstruction(
                LocalPingService.class,
                (mock, context) -> doReturn(expectedMap).when(mock).executePingCommand(any(LocalPingInputs.class)))) {
            Map<String, String> actualMap = new LocalPing().execute(LOCALHOST, "4", "", "", "");

            assertEquals(expectedMap.get(RETURN_RESULT), actualMap.get(RETURN_RESULT));
            assertEquals(expectedMap.get(PACKETS_SENT), actualMap.get(PACKETS_SENT));
        }
    }
}