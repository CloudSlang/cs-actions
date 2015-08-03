package io.cloudslang.content.mail.entities;

/**
 * Created by persdana on 8/3/2015.
 */
public enum EncryptionAlgorithmsEnum {
    DES_EDE3_CBC, RC2_CBC, IDEA_CBC,CAST5_CBC,AES128_CBC,AES192_CBC,AES256_CBC,CAMELLIA128_CBC,CAMELLIA192_CBC,CAMELLIA256_CBC,SEED_CBC,DES_EDE3_WRAP,AES128_WRAP,AES256_WRAP,CAMELLIA128_WRAP,CAMELLIA192_WRAP,CAMELLIA256_WRAP,SEED_WRAP,OTHER;

    public static EncryptionAlgorithmsEnum getEncryptionAlgorithm(String encryptionAlgorithm) {
        if(encryptionAlgorithm == null || encryptionAlgorithm.equals(""))
            return AES256_CBC;

        try {
            return EncryptionAlgorithmsEnum.valueOf(encryptionAlgorithm.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}
