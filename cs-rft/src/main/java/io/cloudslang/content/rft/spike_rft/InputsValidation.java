package io.cloudslang.content.rft.spike_rft;

import com.opsware.pas.content.commons.util.StringUtils;
import io.cloudslang.content.rft.spike_rft.CopierFactory.*;
import io.cloudslang.content.rft.spike_rft.ICopier.*;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

public class InputsValidation {

    public static void checkOptions(String copier, String host, String type) throws Exception {
        switch (copiers.valueOf(copier)) {
            case local:
                if (host == null || !host.trim().equalsIgnoreCase("localhost")) {
                    throw new Exception("When the protocol is local, the host must be localhost!\n");
                }
                break;
            case scp:
                break;
            case ftp:
                if (type == null || type.length() <= 0 || !(type.equalsIgnoreCase("ascii") || type.equalsIgnoreCase("binary"))) {
                    throw new Exception("When the FTP protocol is used, the type must have the value 'ascii' or 'binary'!\n");
                }
                break;
            case sftp:
                break;
            default:
                break;
        }
    }

    public static void setCredentials(ICopier copier, String host, String portString, String username, String password, String privateKeyFile) {
        int port = -1;
        if (portString != null && !portString.isEmpty())
            port = Integer.parseInt(portString);
        if (copiers.valueOf(copier.getProtocolName()) == copiers.local) {
            host = "";
        }
        if (privateKeyFile != null && privateKeyFile.length() > 0) {
            copier.setCredentials(host, port, username, password, privateKeyFile);
        } else {
            copier.setCredentials(host, port, username, password);
        }
    }

    public static boolean setCharacterSet(ICopier copier, String characterSetName) {
        if (copiers.valueOf(copier.getProtocolName()) == copiers.ftp
                || copiers.valueOf(copier.getProtocolName()) == copiers.sftp) {
            if (!StringUtils.isNull(characterSetName)) {
                if (!Charset.isSupported(characterSetName)) {
                    return false;
                }
            }
            copier.setCustomArgument(ICopier.simpleArgument.characterSet, characterSetName);
        } //else do nothing
        return true;
    }

    public static void setTimeout(ICopier src, String srcTimeout) {
        if ((srcTimeout != null) && (srcTimeout.length() > 0)) {
            src.setTimeout(Integer.parseInt(srcTimeout));
        }
    }

    public static void setCustomArgumentForFTP(ICopier src, ICopier dest, String srcProtocol, String destProtocol, String type, boolean passive) throws Exception {
        if (type != null && type.length() > 0) {
            if (copiers.valueOf(srcProtocol) == copiers.ftp || copiers.valueOf(destProtocol) == copiers.ftp) {
                if (copiers.valueOf(srcProtocol) == copiers.ftp) {
                    src.setCustomArgument(simpleArgument.type, type);
                }
                if (copiers.valueOf(destProtocol) == copiers.ftp) {
                    dest.setCustomArgument(simpleArgument.type, type);
                }
            } else {
                throw new Exception("The fileType input must be empty when the FTP protocol is not used!");
            }
        }

        if (passive) {
            if (copiers.valueOf(srcProtocol) == copiers.ftp) {
                src.setCustomArgument(simpleArgument.passive, Boolean.toString(passive));
            }
            if (copiers.valueOf(destProtocol) == copiers.ftp) {
                dest.setCustomArgument(simpleArgument.passive, Boolean.toString(passive));
            }
        }
    }

    public static void setAndValidateCharacterSet(ICopier copier, String characterSet, String source) throws Exception {
        try {
            if (!setCharacterSet(copier, characterSet)) {
                throw new Exception(source + " input: " + characterSet + " is not a valid character set name");
            }
        } catch (IllegalCharsetNameException icne) {
            throw new Exception(source + " input: " + characterSet + " is not a valid character set name");
        }
    }

    public static void validateProtocol(String srcProtocol) throws Exception {
        try {
            protocols.valueOf(srcProtocol);
        } catch (Exception e) {
            throw (new Exception("Protocol " + srcProtocol + " not supported!"));
        }
    }

    public enum protocols {local, scp, ftp, sftp, smb, smb2, smb3}
}
