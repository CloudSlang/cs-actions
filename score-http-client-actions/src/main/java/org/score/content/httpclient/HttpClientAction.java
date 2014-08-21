package org.score.content.httpclient;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/15/14
 */
public class HttpClientAction {

    public Map<String, String> execute(
            String url,
            String authType,
            String username,
            String password,
            String kerberosConfFile,
            String proxyHost,
            String proxyPort,
            String proxyUsername,
            String proxyPassword,
            String trustAllRoots,
            String trustKeystore,
            String trustPassword,
            String keystore,
            String keystorePassword,
            String connectTimeout,
            String socketTimeout,
            String useCookies,
            String keepAlive,
            String headers,
            String responseCharacterSet,
            String destinationFile,
            String followRedirects,
            String queryParams,
            String encodeQueryParams,
            String sourceFile,
            String body,
            String contentType,
            String requestCharacterSet,
            String method,
            SessionObjectHolder cookieStoreHolder,
            SessionObjectHolder connectionPoolHolder) {

        HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(url);
        httpClientInputs.setAuthType(authType);
        httpClientInputs.setUsername(username);
        httpClientInputs.setPassword(password);
        httpClientInputs.setKerberosConfFile(kerberosConfFile);
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
        httpClientInputs.setHeaders(headers);
        httpClientInputs.setResponseCharacterSet(responseCharacterSet);
        httpClientInputs.setDestinationFile(destinationFile);
        httpClientInputs.setFollowRedirects(followRedirects);
        httpClientInputs.setQueryParams(queryParams);
        httpClientInputs.setEncodeQueryParams(encodeQueryParams);
        httpClientInputs.setSourceFile(sourceFile);
        httpClientInputs.setBody(body);
        httpClientInputs.setContentType(contentType);
        httpClientInputs.setRequestCharacterSet(requestCharacterSet);
        httpClientInputs.setMethod(method);
        httpClientInputs.setCookieStoreHolder(cookieStoreHolder);
        httpClientInputs.setConnectionPoolHolder(connectionPoolHolder);

        return new HttpClient().execute(httpClientInputs);
    }
}
