package io.cloudslang.content.database.utils;

import java.net.URI;

/**
 * This utility provides for resolving the address (host and port) given the
 * following inputs, all of which are optional
 * <ol>
 * <li>port
 * <li>host, which may optionally include a port (e.g. www.hp.com:22)
 * <li>defaultPort, in the constructor
 * <li>defaultPort, in the toString(), since it may not be known at construction
 * time.
 * </ol>
 *
 * Since there are several possible conflicting sources of port number, the
 * value that is returned by the toString() and getPort() methods will be in
 * priority order, according to the above list. For example, new
 * Address("www.hp.com:22", "80").toString() will return "www.hp.com:80"
 *
 * @author Gary W. Smith
 *
 */
public class Address {
    public final static int PORT_NOT_SET = -1;
    public final static String INTEGER_PORT = "Port";
    private String hostWithOptionalPort = "";
    private int inputPort = PORT_NOT_SET;
    private boolean isIPV6Literal = false;

    int resolvedPort = PORT_NOT_SET;
    String bareHost; // host, stripped of any trailing colon and port number

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

    public Address(String hostWithOptionalPort, int portFirstOption, int defaultPort) {
        this(hostWithOptionalPort, portFirstOption);
        if (!isPortSet()) {
            resolvedPort = defaultPort;
        }
    }

    /**
     * Returns the host, stripped of any trailing colon and port
     * @return host
     */
    public String getBareHost() {
        return bareHost;
    }

    /**
     * Returns the port
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
            if (firstIdx!=lastIdx) {
                isIPV6Literal = true;
            }
            if (firstIdx == lastIdx || hostWithOptionalPort.charAt(lastIdx-1)==']') {
                split = true;
            } else if (firstIdx !=-1 && lastIdx!=-1) {
                String[] tokens = hostWithOptionalPort.split(":");
                if (tokens.length==9) {
                    lastIdx = hostWithOptionalPort.lastIndexOf(":");
                    split = true;
                }
            }
            if (split) {
                bareHost = hostWithOptionalPort.substring(0,lastIdx);
                resolvedPort = (inputPort == PORT_NOT_SET) ? Integer.parseInt(hostWithOptionalPort.substring(lastIdx+separatorLength)) : inputPort;
            }
            if (bareHost.startsWith("[")) {
                bareHost = bareHost.substring(1);
            }

            if(bareHost.endsWith("]")){
                int size = bareHost.length();
                bareHost = bareHost.substring(0, size - 1);
            }
        }

        verify();
    }

    public String getURIIPV6Literal() {
        String host = getBareHost();
        if (isIPV6Literal) {
            host = '['+host+']';
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
            URI.create("dummy://"+hostPort);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(" host "+getBareHost()
                    + ((getPort()!=PORT_NOT_SET)?" and port " + getPort():"") +
                    "  not valid", e);
        }
    }
}
