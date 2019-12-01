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

package io.cloudslang.content.hashicorp.terraform.entities;

import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import org.jetbrains.annotations.NotNull;

public class CreateVariableInputs {
    public static final String VARIABLE_NAME = "variableName";
    public static final String VARIABLE_VALUE = "variableValue";
    public static final String VARIABLE_CATEGORY = "variableCategory";
    public static final String SENSITIVE = "sensitive";
    public static final String HCL = "hcl";
    public static final String VARIABLE_REQUEST_BODY = "body";


    private final String variableName;
    private final String variableValue;
    private final String variableCategory;

    private final String hcl;
    private final String workspaceId;
    private final String sensitive;
    private final Inputs commonInputs;

    @java.beans.ConstructorProperties({"variableName", "variableValue", "variableCategory", "hcl", "workspaceId", "sensitive", "commonInputs"})
    public CreateVariableInputs(String variableName, String variableValue, String variableCategory, String hcl, String workspaceId, String sensitive,
                                Inputs commonInputs) {

        this.variableName = variableName;
        this.variableValue = variableValue;
        this.variableCategory = variableCategory;
        this.hcl = hcl;
        this.workspaceId = workspaceId;
        this.sensitive = sensitive;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static CreateVariableInputs.CreateVariableInputsBuilder builder() {
        return new CreateVariableInputs.CreateVariableInputsBuilder();
    }


    @NotNull
    public String getVariableName() {
        return variableName;
    }

    @NotNull
    public String getVariableValue() {
        return variableValue;
    }

    @NotNull
    public String getVariableCategory() {
        return variableCategory;
    }

    public String getHcl() {
        return hcl;
    }

    @NotNull
    public String getWorkspaceId() {
        return workspaceId;
    }

    public String getSensitive() {
        return sensitive;
    }

    @NotNull
    public Inputs getCommonInputs() {
        return this.commonInputs;
    }


    public static class CreateVariableInputsBuilder {

        private String variableName;
        private String variableValue;
        private String variableCategory;
        private String hcl;
        private String workspaceId;
        private String sensitive;
        private Inputs commonInputs;

        CreateVariableInputsBuilder() {

        }


        @NotNull
        public CreateVariableInputs.CreateVariableInputsBuilder variableName(@NotNull final String variableName) {
            this.variableName = variableName;
            return this;
        }

        @NotNull
        public CreateVariableInputs.CreateVariableInputsBuilder variableValue(@NotNull final String variableValue) {
            this.variableValue = variableValue;
            return this;
        }

        @NotNull
        public CreateVariableInputs.CreateVariableInputsBuilder variableCategory(@NotNull final String variableCategory) {
            this.variableCategory = variableCategory;
            return this;
        }

        @NotNull
        public CreateVariableInputs.CreateVariableInputsBuilder hcl(@NotNull final String hcl) {
            this.hcl = hcl;
            return this;
        }

        @NotNull
        public CreateVariableInputs.CreateVariableInputsBuilder workspaceId(@NotNull final String workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }

        @NotNull
        public CreateVariableInputs.CreateVariableInputsBuilder sensitive(@NotNull final String sensitive) {
            this.sensitive = sensitive;
            return this;
        }

        public CreateVariableInputs.CreateVariableInputsBuilder commonInputs(@NotNull final Inputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public CreateVariableInputs build() {
            return new CreateVariableInputs(variableName, variableValue, variableCategory, hcl, workspaceId, sensitive, commonInputs);
        }

    }
}
