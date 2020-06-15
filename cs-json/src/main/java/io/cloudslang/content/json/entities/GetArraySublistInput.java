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
import org.jetbrains.annotations.NotNull;

public class GetArraySublistInput {
    private JsonArray array;
    private String fromIndex;
    private String toIndex;


    private GetArraySublistInput() {

    }


    public JsonArray getArray() {
        return array;
    }

    public String getfromIndex() {
        return fromIndex;
    }

    public String gettoIndex() {
        return toIndex;
    }

    public static class Builder {
        private String array;
        private String fromIndex;
        private String toIndex;


        public Builder array(String array) {
            this.array = array;
            return this;
        }


        public Builder fromIndex(String fromIndex) {
            this.fromIndex = fromIndex;
            return this;
        }

        public Builder toIndex(String toIndex) {
            this.toIndex = toIndex;
            return this;
        }


        public @NotNull GetArraySublistInput build() throws Exception {
            GetArraySublistInput input = new GetArraySublistInput();

            input.array = new JsonParser().parse(this.array).getAsJsonArray();

            input.fromIndex = this.fromIndex;

            input.toIndex = this.toIndex;

            return input;
        }
    }
}
