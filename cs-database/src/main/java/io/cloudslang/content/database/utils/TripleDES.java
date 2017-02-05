/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * /**
 * Created by victor on 13.01.2017.
 */
public class TripleDES {
    private static String DEFAULT_CODEPAGE = "windows-1252";
    private static String ENCRYPTION_MODE = "DESede/ECB/PKCS5Padding";
    private static String ENCRYPTION_KEYSPECTYPE = "DESede";

    /**
     * encrypt a plain password
     *
     * @param aPlainPass a password in plain text
     * @return an encrypted password
     * @throws Exception
     */
    public static String encryptPassword(final String aPlainPass) throws Exception {
        if (aPlainPass == null)
            return "";
        byte[] encBytes = encryptString(aPlainPass.getBytes(DEFAULT_CODEPAGE));
        return Base64.encodeBase64String(encBytes);
    }

    public static byte[] md5Hash(@NotNull final String toHash) {
        byte[] digest = DigestUtils.md5(toHash.getBytes());
        byte[] key = new byte[24];
        System.arraycopy(digest, 0, key, 0, 16);
        System.arraycopy(digest, 0, key, 16, 8);
        return key;
    }

    static byte[] encryptString(byte[] text) throws Exception {
        final SecretKey key = new SecretKeySpec(TripleDES.md5Hash("NpWsCaJQj1LaXt)YYnzr\\%zP~RydB*3YGutr*@|A\\ckG3\\Yf%k"), ENCRYPTION_KEYSPECTYPE);
        try {
            final Cipher cipher = Cipher.getInstance(ENCRYPTION_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(text);
        } catch (Exception e) {
            return null;
        }
    }

}


