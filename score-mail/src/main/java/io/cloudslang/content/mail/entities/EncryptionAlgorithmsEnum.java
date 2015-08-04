package io.cloudslang.content.mail.entities;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;

/**
 * Created by persdana on 8/3/2015.
 */
public enum EncryptionAlgorithmsEnum {
    DES_EDE3_CBC(SMIMEEnvelopedGenerator.DES_EDE3_CBC),
    RC2_CBC(SMIMEEnvelopedGenerator.RC2_CBC),
    IDEA_CBC(SMIMEEnvelopedGenerator.IDEA_CBC),
    CAST5_CBC(SMIMEEnvelopedGenerator.CAST5_CBC),
    AES128_CBC(SMIMEEnvelopedGenerator.AES128_CBC),
    AES192_CBC(SMIMEEnvelopedGenerator.AES192_CBC),
    AES256_CBC(SMIMEEnvelopedGenerator.AES256_CBC),
    CAMELLIA128_CBC(SMIMEEnvelopedGenerator.CAMELLIA128_CBC),
    CAMELLIA192_CBC(SMIMEEnvelopedGenerator.CAMELLIA192_CBC),
    CAMELLIA256_CBC(SMIMEEnvelopedGenerator.CAMELLIA256_CBC),
    SEED_CBC(SMIMEEnvelopedGenerator.SEED_CBC),
    DES_EDE3_WRAP(SMIMEEnvelopedGenerator.DES_EDE3_WRAP),
    AES128_WRAP(SMIMEEnvelopedGenerator.AES128_WRAP),
    AES256_WRAP(SMIMEEnvelopedGenerator.AES256_WRAP),
    CAMELLIA128_WRAP(SMIMEEnvelopedGenerator.CAMELLIA128_WRAP),
    CAMELLIA192_WRAP(SMIMEEnvelopedGenerator.CAMELLIA192_WRAP),
    CAMELLIA256_WRAP(SMIMEEnvelopedGenerator.CAMELLIA256_WRAP),
    SEED_WRAP(SMIMEEnvelopedGenerator.SEED_WRAP);

    private static final String SUPPORTED_ENCRYPTION_ALGORITHMS = "DES_EDE3_CBC,RC2_CBC,IDEA_CBC,CAST5_CBC,AES128_CBC,AES192_CBC,AES256_CBC,CAMELLIA128_CBC,CAMELLIA192_CBC,CAMELLIA256_CBC,SEED_CBC,DES_EDE3_WRAP,AES128_WRAP,AES256_WRAP,CAMELLIA128_WRAP,CAMELLIA192_WRAP,CAMELLIA256_WRAP,SEED_WRAP";
    private String encryptionOID = SMIMEEnvelopedGenerator.AES256_CBC;
    private EncryptionAlgorithmsEnum(String encryptionOID) {
        this.encryptionOID = encryptionOID;
    }

    public static EncryptionAlgorithmsEnum getEncryptionAlgorithm(String encryptionAlgorithm) {
        if(StringUtils.isEmpty(encryptionAlgorithm))
            return AES256_CBC;

        try {
            return EncryptionAlgorithmsEnum.valueOf(encryptionAlgorithm.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid encryption algorithm \"" + encryptionAlgorithm + "\". Supported values:" + SUPPORTED_ENCRYPTION_ALGORITHMS);
        }
    }

    public String getEncryptionOID() {
        return this.encryptionOID;
    }
}
