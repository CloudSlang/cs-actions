package io.cloudslang.content.vmware.actions.deploy;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class TransferVmdkToUrl implements ITransferVmdkTo {

    private static final int CHUNK_LENGTH = 524288;

    private OutputStream os;

    public TransferVmdkToUrl(String cookie, URL uploadURL, boolean create)
            throws IOException {

        HttpsURLConnection conn = Uploader.getHTTPSUploadConnection(uploadURL, cookie, CHUNK_LENGTH, 0, create);

        os = conn.getOutputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        return os;
    }
}
