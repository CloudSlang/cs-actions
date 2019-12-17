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

import org.jetbrains.annotations.NotNull;

public class TerraformVariableInputs {


    private final String sensitiveVariableName;
    private final String sensitiveVariableValue;
    private final String variableId;
    private final String variableCategory;
    private final String hcl;
    private final String workspaceId;
    private final String sensitive;
    private final TerraformCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"sensitiveVariableName", "sensitiveVariableValue", "variableCategory", "hcl", "workspaceId", "sensitive", "commonInputs"})
    public TerraformVariableInputs(String sensitiveVariableName, String sensitiveVariableValue, String variableCategory, String hcl, String workspaceId, String sensitive,
                                   TerraformCommonInputs commonInputs) {

        this.sensitiveVariableName = sensitiveVariableName;
        this.sensitiveVariableValue = sensitiveVariableValue;
        this.variableId = variableId;
        this.variableCategory = variableCategory;
        this.hcl = hcl;
        this.workspaceId = workspaceId;
        this.sensitive = sensitive;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static TerraformVariableInputs.TerraformVariableInputsBuilder builder() {
        return new TerraformVariableInputs.TerraformVariableInputsBuilder();
    }


    @NotNull
    public String getSensitiveVariableName() { return sensitiveVariableName; }

    @NotNull
    public String getSensitiveVariableValue() { return sensitiveVariableValue; }
  
    public String getVariableId() {
        return variableId;
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
    public TerraformCommonInputs getCommonInputs() {
        return this.commonInputs;
    }


    public static class TerraformVariableInputsBuilder {


        private String sensitiveVariableName;
        private String sensitiveVariableValue;  
        private String variableId;
        private String variableCategory;
        private String hcl;
        private String workspaceId;
        private String sensitive;
        private TerraformCommonInputs commonInputs;


        TerraformVariableInputsBuilder() {
        }


        @NotNull
        public TerraformVariableInputs.TerraformVariableInputsBuilder sensitiveVariableName(@NotNull final String sensitiveVariableName) {
            this.sensitiveVariableName = sensitiveVariableName;
            return this;
        }

        @NotNull
        public TerraformVariableInputs.TerraformVariableInputsBuilder sensitiveVariableValue(@NotNull final String sensitiveVariableValue) {
            this.sensitiveVariableValue = sensitiveVariableValue;
            return this;
        }

        public TerraformVariableInputs.TerraformVariableInputsBuilder variableId(@NotNull final String variableId) {
            this.variableId = variableId;
            return this;
        }

        @NotNull

        public TerraformVariableInputs.TerraformVariableInputsBuilder variableCategory(@NotNull final String variableCategory) {
            this.variableCategory = variableCategory;
            return this;
        }

        @NotNull
        public TerraformVariableInputs.TerraformVariableInputsBuilder hcl(@NotNull final String hcl) {
            this.hcl = hcl;
            return this;
        }

        @NotNull
        public TerraformVariableInputs.TerraformVariableInputsBuilder workspaceId(@NotNull final String workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }

        @NotNull
        public TerraformVariableInputs.TerraformVariableInputsBuilder sensitive(@NotNull final String sensitive) {
            this.sensitive = sensitive;
            return this;
        }

        public TerraformVariableInputs.TerraformVariableInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public TerraformVariableInputs build() {
          
            return new TerraformVariableInputs(sensitiveVariableName, variableId,sensitiveVariableValue, variableCategory, hcl, workspaceId, sensitive, commonInputs);

        }

    }
}
