package org.score.content.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.score.content.httpclient.build.*;
import org.score.content.httpclient.consume.FinalLocationConsumer;
import org.score.content.httpclient.consume.HeadersConsumer;
import org.score.content.httpclient.consume.HttpResponseConsumer;
import org.score.content.httpclient.consume.StatusConsumer;
import org.score.content.httpclient.execute.HttpClientExecutor;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/15/14
 */
public class HttpClientAction {
    public static final String RETURN_CODE = "returnCode";
    public static final String SUCCESS = "0";
    //outputs
    public static final String RETURN_RESULT = "returnResult";
    public static final String EXCEPTION = "exception";
    public static final String STATUS_CODE = "statusCode";
    public static final String FINAL_LOCATION = "finalLocation";
    public static final String RESPONSE_HEADERS = "responseHeaders";
    public static final String PROTOCOL_VERSION = "protocolVersion";
    public static final String REASON_PHRASE = "reasonPhrase";
    private ProxyRouteBuilder proxyRouteBuilder = new ProxyRouteBuilder();
    private CookieStoreBuilder cookieStoreBuilder;
    private AuthSchemeProviderLookupBuilder authSchemeProviderLookupBuilder;
    private RequestConfigBuilder requestConfigBuilder;
    private HeadersBuilder headersBuilder;
    private ContentTypeBuilder contentTypeBuilder;
    private EntityBuilder httpEntityBuilder;
    private URIBuilder uriBuilder;
    private CredentialsProviderBuilder credentialsProviderBuilder;
    private SSLConnectionSocketFactoryBuilder sslConnectionSocketFactoryBuilder;
    private ConnectionManagerBuilder poolingHttpClientConnectionManagerBuilder;
    private HttpClientExecutor httpClientExecutor;
    private HttpResponseConsumer httpResponseConsumer;
    private FinalLocationConsumer finalLocationConsumer;
    private HeadersConsumer headersConsumer;
    private StatusConsumer statusConsumer;

    final public Map<String, String> execute(
            String url,
            String method,
            String followRedirects,
            String queryParams,
            String encodeQueryParams,
            String sourceFile,
            String requestCharacterSet,
            String body,
            String contentType,
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
            SessionObjectHolder cookieStoreHolder,
            SessionObjectHolder connectionPoolHolder) {

        Map<String, Object> actionRequest = new HashMap<>();
        actionRequest.put(HttpClientInputs.URL.getName(), url);
        actionRequest.put(HttpClientInputs.METHOD.getName(), method);
        actionRequest.put(HttpClientInputs.FOLLOW_REDIRECTS.getName(), followRedirects);
        actionRequest.put(HttpClientInputs.QUERY_PARAMS.getName(), queryParams);
        actionRequest.put(HttpClientInputs.ENCODE_QUERY_PARAMS.getName(), encodeQueryParams);
        actionRequest.put(HttpClientInputs.SOURCE_FILE.getName(), sourceFile);
        actionRequest.put(HttpClientInputs.REQUEST_CHARACTER_SET.getName(), requestCharacterSet);
        actionRequest.put(HttpClientInputs.BODY.getName(), body);
        actionRequest.put(HttpClientInputs.CONTENT_TYPE.getName(), contentType);
        actionRequest.put(HttpClientInputs.AUTH_TYPE.getName(), authType);
        actionRequest.put(HttpClientInputs.USERNAME.getName(), username);
        actionRequest.put(HttpClientInputs.PASSWORD.getName(), password);
        actionRequest.put(HttpClientInputs.KERBEROS_CONFIG_FILE.getName(), kerberosConfFile);
        actionRequest.put(HttpClientInputs.PROXY_HOST.getName(), proxyHost);
        actionRequest.put(HttpClientInputs.PROXY_PORT.getName(), proxyPort);
        actionRequest.put(HttpClientInputs.PROXY_USERNAME.getName(), proxyUsername);
        actionRequest.put(HttpClientInputs.PROXY_PASSWORD.getName(), proxyPassword);
        actionRequest.put(HttpClientInputs.TRUST_ALL_ROOTS.getName(), trustAllRoots);
        actionRequest.put(HttpClientInputs.TRUST_KEYSTORE.getName(), trustKeystore);
        actionRequest.put(HttpClientInputs.TRUST_PASSWORD.getName(), trustPassword);
        actionRequest.put(HttpClientInputs.KEYSTORE.getName(), keystore);
        actionRequest.put(HttpClientInputs.KEYSTORE_PASSWORD.getName(), keystorePassword);
        actionRequest.put(HttpClientInputs.CONNECTION_TIMEOUT.getName(), connectTimeout);
        actionRequest.put(HttpClientInputs.SOCKET_TIMEOUT.getName(), socketTimeout);
        actionRequest.put(HttpClientInputs.USE_COOKIES.getName(), useCookies);
        actionRequest.put(HttpClientInputs.KEEP_ALIVE.getName(), keepAlive);
        actionRequest.put(HttpClientInputs.HEADERS.getName(), headers);
        actionRequest.put(HttpClientInputs.DESTINATION_FILE.getName(), destinationFile);
        actionRequest.put(HttpClientInputs.RESPONSE_CHARACTER_SET.getName(), responseCharacterSet);

        return execute(actionRequest, cookieStoreHolder, connectionPoolHolder);
    }

    final public Map<String, String> execute(Map<String, Object> actionRequest,
                                             SessionObjectHolder cookieStoreHolder,
                                             SessionObjectHolder connectionPoolHolder) {
        buildDefaultServices();

        Map<HttpClientInputs, String> httpClientInputs = new HashMap<>();
        for (HttpClientInputs ooHttpClientInputs : HttpClientInputs.values()) {
            httpClientInputs.put(ooHttpClientInputs, (String) actionRequest.get(ooHttpClientInputs.getName()));
        }

        URI uri = uriBuilder.setUrl(httpClientInputs.get(HttpClientInputs.URL))
                .setQueryParams(httpClientInputs.get(HttpClientInputs.QUERY_PARAMS))
                .setEncodeQueryParams(httpClientInputs.get(HttpClientInputs.ENCODE_QUERY_PARAMS))
                .buildURI();

        ContentType contentType = contentTypeBuilder
                .setContentType(httpClientInputs.get(HttpClientInputs.CONTENT_TYPE))
                .setRequestCharacterSet(httpClientInputs.get(HttpClientInputs.REQUEST_CHARACTER_SET)).buildContentType();

        HttpEntity httpEntity = httpEntityBuilder
                .setBody(httpClientInputs.get(HttpClientInputs.BODY))
                .setFilePath(httpClientInputs.get(HttpClientInputs.SOURCE_FILE))
                .setContentType(contentType).buildEntity();

        RequestBuilder requestBuilder = RequestBuilder.create(httpClientInputs.get(HttpClientInputs.METHOD).toUpperCase());
        requestBuilder.setUri(uri);
        requestBuilder.setEntity(httpEntity);

        HttpRequestBase httpRequestBase = (HttpRequestBase) requestBuilder.build();

        Header[] headers = headersBuilder
                .setHeaders(httpClientInputs.get(HttpClientInputs.HEADERS))
                .setContentType(contentType)
                .buildHeaders();
        httpRequestBase.setHeaders(headers);

        RequestConfig requestConfig = requestConfigBuilder
                .setConnectionTimeout(httpClientInputs.get(HttpClientInputs.CONNECTION_TIMEOUT))
                .setSocketTimeout(httpClientInputs.get(HttpClientInputs.SOCKET_TIMEOUT))
                .setFollowRedirects(httpClientInputs.get(HttpClientInputs.FOLLOW_REDIRECTS))
                .setProxyHost(httpClientInputs.get(HttpClientInputs.PROXY_HOST))
                .setProxyPort(httpClientInputs.get(HttpClientInputs.PROXY_PORT))
                .buildRequestConfig();
        httpRequestBase.setConfig(requestConfig);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        CredentialsProvider credentialsProvider = credentialsProviderBuilder
                .setAuthType(httpClientInputs.get(HttpClientInputs.AUTH_TYPE))
                .setUsername(httpClientInputs.get(HttpClientInputs.USERNAME))
                .setPassword(httpClientInputs.get(HttpClientInputs.PASSWORD))
                .setHost(uri.getHost())
                .setPort(String.valueOf(uri.getPort()))
                .setProxyUsername(httpClientInputs.get(HttpClientInputs.PROXY_USERNAME))
                .setProxyPassword(httpClientInputs.get(HttpClientInputs.PROXY_PASSWORD))
                .setProxyHost(httpClientInputs.get(HttpClientInputs.PROXY_HOST))
                .setProxyPort(httpClientInputs.get(HttpClientInputs.PROXY_PORT))
                .buildCredentialsProvider();
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

        httpClientBuilder.setDefaultAuthSchemeRegistry(authSchemeProviderLookupBuilder
                .setAuthType(httpClientInputs.get(HttpClientInputs.AUTH_TYPE))
                .buildAuthSchemeProviderLookup());

        CookieStore cookieStore = cookieStoreHolder == null ? null : cookieStoreBuilder
                .setUseCookies(httpClientInputs.get(HttpClientInputs.USE_COOKIES))
                .setCookieStoreHolder(cookieStoreHolder)
                .buildCookieStore();
        if (cookieStore != null) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }

        SSLConnectionSocketFactory sslConnectionSocketFactory = sslConnectionSocketFactoryBuilder
                .setTrustAllRoots(httpClientInputs.get(HttpClientInputs.TRUST_ALL_ROOTS))
                .setKeystore(httpClientInputs.get(HttpClientInputs.KEYSTORE))
                .setKeystorePassword(httpClientInputs.get(HttpClientInputs.KEYSTORE_PASSWORD))
                .setTrustKeystore(httpClientInputs.get(HttpClientInputs.TRUST_KEYSTORE))
                .setTrustPassword(httpClientInputs.get(HttpClientInputs.TRUST_PASSWORD)).build();

        PoolingHttpClientConnectionManager connManager = connectionPoolHolder == null ? null :
                poolingHttpClientConnectionManagerBuilder
                        .setConnectionManagerMapKey(
                                httpClientInputs.get(HttpClientInputs.TRUST_ALL_ROOTS),
                                httpClientInputs.get(HttpClientInputs.KEYSTORE),
                                httpClientInputs.get(HttpClientInputs.KEYSTORE_PASSWORD),
                                httpClientInputs.get(HttpClientInputs.TRUST_KEYSTORE),
                                httpClientInputs.get(HttpClientInputs.TRUST_PASSWORD))
                        .setConnectionPoolHolder(connectionPoolHolder)
                        .setSslsf(sslConnectionSocketFactory)
                        .buildConnectionManager();

        httpClientBuilder.setConnectionManager(connManager);

        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        //todo reuse context from session ?
        HttpClientContext context = HttpClientContext.create();

        CloseableHttpResponse httpResponse = httpClientExecutor
                .setCloseableHttpClient(closeableHttpClient)
                .setHttpRequestBase(httpRequestBase)
                .setContext(context).execute();

        Map<String, String> returnResult = new HashMap<>();

        try {
            httpResponseConsumer
                    .setHttpResponse(httpResponse)
                    .setResponseCharacterSet(httpClientInputs.get(HttpClientInputs.RESPONSE_CHARACTER_SET))
                    .setDestinationFile(httpClientInputs.get(HttpClientInputs.DESTINATION_FILE))
                    .consume(returnResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        finalLocationConsumer
                .setUri(uri)
                .setRedirectLocations(context.getRedirectLocations())
                .setTargetHost(context.getTargetHost()).consume(returnResult);

        headersConsumer.setHeaders(httpResponse.getAllHeaders()).consume(returnResult);

        statusConsumer.setStatusLine(httpResponse.getStatusLine()).consume(returnResult);

        returnResult.put(RETURN_CODE, SUCCESS);
        return returnResult;
    }

    public void setProxyRouteBuilder(ProxyRouteBuilder proxyRouteBuilder) {
        this.proxyRouteBuilder = proxyRouteBuilder;
    }

    public void setCookieStoreBuilder(CookieStoreBuilder cookieStoreBuilder) {
        this.cookieStoreBuilder = cookieStoreBuilder;
    }

    public void setAuthSchemeProviderLookupBuilder(AuthSchemeProviderLookupBuilder authSchemeProviderLookupBuilder) {
        this.authSchemeProviderLookupBuilder = authSchemeProviderLookupBuilder;
    }

    public void setRequestConfigBuilder(RequestConfigBuilder requestConfigBuilder) {
        this.requestConfigBuilder = requestConfigBuilder;
    }

    public void setHeadersBuilder(HeadersBuilder headersBuilder) {
        this.headersBuilder = headersBuilder;
    }

    public void setHttpEntityBuilder(EntityBuilder httpEntityBuilder) {
        this.httpEntityBuilder = httpEntityBuilder;
    }

    public void setUriBuilder(URIBuilder uriBuilder) {
        this.uriBuilder = uriBuilder;
    }

    public void setCredentialsProviderBuilder(CredentialsProviderBuilder credentialsProviderBuilder) {
        this.credentialsProviderBuilder = credentialsProviderBuilder;
    }

    public void setSslConnectionSocketFactoryBuilder(SSLConnectionSocketFactoryBuilder sslConnectionSocketFactoryBuilder) {
        this.sslConnectionSocketFactoryBuilder = sslConnectionSocketFactoryBuilder;
    }

    public void setPoolingHttpClientConnectionManagerBuilder(ConnectionManagerBuilder poolingHttpClientConnectionManagerBuilder) {
        this.poolingHttpClientConnectionManagerBuilder = poolingHttpClientConnectionManagerBuilder;
    }

    private void buildDefaultServices() {
        if (uriBuilder == null) {
            uriBuilder = new URIBuilder();
        }
        if (contentTypeBuilder == null) {
            contentTypeBuilder = new ContentTypeBuilder();
        }
        if (httpEntityBuilder == null) {
            httpEntityBuilder = new EntityBuilder();
        }
        if (headersBuilder == null) {
            headersBuilder = new HeadersBuilder();
        }
        if (requestConfigBuilder == null) {
            requestConfigBuilder = new RequestConfigBuilder();
        }
        if (credentialsProviderBuilder == null) {
            credentialsProviderBuilder = new CredentialsProviderBuilder();
        }
        if (authSchemeProviderLookupBuilder == null) {
            authSchemeProviderLookupBuilder = new AuthSchemeProviderLookupBuilder();
        }
        if (cookieStoreBuilder == null) {
            cookieStoreBuilder = new CookieStoreBuilder();
        }
        if (proxyRouteBuilder == null) {
            proxyRouteBuilder = new ProxyRouteBuilder();
        }
        if (sslConnectionSocketFactoryBuilder == null) {
            sslConnectionSocketFactoryBuilder = new SSLConnectionSocketFactoryBuilder();
        }
        if (poolingHttpClientConnectionManagerBuilder == null) {
            poolingHttpClientConnectionManagerBuilder = new ConnectionManagerBuilder();
        }
        if (httpClientExecutor == null) {
            httpClientExecutor = new HttpClientExecutor();
        }
        if (httpResponseConsumer == null) {
            httpResponseConsumer = new HttpResponseConsumer();
        }
        if (finalLocationConsumer == null) {
            finalLocationConsumer = new FinalLocationConsumer();
        }
        if (headersConsumer == null) {
            headersConsumer = new HeadersConsumer();
        }
        if (statusConsumer == null) {
            statusConsumer = new StatusConsumer();
        }
    }
}
