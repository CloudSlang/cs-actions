/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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




package io.cloudslang.content.couchbase.entities.inputs;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;

/**
 * Created by Mihai Tusa
 * 4/9/2017.
 */
public class InputsWrapper {
    private HttpClientInputs httpClientInputs;
    private final CommonInputs commonInputs;

    private BucketInputs bucketInputs;
    private ClusterInputs clusterInputs;
    private NodeInputs nodeInputs;

    private InputsWrapper(Builder builder) {
        this.httpClientInputs = builder.httpClientInputs;
        this.commonInputs = builder.commonInputs;
    }

    public HttpClientInputs getHttpClientInputs() {
        return httpClientInputs;
    }

    public void setUrl(String url) {
        updateHttpClientInputs(url, httpClientInputs.getHeaders(), httpClientInputs.getContentType(), httpClientInputs.getBody());
    }

    public void setHeaders(String headers) {
        updateHttpClientInputs(httpClientInputs.getUrl(), headers, httpClientInputs.getContentType(), httpClientInputs.getBody());
    }

    public void setContentType(String contentType) {
        updateHttpClientInputs(httpClientInputs.getUrl(), httpClientInputs.getHeaders(), contentType, httpClientInputs.getBody());
    }

    public void setBody(String body) {
        updateHttpClientInputs(httpClientInputs.getUrl(), httpClientInputs.getHeaders(), httpClientInputs.getContentType(), body);
    }

    public CommonInputs getCommonInputs() {
        return commonInputs;
    }

    public BucketInputs getBucketInputs() {
        return bucketInputs;
    }

    public void setBucketInputs(BucketInputs bucketInputs) {
        this.bucketInputs = bucketInputs;
    }

    public ClusterInputs getClusterInputs() {
        return clusterInputs;
    }

    public void setClusterInputs(ClusterInputs clusterInputs) {
        this.clusterInputs = clusterInputs;
    }

    public NodeInputs getNodeInputs() {
        return nodeInputs;
    }

    public void setNodeInputs(NodeInputs nodeInputs) {
        this.nodeInputs = nodeInputs;
    }

    private void updateHttpClientInputs(String url, String headers, String contentType, String body) {
        HttpClientInputs inputs = httpClientInputs;
        httpClientInputs = HttpClientInputs.builder()
                .url(url)
                .method(inputs.getMethod())
                .authType(inputs.getAuthType())
                .preemptiveAuth(inputs.getPreemptiveAuth())
                .username(inputs.getUsername())
                .password(inputs.getPassword())
                .proxyHost(inputs.getProxyHost())
                .proxyPort(inputs.getProxyPort())
                .proxyUsername(inputs.getProxyUsername())
                .proxyPassword(inputs.getProxyPassword())
                .tlsVersion(inputs.getTlsVersion())
                .allowedCiphers(inputs.getAllowedCiphers())
                .trustAllRoots(inputs.getTrustAllRoots())
                .x509HostnameVerifier(inputs.getX509HostnameVerifier())
                .trustKeystore(inputs.getTrustKeystore())
                .trustPassword(inputs.getTrustPassword())
                .keystore(inputs.getKeystore())
                .keystorePassword(inputs.getKeystorePassword())
                .connectTimeout(inputs.getConnectTimeout())
                .responseTimeout(inputs.getResponseTimeout())
                .executionTimeout(inputs.getExecutionTimeout())
                .useCookies(inputs.getUseCookies())
                .keepAlive(inputs.getKeepAlive())
                .connectionsMaxPerRoute(inputs.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(inputs.getConnectionsMaxTotal())
                .headers(headers)
                .responseCharacterSet(inputs.getResponseCharacterSet())
                .destinationFile(inputs.getDestinationFile())
                .followRedirects(inputs.getFollowRedirects())
                .queryParams(inputs.getQueryParams())
                .queryParamsAreURLEncoded(inputs.getQueryParamsAreURLEncoded())
                .queryParamsAreFormEncoded(inputs.getQueryParamsAreFormEncoded())
                .formParams(inputs.getFormParams())
                .formParamsAreURLEncoded(inputs.getFormParamsAreURLEncoded())
                .sourceFile(inputs.getSourceFile())
                .body(body)
                .contentType(contentType)
                .requestCharacterSet(inputs.getRequestCharacterSet())
                .multipartBodies(inputs.getMultipartBodies())
                .multipartFiles(inputs.getMultipartFiles())
                .multipartValuesAreURLEncoded(inputs.getMultipartValuesAreURLEncoded())
                .multipartBodiesContentType(inputs.getMultipartBodiesContentType())
                .multipartFilesContentType(inputs.getMultipartFilesContentType())
                .cookieStoreSessionObject(inputs.getCookieStoreSessionObject())
                .connectionPoolSessionObject(inputs.getConnectionPoolSessionObject())
                .build();
    }

    public static class Builder {
        private HttpClientInputs httpClientInputs;
        private CommonInputs commonInputs;

        public InputsWrapper build() {
            return new InputsWrapper(this);
        }

        public Builder withHttpClientInputs(HttpClientInputs httpClientInputs) {
            this.httpClientInputs = httpClientInputs;
            return this;
        }

        public Builder withCommonInputs(CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }
    }
}