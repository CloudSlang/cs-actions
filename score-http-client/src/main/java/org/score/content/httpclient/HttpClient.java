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
public class HttpClient {
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

    public Map<String, String> execute(HttpClientInputs httpClientInputs) {

        buildDefaultServices();

        URI uri = uriBuilder.setUrl(httpClientInputs.getUrl())
                .setQueryParams(httpClientInputs.getQueryParams())
                .setEncodeQueryParams(httpClientInputs.getEncodeQueryParams())
                .buildURI();

        ContentType theContentType = contentTypeBuilder
                .setContentType(httpClientInputs.getContentType())
                .setRequestCharacterSet(httpClientInputs.getRequestCharacterSet()).buildContentType();

        HttpEntity httpEntity = httpEntityBuilder
                .setBody(httpClientInputs.getBody())
                .setFilePath(httpClientInputs.getSourceFile())
                .setContentType(theContentType).buildEntity();

        RequestBuilder requestBuilder = RequestBuilder.create(httpClientInputs.getMethod().toUpperCase());
        requestBuilder.setUri(uri);
        requestBuilder.setEntity(httpEntity);

        HttpRequestBase httpRequestBase = (HttpRequestBase) requestBuilder.build();

        Header[] theHeaders = headersBuilder
                .setHeaders(httpClientInputs.getHeaders())
                .setContentType(theContentType)
                .buildHeaders();
        httpRequestBase.setHeaders(theHeaders);

        RequestConfig requestConfig = requestConfigBuilder
                .setConnectionTimeout(httpClientInputs.getConnectTimeout())
                .setSocketTimeout(httpClientInputs.getSocketTimeout())
                .setFollowRedirects(httpClientInputs.getFollowRedirects())
                .setProxyHost(httpClientInputs.getProxyHost())
                .setProxyPort(httpClientInputs.getProxyPort())
                .buildRequestConfig();
        httpRequestBase.setConfig(requestConfig);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        CredentialsProvider credentialsProvider = credentialsProviderBuilder
                .setAuthType(httpClientInputs.getAuthType())
                .setUsername(httpClientInputs.getUsername())
                .setPassword(httpClientInputs.getPassword())
                .setHost(uri.getHost())
                .setPort(String.valueOf(uri.getPort()))
                .setProxyUsername(httpClientInputs.getProxyUsername())
                .setProxyPassword(httpClientInputs.getProxyPassword())
                .setProxyHost(httpClientInputs.getProxyHost())
                .setProxyPort(httpClientInputs.getProxyPort())
                .buildCredentialsProvider();
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

        httpClientBuilder.setDefaultAuthSchemeRegistry(authSchemeProviderLookupBuilder
                .setAuthType(httpClientInputs.getAuthType())
                .buildAuthSchemeProviderLookup());

        CookieStore cookieStore = httpClientInputs.getCookieStoreHolder() == null ? null : cookieStoreBuilder
                .setUseCookies(httpClientInputs.getUseCookies())
                .setCookieStoreHolder(httpClientInputs.getCookieStoreHolder())
                .buildCookieStore();
        if (cookieStore != null) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }

        SSLConnectionSocketFactory sslConnectionSocketFactory = sslConnectionSocketFactoryBuilder
                .setTrustAllRoots(httpClientInputs.getTrustAllRoots())
                .setKeystore(httpClientInputs.getKeystore())
                .setKeystorePassword(httpClientInputs.getKeystorePassword())
                .setTrustKeystore(httpClientInputs.getTrustKeystore())
                .setTrustPassword(httpClientInputs.getTrustPassword()).build();

        PoolingHttpClientConnectionManager connManager = httpClientInputs.getConnectionPoolHolder() == null ? null :
                poolingHttpClientConnectionManagerBuilder
                        .setConnectionManagerMapKey(
                                httpClientInputs.getTrustAllRoots(),
                                httpClientInputs.getKeystore(),
                                httpClientInputs.getTrustKeystore())
                        .setConnectionPoolHolder(httpClientInputs.getConnectionPoolHolder())
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

        checkKeepAlive(httpRequestBase, connManager, httpClientInputs.getKeepAlive());

        Map<String, String> returnResult = new HashMap<>();

        try {
            httpResponseConsumer
                    .setHttpResponse(httpResponse)
                    .setResponseCharacterSet(httpClientInputs.getResponseCharacterSet())
                    .setDestinationFile(httpClientInputs.getDestinationFile())
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
