package io.cloudslang.content.rft.spike_rft;

import java.util.HashMap;


public class CopierFactory {

    private static HashMap<copiers, Class<? extends ICopier>> executorMap = new HashMap<>();

    static {
        executorMap.put(copiers.scp, ScpCopier.class);
        executorMap.put(copiers.local, LocalCopier.class);
        executorMap.put(copiers.ftp, FtpCopier.class);
        executorMap.put(copiers.sftp, SftpCopier.class);
        executorMap.put(copiers.smb, SmbCopier.class);
        executorMap.put(copiers.smb2, SmbCopier.class);
        executorMap.put(copiers.smb3, SmbCopier.class);
    }

    public static ICopier getExecutor(String name) throws Exception {
        try {
            return getExecutor(copiers.valueOf(name));
        } catch (Exception e) {
            throw (new Exception("Protocol " + name + " not supported!"));
        }
    }

    public static ICopier getExecutor(copiers name) throws Exception {
        return executorMap.get(name).newInstance();
    }

    public enum copiers {local, scp, ftp, sftp, smb, smb2, smb3}
}