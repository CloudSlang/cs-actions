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

package io.cloudslang.content.alibaba.execute;

import io.cloudslang.content.alibaba.entities.CreateInstanceInputs;
import io.cloudslang.content.alibaba.entities.inputs.CommonInputs;
import io.cloudslang.content.alibaba.entities.inputs.InputsWrapper;
import io.cloudslang.content.alibaba.factory.HeadersMapBuilder;
import io.cloudslang.content.alibaba.factory.InputsWrapperBuilder;
import io.cloudslang.content.alibaba.factory.ParametersMapBuilder;
import io.cloudslang.content.httpclient.services.HttpClientService;

import java.util.Map;


import static io.cloudslang.content.alibaba.utils.InputsUtil.setQueryApiParams;
import static io.cloudslang.content.alibaba.utils.InputsUtil.setQueryApiHeaders;
import static io.cloudslang.content.alibaba.utils.OutputsUtil.getValidResponse;

public class QueryApiExecutor {


    @SafeVarargs
    public final <T> Map<String, String> execute(CommonInputs commonInputs, T... builders) throws Exception {
        InputsWrapper inputs = InputsWrapperBuilder.getWrapper(commonInputs, builders);

        Map<String, String> queryParamsMap = ParametersMapBuilder.getParamsMap(inputs);
        Map<String, String> headersMap = HeadersMapBuilder.getHeadersMap(inputs);
        setQueryApiParams(inputs, queryParamsMap);
        setQueryApiHeaders(inputs, headersMap, queryParamsMap);
        Map<String, String> response = new HttpClientService().execute(inputs.getHttpClientInputs());
        return getValidResponse(response);

    }
}
