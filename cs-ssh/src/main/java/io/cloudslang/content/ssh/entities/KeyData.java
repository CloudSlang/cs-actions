package io.cloudslang.content.ssh.entities;


import io.cloudslang.content.ssh.utils.IdentityKeyUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * User: sacalosb
 * Date: 07.01.2016
 */
public class KeyData extends IdentityKey {
    private byte[] prvKeyData;
    private String keyName;

    public KeyData(String prvKeyData) {
        this.setPrvKeyData(prvKeyData);
        this.passPhrase = null;
        this.setKeyName();
    }

    public KeyData(String prvKeyData, String passPhrase) {
        this.setPrvKeyData(prvKeyData);
        this.setPassPhrase(passPhrase);
        this.setKeyName();
    }

    public byte[] getPrvKeyData() {
        return (prvKeyData == null) ? null : Arrays.copyOf(prvKeyData, prvKeyData.length);
    }

    private void setPrvKeyData(String prvKeyData) {
        String fixedPrivateKey = IdentityKeyUtils.fixPrivateKeyFormat(prvKeyData);
        this.prvKeyData = fixedPrivateKey.getBytes(keyEncoding);
    }

    public String getKeyName() {
        return keyName;
    }

    private void setKeyName() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            keyName = new BigInteger(1, messageDigest.digest(prvKeyData)).toString(16);
        } catch (NoSuchAlgorithmException e) {
            keyName = Integer.toHexString(Arrays.hashCode(prvKeyData));
        }
    }
}
