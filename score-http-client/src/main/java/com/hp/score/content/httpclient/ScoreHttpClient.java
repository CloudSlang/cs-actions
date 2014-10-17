package com.hp.score.content.httpclient;

import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import com.hp.score.content.httpclient.build.*;
import com.hp.score.content.httpclient.build.RequestBuilder;
import com.hp.score.content.httpclient.build.auth.AuthSchemeProviderLookupBuilder;
import com.hp.score.content.httpclient.build.auth.AuthTypes;
import com.hp.score.content.httpclient.build.auth.CredentialsProviderBuilder;
import com.hp.score.content.httpclient.build.conn.ConnectionManagerBuilder;
import com.hp.score.content.httpclient.build.conn.SSLConnectionSocketFactoryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import com.hp.score.content.httpclient.consume.FinalLocationConsumer;
import com.hp.score.content.httpclient.consume.HeadersConsumer;
import com.hp.score.content.httpclient.consume.HttpResponseConsumer;
import com.hp.score.content.httpclient.consume.StatusConsumer;
import com.hp.score.content.httpclient.execute.HttpClientExecutor;

import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/15/14
 */
public class ScoreHttpClient {
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
    private RequestBuilder requestBuilder;
    private URIBuilder uriBuilder;
    private CredentialsProviderBuilder credentialsProviderBuilder;
    private SSLConnectionSocketFactoryBuilder sslConnectionSocketFactoryBuilder;
    private ConnectionManagerBuilder poolingHttpClientConnectionManagerBuilder;
    private ContextBuilder contextBuilder;
    private HttpClientExecutor httpClientExecutor;
    private HttpResponseConsumer httpResponseConsumer;
    private FinalLocationConsumer finalLocationConsumer;
    private HeadersConsumer headersConsumer;
    private StatusConsumer statusConsumer;

    public Map<String, String> execute(HttpClientInputs httpClientInputs) {

        HttpComponents httpComponents = buildHttpComponents(httpClientInputs);

        CloseableHttpResponse httpResponse = execute(httpComponents.getCloseableHttpClient(),
                httpComponents.getHttpRequestBase(),
                httpComponents.getHttpClientContext());

        Map<String, String> result = parseResponse(httpResponse,
                httpClientInputs.getResponseCharacterSet(),
                httpClientInputs.getDestinationFile(),
                httpComponents.getUri(),
                httpComponents.getHttpClientContext(),
                httpComponents.getCookieStore(),
                httpClientInputs.getCookieStoreSessionObject());

        checkKeepAlive(httpComponents.getHttpRequestBase(),
                httpComponents.getConnManager(),
                httpClientInputs.getKeepAlive(),
                httpResponse);

        return result;
    }

    public HttpComponents buildHttpComponents(HttpClientInputs httpClientInputs) {
        buildDefaultServices();

        URI uri = uriBuilder.setUrl(httpClientInputs.getUrl())
                .setQueryParams(httpClientInputs.getQueryParams())
                .setEncodeQueryParams(httpClientInputs.getEncodeQueryParams())
                .buildURI();


        ContentType theContentType = contentTypeBuilder
                .setContentType(httpClientInputs.getContentType())
                .setRequestCharacterSet(httpClientInputs.getRequestCharacterSet())
                .buildContentType();

        HttpEntity httpEntity = httpEntityBuilder
                .setFormParams(httpClientInputs.getFormParams())
                .setEncodeFormParams(httpClientInputs.getEncodeFormParams())
                .setBody(httpClientInputs.getBody())
                .setFilePath(httpClientInputs.getSourceFile())
                .setContentType(theContentType)
                .setMultipartValuesAreURLEncoded(httpClientInputs.getMultipartValuesAreURLEncoded())
                .setMultipartBodies(httpClientInputs.getMultipartBodies())
                .setMultipartFiles(httpClientInputs.getMultipartFiles())
                .setMultipartBodiesContentType(httpClientInputs.getMultipartBodiesContentType())
                .setMultipartFilesContentType(httpClientInputs.getMultipartFilesContentType())
                .buildEntity();

        HttpRequestBase httpRequestBase =  requestBuilder
                .setMethod(httpClientInputs.getMethod())
                .setUri(uri)
                .setEntity(httpEntity).build();

        Header[] theHeaders = headersBuilder
                .setHeaders(httpClientInputs.getHeaders())
                .setContentType(theContentType)
                .setEntityContentType(httpEntity!=null ? httpEntity.getContentType() : null)
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

        AuthTypes authTypes = new AuthTypes(httpClientInputs.getAuthType());

        CredentialsProvider credentialsProvider = credentialsProviderBuilder
                .setAuthTypes(authTypes)
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


        Lookup<AuthSchemeProvider> authSchemeLookup = authSchemeProviderLookupBuilder
                .setAuthTypes(authTypes)
                .setHost(uri.getHost())
                .setSkipPortAtKerberosDatabaseLookup(httpClientInputs.getKerberosSkipPortCheck())
                .setKerberosConfigFile(httpClientInputs.getKerberosConfFile())
                .setKerberosLoginConfigFile(httpClientInputs.getKerberosLoginConfFile())
                .setUsername(httpClientInputs.getUsername())
                .setPassword(httpClientInputs.getPassword())
                .buildAuthSchemeProviderLookup();
        httpClientBuilder.setDefaultAuthSchemeRegistry(authSchemeLookup);

        CookieStore cookieStore = httpClientInputs.getCookieStoreSessionObject() == null ? null : cookieStoreBuilder
                .setUseCookies(httpClientInputs.getUseCookies())
                .setCookieStoreSessionObject(httpClientInputs.getCookieStoreSessionObject())
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

        PoolingHttpClientConnectionManager connManager = poolingHttpClientConnectionManagerBuilder
                .setConnectionManagerMapKey(
                        httpClientInputs.getTrustAllRoots(),
                        httpClientInputs.getKeystore(),
                        httpClientInputs.getTrustKeystore())
                .setConnectionPoolHolder(httpClientInputs.getConnectionPoolSessionObject())
                .setSslsf(sslConnectionSocketFactory)
                .setDefaultMaxPerRoute(httpClientInputs.getConnectionsMaxPerRoute())
                .setTotalMax(httpClientInputs.getConnectionsMaxTotal())
                .buildConnectionManager();

        httpClientBuilder.setConnectionManager(connManager);

        if (StringUtils.isEmpty(httpClientInputs.getKeepAlive()) || Boolean.parseBoolean(httpClientInputs.getKeepAlive()) ) {
            httpClientBuilder.setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE);
        } else {
            httpClientBuilder.setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE);
        }

        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

        HttpClientContext context = contextBuilder
                .setAuthSchemeLookup(authSchemeLookup)
                .setAuthTypes(authTypes)
                .setCredentialsProvider(credentialsProvider)
                .setUri(uri)
                .setPreemptiveAuth(httpClientInputs.getPreemptiveAuth()).build();

        HttpComponents result = new HttpComponents();
        result.setCloseableHttpClient(closeableHttpClient);
        result.setHttpRequestBase(httpRequestBase);
        result.setHttpClientContext(context);
        result.setUri(uri);
        result.setConnManager(connManager);
        result.setCookieStore(cookieStore);
        return result;
    }


    public CloseableHttpResponse execute(CloseableHttpClient closeableHttpClient,
                                         HttpRequestBase httpRequestBase,
                                         HttpClientContext context) {
        CloseableHttpResponse httpResponse = httpClientExecutor
                .setCloseableHttpClient(closeableHttpClient)
                .setHttpRequestBase(httpRequestBase)
                .setContext(context)
                .execute();

        return httpResponse;
    }

    public Map<String, String> parseResponse(CloseableHttpResponse httpResponse,
                                             String responseCharacterSet,
                                             String destinationFile,
                                             URI uri,
                                             HttpClientContext httpClientContext,
                                             CookieStore cookieStore,
                                             SerializableSessionObject cookieStoreSessionObject
                                             ) {
        Map<String, String> result = new HashMap<>();

        try {
            httpResponseConsumer
                    .setHttpResponse(httpResponse)
                    .setResponseCharacterSet(responseCharacterSet)
                    .setDestinationFile(destinationFile)
                    .consume(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        finalLocationConsumer
                .setUri(uri)
                .setRedirectLocations(httpClientContext.getRedirectLocations())
                .setTargetHost(httpClientContext.getTargetHost()).consume(result);

        headersConsumer.setHeaders(httpResponse.getAllHeaders()).consume(result);
        statusConsumer.setStatusLine(httpResponse.getStatusLine()).consume(result);

        if (cookieStore != null) {
            try {
                cookieStoreSessionObject.setValue(
                        CookieStoreBuilder.serialize(cookieStore));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        result.put(RETURN_CODE, SUCCESS);
        return result;
    }

    private void checkKeepAlive(HttpRequestBase httpRequestBase, PoolingHttpClientConnectionManager connManager,
                                String keepAliveInput, CloseableHttpResponse httpResponse) {
        boolean keepAlive = StringUtils.isBlank(keepAliveInput) ? true : Boolean.parseBoolean(keepAliveInput);
        if (!keepAlive) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
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
        if (requestBuilder == null) {
            requestBuilder = new RequestBuilder();
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
        if (contextBuilder == null) {
            contextBuilder = new ContextBuilder();
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
