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

public class LinuxPingCommandTest {

    private static final String LINUX_COMMAND_OUTPUT = "PING 10.26.51.44 (10.26.51.44) 56(84) bytes of data.\n" +
            "64 bytes from 10.26.51.44: icmp_seq=1 ttl=119 time=98.3 ms\n" +
            "64 bytes from 10.26.51.44: icmp_seq=2 ttl=119 time=105 ms\n" +
            "64 bytes from 10.26.51.44: icmp_seq=3 ttl=119 time=102 ms\n" +
            "64 bytes from 10.26.51.44: icmp_seq=4 ttl=119 time=101 ms\n" +
            "\n" +
            "--- 10.26.51.44 ping statistics ---\n" +
            "4 packets transmitted, 4 received, 0% packet loss, time 3004ms\n" +
            "rtt min/avg/max/mdev = 98.366/102.086/105.476/2.530 ms\n";

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

        LocalPingCommand linuxPingCommand = new LinuxPingCommand();
        String command = linuxPingCommand.createCommand(localPingInputs);

        assertEquals("ping -w 30 -c 5 -s 100 10.13.14.220", command);
    }

    @Test
    public void testCreateCommandThrowsInvalidTimeoutException() {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost("10.13.14.220")
                .timeout("invalid")
                .build();

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(TIMEOUT_SHOULD_HAVE_A_NUMERIC_VALUE);

        new LinuxPingCommand().createCommand(localPingInputs);
    }

    @Test
    public void testCreateCommandThrowsInvalidPacketCountException() {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost("10.13.14.220")
                .packetCount("invalid")
                .build();

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(PACKET_COUNT_SHOULD_HAVE_A_NUMERIC_VALUE);

        new LinuxPingCommand().createCommand(localPingInputs);
    }

    @Test
    public void testCreateCommandThrowsInvalidPacketSizeException() {
        LocalPingInputs localPingInputs = new LocalPingInputs.LocalPingInputsBuilder()
                .targetHost("10.13.14.220")
                .packetSize("invalid")
                .build();

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(PACKET_SIZE_SHOULD_HAVE_A_NUMERIC_VALUE);

        new LinuxPingCommand().createCommand(localPingInputs);
    }

    @Test
    public void testParseOutput() {
        Map<String, String> resultsMap = new LinuxPingCommand().parseOutput(LINUX_COMMAND_OUTPUT);

        assertEquals("4", resultsMap.get(PACKETS_SENT));
        assertEquals("4", resultsMap.get(PACKETS_RECEIVED));
        assertEquals("0", resultsMap.get(PERCENTAGE_PACKETS_LOST));
        assertEquals("98.366", resultsMap.get(TRANSMISSION_TIME_MIN));
        assertEquals("102.086", resultsMap.get(TRANSMISSION_TIME_AVG));
        assertEquals("105.476", resultsMap.get(TRANSMISSION_TIME_MAX));
    }
}