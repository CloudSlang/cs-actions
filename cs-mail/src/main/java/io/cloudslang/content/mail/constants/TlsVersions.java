package io.cloudslang.content.mail.constants;

import java.lang.reflect.Field;

public final class TlsVersions {
    public static final String SSLv3 = "SSLv3";
    public static final String TLSv1_0 = "TLSv1.0";
    public static final String TLSv1_1 = "TLSv1.1";
    public static final String TLSv1_2 = "TLSv1.2";

    public static boolean validate(String tlsVersion) throws IllegalAccessException {
        Field[] possibleVersions = TlsVersions.class.getFields();
        for(Field possibleVersion : possibleVersions) {
            if(possibleVersion.get(null) != null && possibleVersion.get(null).toString().equals(tlsVersion)) {
                return true;
            }
        }
        return false;
    }
}
