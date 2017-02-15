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

import static java.lang.Thread.sleep;

public class AsyncProgressUpdater extends ProgressUpdater {

    public AsyncProgressUpdater(long totalNoBytes, ManagedObjectReference httpNfcLease, ConnectionResources connectionResources) {
        super(totalNoBytes, httpNfcLease, connectionResources);
    }

    @Override
    public final synchronized void updateBytesSent(final long bytesSent) throws Exception {
        this.bytesSent += bytesSent;
    }

    @Override
    public void run() {
        int percentage = 0;
        try {
            while (bytesSent < totalNoBytes) {
                sleep(100);
                final int newPercentage = getFloorPercentage();
                if (newPercentage != percentage) {
                    updateLeaseProgress(newPercentage);
                    percentage = newPercentage;
                }
            }
            updateProgressCompleted();
        } catch (Exception e) {
            //TODO: log when a logging system is adopted for content
        }
    }
}
