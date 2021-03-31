package io.cloudslang.content.winrm.utils;

public final class Constants {
    public static final String DEFAULT_TIMEOUT ="60000";
    public static final String BASIC = "Basic";
    public static final String HTTPS = "https";
    public static final String DEFAULT_PROXY_PORT = "8080";
    public static final String BOOLEAN_FALSE = "false";
    public static final String BOOLEAN_TRUE = "true";
    public static final String STRICT = "strict";
    public static final String NEW_LINE = "\n";
    public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
    public static final String CHANGE_IT = "changeit";
    public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
    public static final String EXCEPTION_INVALID_PATH = "%s for %s input is not a valid path.";
    public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
    public static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not a valid number value.";
    public static final String EXCEPTION_INVALID_PORT = "The %s for %s input is not a valid port. The valid ports are: '5985' for http and '5986' for https.";
    public static final String EXCEPTION_INVALID_AUTH_TYPE = "The %s for %s input is not a valid authType value. The valid values are: 'Basic','NTLM','Kerberos'.";
    public static final String EXCEPTION_INVALID_HOSTNAME_VERIFIER = "The %s for %s input is not a valid x509HostnameVerifier value. The valid values are: 'strict','browser_compatible','allow_all'.";
    public static final String EXCEPTION_INVALID_PROXY = "%s for input %s is not a valid proxy port.";
}
