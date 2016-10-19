package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.jclouds.entities.aws.AmazonApiServiceType;
import io.cloudslang.content.jclouds.utils.InputsUtil;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.SCOPE_SEPARATOR;

/**
 * Created by Mihai Tusa.
 * 8/10/2016.
 */
public class InputsWrapper {
    private HttpClientInputs httpClientInputs;
    private CommonInputs commonInputs;
    private CustomInputs customInputs;
    private EbsInputs ebsInputs;
    private ElasticIpInputs elasticIpInputs;
    private IamInputs iamInputs;
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

    private InputsWrapper(Builder builder) {
        this.httpClientInputs = builder.httpClientInputs;
        this.commonInputs = builder.commonInputs;
        this.customInputs = builder.customInputs;

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

    public void setCommonInputs(CommonInputs commonInputs) {
        this.commonInputs = commonInputs;
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

    public static class Builder {
        private HttpClientInputs httpClientInputs;
        private CommonInputs commonInputs;
        private CustomInputs customInputs;

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

        public Builder withCustomInputs(CustomInputs inputs) {
            customInputs = inputs;
            return this;
        }

        public Builder withApiService(String inputValue) {
            apiService = AmazonApiServiceType.getValue(inputValue);
            return this;
        }

        public Builder withRequestUri(String inputValue) {
            requestUri = InputsUtil.getDefaultStringInput(inputValue, SCOPE_SEPARATOR);
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