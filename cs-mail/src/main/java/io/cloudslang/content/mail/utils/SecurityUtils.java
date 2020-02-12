package io.cloudslang.content.mail.utils;

import io.cloudslang.content.mail.constants.ExceptionMsgs;
import io.cloudslang.content.mail.entities.DecryptableMessageInput;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public final class SecurityUtils {

    public static void addDecryptionSettings(KeyStore ks, RecipientId recId, DecryptableMessageInput input) throws Exception {
        char[] smimePw = input.getDecryptionKeystorePassword().toCharArray();

        java.security.Security.addProvider(new BouncyCastleProvider());

        try (InputStream decryptionStream = new URL(input.getDecryptionKeystore()).openStream()) {
            ks.load(decryptionStream, smimePw);
        }

        if (StringUtils.EMPTY.equals(input.getDecryptionKeyAlias())) {
            Enumeration aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                String alias = (String) aliases.nextElement();

                if (ks.isKeyEntry(alias)) {
                    input.setDecryptionKeyAlias(alias);
                }
            }

            if (StringUtils.EMPTY.equals(input.getDecryptionKeyAlias())) {
                throw new Exception(ExceptionMsgs.PRIVATE_KEY_ERROR_MESSAGE);
            }
        }

        // find the certificate for the private key and generate a
        // suitable recipient identifier.
        X509Certificate cert = (X509Certificate) ks.getCertificate(input.getDecryptionKeyAlias());
        if (null == cert) {
            throw new Exception("Can't find a key pair with alias \"" + input.getDecryptionKeyAlias() +
                    "\" in the given keystore");
        }
        if (input.isVerifyCertificate()) {
            cert.checkValidity();
        }

        recId.setSerialNumber(cert.getSerialNumber());
        recId.setIssuer(cert.getIssuerX500Principal().getEncoded());
    }
}
