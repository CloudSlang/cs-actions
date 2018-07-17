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

import io.cloudslang.content.alibaba.entities.SignatureInputs;
import io.cloudslang.content.alibaba.entities.alibaba.AlibabaApi;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;

import static io.cloudslang.content.alibaba.entities.constants.Constants.Miscellaneous.SCOPE_SEPARATOR;
import static io.cloudslang.content.alibaba.utils.InputsUtil.getDefaultStringInput;


public class InputsWrapper {
    private final HttpClientInputs httpClientInputs;
    private final CommonInputs commonInputs;


    private final String apiService;
    private final String requestUri;
    private final String requestPayload;
    private final String date;
    private final String securityToken;
    private final String httpVerb;
    private final String headers;
    private final String queryParams;
    private io.cloudslang.content.alibaba.entities.CreateInstanceInputs createInstanceInputs;
    private SignatureInputs signatureInputs;

    private InputsWrapper(Builder builder) {
        this.httpClientInputs = builder.httpClientInputs;
        this.commonInputs = builder.commonInputs;
        this.createInstanceInputs = builder.createInstanceInputs;
        this.signatureInputs = builder.signatureInputs;
        this.apiService = builder.apiService;
        this.requestUri = builder.requestUri;
        this.requestPayload = builder.requestPayload;
        this.date = builder.date;
        this.securityToken = builder.securityToken;
        this.httpVerb = builder.httpVerb;
        this.headers = builder.headers;
        this.queryParams = builder.queryParams;
    }

    public HttpClientInputs getHttpClientInputs() {
        return httpClientInputs;
    }

    public CommonInputs getCommonInputs() {
        return commonInputs;
    }

    public io.cloudslang.content.alibaba.entities.CreateInstanceInputs getInstanceInputs() {
        return createInstanceInputs;
    }

    public SignatureInputs getSignatureInputs() {
        return signatureInputs;
    }

    public void setCreateInstanceInputs(io.cloudslang.content.alibaba.entities.CreateInstanceInputs instanceInputs) {
        this.createInstanceInputs = instanceInputs;
    }

    public void setSignatureInputs(SignatureInputs signatureInputs) {
        this.signatureInputs = signatureInputs;
    }

    public String getApiService() {
        return apiService;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getRequestPayload() {
        return requestPayload;
    }

    public String getDate() {
        return date;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public String getHttpVerb() {
        return httpVerb;
    }

    public String getHeaders() {
        return headers;
    }

    public String getQueryParams() {
        return queryParams;
    }


    public static class Builder {
        private HttpClientInputs httpClientInputs;
        private CommonInputs commonInputs;
        private io.cloudslang.content.alibaba.entities.CreateInstanceInputs createInstanceInputs;
        private SignatureInputs signatureInputs;
        private String apiService;
        private String requestUri;
        private String requestPayload;
        private String date;
        private String securityToken;
        private String httpVerb;
        private String headers;
        private String queryParams;

        public InputsWrapper build() {
            return new InputsWrapper(this);
        }

        public Builder withHttpClientInputs(HttpClientInputs inputs) {
            httpClientInputs = inputs;
            return this;
        }

        public Builder withSignatureInputs(SignatureInputs inputs) {
            signatureInputs = inputs;
            return this;
        }

        public Builder withCreateInstanceInputs(io.cloudslang.content.alibaba.entities.CreateInstanceInputs inputs) {
            createInstanceInputs = inputs;
            return this;
        }

        public Builder withCommonInputs(CommonInputs inputs) {
            commonInputs = inputs;
            return this;
        }

        public Builder withApiService(String inputValue) {
            apiService = AlibabaApi.getApiValue(inputValue);
            return this;
        }

        public Builder withRequestUri(String inputValue) {
            requestUri = getDefaultStringInput(inputValue, SCOPE_SEPARATOR);
            return this;
        }

        public Builder withRequestPayload(String inputValue) {
            requestPayload = inputValue;
            return this;
        }

        public Builder withDate(String inputValue) {
            date = inputValue;
            return this;
        }

        public Builder withSecurityToken(String inputValue) {
            securityToken = inputValue;
            return this;
        }

        public Builder withHttpVerb(String inputValue) {
            httpVerb = inputValue;
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
    }
}
