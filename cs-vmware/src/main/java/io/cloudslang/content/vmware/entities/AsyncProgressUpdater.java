package io.cloudslang.content.vmware.entities;

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
    }

    @Override
    public void run() {
        int percentage = 0;
        try {
            while (bytesSent != totalNoBytes) {
                sleep(100);
                final int newPercentage = getFloorPercentage();
                if (newPercentage != percentage) {
                    updateLeaseProgress(newPercentage);
                    percentage = newPercentage;
                }
            }
            updateProgressCompleted();
        } catch (Exception e) {
            //TODO: log maybe
        }
    }
}
