package com.hp.score.content.httpclient;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/15/14
 */
public class HttpClientAction {

    @Action(name = "Http Client",
            outputs = {
                    @Output(ScoreHttpClient.EXCEPTION),
                    @Output(ScoreHttpClient.STATUS_CODE),
                    @Output(ScoreHttpClient.FINAL_LOCATION),
                    @Output(ScoreHttpClient.RESPONSE_HEADERS),
                    @Output(ScoreHttpClient.PROTOCOL_VERSION),
                    @Output(ScoreHttpClient.REASON_PHRASE),
                    @Output("returnCode"),
                    @Output("returnResult")
            },
            responses = {
                    @Response(text = "success", field = "returnCode", value = "0", matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = "failure", field = "returnCode", value = "-1", matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(
            @Param(value = HttpClientInputs.URL, required = true) String url,
            @Param(HttpClientInputs.AUTH_TYPE) String authType,
            @Param(HttpClientInputs.USERNAME) String username,
            @Param(HttpClientInputs.PASSWORD) String password,
            @Param(HttpClientInputs.KERBEROS_CONFIG_FILE) String kerberosConfFile,
            @Param(HttpClientInputs.KERBEROS_LOGIN_CONFIG_FILE) String kerberosLoginConfFile,
            @Param(HttpClientInputs.KERBEROS_SKIP_PORT_CHECK) String kerberosSkipPortForLookup,
            @Param(HttpClientInputs.PROXY_HOST) String proxyHost,
            @Param(HttpClientInputs.PROXY_PORT) String proxyPort,
            @Param(HttpClientInputs.PROXY_USERNAME) String proxyUsername,
            @Param(HttpClientInputs.PROXY_PASSWORD) String proxyPassword,
            @Param(HttpClientInputs.TRUST_ALL_ROOTS) String trustAllRoots,
            @Param(HttpClientInputs.TRUST_KEYSTORE) String trustKeystore,
            @Param(HttpClientInputs.TRUST_PASSWORD) String trustPassword,
            @Param(HttpClientInputs.KEYSTORE) String keystore,
            @Param(HttpClientInputs.KEYSTORE_PASSWORD) String keystorePassword,
            @Param(HttpClientInputs.CONNECT_TIMEOUT) String connectTimeout,
            @Param(HttpClientInputs.SOCKET_TIMEOUT) String socketTimeout,
            @Param(HttpClientInputs.USE_COOKIES) String useCookies,
            @Param(HttpClientInputs.KEEP_ALIVE) String keepAlive,
            @Param(HttpClientInputs.CONNECTIONS_MAX_PER_ROUTE) String connectionsMaxPerRoot,
            @Param(HttpClientInputs.CONNECTIONS_MAX_TOTAL) String connectionsMaxTotal,
            @Param(HttpClientInputs.HEADERS) String headers,
            @Param(HttpClientInputs.RESPONSE_CHARACTER_SET) String responseCharacterSet,
            @Param(HttpClientInputs.DESTINATION_FILE) String destinationFile,
            @Param(HttpClientInputs.FOLLOW_REDIRECTS) String followRedirects,
            @Param(HttpClientInputs.QUERY_PARAMS) String queryParams,
            @Param(HttpClientInputs.ENCODE_QUERY_PARAMS) String encodeQueryParams,
            @Param(HttpClientInputs.FORM_PARAMS) String formParams,
            @Param(HttpClientInputs.ENCODE_FORM_PARAMS) String encodeFormParams,
            @Param(HttpClientInputs.SOURCE_FILE) String sourceFile,
            @Param(HttpClientInputs.BODY) String body,
            @Param(HttpClientInputs.CONTENT_TYPE) String contentType,
            @Param(HttpClientInputs.REQUEST_CHARACTER_SET) String requestCharacterSet,
            @Param(value = HttpClientInputs.METHOD, required = true) String method,
            @Param(HttpClientInputs.SESSION_COOKIES) SerializableSessionObject httpClientCookieSession,
            @Param(HttpClientInputs.SESSION_CONNECTION_POOL) GlobalSessionObject httpClientPoolingConnectionManager) {

        HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(url);
        httpClientInputs.setAuthType(authType);
        httpClientInputs.setUsername(username);
        httpClientInputs.setPassword(password);
        httpClientInputs.setKerberosConfFile(kerberosConfFile);
        httpClientInputs.setKerberosLoginConfFile(kerberosLoginConfFile);
//        httpClientInputs.setKerberosDomain(kerberosDomain);
//        httpClientInputs.setKerberosKdc(kerberosKdc);
        httpClientInputs.setKerberosSkipPortCheck(kerberosSkipPortForLookup);
        httpClientInputs.setProxyHost(proxyHost);
        httpClientInputs.setProxyPort(proxyPort);
        httpClientInputs.setProxyUsername(proxyUsername);
        httpClientInputs.setProxyPassword(proxyPassword);
        httpClientInputs.setTrustAllRoots(trustAllRoots);
        httpClientInputs.setTrustKeystore(trustKeystore);
        httpClientInputs.setTrustPassword(trustPassword);
        httpClientInputs.setKeystore(keystore);
        httpClientInputs.setKeystorePassword(keystorePassword);
        httpClientInputs.setConnectTimeout(connectTimeout);
        httpClientInputs.setSocketTimeout(socketTimeout);
        httpClientInputs.setUseCookies(useCookies);
        httpClientInputs.setKeepAlive(keepAlive);
        httpClientInputs.setConnectionsMaxPerRoute(connectionsMaxPerRoot);
        httpClientInputs.setConnectionsMaxTotal(connectionsMaxTotal);
        httpClientInputs.setHeaders(headers);
        httpClientInputs.setResponseCharacterSet(responseCharacterSet);
        httpClientInputs.setDestinationFile(destinationFile);
        httpClientInputs.setFollowRedirects(followRedirects);
        httpClientInputs.setQueryParams(queryParams);
        httpClientInputs.setEncodeQueryParams(encodeQueryParams);
        httpClientInputs.setFormParams(formParams);
        httpClientInputs.setEncodeFormParams(encodeFormParams);
        httpClientInputs.setSourceFile(sourceFile);
        httpClientInputs.setBody(body);
        httpClientInputs.setContentType(contentType);
        httpClientInputs.setRequestCharacterSet(requestCharacterSet);
        httpClientInputs.setMethod(method);
        httpClientInputs.setCookieStoreSessionObject(httpClientCookieSession);
        httpClientInputs.setConnectionPoolSessionObject(httpClientPoolingConnectionManager);

        try {
            return new ScoreHttpClient().execute(httpClientInputs);
        } catch (Exception e) {
            return exceptionResult(e.getMessage(), e);
        }
    }

    private Map<String, String> exceptionResult(String message, Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String eStr = writer.toString().replace("" + (char) 0x00, "");

        Map<String, String> returnResult = new HashMap<>();
        returnResult.put("returnResult", message);
        returnResult.put("returnCode", "-1");
        returnResult.put("exception", eStr);
        return returnResult;
    }
}
