package io.cloudslang.content.vmware.actions.deploy;

import com.vmware.vim25.ManagedObjectReference;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncProgressUpdater extends ProgressUpdater {

    final private Logger logger = LoggerFactory.getLogger(SyncProgressUpdater.class);

    public SyncProgressUpdater(final long totalNoBytes, final ManagedObjectReference httpNfcLease, final ConnectionResources connectionResources) {
        super(totalNoBytes, httpNfcLease, connectionResources);
    }

    @Override
    public final void updateBytesSent(final long addedValue) throws Exception {
        int oldPercentage = getFloorPercentage();
        logger.info("oldPercentage = " + oldPercentage);
        this.bytesSent += addedValue;
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
