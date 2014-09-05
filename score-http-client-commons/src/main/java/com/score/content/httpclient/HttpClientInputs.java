package com.score.content.httpclient;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;

/**
 * User: davidmih
 * Date: 7/18/14
 */
public class HttpClientInputs {

    public static final String URL = "url";
    public static final String METHOD = "method";
    public static final String FOLLOW_REDIRECTS = "followRedirects";
    public static final String QUERY_PARAMS = "queryParams";
    public static final String ENCODE_QUERY_PARAMS = "encodeQueryParams";
    public static final String SOURCE_FILE = "sourceFile";
    public static final String REQUEST_CHARACTER_SET = "requestCharacterSet";
    public static final String BODY = "body";
    public static final String CONTENT_TYPE = "contentType";
    public static final String AUTH_TYPE = "authType";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String KERBEROS_CONFIG_FILE = "kerberosConfFile";
    public static final String PROXY_HOST = "proxyHost";
    public static final String PROXY_PORT = "proxyPort";
    public static final String PROXY_USERNAME = "proxyUsername";
    public static final String PROXY_PASSWORD = "proxyPassword";
    public static final String TRUST_ALL_ROOTS = "trustAllRoots";
    public static final String TRUST_KEYSTORE = "trustKeystore";
    public static final String TRUST_PASSWORD = "trustPassword";
    public static final String KEYSTORE = "keystore";
    public static final String KEYSTORE_PASSWORD = "keystorePassword";
    public static final String CONNECT_TIMEOUT = "connectTimeout";
    public static final String SOCKET_TIMEOUT = "socketTimeout";
    public static final String USE_COOKIES = "useCookies";
    public static final String KEEP_ALIVE = "keepAlive";
    public static final String CONNECTIONS_MAX_PER_ROUTE = "connectionsMaxPerRoute";
    public static final String CONNECTIONS_MAX_TOTAL = "connectionsMaxTotal";
    public static final String HEADERS = "headers";
    public static final String RESPONSE_CHARACTER_SET = "responseCharacterSet";
    public static final String DESTINATION_FILE = "destinationFile";

    public final static String SESSION_CONNECTION_POOL = "httpClientPoolingConnectionManager";
    public final static String SESSION_COOKIES = "httpClientCookieSession";

    private String url;
    private String authType;
    private String username;
    private String password;
    private String kerberosConfFile;
    private String proxyHost;
    private String proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private String trustAllRoots;
    private String trustKeystore;
    private String trustPassword;
    private String keystore;
    private String keystorePassword;
    private String connectTimeout;
    private String socketTimeout;
    private String useCookies;
    private String keepAlive;
    private String connectionsMaxPerRoute;
    private String connectionsMaxTotal;
    private String headers;
    private String responseCharacterSet;
    private String destinationFile;
    private String followRedirects;
    private String queryParams;
    private String encodeQueryParams;
    private String sourceFile;
    private String body;
    private String contentType;
    private String requestCharacterSet;
    private String method;
    private SerializableSessionObject cookieStoreSessionObject;
    private GlobalSessionObject connectionPoolSessionObject;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKerberosConfFile() {
        return kerberosConfFile;
    }

    public void setKerberosConfFile(String kerberosConfFile) {
        this.kerberosConfFile = kerberosConfFile;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public String getTrustAllRoots() {
        return trustAllRoots;
    }

    public void setTrustAllRoots(String trustAllRoots) {
        this.trustAllRoots = trustAllRoots;
    }

    public String getTrustKeystore() {
        return trustKeystore;
    }

    public void setTrustKeystore(String trustKeystore) {
        this.trustKeystore = trustKeystore;
    }

    public String getTrustPassword() {
        return trustPassword;
    }

    public void setTrustPassword(String trustPassword) {
        this.trustPassword = trustPassword;
    }

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(String connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(String socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public String getUseCookies() {
        return useCookies;
    }

    public void setUseCookies(String useCookies) {
        this.useCookies = useCookies;
    }

    public String getKeepAlive() {
        return keepAlive;
    }

    public String getConnectionsMaxPerRoute() {
        return connectionsMaxPerRoute;
    }

    public void setConnectionsMaxPerRoute(String connectionsMaxPerRoute) {
        this.connectionsMaxPerRoute = connectionsMaxPerRoute;
    }

    public String getConnectionsMaxTotal() {
        return connectionsMaxTotal;
    }

    public void setConnectionsMaxTotal(String connectionsMaxTotal) {
        this.connectionsMaxTotal = connectionsMaxTotal;
    }

    public void setKeepAlive(String keepAlive) {
        this.keepAlive = keepAlive;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getResponseCharacterSet() {
        return responseCharacterSet;
    }

    public void setResponseCharacterSet(String responseCharacterSet) {
        this.responseCharacterSet = responseCharacterSet;
    }

    public String getDestinationFile() {
        return destinationFile;
    }

    public void setDestinationFile(String destinationFile) {
        this.destinationFile = destinationFile;
    }

    public String getFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(String followRedirects) {
        this.followRedirects = followRedirects;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String getEncodeQueryParams() {
        return encodeQueryParams;
    }

    public void setEncodeQueryParams(String encodeQueryParams) {
        this.encodeQueryParams = encodeQueryParams;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRequestCharacterSet() {
        return requestCharacterSet;
    }

    public void setRequestCharacterSet(String requestCharacterSet) {
        this.requestCharacterSet = requestCharacterSet;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public SerializableSessionObject getCookieStoreSessionObject() {
        return cookieStoreSessionObject;
    }

    public void setCookieStoreSessionObject(SerializableSessionObject cookieStoreSessionObject) {
        this.cookieStoreSessionObject = cookieStoreSessionObject;
    }

    public GlobalSessionObject getConnectionPoolSessionObject() {
        return connectionPoolSessionObject;
    }

    public void setConnectionPoolSessionObject(GlobalSessionObject connectionPoolSessionObject) {
        this.connectionPoolSessionObject = connectionPoolSessionObject;
    }
}
