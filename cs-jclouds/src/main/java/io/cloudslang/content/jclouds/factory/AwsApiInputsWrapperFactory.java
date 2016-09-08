package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.*;

/**
 * Created by Mihai Tusa.
 * 9/5/2016.
 */
public class AwsApiInputsWrapperFactory {
    private AwsApiInputsWrapperFactory() {
    }

    @SafeVarargs
    public static <T> AwsInputsWrapper getWrapper(CommonInputs commonInputs, T... builders) {
        HttpClientInputs httpClientInputs = getHttpClientInputs(commonInputs);

        AwsInputsWrapper wrapper;
        switch (commonInputs.getAction()) {
            case Constants.QueryApiActions.ALLOCATE_ADDRESS:
                wrapper = getAwsWrapperBuild(httpClientInputs, commonInputs, builders);
                break;
            case Constants.QueryApiActions.ATTACH_NETWORK_INTERFACE:
                wrapper = getAwsWrapperBuild(httpClientInputs, commonInputs, builders);
                break;
            case Constants.QueryApiActions.CREATE_VOLUME:
                wrapper = getAwsWrapperBuild(httpClientInputs, commonInputs, builders);
                break;
            case Constants.QueryApiActions.DETACH_NETWORK_INTERFACE:
                wrapper = getAwsWrapperBuild(httpClientInputs, commonInputs, builders);
                break;
            default:
                throw new RuntimeException(Constants.ErrorMessages.UNSUPPORTED_QUERY_API);
        }

        return wrapper;
    }

    private static HttpClientInputs getHttpClientInputs(CommonInputs commonInputs) {
        HttpClientInputs httpClientInputs = new HttpClientInputs();

        httpClientInputs.setUrl(commonInputs.getEndpoint());
        httpClientInputs.setProxyHost(commonInputs.getProxyHost());
        httpClientInputs.setProxyPort(commonInputs.getProxyPort());
        httpClientInputs.setProxyUsername(commonInputs.getProxyUsername());
        httpClientInputs.setProxyPassword(commonInputs.getProxyPassword());
        httpClientInputs.setMethod(commonInputs.getHttpClientMethod());
        httpClientInputs.setAuthType(Constants.AwsParams.AUTHORIZATION_TYPE_ANONYMOUS);
        httpClientInputs.setQueryParamsAreURLEncoded(Boolean.FALSE.toString());

        return httpClientInputs;
    }

    @SafeVarargs
    private static <T> AwsInputsWrapper getAwsWrapperBuild(HttpClientInputs httpClientInputs, CommonInputs commonInputs,
                                                           T... builders) {
        AwsInputsWrapper wrapper = new AwsInputsWrapper.AWSInputsWrapperBuilder()
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
                } else if (builder instanceof ImageInputs) {
                    wrapper.setImageInputs((ImageInputs) builder);
                } else if (builder instanceof InstanceInputs) {
                    wrapper.setInstanceInputs((InstanceInputs) builder);
                } else if (builder instanceof NetworkInputs) {
                    wrapper.setNetworkInputs((NetworkInputs) builder);
                } else if (builder instanceof VolumeInputs) {
                    wrapper.setVolumeInputs((VolumeInputs) builder);
                }
            }

        }

        return wrapper;
    }
}