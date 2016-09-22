package io.cloudslang.content.ssh.utils;


import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import io.cloudslang.content.ssh.entities.IdentityKey;
import io.cloudslang.content.ssh.entities.KeyData;
import io.cloudslang.content.ssh.entities.KeyFile;
import io.cloudslang.content.ssh.exceptions.SSHException;
import io.cloudslang.content.utils.StringUtilities;

/**
 * User: sacalosb
 * Date: 07.01.2016
 */
public class IdentityKeyUtils {
    private static final String KEY_HEADER = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String KEY_FOOTER = "-----END RSA PRIVATE KEY-----";
    private static final String TYPE_HEADER = "Proc-Type: ";
    private static final String DEK_HEADER = "DEK-Info: ";
    private static final String NEW_LINE = "\n";

    public static IdentityKey getIdentityKey(String privateKeyFile, String privateKeyString, String privateKeyPassphrase) {
        IdentityKey identityKey = null;
        if (StringUtilities.isNotEmpty(privateKeyFile)) {
            identityKey = new KeyFile(privateKeyFile);
        }
        if (StringUtilities.isNotEmpty(privateKeyString)) {
            if (identityKey != null) {
                throw new IllegalArgumentException(String.format("%s and %s inputs are mutually exclusive. They can't be both set at the same time.",
                        Constants.PRIVATE_KEY_FILE, Constants.PRIVATE_KEY_DATA));
            }
            identityKey = new KeyData(privateKeyString);
        }
        if (identityKey != null && StringUtilities.isNotEmpty(privateKeyPassphrase)) {
            identityKey.setPassPhrase(privateKeyPassphrase);
        }
        return identityKey;
    }

    public static void setIdentity(JSch jsch, IdentityKey identityKey) throws SSHException {
        byte[] passPhrase = identityKey.getPassPhrase();
        if (identityKey instanceof KeyFile) {
            try {
                jsch.addIdentity(((KeyFile) identityKey).getKeyFilePath(), passPhrase);
            } catch (JSchException e) {
                throw new SSHException("The keyFilePath is invalid.", e);
            }
        } else {
            try {
                jsch.addIdentity(((KeyData) identityKey).getKeyName(), ((KeyData) identityKey).getPrvKeyData(), null, passPhrase);
            } catch (JSchException e) {
                throw new SSHException("The " + Constants.PRIVATE_KEY_DATA + " is invalid.", e);
            }
        }
    }

    public static String fixPrivateKeyFormat(String privateKeyString) {
        if (privateKeyString.contains(NEW_LINE)) {
            return privateKeyString;
        }
        String processedKey = privateKeyString;
        // extract header and footer
        processedKey = processedKey.replace(KEY_HEADER, "").trim();
        processedKey = processedKey.replace(KEY_FOOTER, "").trim();
        // process encryption headers
        String procType = null;
        String dekInfo = null;
        int typeHeaderIndex = processedKey.indexOf(TYPE_HEADER);
        if (typeHeaderIndex >= 0) {
            typeHeaderIndex += TYPE_HEADER.length();
            procType = processedKey.substring(typeHeaderIndex, processedKey.indexOf(" ", typeHeaderIndex)).trim();
            int procTypeHeaderIndex = processedKey.indexOf(procType) + procType.length();
            processedKey = processedKey.substring(procTypeHeaderIndex, processedKey.length()).trim();
        }
        int dekHeaderIndex = processedKey.indexOf(DEK_HEADER);
        if (dekHeaderIndex >= 0) {
            dekHeaderIndex += DEK_HEADER.length();
            dekInfo = processedKey.substring(dekHeaderIndex, processedKey.indexOf(" ", dekHeaderIndex)).trim();
            int dekInfoHeaderIndex = processedKey.indexOf(dekInfo) + dekInfo.length();
            processedKey = processedKey.substring(dekInfoHeaderIndex, processedKey.length()).trim();
        }
        // recreate the key
        processedKey = processedKey.replace(" ", NEW_LINE).trim();
        if (StringUtilities.isNotEmpty(dekInfo)) {
            processedKey = DEK_HEADER + dekInfo + NEW_LINE + NEW_LINE + processedKey;
        }
        if (StringUtilities.isNotEmpty(procType)) {
            processedKey = TYPE_HEADER + procType + NEW_LINE + processedKey;
        }
        processedKey = KEY_HEADER + NEW_LINE + processedKey + NEW_LINE + KEY_FOOTER + NEW_LINE;
        return processedKey;
    }
}
