/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.entities;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TimedoutFaultMsg;
import io.cloudslang.content.vmware.connection.ConnectionResources;

import static java.lang.Math.floor;

/**
 * Created by giloan on 10/5/2016.
 */
public abstract class ProgressUpdater implements Runnable {

    protected final long totalNoBytes;
    protected final ManagedObjectReference httpNfcLease;
    protected final ConnectionResources connectionResources;
    protected long bytesSent;

    public ProgressUpdater(final long totalNoBytes, ManagedObjectReference httpNfcLease, final ConnectionResources connectionResources) {
        this.totalNoBytes = totalNoBytes;
        this.httpNfcLease = httpNfcLease;
        this.connectionResources = connectionResources;
        this.bytesSent = 0;
    }

    public abstract void updateBytesSent(final long bytesSent) throws Exception;

    protected final synchronized void updateLeaseProgress(final int percentage) throws RuntimeFaultFaultMsg, TimedoutFaultMsg {
        connectionResources.getVimPortType().httpNfcLeaseProgress(httpNfcLease, percentage);
    }

    protected final void updateProgressCompleted() throws Exception {
        connectionResources.getVimPortType().httpNfcLeaseComplete(httpNfcLease);
    }

    protected final int getFloorPercentage() {
        return (int) floor(((float) this.bytesSent / this.totalNoBytes) * 100);
    }

}
