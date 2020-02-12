package io.cloudslang.content.mail.entities;

public interface DecryptableMessageInput {
    String getDecryptionKeystorePassword();

    String getDecryptionKeystore();

    String getDecryptionKeyAlias();

    void setDecryptionKeyAlias(String alias);

    boolean isVerifyCertificate();
}
