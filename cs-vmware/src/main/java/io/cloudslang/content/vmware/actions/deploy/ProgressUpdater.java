package io.cloudslang.content.vmware.actions.deploy;

import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TimedoutFaultMsg;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgressUpdater implements Runnable {

    final private Logger logger = LoggerFactory.getLogger(ProgressUpdater.class);

    private final long totalNoBytes;
    private final ManagedObjectReference httpNfcLease;
    private final ConnectionResources connectionResources;
    private volatile long bytesSent;

    private volatile boolean shutdown;

    public ProgressUpdater(long totalNoBytes, ConnectionResources connectionResources, ManagedObjectReference httpNfcLease) {
        this.totalNoBytes = totalNoBytes;
        this.connectionResources = connectionResources;
        this.httpNfcLease = httpNfcLease;
        updateBytesSent(0);
        logger.info("Progress updater was created! Amount to transfer is: " + totalNoBytes);
    }

    public void updateBytesSent(long addedValue) {
        if (0 < addedValue) {
            this.bytesSent += addedValue;
        }
        logger.info("The bytesSent value was increased. It's now: " + bytesSent);
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                int percentage = (int) (((float) bytesSent / totalNoBytes) * 100);
                logger.info("HttpNfcLease progress is updated to " + percentage + "%. (totalSize: " + totalNoBytes + "; bytesSent: " + bytesSent + ")");
                connectionResources.getVimPortType().httpNfcLeaseProgress(httpNfcLease, percentage);
                logger.info("HttpNfcLease progress was updated!");
                Thread.sleep(3000);
            }
        } catch (RuntimeFaultFaultMsg runtimeFaultFaultMsg) {
            logger.error(ExceptionUtils.getStackTrace(runtimeFaultFaultMsg));
        } catch (InterruptedException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        } catch (TimedoutFaultMsg timedoutFaultMsg) {
            logger.error(ExceptionUtils.getStackTrace(timedoutFaultMsg));
        }
    }

    private boolean leaseIsReady() throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        return "ready" == OvfUtils.gethttpNfcLeaseState(connectionResources, httpNfcLease);
    }

    public void shutdown() {
        shutdown = true;
    }
}
