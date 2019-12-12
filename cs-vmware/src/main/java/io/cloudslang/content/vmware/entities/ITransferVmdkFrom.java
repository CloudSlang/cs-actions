

package io.cloudslang.content.vmware.entities;

import java.io.OutputStream;

public interface ITransferVmdkFrom {

    long uploadTo(final OutputStream outputStream, final ProgressUpdater progressUpdater) throws Exception;
}
