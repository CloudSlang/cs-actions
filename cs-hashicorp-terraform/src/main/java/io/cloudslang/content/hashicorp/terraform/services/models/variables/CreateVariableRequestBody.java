/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.hashicorp.terraform.services.models.variables;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateVariableRequestBody {
    @JsonProperty("data")
    CreateVariableData data;


    public CreateVariableData getData() {
        return data;
    }

    public void setData(CreateVariableData data) {
        this.data = data;
    }


    public class CreateVariableData {
        Attributes attributes;
        Relationships relationships;
        String type;

        public Relationships getRelationships() {
            return relationships;
        }

        public void setRelationships(Relationships relationships) {
            this.relationships = relationships;
        }

        public Attributes getAttributes() {
            return attributes;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setAttributes(Attributes attributes) {
            this.attributes = attributes;
        }
    }


    public class Attributes {
        String key;
        String value;
        String category;
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

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
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

    public class Relationships {

        Workspace workspace;

        public Workspace getWorkspace() {
            return workspace;
        }

        public void setWorkspace(Workspace workspace) {
            this.workspace = workspace;
        }
    }


    public class Workspace {
        Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    public class Data {
        String id;
        String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
