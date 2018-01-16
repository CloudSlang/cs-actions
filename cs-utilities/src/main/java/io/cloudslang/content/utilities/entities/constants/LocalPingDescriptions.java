package io.cloudslang.content.utilities.entities.constants;

public class LocalPingDescriptions {

    public static class OperationDescription {
        public static final String LOCAL_PING_OPERATION_DESC = "This operation runs a Ping command locally.";
    }

    public static class InputsDescription {
        public static final String IP_VERSION_INPUT_DESC = "IP version forced to the ping command executed on the target host. " +
                "For Windows -4 or -6 parameters will be added.On Linux will be used ping or ping6. " +
                "For Solaris -A inet or -A inet6 parameters will be added. " +
                "For empty string the operation will decide what format to use if targetHost is an ip literal; if targetHost is given as a hostname default 'ping' command will be used on each operating system.\n" +
                "Values: 4, 6, '' (empty string without quotes).\n" +
                "Default: '' (empty string without quotes).";
        public static final String PACKET_SIZE_INPUT_DESC = "The size of the ping packet";
        public static final String TIMEOUT_INPUT_DESC = "The timeout in miliseconds for the Local Ping operation.\n Default: 90000.";
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
