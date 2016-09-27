package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.jclouds.entities.inputs.*;

/**
 * Created by Mihai Tusa.
 * 9/5/2016.
 */
public class InputsWrapperBuilder {
    private static final String AUTHORIZATION_TYPE_ANONYMOUS = "anonymous";
    private static final String UNKNOWN_BUILDER_TYPE = "Unknown builder type.";

    private InputsWrapperBuilder() {
    }

    @SafeVarargs
    public static <T> InputsWrapper getWrapper(CommonInputs commonInputs, T... builders) {
        HttpClientInputs httpClientInputs = getHttpClientInputs(commonInputs);

        return buildWrapper(httpClientInputs, commonInputs, builders);
    }

    private static HttpClientInputs getHttpClientInputs(CommonInputs commonInputs) {
        HttpClientInputs httpClientInputs = new HttpClientInputs();

        httpClientInputs.setUrl(commonInputs.getEndpoint());
        httpClientInputs.setProxyHost(commonInputs.getProxyHost());
        httpClientInputs.setProxyPort(commonInputs.getProxyPort());
        httpClientInputs.setProxyUsername(commonInputs.getProxyUsername());
        httpClientInputs.setProxyPassword(commonInputs.getProxyPassword());
        httpClientInputs.setMethod(commonInputs.getHttpClientMethod());
        httpClientInputs.setAuthType(AUTHORIZATION_TYPE_ANONYMOUS);
        httpClientInputs.setQueryParamsAreURLEncoded(Boolean.FALSE.toString());

        return httpClientInputs;
    }

    @SafeVarargs
    private static <T> InputsWrapper buildWrapper(HttpClientInputs httpClientInputs, CommonInputs commonInputs, T... builders) {
        InputsWrapper wrapper = new InputsWrapper.InputsWrapperBuilder()
                .withHttpClientInputs(httpClientInputs)
                .withCommonInputs(commonInputs)
                .withAction(commonInputs.getAction())
                .withApiService(commonInputs.getApiService())
                .withRequestUri(commonInputs.getRequestUri())
                .withRequestPayload(commonInputs.getRequestPayload())
                .withHttpVerb(commonInputs.getHttpClientMethod())
                .build();

        if (builders.length > 0) {
            for (T builder : builders) {
                if (builder instanceof CustomInputs) {
                    wrapper.setCustomInputs((CustomInputs) builder);
                } else if (builder instanceof ElasticIpInputs) {
                    wrapper.setElasticIpInputs((ElasticIpInputs) builder);
                } else if (builder instanceof ImageInputs) {
                    wrapper.setImageInputs((ImageInputs) builder);
                } else if (builder instanceof InstanceInputs) {
                    wrapper.setInstanceInputs((InstanceInputs) builder);
                } else if (builder instanceof NetworkInputs) {
                    wrapper.setNetworkInputs((NetworkInputs) builder);
                } else if (builder instanceof VolumeInputs) {
                    wrapper.setVolumeInputs((VolumeInputs) builder);
                } else {
                    throw new RuntimeException(UNKNOWN_BUILDER_TYPE);
                }
            }
        }

        return wrapper;
    }
}