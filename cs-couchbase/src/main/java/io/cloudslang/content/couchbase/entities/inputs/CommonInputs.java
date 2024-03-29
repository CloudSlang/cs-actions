/*
 * Copyright 2019-2024 Open Text
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




package io.cloudslang.content.couchbase.entities.inputs;

import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.COMMA;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

/**
 * Created by Mihai Tusa
 * 3/26/2017.
 */
public class CommonInputs {
    private final String action;
    private final String api;
    private final String endpoint;
    private final String delimiter;

    private CommonInputs(Builder builder) {
        this.action = builder.action;
        this.api = builder.api;
        this.endpoint = builder.endpoint;
        this.delimiter = builder.delimiter;
    }

    public String getAction() {
        return action;
    }

    public String getApi() {
        return api;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public static class Builder {
        private String action;
        private String api;
        private String endpoint;
        private String delimiter;

        public CommonInputs build() {
            return new CommonInputs(this);
        }

        public Builder withAction(String inputValue) {
            action = inputValue;
            return this;
        }

        public Builder withApi(String inputValue) {
            api = inputValue;
            return this;
        }

        public Builder withEndpoint(String inputValue) {
            endpoint = inputValue;
            return this;
        }

        public Builder withDelimiter(String inputValue) {
            delimiter = defaultIfBlank(inputValue, COMMA);
            return this;
        }
    }
}