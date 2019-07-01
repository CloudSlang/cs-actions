/*
 * (c) Copyright 2019 Micro Focus, L.P.
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

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class GetCredentialFromManagerController {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String KEY = "key";
    private static final String VALUE = "value";

    @NotNull
    public static String getUsernameFromDataArray(@NotNull final JsonNode dataArray) {
        return getValueFromDataArray(dataArray, USERNAME);
    }

    @NotNull
    public static String getPasswordFromDataArray(@NotNull final JsonNode dataArray) {
        return getValueFromDataArray(dataArray, PASSWORD);
    }

    @NotNull
    public static String getValueFromDataArray(@NotNull final JsonNode dataArray, @NotNull final String keyName) {
        if (dataArray.isArray()) {
            for (final JsonNode nodeElement : dataArray) {
                if (nodeElement.get(KEY).asText(EMPTY).equalsIgnoreCase(keyName)) {
                    return nodeElement.get(VALUE).asText(EMPTY);
                }
            }
        }
        return EMPTY;
    }
}
