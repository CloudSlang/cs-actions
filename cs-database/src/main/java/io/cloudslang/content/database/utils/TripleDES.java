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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.security.MessageDigest;
import java.util.Arrays;


/**
 * /**
 * Created by victor on 13.01.2017.
 */
public class TripleDES {
    static String DEFAULT_CODEPAGE = "windows-1252";
    private static String ENCRYPTION_MODE = "DESede/ECB/PKCS5Padding";
    private static String ENCRYPTION_KEYSPECTYPE = "DESede";

    public static String decryptPassword(String encPass) {
        try {
            return decryptString(Base64.decode(encPass));
        } catch (Exception e) {
            return "";
        }
    }

    public static String createPassword() throws Exception {
        String input1 = new String(TripleDES.getPassword(System.in, "*Enter password "));
        String input2 = new String(TripleDES.getPassword(System.in, "Enter password again "));
        if (!(input1.equals(input2))) {
            System.out.println("\n\nThe passwords you entered are not the same!\nPlease try again...\n\n");
            return createPassword();
        } else {
            byte[] encString = encryptString(input1.getBytes(DEFAULT_CODEPAGE));

            return Base64.encode(encString);
        }
    }

    /**
     * encrypt a plain password
     *
     * @param aPlainPass a password in plain text
     * @return an encrypted password
     * @throws Exception
     */
    public static String encryptPassword(String aPlainPass) throws Exception {
        String retEncryptedPass = null;
        if (aPlainPass == null)
            return "";
        byte[] encBytes = encryptString(aPlainPass.getBytes(DEFAULT_CODEPAGE));
        retEncryptedPass = Base64.encode(encBytes);

        return retEncryptedPass;
    }

    private static byte[] md5Hash(String toHash) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(toHash.getBytes());
        byte[] key = new byte[24];
        System.arraycopy(digest, 0, key, 0, 16);
        System.arraycopy(digest, 0, key, 16, 8);

        return key;
    }

    private static SecretKeySpec secretKeySpec() throws Exception {
        return new SecretKeySpec(TripleDES.md5Hash("NpWsCaJQj1LaXt)YYnzr\\%zP~RydB*3YGutr*@|A\\ckG3\\Yf%k"), ENCRYPTION_KEYSPECTYPE);
    }

    private static String decryptString(byte[] text) throws Exception {
        SecretKey key = secretKeySpec();

        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_MODE);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] data = cipher.doFinal(text);

            return new String(data, 0, data.length);
        } catch (Exception e) {
            return null;
        }
    }

    static byte[] encryptString(byte[] text) throws Exception {
        SecretKey key = secretKeySpec();

        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] data = cipher.doFinal(text);

            return data;
        } catch (Exception e) {
            return null;
        }
    }

    private static final char[] getPassword(InputStream in, String prompt) throws IOException {
        MaskingThread maskingthread = new MaskingThread(prompt);
        Thread thread = new Thread(maskingthread);
        thread.start();

        char[] lineBuffer;
        char[] buf;

        buf = lineBuffer = new char[128];

        int room = buf.length;
        int offset = 0;
        int c;

        loop:
        while (true) {
            switch (c = in.read()) {
                case -1:
                case '\n':
                    break loop;

                case '\r':
                    int c2 = in.read();
                    if ((c2 != '\n') && (c2 != -1)) {
                        if (!(in instanceof PushbackInputStream)) {
                            in = new PushbackInputStream(in);
                        }
                        ((PushbackInputStream) in).unread(c2);
                    } else {
                        break loop;
                    }

                default:
                    if (--room < 0) {
                        buf = new char[offset + 128];
                        room = buf.length - offset - 1;
                        System.arraycopy(lineBuffer, 0, buf, 0, offset);
                        Arrays.fill(lineBuffer, ' ');
                        lineBuffer = buf;
                    }
                    buf[offset++] = (char) c;
                    break;
            }
        }
        maskingthread.stopMasking();
        if (offset == 0) {
            return null;
        }
        char[] ret = new char[offset];
        System.arraycopy(buf, 0, ret, 0, offset);
        Arrays.fill(buf, ' ');
        return ret;
    }

}


