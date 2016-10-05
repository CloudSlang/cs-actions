package io.cloudslang.content.vmware.actions.deploy;

import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TimedoutFaultMsg;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;

public class TransferVmdkTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(TransferVmdkTask.class);

    private final ITransferVmdkFrom source;
    private final TransferVmdkToUrl destination;
    private final ProgressUpdater progressUpdater;

    protected long written;

    public TransferVmdkTask(ITransferVmdkFrom fromFile, TransferVmdkToUrl toUrl,
                            ProgressUpdater progressUpdater) throws RuntimeFaultFaultMsg, TimedoutFaultMsg {
        this.source = fromFile;
        this.destination = toUrl;
        this.progressUpdater = progressUpdater;
    }

    @Override
    public void run() {
        try (InputStream input = source.getInputStream(); OutputStream output = destination.getOutputStream()) {
            written = Uploader.copyAll(input, output, progressUpdater);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
