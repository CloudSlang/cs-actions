package io.cloudslang.content.vmware.entities;

import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TimedoutFaultMsg;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;

public class TransferVmdkTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(TransferVmdkTask.class);

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
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
