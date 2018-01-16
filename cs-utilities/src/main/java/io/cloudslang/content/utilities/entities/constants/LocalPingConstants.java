package io.cloudslang.content.utilities.entities.constants;

/**
 * Created by pinteae on 1/12/2018.
 */
public class LocalPingConstants {
    public static final String INVALID_ARGUMENT_IP_VERSION = "Invalid argument found. IpVersion does not support argument:%s. Supported arguments are: '', '4', '6'";
    public static final String TIMEOUT_SHOULD_HAVE_A_NUMERIC_VALUE = "Timeout should have a numeric value.";
    public static final String PACKET_COUNT_SHOULD_HAVE_A_NUMERIC_VALUE = "PacketCount should have a numeric value.";
    public static final String PACKET_SIZE_SHOULD_HAVE_A_NUMERIC_VALUE = "PacketSize should have a numeric value.";
    public static final int DEFAULT_PACKET_SIZE = 56;
    public static final int DEFAULT_PACKET_COUNT = 4;
    public static final String PACKETS_SENT = "packetsSent";
    public static final String PACKETS_RECEIVED = "packetsReceived";
    public static final String PERCENTAGE_PACKETS_LOST = "percentagePacketsLost";
    public static final String TRANSMISSION_TIME_MIN = "transmissionTimeMin";
    public static final String TRANSMISSION_TIME_MAX = "transmissionTimeMax";
    public static final String TRANSMISSION_TIME_AVG = "transmissionTimeAvg";
    public static final String TARGET_HOST = "targetHost";
    public static final String PACKET_COUNT = "packetCount";
    public static final String PACKET_SIZE = "packetSize";
    public static final String TIMEOUT = "timeout";
    public static final String IP_VERSION = "ipVersion";
    public static final String COMMA = ",";
    public static final String HUNDRED_PERCENT_LOSS = "100";
    public static final String SLASH = "/";
    public static final String LOCAL_PING_OPERATION_NAME = "Local Ping";
}
