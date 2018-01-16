package io.cloudslang.content.utilities.services.localping;

import io.cloudslang.content.utilities.entities.LocalPingInputs;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.DEFAULT_PACKET_SIZE;
import static io.cloudslang.content.utilities.entities.constants.LocalPingConstants.INVALID_ARGUMENT_IP_VERSION;
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
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by pinteae on 1/12/2018.
 */
public class LinuxPingCommand implements LocalPingCommand {

    @Override
    public String createCommand(LocalPingInputs localPingInputs) {
        StringBuilder command = new StringBuilder();
        String targetHost = localPingInputs.getTargetHost();
        String ipVersion = localPingInputs.getIpVersion();

        if (ipVersion == null || ipVersion.isEmpty()) {
            if (!InetAddressValidator.getInstance().isValidInet6Address(targetHost)) {
                command.append("ping ");
            } else {
                command.append("ping6 ");
            }
        } else {
            if (ipVersion.equals("6")) {
                command.append("ping6 ");
            } else if (ipVersion.equals("4")) {
                command.append("ping ");
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
            command.append(format("-c %s ", packetCount));
        }

        String packetSize = localPingInputs.getPacketSize();
        if (isNotEmpty(packetSize)) {
            try {
                Integer.parseInt(packetSize);
            } catch (NumberFormatException nfe) {
                throw new RuntimeException(PACKET_SIZE_SHOULD_HAVE_A_NUMERIC_VALUE);
            }
            command.append(format("-s %s ", packetSize));
        } else {
            command.append(format("-s %d ", DEFAULT_PACKET_SIZE));
        }

        command.append(localPingInputs.getTargetHost());

        return command.toString();
    }

    @Override
    public Map<String, String> parseOutput(String output) {
        Map<String, String> resultMap = new HashMap<>();

        resultMap.put(RETURN_RESULT, output);
        resultMap.put(PACKETS_SENT, extractValue(output, "---\n ", " packets transmitted, "));
        resultMap.put(PACKETS_RECEIVED, extractValue(output, "packets transmitted, ", " received, "));
        resultMap.put(PERCENTAGE_PACKETS_LOST, extractValue(output, " received, ", "% packet loss"));

        String minMaxAvg = extractValue(output, "rtt min/avg/max/mdev = ", " ms");

        String[] roundTripTime = minMaxAvg.split(SLASH);

        resultMap.put(TRANSMISSION_TIME_MIN, roundTripTime[0]);
        resultMap.put(TRANSMISSION_TIME_MAX, roundTripTime[1]);
        resultMap.put(TRANSMISSION_TIME_AVG, roundTripTime[2]);

        return resultMap;
    }
}
