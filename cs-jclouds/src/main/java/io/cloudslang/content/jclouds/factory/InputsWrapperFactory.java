package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.AwsInputsWrapper;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.factory.helpers.InputsWrapperHelper;

/**
 * Created by Mihai Tusa.
 * 9/5/2016.
 */
public class InputsWrapperFactory {
    private InputsWrapperFactory() {
    }

    @SafeVarargs
    public static <T> AwsInputsWrapper getWrapper(CommonInputs commonInputs, T... builders) {
        HttpClientInputs httpClientInputs = new InputsWrapperHelper().getHttpClientInputs(commonInputs);

        AwsInputsWrapper wrapper;
        switch (commonInputs.getAction()) {
            case Constants.QueryApiActions.ALLOCATE_ADDRESS:
                wrapper = new InputsWrapperHelper().getAwsWrapperBuild(httpClientInputs, commonInputs, builders);
                break;
            case Constants.QueryApiActions.ATTACH_NETWORK_INTERFACE:
                wrapper = new InputsWrapperHelper().getAwsWrapperBuild(httpClientInputs, commonInputs, builders);
                break;
            case Constants.QueryApiActions.CREATE_VOLUME:
                wrapper = new InputsWrapperHelper().getAwsWrapperBuild(httpClientInputs, commonInputs, builders);
                break;
            case Constants.QueryApiActions.DELETE_NETWORK_INTERFACE:
                wrapper = new InputsWrapperHelper().getAwsWrapperBuild(httpClientInputs, commonInputs, builders);
                break;
            case Constants.QueryApiActions.DETACH_NETWORK_INTERFACE:
                wrapper = new InputsWrapperHelper().getAwsWrapperBuild(httpClientInputs, commonInputs, builders);
                break;
            default:
                throw new RuntimeException(Constants.ErrorMessages.UNSUPPORTED_QUERY_API);
        }

        return wrapper;
    }
}