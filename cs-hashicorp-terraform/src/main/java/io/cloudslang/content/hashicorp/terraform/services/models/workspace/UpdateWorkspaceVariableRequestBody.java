

package io.cloudslang.content.hashicorp.terraform.services.models.workspace;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateWorkspaceVariableRequestBody {
    @JsonProperty("data")
    UpdateWorkspaceVariableData data;

    public UpdateWorkspaceVariableData getData() {
        return data;
    }

    public void setData(UpdateWorkspaceVariableData data) {
        this.data = data;
    }

    public class UpdateWorkspaceVariableData {
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
