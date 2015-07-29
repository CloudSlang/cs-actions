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
    private String srcHost;
    private int srcPort;
    private String srcUsername;
    private String srcPassword;
    private String destHost;
    private int destPort;
    private String destUsername;
    private String destPassword;
    private boolean remoteToRemote;

    public ConnectionDetails(String destHost, int destPort, String destUsername, String destPassword) {
        this.destHost = destHost;
        this.destPort = destPort;
        this.destUsername = destUsername;
        this.destPassword = destPassword;
        this.remoteToRemote = false;
    }

    public ConnectionDetails(String srcHost, int srcPort, String srcUsername, String srcPassword, String destHost, int destPort, String destUsername, String destPassword) {
        this.destHost = destHost;
        this.destPort = destPort;
        this.destUsername = destUsername;
        this.destPassword = destPassword;
        this.srcHost = srcHost;
        this.srcPort = srcPort;
        this.srcUsername = srcUsername;
        this.srcPassword = srcPassword;
        this.remoteToRemote = true;
    }

    public String getSrcPassword() {
        return srcPassword;
    }

    public String getSrcUsername() {
        return srcUsername;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public String getSrcHost() {
        return srcHost;
    }

    public String getDestHost() {
        return destHost;
    }

    public int getDestPort() {
        return destPort;
    }

    public String getDestUsername() {
        return destUsername;
    }

    public String getDestPassword() {
        return destPassword;
    }

    public boolean isRemoteToRemote() {
        return remoteToRemote;
    }


}
