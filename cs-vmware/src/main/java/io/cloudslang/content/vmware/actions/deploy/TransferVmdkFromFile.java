package io.cloudslang.content.vmware.actions.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static io.cloudslang.content.vmware.constants.Constants.SIZE_4K;

public class TransferVmdkFromFile implements ITransferVmdkFrom {

    private FileInputStream fis;

    public TransferVmdkFromFile(File vmdkFile) throws IOException {
        fis = new FileInputStream(vmdkFile);
    }

    @Override
    public long uploadTo(OutputStream outputStream, ProgressUpdater progressUpdater) throws Exception {
        long bytesCopied = 0;
        final byte[] buffer = new byte[SIZE_4K];
        int read;
        try (final InputStream is = fis) {
            while (0 <= (read = is.read(buffer))) {
                outputStream.write(buffer, 0, read);
                outputStream.flush();
                bytesCopied += read;
                progressUpdater.updateBytesSent(read);
//            logger.info("update progress bar with: " + read);
                System.out.println("Thread id: " + Thread.currentThread().getId() + " thread name: " + Thread.currentThread().getName());
            }
        }
        return bytesCopied;
    }
}
