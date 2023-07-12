/*
 * Copyright 2021-2023 Open Text
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
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.json.utils.ExceptionMsgs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class GetArraySublistInput {
    private JsonArray array;
    private Integer fromIndex;
    private Integer toIndex;


    private GetArraySublistInput() {

    }


    public JsonArray getArray() {
        return array;
    }


    public int getFromIndex() {
        return fromIndex >= 0 ? fromIndex : array.size() + fromIndex;
    }


    public int getToIndex() {
        if(toIndex == null) {
            return array.size();
        }
        return toIndex >= 0 ? toIndex : array.size() + toIndex;
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

            if(StringUtils.isEmpty(this.array)) {
                throw new IllegalArgumentException(String.format(ExceptionMsgs.NULL_OR_EMPTY_INPUT, Constants.InputNames.ARRAY));
            }
            input.array = new JsonParser().parse(this.array).getAsJsonArray();

            input.fromIndex = Integer.parseInt(this.fromIndex);

            if (StringUtils.isNotEmpty(this.toIndex)) {
                input.toIndex = Integer.parseInt(this.toIndex);
            }

            return input;
        }
    }
}
