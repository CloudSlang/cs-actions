package io.cloudslang.content.redhat.utils;

public class Utils {
    public static void validateProtocol(String protocol) throws Exception {
        if (!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https"))
            throw new Exception("Invalid protocol: " + protocol);
    }
}
