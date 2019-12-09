

package io.cloudslang.content.vmware.entities;

import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TimedoutFaultMsg;

import java.io.OutputStream;

public class TransferVmdkTask implements Runnable {

    private final ITransferVmdkFrom source;
    private final TransferVmdkToUrl destination;
    private final ProgressUpdater progressUpdater;

    public TransferVmdkTask(final ITransferVmdkFrom fromFile, final TransferVmdkToUrl toUrl,
                            ProgressUpdater progressUpdater) throws RuntimeFaultFaultMsg, TimedoutFaultMsg {
        this.source = fromFile;
        this.destination = toUrl;
        this.progressUpdater = progressUpdater;
    }

    @Override
    public void run() {
        try (OutputStream output = destination.getOutputStream()) {
            source.uploadTo(output, progressUpdater);
        } catch (Exception e) {
            //TODO: log when a logging system is adopted for content
        }
    }
}
