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

package io.cloudslang.content.amazon.factory;

import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.amazon.entities.inputs.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static io.cloudslang.content.amazon.utils.InputsUtil.getUrlFromApiService;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;

/**
 * Created by Mihai Tusa.
 * 9/5/2016.
 */
public class InputsWrapperBuilder {
    private static final String AUTHORIZATION_TYPE_ANONYMOUS = "anonymous";
    private static final String UNKNOWN_BUILDER_TYPE = "Unknown builder type.";

    private InputsWrapperBuilder() {
        // prevent instantiation
    }

    @SafeVarargs
    public static <T> InputsWrapper getWrapper(CommonInputs commonInputs, T... builders) {
        HttpClientInputs httpClientInputs = getHttpClientInputs(commonInputs, builders);

        return buildWrapper(httpClientInputs, commonInputs, builders);
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

        if (builders.length > START_INDEX) {
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
                } else if (builder instanceof StorageInputs) {
                    wrapper.setStorageInputs((StorageInputs) builder);
                } else if (builder instanceof VolumeInputs) {
                    wrapper.setVolumeInputs((VolumeInputs) builder);
                } else if (builder instanceof FilterInputs) {
                    wrapper.setFilterInputs((FilterInputs) builder);
                } else {
                    throw new RuntimeException(UNKNOWN_BUILDER_TYPE);
                }
            }
        }

        return wrapper;
    }

    @SafeVarargs
    private static <T> HttpClientInputs getHttpClientInputs(CommonInputs commonInputs, T... builders) {
        HttpClientInputs httpClientInputs = new HttpClientInputs();

        String prefix = getPrefix(builders);

        httpClientInputs.setUrl(getUrlFromApiService(commonInputs.getEndpoint(), commonInputs.getApiService(), prefix));
        httpClientInputs.setProxyHost(commonInputs.getProxyHost());
        httpClientInputs.setProxyPort(commonInputs.getProxyPort());
        httpClientInputs.setProxyUsername(commonInputs.getProxyUsername());
        httpClientInputs.setProxyPassword(commonInputs.getProxyPassword());
        httpClientInputs.setMethod(commonInputs.getHttpClientMethod());
        httpClientInputs.setAuthType(AUTHORIZATION_TYPE_ANONYMOUS);
        httpClientInputs.setQueryParamsAreURLEncoded(Boolean.FALSE.toString());

        return httpClientInputs;
    }

    private static <T> String getPrefix(T[] builders) {
        String prefix = EMPTY;
        if (builders.length > START_INDEX) {
            for (T builder : builders) {
                if (builder instanceof StorageInputs && isNotBlank(((StorageInputs) builder).getBucketName())) {
                    prefix = ((StorageInputs) builder).getBucketName();
                }
            }
        }

        return prefix;
    }
}