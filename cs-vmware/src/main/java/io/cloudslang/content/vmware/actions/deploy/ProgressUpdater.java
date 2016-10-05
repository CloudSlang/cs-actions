package io.cloudslang.content.vmware.actions.deploy;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TimedoutFaultMsg;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.floor;

/**
 * Created by giloan on 10/5/2016.
 */
public abstract class ProgressUpdater implements Runnable {

    protected final long totalNoBytes;
    protected final ManagedObjectReference httpNfcLease;
    protected final ConnectionResources connectionResources;
    final private Logger logger = LoggerFactory.getLogger(ProgressUpdater.class);
    protected long bytesSent;

    public ProgressUpdater(long totalNoBytes, ManagedObjectReference httpNfcLease, ConnectionResources connectionResources) {
        this.totalNoBytes = totalNoBytes;
        this.httpNfcLease = httpNfcLease;
        this.connectionResources = connectionResources;
        this.bytesSent = 0;
    }

    abstract void updateBytesSent(final long bytesSent) throws Exception;

    protected final synchronized void updateLeaseProgress(final int percentage) throws RuntimeFaultFaultMsg, TimedoutFaultMsg {
        logger.info("HttpNfcLease progress is updated to " + getFloorPercentage() + "%. (totalSize: " + totalNoBytes + "; bytesSent: " + bytesSent + ")");
        connectionResources.getVimPortType().httpNfcLeaseProgress(httpNfcLease, percentage);
    }

    protected final void updateProgressCompleted() throws Exception {
        connectionResources.getVimPortType().httpNfcLeaseComplete(httpNfcLease);
        logger.info("HttpNfcLease is completed!");
    }

    protected final int getFloorPercentage() {
        return (int) floor(((float) this.bytesSent / this.totalNoBytes) * 100);
    }

}
