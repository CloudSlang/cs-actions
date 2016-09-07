package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.jclouds.entities.aws.HttpClientMethod;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.*;

/**
 * Created by Mihai Tusa.
 * 9/5/2016.
 */
public class AwsApiInputsWrapperFactory {
    private AwsApiInputsWrapperFactory() {
    }

    public static AwsInputsWrapper getWrapper(CommonInputs commonInputs, CustomInputs customInputs,
                                              VolumeInputs volumeInputs, NetworkInputs networkInputs,
                                              String action) {
        HttpClientInputs httpClientInputs = getHttpClientInputs(commonInputs);
        AwsInputsWrapper wrapper;

        switch (action) {
            case Constants.QueryApiActions.ALLOCATE_ADDRESS:
                wrapper = getSpecificAwsInputsWrapper(commonInputs, customInputs, httpClientInputs, networkInputs, action);
                break;
            case Constants.QueryApiActions.ATTACH_NETWORK_INTERFACE:
                wrapper = getSpecificAwsInputsWrapper(commonInputs, customInputs, httpClientInputs, networkInputs, action);
                break;
            case Constants.QueryApiActions.CREATE_VOLUME:
                wrapper = getSpecificAwsInputsWrapper(commonInputs, customInputs, httpClientInputs, volumeInputs, action);
                break;
            case Constants.QueryApiActions.DETACH_NETWORK_INTERFACE:
                wrapper = getSpecificAwsInputsWrapper(commonInputs, customInputs, httpClientInputs, networkInputs, action);
                break;
            default:
                throw new RuntimeException(Constants.ErrorMessages.UNSUPPORTED_QUERY_API);
        }

        return wrapper;
    }

    private static <T> AwsInputsWrapper getSpecificAwsInputsWrapper(CommonInputs commonInputs, CustomInputs customInputs,
                                                                    HttpClientInputs httpClientInputs, T specificInputs,
                                                                    String action) {
        if (specificInputs instanceof NetworkInputs) {
            return new AwsInputsWrapper.AWSInputsWrapperBuilder()
                    .withCommonInputs(commonInputs)
                    .withCustomInputs(customInputs)
                    .withHttpClientInputs(httpClientInputs)
                    .withApiService(Constants.Apis.AMAZON_EC2_API)
                    .withRequestUri(Constants.Miscellaneous.EMPTY)
                    .withRequestPayload(Constants.Miscellaneous.EMPTY)
                    .withHttpVerb(HttpClientMethod.GET.toString())
                    .withAction(action)
                    .withNetworkInputs((NetworkInputs) specificInputs)
                    .build();
        } else if (specificInputs instanceof VolumeInputs) {
            return new AwsInputsWrapper.AWSInputsWrapperBuilder()
                    .withCommonInputs(commonInputs)
                    .withCustomInputs(customInputs)
                    .withHttpClientInputs(httpClientInputs)
                    .withApiService(Constants.Apis.AMAZON_EC2_API)
                    .withRequestUri(Constants.Miscellaneous.EMPTY)
                    .withRequestPayload(Constants.Miscellaneous.EMPTY)
                    .withHttpVerb(HttpClientMethod.GET.toString())
                    .withAction(action)
                    .withVolumeInputs((VolumeInputs) specificInputs)
                    .build();
        } else {
            throw new RuntimeException(Constants.ErrorMessages.UNSUPPORTED_QUERY_API);
        }

    }

    private static HttpClientInputs getHttpClientInputs(CommonInputs commonInputs) {
        HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(commonInputs.getEndpoint());
        httpClientInputs.setProxyHost(commonInputs.getProxyHost());
        httpClientInputs.setProxyPort(commonInputs.getProxyPort());
        httpClientInputs.setProxyUsername(commonInputs.getProxyUsername());
        httpClientInputs.setProxyPassword(commonInputs.getProxyPassword());
        httpClientInputs.setMethod(HttpClientMethod.GET.toString());
        httpClientInputs.setAuthType(Constants.AwsParams.AUTHORIZATION_TYPE_ANONYMOUS);
        httpClientInputs.setQueryParamsAreURLEncoded(Boolean.FALSE.toString());

        return httpClientInputs;
    }
}