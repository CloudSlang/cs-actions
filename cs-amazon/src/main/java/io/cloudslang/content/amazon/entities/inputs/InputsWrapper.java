/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.amazon.entities.aws.AmazonApi;
import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.SCOPE_SEPARATOR;

/**
 * Created by Mihai Tusa.
 * 8/10/2016.
 */
public class InputsWrapper {
    private final HttpClientInputs httpClientInputs;
    private final CommonInputs commonInputs;
    private CustomInputs customInputs;
    private EbsInputs ebsInputs;
    private ElasticIpInputs elasticIpInputs;
    private IamInputs iamInputs;
    private ImageInputs imageInputs;
    private InstanceInputs instanceInputs;
    private LoadBalancerInputs loadBalancerInputs;
    private NetworkInputs networkInputs;
    private StorageInputs storageInputs;
    private VolumeInputs volumeInputs;
    private FilterInputs filterInputs;

    private final String apiService;
    private final String requestUri;
    private final String requestPayload;
    private final String date;
    private final String securityToken;
    private final String httpVerb;
    private final String headers;
    private final String queryParams;

    private InputsWrapper(Builder builder) {
        this.httpClientInputs = builder.httpClientInputs;
        this.commonInputs = builder.commonInputs;

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

    public CustomInputs getCustomInputs() {
        return customInputs;
    }

    public void setCustomInputs(CustomInputs customInputs) {
        this.customInputs = customInputs;
    }

    public EbsInputs getEbsInputs() {
        return ebsInputs;
    }

    public void setEbsInputs(EbsInputs ebsInputs) {
        this.ebsInputs = ebsInputs;
    }

    public ElasticIpInputs getElasticIpInputs() {
        return elasticIpInputs;
    }

    public void setElasticIpInputs(ElasticIpInputs elasticIpInputs) {
        this.elasticIpInputs = elasticIpInputs;
    }

    public IamInputs getIamInputs() {
        return iamInputs;
    }

    public void setIamInputs(IamInputs iamInputs) {
        this.iamInputs = iamInputs;
    }

    public ImageInputs getImageInputs() {
        return imageInputs;
    }

    public void setImageInputs(ImageInputs imageInputs) {
        this.imageInputs = imageInputs;
    }

    public InstanceInputs getInstanceInputs() {
        return instanceInputs;
    }

    public void setInstanceInputs(InstanceInputs instanceInputs) {
        this.instanceInputs = instanceInputs;
    }

    public LoadBalancerInputs getLoadBalancerInputs() {
        return loadBalancerInputs;
    }

    public void setLoadBalancerInputs(LoadBalancerInputs loadBalancerInputs) {
        this.loadBalancerInputs = loadBalancerInputs;
    }

    public NetworkInputs getNetworkInputs() {
        return networkInputs;
    }

    public StorageInputs getStorageInputs() {
        return storageInputs;
    }

    public void setStorageInputs(StorageInputs storageInputs) {
        this.storageInputs = storageInputs;
    }

    public void setNetworkInputs(NetworkInputs networkInputs) {
        this.networkInputs = networkInputs;
    }

    public VolumeInputs getVolumeInputs() {
        return volumeInputs;
    }

    public void setVolumeInputs(VolumeInputs volumeInputs) {
        this.volumeInputs = volumeInputs;
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

    public FilterInputs getFilterInputs() {
        return filterInputs;
    }

    public void setFilterInputs(@NotNull final FilterInputs filterInputs) {
        this.filterInputs = filterInputs;
    }

    public static class Builder {
        private HttpClientInputs httpClientInputs;
        private CommonInputs commonInputs;

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

        public Builder withCommonInputs(CommonInputs inputs) {
            commonInputs = inputs;
            return this;
        }

        public Builder withApiService(String inputValue) {
            apiService = AmazonApi.getApiValue(inputValue);
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
