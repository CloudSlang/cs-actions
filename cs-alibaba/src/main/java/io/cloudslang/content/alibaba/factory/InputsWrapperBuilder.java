/*
 * (c) Copyright 2018 Micro Focus, L.P.
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

package io.cloudslang.content.alibaba.factory;

import io.cloudslang.content.alibaba.entities.CreateInstanceInputs;
import io.cloudslang.content.alibaba.entities.SignatureInputs;
import io.cloudslang.content.alibaba.entities.constants.Inputs;
import io.cloudslang.content.alibaba.entities.inputs.CommonInputs;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;

import static io.cloudslang.content.alibaba.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.alibaba.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.alibaba.utils.InputsUtil.getUrlFromApiService;
import io.cloudslang.content.alibaba.entities.inputs.InputsWrapper;

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

                if (builder instanceof io.cloudslang.content.alibaba.entities.CreateInstanceInputs) {
                    wrapper.setCreateInstanceInputs((io.cloudslang.content.alibaba.entities.CreateInstanceInputs) builder);

                }else if (builder instanceof SignatureInputs) {
                    wrapper.setSignatureInputs((SignatureInputs) builder);

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

        String prefix = EMPTY;

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


}