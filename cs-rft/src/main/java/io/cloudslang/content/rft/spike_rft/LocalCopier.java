package io.cloudslang.content.rft.spike_rft;

/**
 * </p> Copies a file to/from the local host.
 *
 * @version 1.0, 21 April 2008
 * @author Cristina Nistor
 * @version 2.0, 15 May 2008, modified by Stephen Atwell
 * @version 2.1, 19 May 2008, modified by Cristina Nistor
 */

import com.opsware.pas.content.commons.util.FSUtil;

import java.io.File;

public class LocalCopier extends SimpleCopier {

    private String overwrite = "true";

    public LocalCopier() {

    }

    protected IReader getFile(String source) throws Exception {
        return new SimpleReader(new File(source).getName(), new File(source));
    }

    protected void getFile(String source, File destFile) throws Exception {
        putFile(new SimpleReader(new File(source).getName(), new File(source)), destFile.getAbsolutePath());
    }

    protected void putFile(IReader sourceFile, String destination) throws Exception {
        File dest = new File(destination);
        if (dest.isDirectory()) {
            dest = new File(destination, sourceFile.getFileName());
        }
        if (overwrite != null && overwrite.equals("false") && (dest.exists())) {
            throw new Exception(destination + " exists, but overwrite is disabled");
        }
        FSUtil.recursiveCopy(sourceFile.getFile(), dest);
    }

    public void setCustomArgument(simpleArgument name, String value) {
        switch (name) {
            case overwrite:
                overwrite = value;
                break;
            default:
                throw new UnsupportedOperationException(getProtocolName() + " does not allow " + name.name() + " to be set");
        }
    }

    public String getCustomArgument(simpleArgument name) {
        switch (name) {
            case overwrite:
                return overwrite;
            default:
                return super.getCustomArgument(name);
        }
    }

    public String getProtocolName() {
        return CopierFactory.copiers.local.name();
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
