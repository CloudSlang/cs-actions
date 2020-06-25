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
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.json.utils.ExceptionMsgs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class MergeArraysInput {
    private JsonArray array1;
    private JsonArray array2;


    private MergeArraysInput() {

    }


    public JsonArray getArray1() {
        return array1;
    }


    public JsonArray getArray2() {
        return array2;
    }


    public static class Builder {
        private final JsonParser jsonParser = new JsonParser();
        private String array1;
        private String array2;


        public Builder array1(String array1) {
            this.array1 = array1;
            return this;
        }


        public Builder array2(String array2) {
            this.array2 = array2;
            return this;
        }


        public @NotNull MergeArraysInput build() throws Exception {
            MergeArraysInput input = new MergeArraysInput();

            if (StringUtils.isNotBlank(this.array1)) {
                try {
                    input.array1 = this.jsonParser.parse(this.array1).getAsJsonArray();
                } catch (Exception ex) {
                    String msg = String.format(ExceptionMsgs.EXCEPTION_WHILE_PARSING, Constants.InputNames.ARRAY1, ex.getMessage());
                    throw new Exception(msg, ex);
                }
            }

            if (StringUtils.isNotBlank(this.array2)) {
                try {
                    input.array2 = this.jsonParser.parse(this.array2).getAsJsonArray();
                } catch (Exception ex) {
                    String msg = String.format(ExceptionMsgs.EXCEPTION_WHILE_PARSING, Constants.InputNames.ARRAY2, ex.getMessage());
                    throw new Exception(msg, ex);
                }
            }

            return input;
        }
    }
}
