package io.cloudslang.content.utilities.services.localping;

import static java.lang.String.format;

/**
 * Created by pinteae on 1/11/2018.
 */
public class LocalPingCommandFactory {

    private static final String UNSUPPORTED_OPERATING_SYSTEM_S = "Unsupported operating system %s";
    private static final String WINDOWS = "Windows";
    private static final String LINUX = "Linux";
    private static final String SUN_OS = "SunOS";

    public static LocalPingCommand getLocalPingCommand(String osFamily) {
        switch (osFamily) {
            case WINDOWS:
                return new WindowsPingCommand();
            case LINUX:
                return new LinuxPingCommand();
            case SUN_OS:
                return new SunOsPingCommand();

            default:
                throw new RuntimeException(format(UNSUPPORTED_OPERATING_SYSTEM_S, osFamily));
        }
    }
}
