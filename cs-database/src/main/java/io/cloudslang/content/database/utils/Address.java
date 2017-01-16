/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.utils;

import java.net.URI;

/**
 * Created by victor on 13.01.2017.
 */
public class Address {
    public final static int PORT_NOT_SET = -1;
    int resolvedPort = PORT_NOT_SET;
    String bareHost; // host, stripped of any trailing colon and port number
    private String hostWithOptionalPort = "";
    private int inputPort = PORT_NOT_SET;
    private boolean isIPV6Literal = false;

    /**
     * Single-argument constructor. Note that host may contain a port (e.g.
     * www.hp.com:22)
     *
     * @param hostWithOptionalPort
     */
    public Address(String hostWithOptionalPort) {
        this(hostWithOptionalPort, null);
    }

    /**
     * Constructor
     *
     * @param hostWithOptionalPort
     * @param portFirstOption
     */
    public Address(String hostWithOptionalPort, String portFirstOption) {
        if (hostWithOptionalPort != null)
            this.hostWithOptionalPort = hostWithOptionalPort;

        if (portFirstOption != null && !portFirstOption.isEmpty())
            this.inputPort = Integer.parseInt(portFirstOption);

        parse();
    }

    /**
     * Constructor
     *
     * @param hostWithOptionalPort
     * @param portFirstOption
     */
    public Address(String hostWithOptionalPort, int portFirstOption) {
        if (hostWithOptionalPort != null)
            this.hostWithOptionalPort = hostWithOptionalPort;

        if (portFirstOption >= 0)
            this.inputPort = portFirstOption;

        parse();
    }

    /**
     * Returns the host, stripped of any trailing colon and port
     *
     * @return host
     */
    public String getBareHost() {
        return bareHost;
    }

    /**
     * Returns the port
     *
     * @return
     */
    public int getPort() {
        return resolvedPort;
    }

    public boolean isPortSet() {
        return resolvedPort != PORT_NOT_SET;
    }

    /**
     * Return whether the host is of IPv6 format or not.
     *
     * @return
     */
    public boolean isIPV6Literal() {
        return isIPV6Literal;
    }

    void parse() {
        int firstIdx = hostWithOptionalPort.indexOf(':');
        bareHost = hostWithOptionalPort;
        resolvedPort = inputPort;
        if (firstIdx != -1) {
            boolean split = false;
            int separatorLength = 1;
            int lastIdx = hostWithOptionalPort.lastIndexOf(':');
            if (firstIdx != lastIdx) {
                isIPV6Literal = true;
            }
            if (firstIdx == lastIdx || hostWithOptionalPort.charAt(lastIdx - 1) == ']') {
                split = true;
            } else if (firstIdx != -1 && lastIdx != -1) {
                String[] tokens = hostWithOptionalPort.split(":");
                if (tokens.length == 9) {
                    lastIdx = hostWithOptionalPort.lastIndexOf(":");
                    split = true;
                }
            }
            if (split) {
                bareHost = hostWithOptionalPort.substring(0, lastIdx);
                resolvedPort = (inputPort == PORT_NOT_SET) ? Integer.parseInt(hostWithOptionalPort.substring(lastIdx + separatorLength)) : inputPort;
            }
            if (bareHost.startsWith("[")) {
                bareHost = bareHost.substring(1);
            }

            if (bareHost.endsWith("]")) {
                int size = bareHost.length();
                bareHost = bareHost.substring(0, size - 1);
            }
        }

        verify();
    }

    public String getURIIPV6Literal() {
        String host = getBareHost();
        if (isIPV6Literal) {
            host = '[' + host + ']';
        }
        return host;
    }

    public String getHostAndPortForURI() {
        String hostPort = getURIIPV6Literal();
        if (resolvedPort != PORT_NOT_SET) {
            hostPort = hostPort + ":" + resolvedPort;
        }
        return hostPort;
    }

    private void verify() {

        String hostPort = getHostAndPortForURI();
        try {
            URI.create("dummy://" + hostPort);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(" host " + getBareHost()
                    + ((getPort() != PORT_NOT_SET) ? " and port " + getPort() : "") +
                    "  not valid", e);
        }
    }
}
