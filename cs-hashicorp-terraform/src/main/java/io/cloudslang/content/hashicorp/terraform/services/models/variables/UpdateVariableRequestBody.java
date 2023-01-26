/*
 * (c) Copyright 2022 Micro Focus, L.P.
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
 * limitations under the License .
 */
package io.cloudslang.content.hashicorp.terraform.services.models.variables;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateVariableRequestBody {
    @JsonProperty("data")
    UpdateVariableData data;

    public UpdateVariableData getData() {
        return data;
    }

    public void setData(UpdateVariableData data) {
        this.data = data;
    }

    public class UpdateVariableData {
        Attributes attributes;
        String type;
        String id;

        public Attributes getAttributes() {
            return attributes;
        }

        public void setAttributes(Attributes attributes) {
            this.attributes = attributes;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Attributes {
        String key;
        String value;
        String hcl;
        String sensitive;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getHcl() {
            return hcl;
        }

        public void setHcl(String hcl) {
            this.hcl = hcl;
        }

        public String getSensitive() {
            return sensitive;
        }

        public void setSensitive(String sensitive) {
            this.sensitive = sensitive;
        }

    }
}
