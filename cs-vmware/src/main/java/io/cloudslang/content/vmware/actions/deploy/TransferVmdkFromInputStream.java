package io.cloudslang.content.vmware.actions.deploy;

import java.io.InputStream;
import java.io.OutputStream;

import static io.cloudslang.content.vmware.constants.Constants.SIZE_4K;

/**
 * Created by giloan on 10/5/2016.
 */
public class TransferVmdkFromInputStream implements ITransferVmdkFrom {

    private static final String INPUT_STREAM_MAY_NOT_BE_NULL = "Source input stream may not be null";
    private final InputStream inputStream;
    private final long length;

    public TransferVmdkFromInputStream(final InputStream inputStream, long length) {
        if (inputStream == null) {
            throw new IllegalArgumentException(INPUT_STREAM_MAY_NOT_BE_NULL);
        }
        this.inputStream = inputStream;
        this.length = length;
    }

    @Override
    public long uploadTo(OutputStream outputStream, ProgressUpdater progressUpdater) throws Exception {
        long bytesCopied = 0;
        final byte[] buffer = new byte[SIZE_4K];
        try (InputStream is = inputStream) {
            while (length > bytesCopied) {
                final int bytesRead = is.read(buffer, 0, (int) Math.min(SIZE_4K, length - bytesCopied));
                if (bytesRead == -1) {
                    break;
                }
                outputStream.write(buffer, 0, bytesRead);
                outputStream.flush();
                bytesCopied += bytesRead;
                progressUpdater.updateBytesSent(bytesRead);
//            logger.info("update progress bar with: " + bytesRead);
                System.out.println("Thread id: " + Thread.currentThread().getId() + " thread name: " + Thread.currentThread().getName());
            }
        }
        return bytesCopied;
    }
}
