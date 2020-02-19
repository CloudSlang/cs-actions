/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package io.cloudslang.content.httpclient.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.components.HttpComponents;
import io.cloudslang.content.httpclient.build.*;
import io.cloudslang.content.httpclient.build.auth.AuthSchemeProviderLookupBuilder;
import io.cloudslang.content.httpclient.build.auth.AuthTypes;
import io.cloudslang.content.httpclient.build.auth.CredentialsProviderBuilder;
import io.cloudslang.content.httpclient.build.conn.ConnectionManagerBuilder;
import io.cloudslang.content.httpclient.build.conn.SSLConnectionSocketFactoryBuilder;
import io.cloudslang.content.httpclient.consume.FinalLocationConsumer;
import io.cloudslang.content.httpclient.consume.HeadersConsumer;
import io.cloudslang.content.httpclient.consume.HttpResponseConsumer;
import io.cloudslang.content.httpclient.consume.StatusConsumer;
import io.cloudslang.content.httpclient.execute.HttpClientExecutor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/15/14
 */
public class HttpClientService {
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
        initSessionsObjects(httpClientInputs);
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

    private void initSessionsObjects(HttpClientInputs httpClientInputs) {
        SerializableSessionObject cookieStoreSessionObject = httpClientInputs.getCookieStoreSessionObject();
        if (cookieStoreSessionObject == null) {
            httpClientInputs.setCookieStoreSessionObject(new SerializableSessionObject());
        }

        GlobalSessionObject globalSessionObject = httpClientInputs.getConnectionPoolSessionObject();
        if (globalSessionObject == null) {
            httpClientInputs.setConnectionPoolSessionObject(new GlobalSessionObject());
        }
    }

    public HttpComponents buildHttpComponents(HttpClientInputs httpClientInputs) {
        buildDefaultServices();

        URI uri = uriBuilder.setUrl(httpClientInputs.getUrl())
                .setQueryParams(httpClientInputs.getQueryParams())
                .setQueryParamsAreURLEncoded(httpClientInputs.getQueryParamsAreURLEncoded())
                .setQueryParamsAreFormEncoded(httpClientInputs.getQueryParamsAreFormEncoded())
                .buildURI();


        ContentType theContentType = contentTypeBuilder
                .setContentType(httpClientInputs.getContentType())
                .setRequestCharacterSet(httpClientInputs.getRequestCharacterSet())
                .buildContentType();

        HttpEntity httpEntity = httpEntityBuilder
                .setFormParams(httpClientInputs.getFormParams())
                .setFormParamsAreURLEncoded(httpClientInputs.getFormParamsAreURLEncoded())
                .setBody(httpClientInputs.getBody())
                .setFilePath(httpClientInputs.getSourceFile())
                .setContentType(theContentType)
                .setMultipartValuesAreURLEncoded(httpClientInputs.getMultipartValuesAreURLEncoded())
                .setMultipartBodies(httpClientInputs.getMultipartBodies())
                .setMultipartFiles(httpClientInputs.getMultipartFiles())
                .setMultipartBodiesContentType(httpClientInputs.getMultipartBodiesContentType())
                .setMultipartFilesContentType(httpClientInputs.getMultipartFilesContentType())
                .setChunkedRequestEntity(httpClientInputs.getChunkedRequestEntity())
                .buildEntity();

        HttpRequestBase httpRequestBase = requestBuilder
                .setMethod(httpClientInputs.getMethod())
                .setUri(uri)
                .setEntity(httpEntity).build();

        List<Header> theHeaders = headersBuilder
                .setHeaders(httpClientInputs.getHeaders())
                .setContentType(theContentType)
                .setEntityContentType(httpEntity != null ? httpEntity.getContentType() : null)
                .buildHeaders();

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
                .setHeaders(theHeaders)
                .setSkipPortAtKerberosDatabaseLookup(httpClientInputs.getKerberosSkipPortCheck())
                .setKerberosConfigFile(httpClientInputs.getKerberosConfFile())
                .setKerberosLoginConfigFile(httpClientInputs.getKerberosLoginConfFile())
                .setUsername(httpClientInputs.getUsername())
                .setPassword(httpClientInputs.getPassword())
                .buildAuthSchemeProviderLookup();
        httpClientBuilder.setDefaultAuthSchemeRegistry(authSchemeLookup);

        httpRequestBase.setHeaders(theHeaders.toArray(new Header[theHeaders.size()]));

        CookieStore cookieStore = cookieStoreBuilder
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
                .setTrustPassword(httpClientInputs.getTrustPassword())
                .setX509HostnameVerifier(httpClientInputs.getX509HostnameVerifier())
                .setInputTLS(httpClientInputs.getTlsVersion())
                .setallowedCyphers(httpClientInputs.getAllowedCyphers())
                .build();

        String connectionKey = ConnectionManagerBuilder.buildConnectionManagerMapKey(httpClientInputs.getTrustAllRoots(),
                httpClientInputs.getX509HostnameVerifier(),
                httpClientInputs.getKeystore(),
                httpClientInputs.getTrustKeystore());
        PoolingHttpClientConnectionManager connManager = poolingHttpClientConnectionManagerBuilder
                .setConnectionManagerMapKey(connectionKey)
                .setConnectionPoolHolder(httpClientInputs.getConnectionPoolSessionObject())
                .setSslsf(sslConnectionSocketFactory)
                .setDefaultMaxPerRoute(httpClientInputs.getConnectionsMaxPerRoute())
                .setTotalMax(httpClientInputs.getConnectionsMaxTotal())
                .buildConnectionManager();

        httpClientBuilder.setConnectionManager(connManager);

        if (StringUtils.isEmpty(httpClientInputs.getKeepAlive()) || Boolean.parseBoolean(httpClientInputs.getKeepAlive())) {
            httpClientBuilder.setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE);
        } else {
            httpClientBuilder.setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE);
        }

        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false));

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
        result.setHttpClientBuilder(httpClientBuilder);
        return result;
    }


    public CloseableHttpResponse execute(CloseableHttpClient closeableHttpClient,
                                         HttpRequestBase httpRequestBase,
                                         HttpClientContext context) {

        return httpClientExecutor
                .setCloseableHttpClient(closeableHttpClient)
                .setHttpRequestBase(httpRequestBase)
                .setContext(context)
                .execute();
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
        boolean keepAlive = StringUtils.isBlank(keepAliveInput) || Boolean.parseBoolean(keepAliveInput);
        if (keepAlive) {
            httpRequestBase.releaseConnection();
        } else {
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
