/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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


package io.cloudslang.content.json.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.utils.StringUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.json.utils.Constants.AddPropertyToObject.EMPTY_JSON;
import static io.cloudslang.content.json.utils.Constants.AddPropertyToObject.JSON_EXCEPTION;

public class InputsValidation {

    @NotNull
    public static List<String> verifyJsonObject(@NotNull final String jsonObject, @NotNull ObjectMapper objectMapper) {
        final List<String> exceptionMessages = new ArrayList<>();
        if (StringUtilities.isBlank(jsonObject)) {
            exceptionMessages.add(EMPTY_JSON);
        }
        try {
            JsonNode jsonRoot = objectMapper.readTree(jsonObject);
            if (jsonRoot == null || !(jsonRoot.isContainerNode() && jsonRoot.isObject())) {
                throw new Exception();
            }
        } catch (Exception exception) {
            exceptionMessages.add(JSON_EXCEPTION);
        }
        return exceptionMessages;

    }
}
