package io.cloudslang.content.vmware.actions.deploy;

import com.vmware.vim25.ManagedObjectReference;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Thread.sleep;

/**
 * Created by giloan on 10/5/2016.
 */
public class AsyncProgressUpdater extends ProgressUpdater {

    final private Logger logger = LoggerFactory.getLogger(AsyncProgressUpdater.class);

    public AsyncProgressUpdater(long totalNoBytes, ManagedObjectReference httpNfcLease, ConnectionResources connectionResources) {
        super(totalNoBytes, httpNfcLease, connectionResources);
    }

    @Override
    public final synchronized void updateBytesSent(final long bytesSent) throws Exception {
        this.bytesSent += bytesSent;
        logger.info("Sent bytes were updated. Updated value is: " + this.bytesSent);
    }

    @Override
    public void run() {
        int percentage = 0;
        try {
            while (bytesSent != totalNoBytes) {
                sleep(100);
                logger.info("Total no bytes: " + totalNoBytes);
                logger.info("Bytes Sent: " + bytesSent);
                logger.info("Current percentage: " + getFloorPercentage());
                final int newPercentage = getFloorPercentage();
                if (newPercentage != percentage) {
                    logger.info("Percentage changed: " + getFloorPercentage());
                    updateLeaseProgress(newPercentage);
                    percentage = newPercentage;
                }
            }
            updateProgressCompleted();
        } catch (Exception e) {
        }
    }
}
