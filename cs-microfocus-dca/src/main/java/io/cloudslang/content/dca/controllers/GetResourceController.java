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
package io.cloudslang.content.dca.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.dca.utils.Constants.VALUE;
import static io.cloudslang.content.dca.utils.OutputNames.DNS_NAME;
import static io.cloudslang.content.dca.utils.OutputNames.NAME;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class GetResourceController {
    @NotNull
    public static String getDnsNameFromArrayNode(@NotNull final JsonNode node) {
        if (node.isArray()) {
            for (final JsonNode attribute : node) {
                if (attribute.get(NAME).asText().equalsIgnoreCase(DNS_NAME)) {
                    return attribute.get(VALUE).asText();
                }
            }
        }
        return EMPTY;
    }
}
