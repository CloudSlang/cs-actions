

package io.cloudslang.content.vmware.entities;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class TransferVmdkToUrl implements ITransferVmdkTo {

    private static final int CHUNK_LENGTH = 524288;

    private OutputStream outputStream;

    public TransferVmdkToUrl(final URL uploadURL, final boolean create) throws IOException {
        final HttpsURLConnection conn = Uploader.getHTTPSUploadConnection(uploadURL, CHUNK_LENGTH, 0, create);
        outputStream = conn.getOutputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }
}
