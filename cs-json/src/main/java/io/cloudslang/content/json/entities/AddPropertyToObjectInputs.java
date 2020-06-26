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

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class AddPropertyToObjectInputs {


    private final String jsonObject;
    private final String newPropertyName;
    private final String newPropertyValue;


    @java.beans.ConstructorProperties({"jsonObject", "newPropertyName", "newPropertyValue"})
    private AddPropertyToObjectInputs(String jsonObject, String newPropertyName, String newPropertyValue) {

        this.jsonObject = jsonObject;
        this.newPropertyName = newPropertyName;
        this.newPropertyValue = newPropertyValue;
    }


    @NotNull
    public static AddPropertyToObjectInputsBuilder builder() {
        return new AddPropertyToObjectInputsBuilder();
    }


    @NotNull
    public String getJsonObject() {
        return jsonObject;
    }


    @NotNull
    public String getNewPropertyName() {
        return newPropertyName;
    }


    @NotNull
    public String getNewPropertyValue() {
        return newPropertyValue;
    }

    public static class AddPropertyToObjectInputsBuilder {

        String jsonObject = EMPTY;
        String newPropertyName = EMPTY;
        String newPropertyValue = EMPTY;


        AddPropertyToObjectInputsBuilder() {
        }


        @NotNull
        public AddPropertyToObjectInputs.AddPropertyToObjectInputsBuilder jsonObject(@NotNull final String jsonObject) {
            this.jsonObject = jsonObject;
            return this;
        }


        @NotNull
        public AddPropertyToObjectInputs.AddPropertyToObjectInputsBuilder newPropertyName(@NotNull final String newPropertyName) {
            this.newPropertyName = newPropertyName;
            return this;
        }


        @NotNull
        public AddPropertyToObjectInputs.AddPropertyToObjectInputsBuilder newPropertyValue(@NotNull final String newPropertyValue) {
            this.newPropertyValue = newPropertyValue;
            return this;
        }


        public AddPropertyToObjectInputs build() {
            return new AddPropertyToObjectInputs(jsonObject, newPropertyName, newPropertyValue);
        }
    }
}
