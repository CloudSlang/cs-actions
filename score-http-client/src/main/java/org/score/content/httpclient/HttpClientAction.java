package org.score.content.httpclient;

import org.apache.commons.lang3.StringUtils;
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

        buildDefaultServices();

        URI uri = uriBuilder.setUrl(url)
                .setQueryParams(queryParams)
                .setEncodeQueryParams(encodeQueryParams)
                .buildURI();

        ContentType theContentType = contentTypeBuilder
                .setContentType(contentType)
                .setRequestCharacterSet(requestCharacterSet).buildContentType();

        HttpEntity httpEntity = httpEntityBuilder
                .setBody(body)
                .setFilePath(sourceFile)
                .setContentType(theContentType).buildEntity();

        RequestBuilder requestBuilder = RequestBuilder.create(method.toUpperCase());
        requestBuilder.setUri(uri);
        requestBuilder.setEntity(httpEntity);

        HttpRequestBase httpRequestBase = (HttpRequestBase) requestBuilder.build();

        Header[] theHeaders = headersBuilder
                .setHeaders(headers)
                .setContentType(theContentType)
                .buildHeaders();
        httpRequestBase.setHeaders(theHeaders);

        RequestConfig requestConfig = requestConfigBuilder
                .setConnectionTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .setFollowRedirects(followRedirects)
                .setProxyHost(proxyHost)
                .setProxyPort(proxyPort)
                .buildRequestConfig();
        httpRequestBase.setConfig(requestConfig);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        CredentialsProvider credentialsProvider = credentialsProviderBuilder
                .setAuthType(authType)
                .setUsername(username)
                .setPassword(password)
                .setHost(uri.getHost())
                .setPort(String.valueOf(uri.getPort()))
                .setProxyUsername(proxyUsername)
                .setProxyPassword(proxyPassword)
                .setProxyHost(proxyHost)
                .setProxyPort(proxyPort)
                .buildCredentialsProvider();
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

        httpClientBuilder.setDefaultAuthSchemeRegistry(authSchemeProviderLookupBuilder
                .setAuthType(authType)
                .buildAuthSchemeProviderLookup());

        CookieStore cookieStore = cookieStoreHolder == null ? null : cookieStoreBuilder
                .setUseCookies(useCookies)
                .setCookieStoreHolder(cookieStoreHolder)
                .buildCookieStore();
        if (cookieStore != null) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }

        SSLConnectionSocketFactory sslConnectionSocketFactory = sslConnectionSocketFactoryBuilder
                .setTrustAllRoots(trustAllRoots)
                .setKeystore(keystore)
                .setKeystorePassword(keystorePassword)
                .setTrustKeystore(trustKeystore)
                .setTrustPassword(trustPassword).build();

        PoolingHttpClientConnectionManager connManager = connectionPoolHolder == null ? null :
                poolingHttpClientConnectionManagerBuilder
                        .setConnectionManagerMapKey(
                                trustAllRoots,
                                keystore,
                                keystorePassword,
                                trustKeystore,
                                trustPassword)
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

        checkKeepAlive(httpRequestBase, connManager, keepAlive);

        Map<String, String> returnResult = new HashMap<>();

        try {
            httpResponseConsumer
                    .setHttpResponse(httpResponse)
                    .setResponseCharacterSet(responseCharacterSet)
                    .setDestinationFile(destinationFile)
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

    private void checkKeepAlive(HttpRequestBase httpRequestBase, PoolingHttpClientConnectionManager connManager, String keepAliveInput) {
        boolean keepAlive = StringUtils.isBlank(keepAliveInput) ? true : Boolean.parseBoolean(keepAliveInput);
        if (!keepAlive) {
            httpRequestBase.releaseConnection();
            connManager.closeExpiredConnections();
        }
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
