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
package io.cloudslang.content.utilities.entities.constants;

public class LocalPingDescriptions {

    public static class OperationDescription {
        public static final String LOCAL_PING_OPERATION_DESC = "This operation runs a Ping command locally.";
    }

    public static class InputsDescription {
        public static final String IP_VERSION_INPUT_DESC = "IP version forced to the ping command executed on the target host. " +
                "For Windows -4 or -6 parameters will be added. On Linux, ping or ping6 will be used. " +
                "For Solaris -A inet or -A inet6 parameters will be added. " +
                "For empty string the operation will decide what format to use if targetHost is an ip literal; if targetHost is given as a hostname default 'ping' command will be used on each operating system." +
                "Values: 4, 6, '' (empty string without quotes)." +
                "Default: '' (empty string without quotes).";
        public static final String PACKET_SIZE_INPUT_DESC = "The size of the ping packet";
        public static final String TIMEOUT_INPUT_DESC = "The timeout in milliseconds for the Local Ping operation. Default: 10000." +
                "Note: When using timeout on an operating system belonging to SunOs family, the command will ignore the rest of the options (packetSize, packetCount, ipVersion).";
        public static final String TARGET_HOST_INPUT_DESC = "The target host to ping.";
        public static final String PACKETS_COUNT_INPUT_DESC = "The number of packets to send.";
    }

    public static class OutputsDescription {
        public static final String PACKETS_SENT_OUTPUT_DESC = "The number of packets sent.";
        public static final String PACKETS_RECEIVED_OUTPUT_DESC = "The number of packets received.";
        public static final String PERCENTAGE_PACKETS_LOST_OUTPUT_DESC = "The percentage of packets lost.";
        public static final String TRANSMISSION_TIME_MIN_OUTPUT_DESC = "The minimum time taken for transmitting the packet.";
        public static final String TRANSMISSION_TIME_MAX_OUTPUT_DESC = "The maximum time taken for transmitting the packet.";
        public static final String TRANSMISSION_TIME_AVG_OUTPUT_DESC = "The average time taken for transmitting the packet.";
        public static final String LOCAL_PING_RETURN_RESULT_DESC = "The raw output of the ping command.";
    }
}
