package io.cloudslang.content.vmware.actions.deploy;

import java.io.OutputStream;

public interface ITransferVmdkFrom {

    long uploadTo(OutputStream outputStream, ProgressUpdater progressUpdater) throws Exception;
}
