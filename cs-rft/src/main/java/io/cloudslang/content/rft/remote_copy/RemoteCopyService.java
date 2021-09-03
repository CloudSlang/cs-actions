package io.cloudslang.content.rft.remote_copy;

import com.opsware.pas.content.commons.util.StringUtils;
import io.cloudslang.content.rft.remote_copy.CopierFactory.copiers;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.rft.utils.Constants.VERSION;
import static io.cloudslang.content.rft.utils.Inputs.RemoteCopyInputs.DEST_CHARACTER_SET;
import static io.cloudslang.content.rft.utils.Inputs.RemoteCopyInputs.SRC_CHARACTER_SET;

public class RemoteCopyService {

    public Map<String, String> execute(RemoteCopyInputs inputs) {

        Map<String, String> results = new HashMap();

        try {
            validateProtocol(inputs.getSourceProtocol());
            validateProtocol(inputs.getDestinationProtocol());
            ICopier src = CopierFactory.getExecutor(inputs.getSourceProtocol());
            src.setProtocol(inputs.getSourceProtocol());
            ICopier dest = CopierFactory.getExecutor(inputs.getDestinationProtocol());
            dest.setProtocol(inputs.getDestinationProtocol());

            src.setVersion(VERSION);
            dest.setVersion(VERSION);

            checkOptions(inputs.getSourceProtocol(), inputs.getSourceHost());
            checkOptions(inputs.getDestinationProtocol(), inputs.getDestinationHost());

            setCredentials(src, inputs.getSourceHost(), inputs.getSourcePort(), inputs.getSourceUsername(), inputs.getSourcePassword(),
                    inputs.getSourcePrivateKeyFile());
            setCredentials(dest, inputs.getDestinationHost(), inputs.getDestinationPort(), inputs.getDestinationUsername(),
                    inputs.getDestinationPassword(), inputs.getDestinationPrivateKeyFile());

            setAndValidateCharacterSet(src, inputs.getSourceCharacterSet(), SRC_CHARACTER_SET);
            setAndValidateCharacterSet(dest, inputs.getDestinationCharacterSet(), DEST_CHARACTER_SET);

            setConnectionTimeout(src, inputs.getConnectionTimeout());
            setConnectionTimeout(dest, inputs.getConnectionTimeout());
            setExecutionTimeout(src, inputs.getExecutionTimeout());
            setExecutionTimeout(dest, inputs.getExecutionTimeout());

            src.copyTo(dest, inputs.getSourcePath(), inputs.getDestinationPath());
            results.put(RETURN_RESULT, "Copy completed successfully!");
            results.put(RETURN_CODE, "0");

        } catch (Exception e) {
            results.put(EXCEPTION, String.valueOf(e));
            results.put(RETURN_RESULT, (e.getMessage()));
            results.put(RETURN_CODE, "-1");
        }
        return results;

    }

    public static void setConnectionTimeout(ICopier src, String connectionTimeout) {
        if ((connectionTimeout != null) && (connectionTimeout.length() > 0)) {
            src.setConnectionTimeout(Integer.parseInt(connectionTimeout));
        }
    }

    public static void setExecutionTimeout(ICopier src, String executionTimeout) {
        if ((executionTimeout != null) && (executionTimeout.length() > 0)) {
            src.setExecutionTimeout(Integer.parseInt(executionTimeout));
        }
    }

    private void validateProtocol(String srcProtocol) throws Exception {
        try {
            protocols.valueOf(srcProtocol);
        } catch (Exception e) {
            throw (new Exception("Protocol " + srcProtocol + " not supported!"));
        }
    }

    public enum protocols {local, scp, sftp, smb3}

    public static void checkOptions(String copier, String host) throws Exception {
        switch (copiers.valueOf(copier)) {
            case local:
                if (host == null || !host.trim().equalsIgnoreCase("localhost")) {
                    throw new Exception("When the protocol is local, the host must be localhost!\n");
                }
                break;
            case scp:
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

    private void setAndValidateCharacterSet(ICopier copier, String characterSet, String source) throws Exception {
        try {
            if (!setCharacterSet(copier, characterSet)) {
                throw new Exception(source + " input: " + characterSet + " is not a valid character set name");
            }
        } catch (IllegalCharsetNameException icne) {
            throw new Exception(source + " input: " + characterSet + " is not a valid character set name");
        }
    }

    public static boolean setCharacterSet(ICopier copier, String characterSetName) {
        if (copiers.valueOf(copier.getProtocolName()) == copiers.sftp) {
            if (!StringUtils.isNull(characterSetName)) {
                if (!Charset.isSupported(characterSetName)) {
                    return false;
                }
            }
            copier.setCustomArgument(ICopier.simpleArgument.characterSet, characterSetName);
        }
        return true;
    }

}
