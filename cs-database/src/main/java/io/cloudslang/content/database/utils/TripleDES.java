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
 * TripleDES class for Encrypting/Decrypting Passwords, this code is copied from
 * ContentUtils/Invocation/RSFlowInvokeJava/src/
 * com.opsware.pas.content.utils.rsflowinvoke
 * TODO: remove the old one and change codes that ref to the old one
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

class MaskingThread extends Thread {
    private volatile boolean stop;
    private char echochar = '*';

    /**
     * @param prompt The prompt displayed to the user
     */
    public MaskingThread(String prompt) {
        System.out.print(prompt);
    }

    /**
     * Begin masking until asked to stop.
     */
    public void run() {

        int priority = Thread.currentThread().getPriority();
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        try {
            stop = true;
            while (stop) {
                System.out.print("\010" + echochar);
                try {
                    // attempt masking at this rate
                    Thread.sleep(1);
                } catch (InterruptedException iex) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        } finally { // restore the original priority
            Thread.currentThread().setPriority(priority);
        }
    }

    /**
     * Instruct the thread to stop masking.
     */
    public void stopMasking() {
        this.stop = false;
    }
}

class Base64 extends java.util.prefs.AbstractPreferences {

    //keep the key state

    private java.util.Hashtable<String, String> encodedStore = new java.util.Hashtable<String, String>();


    /**
     * Creates a new instance of YourPreferences
     */

    private Base64(java.util.prefs.AbstractPreferences prefs, java.lang.String string) {

        super(prefs, string);

    }


    public static String encode(byte[] raw) throws java.io.UnsupportedEncodingException {
        Base64 me = new Base64(null, "");
        return me.encodeBase64(raw);

    }


    public static byte[] decode(String base64String) throws java.io.IOException {

        Base64 me = new Base64(null, "");

        return me.decodeBase64("Standard_key", base64String);

    }


    public java.lang.String encodeBase64(byte[] raw) throws java.io.UnsupportedEncodingException {

        String key = new String(raw, TripleDES.DEFAULT_CODEPAGE);

        this.putByteArray(key, raw);

        return this.encodedStore.get(key);

    }


    public byte[] decodeBase64(java.lang.String key, java.lang.String base64String)

            throws java.io.UnsupportedEncodingException, java.io.IOException {

        this.encodedStore.put(key, base64String);

        byte[] byteResults = this.getByteArray(key, new byte[0]);

        return byteResults;

    }

    //intercept key lookup and return our own base64 encoded string to super

    public String get(String key, String def) {

        return this.encodedStore.get(key);

    }

    //intercepts put captures the base64 encoded string and returns it

    public void put(String key, String value) {

        this.encodedStore.put(key, value);//save the encoded string

    }

    //dummy implementation as AbstractPreferences is extended to get acces to protected

    //methods and to overide put(String,String) and get(String,String)

    protected java.util.prefs.AbstractPreferences childSpi(String name) {
        return null;
    }

    protected String[] childrenNamesSpi() throws java.util.prefs.BackingStoreException {
        return null;
    }

    protected void flushSpi() throws java.util.prefs.BackingStoreException {
    }

    protected String getSpi(String key) {
        return null;
    }

    protected String[] keysSpi() throws java.util.prefs.BackingStoreException {
        return null;
    }

    protected void putSpi(String key, String value) {
    }

    protected void removeNodeSpi() throws java.util.prefs.BackingStoreException {
    }

    protected void removeSpi(String key) {
    }

    protected void syncSpi() throws java.util.prefs.BackingStoreException {
    }

}


