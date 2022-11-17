/*
 * (c) Copyright 2022 EntIT Software LLC, a Micro Focus company, L.P.
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
import org.apache.commons.validator.routines.InetAddressValidator;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OtherValues.EMPTY_STRING;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.DEFAULT_PACKET_COUNT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.INVALID_ARGUMENT_IP_VERSION;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.IP_VERSION_4;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.IP_VERSION_6;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_RECEIVED;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_SENT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKET_COUNT_SHOULD_HAVE_A_NUMERIC_VALUE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKET_SIZE_SHOULD_HAVE_A_NUMERIC_VALUE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PERCENTAGE_PACKETS_LOST;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.SLASH;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TIMEOUT_SHOULD_HAVE_A_NUMERIC_VALUE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_AVG;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_MAX;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_MIN;
import static io.cloudslang.content.utilities.util.CommandOutputParser.extractValue;
import static io.cloudslang.content.utils.NumberUtilities.isValidInt;
import static io.cloudslang.content.utils.NumberUtilities.isValidLong;
import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by pinteae on 1/12/2018.
 */
public class LinuxPingCommand implements LocalPingCommand {

    @Override
    public String createCommand(LocalPingInputs localPingInputs) {
        StringBuilder command = new StringBuilder();
        final String targetHost = localPingInputs.getTargetHost();
        final String ipVersion = localPingInputs.getIpVersion();

        if (isEmpty(ipVersion)) {
            if (InetAddressValidator.getInstance().isValidInet6Address(targetHost)) {
                command.append("/usr/bin/ping6 ");
            } else {
                command.append("/usr/bin/ping ");
            }
        } else {
            if (ipVersion.equals(IP_VERSION_6)) {
                command.append("/usr/bin/ping6 ");
            } else if (ipVersion.equals(IP_VERSION_4)) {
                command.append("/usr/bin/ping ");
            } else {
                throw new IllegalArgumentException(format(INVALID_ARGUMENT_IP_VERSION, ipVersion));
            }
        }

        final String timeout = localPingInputs.getTimeout();
        if (isNotEmpty(timeout)) {
            if (!isValidLong(timeout)) {
                throw new RuntimeException(TIMEOUT_SHOULD_HAVE_A_NUMERIC_VALUE);
            }
            //transform timeout value from milliseconds to seconds
            Long timeoutValue = parseLong(timeout) / 1000;
            command.append(format("-w %s ", valueOf(timeoutValue)));
        }

        final String packetCount = localPingInputs.getPacketCount();
        if (isNotEmpty(packetCount)) {
            if (!isValidLong(packetCount)) {
                throw new RuntimeException(PACKET_COUNT_SHOULD_HAVE_A_NUMERIC_VALUE);
            }
            command.append(format("-c %s ", packetCount));
        } else {
            command.append(format("-c %s ", DEFAULT_PACKET_COUNT));
        }

        final String packetSize = localPingInputs.getPacketSize();
        if (isNotEmpty(packetSize)) {
            if (!isValidInt(packetSize)) {
                throw new RuntimeException(PACKET_SIZE_SHOULD_HAVE_A_NUMERIC_VALUE);
            }
            command.append(format("-s %s ", packetSize));
        }

        command.append(localPingInputs.getTargetHost());

        return command.toString();
    }

    @Override
    public Map<String, String> parseOutput(String output) {
        Map<String, String> resultMap = new HashMap<>();

        resultMap.put(RETURN_RESULT, output);
        resultMap.put(PACKETS_SENT, extractValue(output, "---\n", " packets transmitted, "));
        resultMap.put(PACKETS_RECEIVED, extractValue(output, "packets transmitted, ", " received, "));
        resultMap.put(PERCENTAGE_PACKETS_LOST, extractValue(output, " received, ", "% packet loss"));

        final String minAvgMax = extractValue(output, "rtt min/avg/max/mdev = ", " ms");

        if(isNotEmpty(minAvgMax)) {
            final String[] roundTripTime = minAvgMax.split(SLASH);

            resultMap.put(TRANSMISSION_TIME_MIN, roundTripTime[0]);
            resultMap.put(TRANSMISSION_TIME_AVG, roundTripTime[1]);
            resultMap.put(TRANSMISSION_TIME_MAX, roundTripTime[2]);
        } else {
            resultMap.put(TRANSMISSION_TIME_MIN, EMPTY_STRING);
            resultMap.put(TRANSMISSION_TIME_AVG, EMPTY_STRING);
            resultMap.put(TRANSMISSION_TIME_MAX, EMPTY_STRING);
        }

        return resultMap;
    }
}
