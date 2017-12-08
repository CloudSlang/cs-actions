/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.utilities.entities.constants;

import java.util.regex.Pattern;

/**
 * Created by Tirla Florin-Alin on 24/11/2017.
 **/
public class OsDetectorConstants {
    public static final String HOST = "host";

    public static final String NMAP_PATH = "nmapPath";
    public static final String NMAP_ARGUMENTS = "nmapArguments";
    public static final String NMAP_VALIDATOR = "nmapValidator";
    public static final String NMAP_TIMEOUT = "nmapTimeout";

    public static final String DEFAULT_NMAP_PATH = "nmap";
    public static final String DEFAULT_NMAP_ARGUMENTS = "-sS -sU -O -Pn --top-ports 20";

    public static final String RESTRICTIVE_NMAP_VALIDATOR = "restrictive";
    public static final String PERMISSIVE_NMAP_VALIDATOR = "permissive";
    public static final String DEFAULT_NMAP_TIMEOUT = "30000";
    public static final String DEFAULT_POWER_SHELL_OP_TIMEOUT = "60000";
    public static final String DEFAULT_SSH_TIMEOUT = "90000";
    public static final String DEFAULT_PROXY_PORT = "8080";
    public static final String SSH_TIMEOUT = "sshTimeout";
    public static final String SSH_CONNECT_TIMEOUT = "sshConnectTimeout";
    public static final String POWERSHELL_OPERATION_TIMEOUT = "powershellOperationTimeout";
    public static final String DEFAULT_ALLOWED_CIPHERS = "aes128-ctr,aes128-cbc,3des-ctr,3des-cbc,blowfish-cbc,aes192-ctr,aes192-cbc,aes256-ctr,aes256-cbc";
    public static final String DETECTION = " detection";
    public static final String KNOWN_HOSTS_STRICT = "strict";
    public static final String BASIC_AUTH = "basic";

    // Allow whitespace, any letter, any digit, dash, comma, dot, equals
    public static final Pattern ALLOWED_CHARACTERS = Pattern.compile("[ a-zA-Z0-9\\-,.=]*");
    public static final char[] ILLEGAL_CHARACTERS = {'*', '?', '[', ']', '$', '|', '<', '>', '&', '%', '(', ')', '!', ';', '\\'};

    public static final String OS_VERSION = "osVersion";
    public static final String OS_NAME = "osName";
    public static final String OS_FAMILY = "osFamily";
    public static final String OS_ARCHITECTURE = "osArchitecture";
    public static final String OS_COMMANDS = "osCommands";
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
