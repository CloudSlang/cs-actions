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

/**
 * Created by victor on 13.01.2017.
 */
class Base64 extends java.util.prefs.AbstractPreferences {

    //keep the key state

    private java.util.Hashtable<String, String> encodedStore = new java.util.Hashtable<String, String>();


    /**
     * Creates a new instance of YourPreferences
     */

    private Base64(java.util.prefs.AbstractPreferences prefs, String string) {

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


    public String encodeBase64(byte[] raw) throws java.io.UnsupportedEncodingException {

        String key = new String(raw, TripleDES.DEFAULT_CODEPAGE);

        this.putByteArray(key, raw);

        return this.encodedStore.get(key);

    }


    public byte[] decodeBase64(String key, String base64String)

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
