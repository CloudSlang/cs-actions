/*
 * Copyright 2021-2024 Open Text
 * This program and the accompanying materials
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


package io.cloudslang.content.json.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ArrayIteratorInput {
    private JsonArray array;
    private GlobalSessionObject<Map<String, Object>> globalSessionObject;


    private ArrayIteratorInput() {
    }


    public JsonArray getArray() {
        return array;
    }

    public static class Builder {
        private final JsonParser jsonParser = new JsonParser();

        private String array;


        public Builder array(String array) {
            this.array = array;
            return this;
        }


        public @NotNull ArrayIteratorInput build() throws Exception {
            ArrayIteratorInput input = new ArrayIteratorInput();

            if (StringUtils.isNotBlank(this.array)) {
                input.array = this.jsonParser.parse(this.array).getAsJsonArray();
            }

            return input;
        }
    }
}
