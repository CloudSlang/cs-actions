/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.json.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cloudslang.content.json.entities.AddPropertyToObjectInputs;
import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.json.utils.Constants.AddPropertyToObject.ADD_PROPERTY_EXCEPTION;

public class AddPropertyToObjectImpl {

    @NotNull
    public static String addPropertyToObject(AddPropertyToObjectInputs addPropertyToObjectInputs, ObjectMapper objectMapper) throws Exception {
        JsonNode jsonRoot = objectMapper.readTree(addPropertyToObjectInputs.getJsonObject());
        if (!(jsonRoot instanceof ObjectNode)) {
            throw new Exception(ADD_PROPERTY_EXCEPTION);
        }
        ContainerNode jsonResult = ((ObjectNode) jsonRoot).put(addPropertyToObjectInputs.getNewPropertyName(),
                addPropertyToObjectInputs.getNewPropertyValue());
        if (jsonResult == null) {
            throw new Exception(ADD_PROPERTY_EXCEPTION);
        }
        return jsonResult.toString();
    }

}