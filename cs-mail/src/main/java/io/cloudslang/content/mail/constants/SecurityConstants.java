package io.cloudslang.content.mail.constants;

public final class SecurityConstants {
    public static final String BOUNCY_CASTLE_PROVIDER = "BC";
    public static final String PKCS_KEYSTORE_TYPE = "PKCS12";
    public static final String ENCRYPTED_CONTENT_TYPE = "application/pkcs7-mime; name=\"smime.p7m\"; " +
            "smime-type=enveloped-data";
    public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
    public static final String ENCRYPT_RECID = "The encryption recId is: ";
    public static final String SSL = "SSL";
    public static final String DEFAULT_PASSWORD_FOR_STORE = "changeit";
    public static final String SSL_SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
    public static final String SECURE_SUFFIX_FOR_POP3_AND_IMAP = "s";
}
