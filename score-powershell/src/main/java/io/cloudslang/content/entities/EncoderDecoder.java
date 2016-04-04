package io.cloudslang.content.entities;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by giloan on 4/3/2016.
 */
public class EncoderDecoder {

    public static String encodeStringInBase64(String str) {
        return new String(Base64.encodeBase64(str.getBytes()));
    }

    public static String decodeBase64String(String str) {
        return new String(Base64.decodeBase64(str));
    }
}
