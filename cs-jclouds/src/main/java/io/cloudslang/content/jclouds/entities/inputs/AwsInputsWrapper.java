package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.jclouds.entities.aws.AmazonApiServiceType;
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
    private ImageInputs imageInputs;
    private InstanceInputs instanceInputs;
    private NetworkInputs networkInputs;
    private VolumeInputs volumeInputs;

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

    public void setCommonInputs(CommonInputs commonInputs) {
        this.commonInputs = commonInputs;
    }

    public CustomInputs getCustomInputs() {
        return customInputs;
    }

    public void setCustomInputs(CustomInputs customInputs) {
        this.customInputs = customInputs;
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

    public NetworkInputs getNetworkInputs() {
        return networkInputs;
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

    public String getAction() {
        return action;
    }

    public static class AWSInputsWrapperBuilder {
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

        public AWSInputsWrapperBuilder withApiService(String inputValue) {
            apiService = AmazonApiServiceType.getValue(inputValue);
            return this;
        }

        public AWSInputsWrapperBuilder withRequestUri(String inputValue) {
            requestUri = InputsUtil.getDefaultStringInput(inputValue, Constants.Miscellaneous.SCOPE_SEPARATOR);
            return this;
        }

        public AWSInputsWrapperBuilder withRequestPayload(String inputValue) {
            requestPayload = inputValue;
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
            httpVerb = inputValue;
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