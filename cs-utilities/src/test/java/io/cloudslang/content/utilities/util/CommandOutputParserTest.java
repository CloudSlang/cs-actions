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


package io.cloudslang.content.utilities.util;

import org.junit.Test;

import static io.cloudslang.content.utilities.util.CommandOutputParser.extractValue;
import static org.junit.Assert.assertEquals;

public class CommandOutputParserTest {

    private static final String LINUX_COMMAND_OUTPUT = "PING 10.0.0.1 (10.0.0.1) 56(84) bytes of data.\n" +
            "64 bytes from 10.0.0.1: icmp_seq=1 ttl=119 time=91.8 ms\n" +
            "64 bytes from 10.0.0.1: icmp_seq=2 ttl=119 time=91.7 ms\n" +
            "64 bytes from 10.0.0.1: icmp_seq=3 ttl=119 time=91.7 ms\n" +
            "64 bytes from 10.0.0.1: icmp_seq=4 ttl=119 time=91.6 ms\n" +
            "\n" +
            "--- 10.0.0.1 ping statistics ---\n" +
            "4 packets transmitted, 4 received, 0% packet loss, time 3005ms\n" +
            "rtt min/avg/max/mdev = 91.609/91.745/91.820/0.313 ms\n";
    private static final String SUN_OS_COMMAND_OUTPUT = "PING xyz: 56 data bytes\n" +
            "64 bytes from xyz (4::390:a00:2018:ab3d:7jks): icmp_seq=0. time=0.479 ms\n" +
            "64 bytes from xyz (4::390:a00:2018:ab3d:7jks): icmp_seq=1. time=0.843 ms\n" +
            "64 bytes from xyz (4::390:a00:2018:ab3d:7jks): icmp_seq=2. time=0.516 ms\n" +
            "64 bytes from xyz (4::390:a00:2018:ab3d:7jks): icmp_seq=3. time=4.943 ms\n" +
            "64 bytes from xyz (4::390:a00:2018:ab3d:7jks): icmp_seq=4. time=0.485 ms\n" +
            "64 bytes from xyz (4::390:a00:2018:ab3d:7jks): icmp_seq=5. time=2.201 ms\n" +
            "----xyz PING Statistics----     \n" +
            "6 packets transmitted, 6 packets received, 0% packet loss     \n" +
            "round-trip (ms)  min/avg/max/stddev = 0.479/1.583/4.943/1.823";

    @Test
    public void extractValueWithEndTag() {
        assertEquals("4", extractValue(LINUX_COMMAND_OUTPUT, "---\n", " packets transmitted, "));
        assertEquals("4", extractValue(LINUX_COMMAND_OUTPUT, "packets transmitted, ", " received, "));
        assertEquals("0", extractValue(LINUX_COMMAND_OUTPUT, " received, ", "% packet loss"));
    }

    @Test
    public void extractValueWithoutEndTag() {
        assertEquals("0.479/1.583/4.943/1.823", extractValue(SUN_OS_COMMAND_OUTPUT, "round-trip (ms)  min/avg/max/stddev = "));
    }
}