package io.cloudslang.content.utilities.entities.constants;

/**
 * Created by Tirla Florin-Alin on 24/11/2017.
 **/
public class OsDetectorConstants {
    public static final String WINDOWS = "Windows";
    public static final String WINDOWS_OS_NAME_KEY = "OS Name:";
    public static final String WINDOWS_OS_VERSION_KEY = "OS Version:";
    public static final String WINDOWS_OS_ARCHITECTURE_KEY = "System Type:";
    public static final String QUOTES = "\"'";
    public static final String LINUX_OS_NAME_KEY = "NAME=";
    public static final String LINUX_OS_VERSION_KEY = "VERSION=";
    public static final String LINUX_OS_ARCHITECTURE_KEY = "ARCHITECTURE=";
    public static final String UNIX_OS_FAMILY_KEY = "FAMILY=";
    private static final String MAC_COMMAND = "sw_vers";
    private static final String WINDOWS_COMMAND = "systeminfo";
    private static final String LINUX_COMMAND = "printf " + UNIX_OS_FAMILY_KEY + " && uname; printf " + LINUX_OS_ARCHITECTURE_KEY + " && uname -m; cat /etc/os-release;";
    public static final String OS_DETECTOR_COMMAND = LINUX_COMMAND + " " + MAC_COMMAND + " || " + WINDOWS_COMMAND;
    public static final String WINDOWS_LINE_SEPARATOR = "\r\n";
    public static final String UNIX_LINE_SEPARATOR = "\n";
    public static final String EARLY_MAC_LINE_SEPARATOR = "\r";
}
