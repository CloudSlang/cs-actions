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
package io.cloudslang.content.json.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class GetArrayEntryInput {
    private JsonArray array;
    private int index;


    private GetArrayEntryInput() {

    }


    public @NotNull JsonArray getArray() {
        return array;
    }


    public int getIndex() {
        return index;
    }


    public static class Builder {
        private String array;
        private String index;


        public Builder array(String array) {
            this.array = array;
            return this;
        }


        public Builder index(String index) {
            this.index = index;
            return this;
        }


        public GetArrayEntryInput build() throws Exception {
            GetArrayEntryInput input = new GetArrayEntryInput();

            input.array = new JsonParser()
                    .parse(StringUtils.defaultString(this.array))
                    .getAsJsonArray();

            input.index = Integer.parseInt(this.index);

            return input;
        }
    }
}
