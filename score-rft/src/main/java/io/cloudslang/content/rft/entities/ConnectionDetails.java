/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.rft.entities;

/**
 * Date: 7/21/2015
 *
 * @author lesant
 */
public class ConnectionDetails {
    private String destinationHost;
    private int destinationPort;
    private String destinationUsername;
    private String destinationPassword;

    public ConnectionDetails(String destinationHost, int destinationPort, String destinationUsername, String destinationPassword) {
        this.destinationHost = destinationHost;
        this.destinationPort = destinationPort;
        this.destinationUsername = destinationUsername;
        this.destinationPassword = destinationPassword;
    }

    public String getDestinationHost() {
        return destinationHost;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public String getDestinationUsername() {
        return destinationUsername;
    }

    public String getDestinationPassword() {
        return destinationPassword;
    }

}
