/*
 * (c) Copyright 2022 Micro Focus
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
package io.cloudslang.content.httpclient.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.INVALID_STRING_ARRAY_INPUT_DESC;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class HttpClientService {

    public static Map<String, String> httpClientGet(HttpClientInputs httpClientInputs) {

        try {

            return null;

        } catch (Exception e) {
            Map<String, String> result = new HashMap<>();
            result.put(STATUS_CODE, EMPTY);
            result.put(RETURN_RESULT, INVALID_STRING_ARRAY_INPUT_DESC);
            result.put(EXCEPTION, e.toString());
            return result;
        }

    }
}
