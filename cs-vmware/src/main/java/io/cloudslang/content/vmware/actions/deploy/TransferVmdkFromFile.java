package io.cloudslang.content.vmware.actions.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TransferVmdkFromFile implements ITransferVmdkFrom {

    private FileInputStream fis;

    public TransferVmdkFromFile(File vmdkFile) throws IOException {
        fis = new FileInputStream(vmdkFile);
    }

    @Override
    public InputStream getInputStream() {
        return fis;
    }
}
