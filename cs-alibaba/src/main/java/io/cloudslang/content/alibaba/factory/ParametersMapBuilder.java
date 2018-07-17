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

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;


import io.cloudslang.content.alibaba.entities.inputs.InputsWrapper;


import static io.cloudslang.content.alibaba.utils.InputsUtil.getHeadersOrQueryParamsMap;

import static io.cloudslang.content.alibaba.entities.constants.Constants.Apis.ECS_API;


import static io.cloudslang.content.alibaba.entities.constants.Constants.Miscellaneous.AMPERSAND;
import static io.cloudslang.content.alibaba.entities.constants.Constants.Miscellaneous.EQUAL;


public class ParametersMapBuilder {
    private static final String UNSUPPORTED_ALIBABA_API = "Unsupported Alibaba API.";

    private ParametersMapBuilder() {
        // prevent instantiation
    }

    public static Map<String, String> getParamsMap(InputsWrapper wrapper) throws Exception {
        Map<String, String> queryParamsMap;
        if (isBlank(wrapper.getCommonInputs().getQueryParams())) {
            switch (wrapper.getCommonInputs().getApiService()) {
                case ECS_API:
                    return ECSQueryParamsMapBuilder.getEcsQueryParamsMap(wrapper);
                default:
                    throw new RuntimeException(UNSUPPORTED_ALIBABA_API);
            }
        } else {
            queryParamsMap = getHeadersOrQueryParamsMap(new HashMap<String, String>(), wrapper.getCommonInputs().getQueryParams(), AMPERSAND, EQUAL, false);
        }

        return queryParamsMap;
    }
}

