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

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.COMMA;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.INVALID_ARGUMENT_IP_VERSION;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.IP_VERSION_4;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.IP_VERSION_6;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_RECEIVED;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_SENT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKET_COUNT_SHOULD_HAVE_A_NUMERIC_VALUE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKET_SIZE_SHOULD_HAVE_A_NUMERIC_VALUE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PERCENTAGE_PACKETS_LOST;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TIMEOUT_SHOULD_HAVE_A_NUMERIC_VALUE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_AVG;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_MAX;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_MIN;
import static io.cloudslang.content.utilities.util.CommandOutputParser.extractValue;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by pinteae on 1/11/2018.
 */
public class WindowsPingCommand implements LocalPingCommand {

    @Override
    public String createCommand(LocalPingInputs localPingInputs) {
        StringBuilder command = new StringBuilder();
        command.append("ping ");

        String ipVersion = localPingInputs.getIpVersion();
        if (isNotEmpty(ipVersion)) {
            if (ipVersion.equals(IP_VERSION_6)) {
                command.append("-6 ");
            } else if (ipVersion.equals(IP_VERSION_4)) {
                command.append("-4 ");
            } else {
                throw new IllegalArgumentException(format(INVALID_ARGUMENT_IP_VERSION, ipVersion));
            }
        }

        String timeout = localPingInputs.getTimeout();
        if (isNotEmpty(timeout)) {
            try {
                Long.parseLong(timeout);
            } catch (NumberFormatException nfe) {
                throw new RuntimeException(TIMEOUT_SHOULD_HAVE_A_NUMERIC_VALUE);
            }
            command.append(format("-w %s ", timeout));
        }

        String packetCount = localPingInputs.getPacketCount();
        if (isNotEmpty(packetCount)) {
            try {
                Long.parseLong(packetCount);
            } catch (NumberFormatException nfe) {
                throw new RuntimeException(PACKET_COUNT_SHOULD_HAVE_A_NUMERIC_VALUE);
            }
            command.append(format("-n %s ", packetCount));
        }

        String packetSize = localPingInputs.getPacketSize();
        if (isNotEmpty(packetSize)) {
            try {
                Integer.parseInt(packetSize);
            } catch (NumberFormatException nfe) {
                throw new RuntimeException(PACKET_SIZE_SHOULD_HAVE_A_NUMERIC_VALUE);
            }
            command.append(format("-l %s ", packetSize));
        }

        command.append(localPingInputs.getTargetHost());

        return command.toString();
    }

    @Override
    public Map<String, String> parseOutput(String output) {
        Map<String, String> resultMap = new HashMap<>();

        resultMap.put(RETURN_RESULT, output);
        resultMap.put(PACKETS_SENT, extractValue(output, "Sent = ", COMMA));
        resultMap.put(PACKETS_RECEIVED, extractValue(output, "Received = ", COMMA));
        resultMap.put(PERCENTAGE_PACKETS_LOST, extractValue(output, " (", "% loss)"));
        resultMap.put(TRANSMISSION_TIME_MIN, extractValue(output, "Minimum = ", "ms"));
        resultMap.put(TRANSMISSION_TIME_MAX, extractValue(output, "Maximum = ", "ms"));
        resultMap.put(TRANSMISSION_TIME_AVG, extractValue(output, "Average = ", "ms"));

        return resultMap;
    }
}
