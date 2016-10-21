package io.cloudslang.content.vmware.entities;

import io.cloudslang.content.vmware.utils.OvfUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static io.cloudslang.content.vmware.constants.Constants.SIZE_4K;

public class TransferVmdkFromFile implements ITransferVmdkFrom {

    private FileInputStream fis;

    public TransferVmdkFromFile(final File vmdkFile) throws IOException {
        fis = new FileInputStream(vmdkFile);
    }

    @Override
    public long uploadTo(final OutputStream outputStream, final ProgressUpdater progressUpdater) throws Exception {
        long bytesCopied = 0;
        final byte[] buffer = new byte[SIZE_4K];
        int read;
        try (final InputStream is = fis) {
            while (0 <= (read = is.read(buffer))) {
                bytesCopied = OvfUtils.writeToStream(outputStream, progressUpdater, bytesCopied, buffer, read);
            }
        }
        return bytesCopied;
    }
}
