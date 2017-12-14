/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.dropbox.entities.inputs;

import static io.cloudslang.content.dropbox.entities.dropbox.ApiVersion.getApiVersion;

/**
 * Created by TusaM
 * 5/26/2017.
 */
public class CommonInputs {
    private final String action;
    private final String accessToken;
    private final String api;
    private final String endpoint;
    private final String version;

    private CommonInputs(Builder builder) {
        this.action = builder.action;
        this.accessToken = builder.accessToken;
        this.api = builder.api;
        this.endpoint = builder.endpoint;
        this.version = builder.version;
    }

    public String getAction() {
        return action;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getApi() {
        return api;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getVersion() {
        return version;
    }

    public static class Builder {
        private String action;
        private String accessToken;
        private String api;
        private String endpoint;
        private String version;

        public CommonInputs build() {
            return new CommonInputs(this);
        }

        public Builder withAction(String inputValue) {
            action = inputValue;
            return this;
        }

        public Builder withAccessToken(String input) {
            accessToken = input;
            return this;
        }

        public Builder withApi(String inputValue) {
            api = inputValue;
            return this;
        }

        public Builder withEndpoint(String input) {
            endpoint = input;
            return this;
        }

        public Builder withVersion(String input) {
            version = getApiVersion(input);
            return this;
        }
    }
}