package io.cloudslang.content.vmware.actions.deploy;

import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TimedoutFaultMsg;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TransferVmdk extends Thread {

    private final Logger logger = LoggerFactory.getLogger(TransferVmdk.class);

    private final String deviceKey;
    private final ITransferVmdkFrom source;
    private final TransferVmdkToUrl destination;
    private final ProgressUpdater progressUpdater;

    protected long written;
    protected boolean complete = false;

    public TransferVmdk(String deviceKey, ITransferVmdkFrom fromFile, TransferVmdkToUrl toUrl,
                        ProgressUpdater progressUpdater) throws RuntimeFaultFaultMsg, TimedoutFaultMsg {
        this.deviceKey = deviceKey;
        this.source = fromFile;
        this.destination = toUrl;
        this.progressUpdater = progressUpdater;
    }

    public void run() {
        try (InputStream input = source.getInputStream(); OutputStream output = destination.getOutputStream()) {
            complete = false;
            written = Uploader.copyAll(input, output, progressUpdater);
            complete = true;
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
