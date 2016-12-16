/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.factory;

import io.cloudslang.content.amazon.utils.InputsUtil;
import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.amazon.entities.inputs.*;

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
        return buildWrapper(getHttpClientInputs(commonInputs), commonInputs, builders);
    }

    @SafeVarargs
    private static <T> InputsWrapper buildWrapper(HttpClientInputs httpClientInputs, CommonInputs commonInputs, T... builders) {
        InputsWrapper wrapper = new InputsWrapper.Builder()
                .withHttpClientInputs(httpClientInputs)
                .withCommonInputs(commonInputs)
                .withApiService(commonInputs.getApiService())
                .withRequestUri(commonInputs.getRequestUri())
                .withRequestPayload(commonInputs.getRequestPayload())
                .withHttpVerb(commonInputs.getHttpClientMethod())
                .build();

        if (builders.length > 0) {
            for (T builder : builders) {
                if (builder instanceof CustomInputs) {
                    wrapper.setCustomInputs((CustomInputs) builder);
                } else if (builder instanceof EbsInputs) {
                    wrapper.setEbsInputs((EbsInputs) builder);
                } else if (builder instanceof ElasticIpInputs) {
                    wrapper.setElasticIpInputs((ElasticIpInputs) builder);
                } else if (builder instanceof IamInputs) {
                    wrapper.setIamInputs((IamInputs) builder);
                } else if (builder instanceof ImageInputs) {
                    wrapper.setImageInputs((ImageInputs) builder);
                } else if (builder instanceof InstanceInputs) {
                    wrapper.setInstanceInputs((InstanceInputs) builder);
                } else if (builder instanceof LoadBalancerInputs) {
                    wrapper.setLoadBalancerInputs((LoadBalancerInputs) builder);
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

    private static HttpClientInputs getHttpClientInputs(CommonInputs commonInputs) {
        HttpClientInputs httpClientInputs = new HttpClientInputs();

        httpClientInputs.setUrl(InputsUtil.getUrlFromApiService(commonInputs.getEndpoint(), commonInputs.getApiService()));
        httpClientInputs.setProxyHost(commonInputs.getProxyHost());
        httpClientInputs.setProxyPort(commonInputs.getProxyPort());
        httpClientInputs.setProxyUsername(commonInputs.getProxyUsername());
        httpClientInputs.setProxyPassword(commonInputs.getProxyPassword());
        httpClientInputs.setMethod(commonInputs.getHttpClientMethod());
        httpClientInputs.setAuthType(AUTHORIZATION_TYPE_ANONYMOUS);
        httpClientInputs.setQueryParamsAreURLEncoded(Boolean.FALSE.toString());

        return httpClientInputs;
    }
}
