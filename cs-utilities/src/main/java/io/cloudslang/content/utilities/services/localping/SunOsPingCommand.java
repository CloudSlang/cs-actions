package io.cloudslang.content.utilities.services.localping;

import io.cloudslang.content.utilities.entities.LocalPingInputs;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.DEFAULT_PACKET_COUNT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.DEFAULT_PACKET_SIZE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.INVALID_ARGUMENT_IP_VERSION;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_RECEIVED;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKETS_SENT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKET_COUNT_SHOULD_HAVE_A_NUMERIC_VALUE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PACKET_SIZE_SHOULD_HAVE_A_NUMERIC_VALUE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.PERCENTAGE_PACKETS_LOST;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.SLASH;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_AVG;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_MAX;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.TRANSMISSION_TIME_MIN;
import static io.cloudslang.content.utilities.util.CommandOutputParser.extractValue;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by pinteae on 1/12/2018.
 */
public class SunOsPingCommand implements LocalPingCommand {

    @Override
    public String createCommand(LocalPingInputs localPingInputs) {
        StringBuilder command = new StringBuilder();
        command.append(format("ping -s %s ", localPingInputs.getTargetHost()));

        String ipVersion = localPingInputs.getIpVersion();
        if (isNotEmpty(ipVersion)) {
            if (ipVersion.equals("6")) {
                command.append("-A inet6 ");
            } else if (ipVersion.equals("4")) {
                command.append("-A inet ");
            } else {
                throw new IllegalArgumentException(format(INVALID_ARGUMENT_IP_VERSION, ipVersion));
            }
        }

        String packetSize = localPingInputs.getPacketSize();
        if (isNotEmpty(packetSize)) {
            try {
                Integer.parseInt(packetSize);
            } catch (NumberFormatException nfe) {
                throw new RuntimeException(PACKET_SIZE_SHOULD_HAVE_A_NUMERIC_VALUE);
            }
            command.append(format(" %s ", packetSize));
        } else {
            command.append(format(" %s ", DEFAULT_PACKET_SIZE));
        }

        String packetCount = localPingInputs.getPacketCount();
        if (isNotEmpty(packetCount)) {
            try {
                Long.parseLong(packetCount);
            } catch (NumberFormatException nfe) {
                throw new RuntimeException(PACKET_COUNT_SHOULD_HAVE_A_NUMERIC_VALUE);
            }
            command.append(format(" %s ", packetCount));
        } else {
            command.append(format(" %s ", DEFAULT_PACKET_COUNT));
        }

        return command.toString();
    }

    @Override
    public Map<String, String> parseOutput(String output) {
        Map<String, String> resultMap = new HashMap<>();

        resultMap.put(RETURN_RESULT, output);
        resultMap.put(PACKETS_SENT, extractValue(output, "---\n", " packets transmitted, "));
        resultMap.put(PACKETS_RECEIVED, extractValue(output, "packets transmitted, ", " packets received, "));
        resultMap.put(PERCENTAGE_PACKETS_LOST, extractValue(output, " received, ", "% packet loss"));

        String minMaxAvg = extractValue(output, "round-trip (ms)  min/avg/max/stddev = ");

        String[] roundTripTime = minMaxAvg.split(SLASH);

        resultMap.put(TRANSMISSION_TIME_MIN, roundTripTime[0]);
        resultMap.put(TRANSMISSION_TIME_MAX, roundTripTime[1]);
        resultMap.put(TRANSMISSION_TIME_AVG, roundTripTime[2]);

        return resultMap;
    }
}
