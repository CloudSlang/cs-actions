package io.cloudslang.content.httpclient.utils;

public class Inputs {

    public static class HTTPInputs {

        //FROM WIKI
        //==================================================================================================================
        public static final String HOST = "host";
        public static final String PROTOCOL = "protocol";

        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String TLS_VERSION = "tlsVersion";
        public static final String ALLOWED_CIPHERS = "allowedCiphers";
        public static final String TRUST_ALL_ROOTS = "trustAllRoots";
        public static final String X509_HOSTNAME_VERIFIER = "x509HostnameVerifier";
        public static final String TRUST_KEYSTORE = "trustKeystore";
        public static final String TRUST_PASSWORD = "trustPassword";
        public static final String KEYSTORE = "keystore";
        public static final String KEYSTORE_PASSWORD = "keystorePassword";
        public static final String CONNECT_TIMEOUT = "connectTimeout";
        public static final String EXECUTION_TIMEOUT = "executionTimeout";
        public static final String KEEP_ALIVE = "keepAlive";
        public static final String CONNECTIONS_MAX_PER_ROUTE = "connectionsMaxPerRoute";
        public static final String CONNECTIONS_MAX_TOTAL = "connectionsMaxTotal";
        //==================================================================================================================

        public static final String URL = "url";
        public static final String METHOD = "method";
        public static final String FOLLOW_REDIRECTS = "followRedirects";
        public static final String QUERY_PARAMS = "queryParams";
        public static final String QUERY_PARAMS_ARE_URLENCODED = "queryParamsAreURLEncoded";
        public static final String QUERY_PARAMS_ARE_FORM_ENCODED = "queryParamsAreFormEncoded";
        public static final String FORM_PARAMS = "formParams";
        public static final String FORM_PARAMS_ARE_URLENCODED = "formParamsAreURLEncoded";
        public static final String SOURCE_FILE = "sourceFile";
        public static final String REQUEST_CHARACTER_SET = "requestCharacterSet";
        public static final String BODY = "body";
        public static final String CONTENT_TYPE = "contentType";
        public static final String AUTH_TYPE = "authType";
        public static final String PREEMPTIVE_AUTH = "preemptiveAuth";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String KERBEROS_CONFIG_FILE = "kerberosConfFile";
        public static final String KERBEROS_LOGIN_CONFIG_FILE = "kerberosLoginConfFile";
        public static final String KERBEROS_SKIP_PORT_CHECK = "kerberosSkipPortForLookup";
        public static final String SOCKET_TIMEOUT = "socketTimeout";
        public static final String USE_COOKIES = "useCookies";
        public static final String HEADERS = "headers";
        public static final String RESPONSE_CHARACTER_SET = "responseCharacterSet";
        public static final String DESTINATION_FILE = "destinationFile";
        public static final String MULTIPART_BODIES = "multipartBodies";
        public static final String MULTIPART_BODIES_CONTENT_TYPE = "multipartBodiesContentType";
        public static final String MULTIPART_FILES = "multipartFiles";
        public static final String MULTIPART_FILES_CONTENT_TYPE = "multipartFilesContentType";
        public static final String MULTIPART_VALUES_ARE_URLENCODED = "multipartValuesAreURLEncoded";
        public static final String CHUNKED_REQUEST_ENTITY = "chunkedRequestEntity";
        public final static String SESSION_CONNECTION_POOL = "httpClientPoolingConnectionManager";
        public final static String SESSION_COOKIES = "httpClientCookieSession";

    }
}
