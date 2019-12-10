package io.cloudslang.content.hashicorp.terraform.services.models.variables;

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
        @JsonProperty("id")
        String variableId;
        String type;

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

        public String getVariableId() {
            return variableId;
        }

        public void setVariableId(String variableId) {
            this.variableId = variableId;
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
