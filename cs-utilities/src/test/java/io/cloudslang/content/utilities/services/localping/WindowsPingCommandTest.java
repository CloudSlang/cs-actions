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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_RECEIVED;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_SENT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKET_COUNT_SHOULD_HAVE_A_NUMERIC_VALUE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKET_SIZE_SHOULD_HAVE_A_NUMERIC_VALUE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PERCENTAGE_PACKETS_LOST;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TIMEOUT_SHOULD_HAVE_A_NUMERIC_VALUE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_AVG;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_MAX;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_MIN;
import static org.junit.Assert.assertEquals;

public class WindowsPingCommandTest {

    private static final String WINDOWS_COMMAND_OUTPUT = "Pinging OuzoCSA490.hpswlabs.adapps.hp.com [10.13.14.220] with 32 bytes of data:\n" +
            "Reply from 10.13.14.220: bytes=32 time=185ms TTL=122\n" +
            "Reply from 10.13.14.220: bytes=32 time=186ms TTL=122\n" +
            "Reply from 10.13.14.220: bytes=32 time=185ms TTL=122\n" +
            "Reply from 10.13.14.220: bytes=32 time=186ms TTL=122\n" +
            "\n" +
            "Ping statistics for 10.13.14.220:\n" +
            "    Packets: Sent = 4, Received = 4, Lost = 0 (0% loss),\n" +
            "Approximate round trip times in milli-seconds:\n" +
            "    Minimum = 185ms, Maximum = 186ms, Average = 185ms";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testCreateCommandSuccess() {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost("10.13.14.220")
                .packetCount("5")
                .packetSize("100")
                .timeout("30000")
                .build();

        LocalPingCommand windowsPingCommand = new WindowsPingCommand();
        String command = windowsPingCommand.createCommand(localPingInputs);

        assertEquals("ping -w 30000 -n 5 -l 100 10.13.14.220", command);
    }

    @Test
    public void testCreateCommandThrowsInvalidTimeoutException() {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost("10.13.14.220")
                .timeout("invalid")
                .build();

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(TIMEOUT_SHOULD_HAVE_A_NUMERIC_VALUE);

        new WindowsPingCommand().createCommand(localPingInputs);
    }

    @Test
    public void testCreateCommandThrowsInvalidPacketCountException() {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost("10.13.14.220")
                .packetCount("invalid")
                .build();

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(PACKET_COUNT_SHOULD_HAVE_A_NUMERIC_VALUE);

        new WindowsPingCommand().createCommand(localPingInputs);
    }

    @Test
    public void testCreateCommandThrowsInvalidPacketSizeException() {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost("10.13.14.220")
                .packetSize("invalid")
                .build();

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(PACKET_SIZE_SHOULD_HAVE_A_NUMERIC_VALUE);

        new WindowsPingCommand().createCommand(localPingInputs);
    }

    @Test
    public void testParseOutput() {
        Map<String, String> resultsMap = new WindowsPingCommand().parseOutput(WINDOWS_COMMAND_OUTPUT);

        assertEquals("4", resultsMap.get(PACKETS_SENT));
        assertEquals("4", resultsMap.get(PACKETS_RECEIVED));
        assertEquals("0", resultsMap.get(PERCENTAGE_PACKETS_LOST));
        assertEquals("185", resultsMap.get(TRANSMISSION_TIME_MIN));
        assertEquals("185", resultsMap.get(TRANSMISSION_TIME_AVG));
        assertEquals("186", resultsMap.get(TRANSMISSION_TIME_MAX));
    }

}