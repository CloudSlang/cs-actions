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

import io.cloudslang.content.alibaba.entities.inputs.InputsWrapper;


import java.util.HashMap;
import java.util.Map;


import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaParams.HEADER_DELIMITER;
import static io.cloudslang.content.alibaba.entities.constants.Constants.Miscellaneous.COLON;
import static io.cloudslang.content.alibaba.utils.InputsUtil.getHeadersOrQueryParamsMap;
import static org.apache.commons.lang3.StringUtils.isBlank;


public class HeadersMapBuilder {
    private HeadersMapBuilder() {
        // prevent instantiation
    }

    public static Map<String, String> getHeadersMap(InputsWrapper wrapper) {
        Map<String, String> headersMap = isBlank(wrapper.getCommonInputs().getHeaders()) ? new HashMap<String, String>() :
                getHeadersOrQueryParamsMap(new HashMap<String, String>(), wrapper.getCommonInputs().getHeaders(), HEADER_DELIMITER, COLON, true);

        switch (wrapper.getCommonInputs().getApiService()) {

            default:
                return headersMap;
        }
    }
}