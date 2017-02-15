/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.entities;

import com.vmware.vim25.ManagedObjectReference;
import io.cloudslang.content.vmware.connection.ConnectionResources;

public class SyncProgressUpdater extends ProgressUpdater {

    public SyncProgressUpdater(final long totalNoBytes, final ManagedObjectReference httpNfcLease, final ConnectionResources connectionResources) {
        super(totalNoBytes, httpNfcLease, connectionResources);
    }

    @Override
    public final void updateBytesSent(final long addedValue) throws Exception {
        int oldPercentage = getFloorPercentage();
        this.bytesSent += addedValue; //getFloorPercentage() could return a different value now
        if (bytesSent == totalNoBytes) {
            updateProgressCompleted();
        } else if (getFloorPercentage() != oldPercentage) {
            updateLeaseProgress(getFloorPercentage());
        }
    }

    @Override
    public void run() {
    }
}
