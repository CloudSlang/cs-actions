package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.jclouds.entities.aws.AmazonApiServiceType;
import io.cloudslang.content.jclouds.entities.aws.HttpClientMethod;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.utils.InputsUtil;

/**
 * Created by Mihai Tusa.
 * 8/10/2016.
 */
public class AwsInputsWrapper {
    private HttpClientInputs httpClientInputs;
    private CommonInputs commonInputs;
    private CustomInputs customInputs;
    private VolumeInputs volumeInputs;
    private NetworkInputs networkInputs;

    private String apiService;
    private String requestUri;
    private String requestPayload;
    private String date;
    private String securityToken;
    private String httpVerb;
    private String headers;
    private String queryParams;
    private String action;

    private AwsInputsWrapper(AWSInputsWrapperBuilder builder) {
        this.httpClientInputs = builder.httpClientInputs;
        this.commonInputs = builder.commonInputs;
        this.customInputs = builder.customInputs;
        this.volumeInputs = builder.volumeInputs;
        this.networkInputs = builder.networkInputs;

        this.apiService = builder.apiService;
        this.requestUri = builder.requestUri;
        this.requestPayload = builder.requestPayload;
        this.date = builder.date;
        this.securityToken = builder.securityToken;
        this.httpVerb = builder.httpVerb;
        this.headers = builder.headers;
        this.queryParams = builder.queryParams;
        this.action = builder.action;
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

    public VolumeInputs getVolumeInputs() {
        return volumeInputs;
    }

    public NetworkInputs getNetworkInputs() {
        return networkInputs;
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

    public String getAction() {
        return action;
    }

    public static class AWSInputsWrapperBuilder {
        private HttpClientInputs httpClientInputs;
        private CommonInputs commonInputs;
        private CustomInputs customInputs;
        private VolumeInputs volumeInputs;
        private NetworkInputs networkInputs;

        private String apiService;
        private String requestUri;
        private String requestPayload;
        private String date;
        private String securityToken;
        private String httpVerb;
        private String headers;
        private String queryParams;
        private String action;

        public AwsInputsWrapper build() {
            return new AwsInputsWrapper(this);
        }

        public AWSInputsWrapperBuilder withHttpClientInputs(HttpClientInputs inputs) {
            httpClientInputs = inputs;
            return this;
        }

        public AWSInputsWrapperBuilder withCommonInputs(CommonInputs inputs) {
            commonInputs = inputs;
            return this;
        }

        public AWSInputsWrapperBuilder withCustomInputs(CustomInputs inputs) {
            customInputs = inputs;
            return this;
        }

        public AWSInputsWrapperBuilder withVolumeInputs(VolumeInputs inputs) {
            volumeInputs = inputs;
            return this;
        }

        public AWSInputsWrapperBuilder withNetworkInputs(NetworkInputs inputs) {
            networkInputs = inputs;
            return this;
        }

        public AWSInputsWrapperBuilder withApiService(String inputValue) {
            apiService = AmazonApiServiceType.getValue(inputValue);
            return this;
        }

        public AWSInputsWrapperBuilder withRequestUri(String inputValue) {
            requestUri = InputsUtil.getDefaultStringInput(inputValue, Constants.Miscellaneous.SCOPE_SEPARATOR);
            return this;
        }

        public AWSInputsWrapperBuilder withRequestPayload(String inputValue) {
            requestPayload = InputsUtil.getDefaultStringInput(inputValue, Constants.Miscellaneous.EMPTY);
            return this;
        }

        public AWSInputsWrapperBuilder withDate(String inputValue) {
            date = inputValue;
            return this;
        }

        public AWSInputsWrapperBuilder withSecurityToken(String inputValue) {
            securityToken = inputValue;
            return this;
        }

        public AWSInputsWrapperBuilder withHttpVerb(String inputValue) {
            httpVerb = HttpClientMethod.getValue(inputValue);
            return this;
        }

        public AWSInputsWrapperBuilder withHeaders(String inputValue) {
            headers = inputValue;
            return this;
        }

        public AWSInputsWrapperBuilder withQueryParams(String inputValue) {
            queryParams = inputValue;
            return this;
        }

        public AWSInputsWrapperBuilder withAction(String inputValue) {
            action = inputValue;
            return this;
        }
    }
}