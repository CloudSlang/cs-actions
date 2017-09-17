/***********************************************************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
***********************************************************************************************************************/
package io.cloudslang.content.utils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Other utilities like ip, email, port, and other validation
 * Created by victor on 01.09.2016.
 */
public final class OtherUtilities {
    private static final Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern ipPortPattern = Pattern.compile("^\\d|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5]$", Pattern.CASE_INSENSITIVE);
    private static final Pattern ipPattern = Pattern.compile("((^((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))$)|(^((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?$))", Pattern.CASE_INSENSITIVE);

    private OtherUtilities() {
    }

    /**
     * Given an email string, it checks if it's a valid email with a regex (see emailPattern attribute)
     *
     * @param emailStr the string to check if it's a valid email
     * @return true if it's a valid email, false otherwise
     */
    public static boolean isValidEmail(@NotNull final String emailStr) {
        return emailPattern.matcher(emailStr).matches();
    }

    /**
     * Given an ip port string, it checks if it's a valid ip port with a regex (see ipPortPattern attribute)
     *
     * @param portStr the string to check if it's a valid ip port
     * @return true if it's a valid ip port, false otherwise
     */
    public static boolean isValidIpPort(@NotNull final String portStr) {
        return ipPortPattern.matcher(StringUtils.strip(portStr)).matches();
    }

    /**
     * Given an ip string, it checks if it's a valid ip with a regex (see ipPattern attribute)
     *
     * @param ipStr the string to check if it's a valid ip
     * @return true if it's a valid ip, false otherwise
     */
    public static boolean isValidIp(@NotNull final String ipStr) {
        return ipPattern.matcher(StringUtils.strip(ipStr)).matches();
    }

    /**
     * Change the from windows line ending in unix line ending
     *
     * @param string the string in which to change the line ending
     * @return a new string with the line ending changed from windows to unix
     */
    @NotNull
    public static String changeNewLineFromWindowsToUnix(@NotNull final String string) {
        return string.replace("\r\n", "\n");
    }

    /**
     * Change the from unix line ending in windows line ending
     *
     * @param string the string in which to change the line ending
     * @return a new string with the line ending changed from unix to windows
     */
    @NotNull
    public static String changeNewLineFromUnixToWindows(@NotNull final String string) {
        return string.replace("\n", "\r\n");
    }
}
