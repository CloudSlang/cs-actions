package io.cloudslang.content.json.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class AddToArrayInput {
    private JsonArray array;
    private String element;
    private Integer index;


    private AddToArrayInput() {
    }


    public JsonArray getArray() {
        return array;
    }


    public String getElement() {
        return element;
    }


    public Integer getIndex() {
        return index;
    }


    public static class Builder {
        private String array;
        private String element;
        private String index;


        public Builder array(String array) {
            this.array = array;
            return this;
        }


        public Builder element(String element) {
            this.element = element;
            return this;
        }


        public Builder index(String index) {
            this.index = index;
            return this;
        }


        public @NotNull AddToArrayInput build() throws Exception {
            AddToArrayInput input = new AddToArrayInput();

            if (StringUtils.isNotBlank(this.array)) {
                input.array = new JsonParser().parse(this.array).getAsJsonArray();
            }

            input.element = this.element;

            if (StringUtils.isNotBlank(this.index)) {
                input.index = Integer.parseInt(this.index);
            }

            return input;
        }
    }
}
