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

public class TerraformWorkspaceVariableInputs {


    private final String workspaceVariableName;
    private final String workspaceVariableValue;
    private final String sensitiveWorkspaceVariableValue;
    private final String workspaceVariableId;
    private final String workspaceVariableCategory;
    private final String hcl;
    private final String workspaceId;
    private final String sensitive;
    private final String workspaceVariableJson;
    private final String sensitiveWorkspaceVariableJson;
    private final String sensitiveWorkspaceVariableRequestBody;
    private final TerraformCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"workspaceVariableName","workspaceVariableValue", "workspaceVariableId" , "sensitiveWorkspaceVariableValue", "workspaceVariableCategory", "hcl", "workspaceId", "sensitive", "workspaceVariableJson", "sensitiveWorkspaceVariableJson","sensitiveWorkspaceVariableRequestBody", "commonInputs"})
    public TerraformWorkspaceVariableInputs(String workspaceVariableName, String workspaceVariableValue, String workspaceVariableId, String sensitiveWorkspaceVariableValue, String workspaceVariableCategory, String hcl, String workspaceId, String sensitive, String workspaceVariableJson, String sensitiveWorkspaceVariableJson, String sensitiveWorkspaceVariableRequestBody,
                                   TerraformCommonInputs commonInputs) {

        this.workspaceVariableName = workspaceVariableName;
        this.workspaceVariableValue = workspaceVariableValue;
        this.sensitiveWorkspaceVariableValue = sensitiveWorkspaceVariableValue;
        this.workspaceVariableId = workspaceVariableId;
        this.workspaceVariableCategory = workspaceVariableCategory;
        this.hcl = hcl;
        this.workspaceId = workspaceId;
        this.sensitive = sensitive;
        this.workspaceVariableJson = workspaceVariableJson;
        this.sensitiveWorkspaceVariableJson = sensitiveWorkspaceVariableJson;
        this.sensitiveWorkspaceVariableRequestBody = sensitiveWorkspaceVariableRequestBody;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static TerraformWorkspaceVariableInputs.TerraformWorkspaceVariableInputsBuilder builder() {
        return new TerraformWorkspaceVariableInputs.TerraformWorkspaceVariableInputsBuilder();
    }

    @NotNull
    public String getWorkspaceVariableName() { return workspaceVariableName; }
    @NotNull
    public String getWorkspaceVariableValue() { return workspaceVariableValue; }

    @NotNull
    public String getSensitiveWorkspaceVariableValue() { return sensitiveWorkspaceVariableValue; }

    @NotNull
    public String getWorkspaceVariableId() { return workspaceVariableId; }

    @NotNull
    public String getWorkspaceVariableCategory() { return workspaceVariableCategory; }


    public String getHcl() { return hcl; }

    @NotNull
    public String getWorkspaceId() { return workspaceId; }

    public String getSensitive() { return sensitive; }

    @NotNull
    public String getWorkspaceVariableJson() { return workspaceVariableJson; }

    @NotNull
    public String getSensitiveWorkspaceVariableRequestBody() { return sensitiveWorkspaceVariableRequestBody; }

    @NotNull
    public String getSensitiveWorkspaceVariableJson() { return sensitiveWorkspaceVariableJson; }


    @NotNull
    public TerraformCommonInputs getCommonInputs() { return this.commonInputs; }


    public static class TerraformWorkspaceVariableInputsBuilder {

        private String workspaceVariableName;
        private String workspaceVariableValue;
        private String sensitiveWorkspaceVariableValue;
        private String workspaceVariableId;
        private String workspaceVariableCategory;
        private String hcl;
        private String workspaceId;
        private String sensitive;
        private String workspaceVariableJson;
        private String sensitiveWorkspaceVariableJson;
        private String sensitiveWorkspaceVariableRequestBody;
        private TerraformCommonInputs commonInputs;


        TerraformWorkspaceVariableInputsBuilder() {
        }

        @NotNull
        public TerraformWorkspaceVariableInputs.TerraformWorkspaceVariableInputsBuilder workspaceVariableName(@NotNull final String workspaceVariableName) {
            this.workspaceVariableName = workspaceVariableName;
            return this;
        }

        @NotNull
        public TerraformWorkspaceVariableInputs.TerraformWorkspaceVariableInputsBuilder workspaceVariableValue(@NotNull final String workspaceVariableValue) {
            this.workspaceVariableValue = workspaceVariableValue;
            return this;
        }


        @NotNull
        public TerraformWorkspaceVariableInputs.TerraformWorkspaceVariableInputsBuilder sensitiveWorkspaceVariableValue(@NotNull final String sensitiveWorkspaceVariableValue) {
            this.sensitiveWorkspaceVariableValue = sensitiveWorkspaceVariableValue;
            return this;
        }

        public TerraformWorkspaceVariableInputs.TerraformWorkspaceVariableInputsBuilder workspaceVariableId(@NotNull final String workspaceVariableId) {
            this.workspaceVariableId = workspaceVariableId;
            return this;
        }

        @NotNull

        public TerraformWorkspaceVariableInputs.TerraformWorkspaceVariableInputsBuilder workspaceVariableCategory(@NotNull final String workspaceVariableCategory) {
            this.workspaceVariableCategory = workspaceVariableCategory;
            return this;
        }


        public TerraformWorkspaceVariableInputs.TerraformWorkspaceVariableInputsBuilder hcl(final String hcl) {
            this.hcl = hcl;
            return this;
        }

        @NotNull
        public TerraformWorkspaceVariableInputs.TerraformWorkspaceVariableInputsBuilder workspaceId(@NotNull final String workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }


        public TerraformWorkspaceVariableInputs.TerraformWorkspaceVariableInputsBuilder sensitive(final String sensitive) {
            this.sensitive = sensitive;
            return this;
        }

        @NotNull
        public TerraformWorkspaceVariableInputs.TerraformWorkspaceVariableInputsBuilder workspaceVariableJson(@NotNull final String workspaceVariableJson) {
            this.workspaceVariableJson = workspaceVariableJson;
            return this;
        }

        @NotNull
        public TerraformWorkspaceVariableInputs.TerraformWorkspaceVariableInputsBuilder sensitiveWorkspaceVariableJson(final String sensitiveWorkspaceVariableJson) {
            this.sensitiveWorkspaceVariableJson = sensitiveWorkspaceVariableJson;
            return this;
        }

        @NotNull
        public TerraformWorkspaceVariableInputs.TerraformWorkspaceVariableInputsBuilder sensitiveWorkspaceVariableRequestBody(@NotNull final String sensitiveWorkspaceVariableRequestBody) {
            this.sensitiveWorkspaceVariableRequestBody = sensitiveWorkspaceVariableRequestBody;
            return this;
        }

        public TerraformWorkspaceVariableInputs.TerraformWorkspaceVariableInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public TerraformWorkspaceVariableInputs build() {

            return new TerraformWorkspaceVariableInputs(workspaceVariableName, workspaceVariableValue, workspaceVariableId,sensitiveWorkspaceVariableValue, workspaceVariableCategory, hcl, workspaceId, sensitive, workspaceVariableJson, sensitiveWorkspaceVariableJson,sensitiveWorkspaceVariableRequestBody, commonInputs);

        }

    }
}
