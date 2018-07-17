/*
 * (c) Copyright 2018 Micro Focus, L.P.
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

package io.cloudslang.content.alibaba.entities.inputs;

import io.cloudslang.content.alibaba.entities.alibaba.AlibabaApi;

import java.net.MalformedURLException;
import java.net.URL;

import static io.cloudslang.content.alibaba.utils.InputsUtil.getDefaultStringInput;
import static io.cloudslang.content.alibaba.utils.InputsUtil.getUrlFromApiService;

import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.alibaba.entities.constants.Constants.Miscellaneous.COMMA_DELIMITER;
import static io.cloudslang.content.alibaba.entities.constants.Constants.Miscellaneous.EMPTY;


public class CommonInputs {
    private final String endpoint;
    private final String accessKeyId;
    private final String accessKeySecret;
    private final String proxyHost;
    private final String proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final String delimiter;
    private final String version;
    private final String headers;
    private final String queryParams;
    private final String apiService;
    private final String requestUri;
    private final String action;
    private final String requestPayload;
    private final String httpClientMethod;

    private CommonInputs(Builder builder) {
        this.endpoint = builder.endpoint;
        this.accessKeyId = builder.accessKeyId;
        this.accessKeySecret = builder.accessKeySecret;
        this.proxyHost = builder.proxyHost;
        this.proxyPort = builder.proxyPort;
        this.proxyUsername = builder.proxyUsername;
        this.proxyPassword = builder.proxyPassword;
        this.delimiter = builder.delimiter;
        this.version = builder.version;
        this.headers = builder.headers;
        this.queryParams = builder.queryParams;
        this.apiService = builder.apiService;
        this.requestUri = builder.requestUri;
        this.action = builder.action;
        this.requestPayload = builder.requestPayload;
        this.httpClientMethod = builder.httpClientMethod;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public String getVersion() {
        return version;
    }

    public String getHeaders() {
        return headers;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public String getApiService() {
        return apiService;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getAction() {
        return action;
    }

    public String getRequestPayload() {
        return requestPayload;
    }

    public String getHttpClientMethod() {
        return httpClientMethod;
    }

    public static class Builder {
        private String endpoint;
        private String accessKeyId;
        private String accessKeySecret;
        private String proxyHost;
        private String proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private String delimiter;
        private String version;
        private String headers;
        private String queryParams;
        private String apiService;
        private String requestUri;
        private String action;
        private String requestPayload;
        private String httpClientMethod;

        public CommonInputs build() {
            return new CommonInputs(this);
        }

        public Builder withEndpoint(String inputValue, String apiService, String prefix) throws MalformedURLException {
            endpoint = new URL(getUrlFromApiService(inputValue, apiService, prefix)).toString();
            return this;
        }

        public Builder withAccessKeyId(String inputValue) {
            accessKeyId = inputValue;
            return this;
        }

        public Builder withAccessKeySecret(String inputValue) {
            accessKeySecret = inputValue;
            return this;
        }

        public Builder withProxyHost(String inputValue) {
            proxyHost = inputValue;
            return this;
        }

        public Builder withProxyPort(String inputValue) {
            proxyPort = inputValue;
            return this;
        }

        public Builder withProxyUsername(String inputValue) {
            proxyUsername = inputValue;
            return this;
        }

        public Builder withProxyPassword(String inputValue) {
            proxyPassword = inputValue;
            return this;
        }

        public Builder withDelimiter(String inputValue) {
            delimiter = getDefaultStringInput(inputValue, COMMA_DELIMITER);
            return this;
        }

        public Builder withVersion(String inputValue) {
            version = inputValue;
            return this;
        }

        public Builder withHeaders(String inputValue) {
            headers = inputValue;
            return this;
        }

        public Builder withQueryParams(String inputValue) {
            queryParams = inputValue;
            return this;
        }

        public Builder withApiService(String inputValue) {
            apiService = AlibabaApi.getApiValue(inputValue);
            return this;
        }

        public Builder withRequestUri(String inputValue) {
            requestUri = getDefaultStringInput(inputValue, EMPTY);
            return this;
        }

        public Builder withAction(String inputValue) {
            action = inputValue;
            return this;
        }

        public Builder withRequestPayload(String inputValue) {
            requestPayload = getDefaultStringInput(inputValue, EMPTY);
            return this;
        }

        public Builder withHttpClientMethod(String inputValue) {
            httpClientMethod = getDefaultStringInput(inputValue, HTTP_CLIENT_METHOD_GET);
            return this;
        }
    }
}
