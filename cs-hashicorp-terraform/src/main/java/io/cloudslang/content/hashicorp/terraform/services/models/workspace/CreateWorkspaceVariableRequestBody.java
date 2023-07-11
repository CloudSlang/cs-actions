

package io.cloudslang.content.hashicorp.terraform.services.models.workspace;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateWorkspaceVariableRequestBody {
    @JsonProperty("data")
    CreateWorkspaceVariableData data;


    public CreateWorkspaceVariableData getData() {
        return data;
    }

    public void setData(CreateWorkspaceVariableData data) {
        this.data = data;
    }


    public class CreateWorkspaceVariableData {
        Attributes attributes;
        String type;

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


}
