package org.score.content.httpclient;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/18/14
 */
public enum HttpClientInputs {
    URL("url"),
    METHOD("method"),
    FOLLOW_REDIRECTS("followRedirects"),
    QUERY_PARAMS("queryParams"),
    ENCODE_QUERY_PARAMS("encodeQueryParams"),
    SOURCE_FILE("sourceFile"),
    REQUEST_CHARACTER_SET("requestCharacterSet"),
    BODY("body"),
    CONTENT_TYPE("contentType"),
    AUTH_TYPE("authType"),
    USERNAME("username"),
    PASSWORD("password"),
    KERBEROS_CONFIG_FILE("kerberosConfFile"),
    PROXY_TYPE("proxyType"),
    PROXY_HOST("proxyHost"),
    PROXY_PORT("proxyPort"),
    PROXY_AUTH_TYPE("proxyAuthType"),
    PROXY_USERNAME("proxyUsername"),
    PROXY_PASSWORD("proxyPassword"),
    TRUST_ALL_ROOTS("trustAllRoots"),
    TRUST_KEYSTORE("trustKeystore"),
    TRUST_PASSWORD("trustPassword"),
    KEYSTORE("keystore"),
    KEYSTORE_PASSWORD("keystorePassword"),
    CONNECTION_TIMEOUT("connectTimeout"),
    SOCKET_TIMEOUT("socketTimeout"),
    USE_COOKIES("useCookies"),
    KEEP_ALIVE("keepAlive"),
    HEADERS("headers"),
    RESPONSE_CHARACTER_SET("responseCharacterSet"),
    DESTINATION_FILE("destinationFile");

//    INPUT_NAME_LIST("inputNameList"),
//    INPUT_VALUE_LIST("inputValueList"),
//    NAME_LIST("nameList"),
//    VALUES_LIST("valueList"),
//    FILE_NAME_LIST("fileNameList"),
//    FILE_VALUE_LIST("fileValueList"),
//    OFFSET("offset"),
//    LENGTH("length");

    private final String name;

    HttpClientInputs(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
