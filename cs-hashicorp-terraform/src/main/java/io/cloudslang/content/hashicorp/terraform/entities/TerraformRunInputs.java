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

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class TerraformRunInputs {

    private String workspaceId;
    private String runMessage;
    private String isDestroy;
    private String runId;
    private String runComment;
    private TerraformCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"workspaceId", "workspaceName", "runMessage", "isDestroy", "runId", "runComment"})
    public TerraformRunInputs(String workspaceId, String runMessage, String isDestroy, String runId, String runComment, TerraformCommonInputs commonInputs) {
        this.workspaceId = workspaceId;
        this.runMessage = runMessage;
        this.isDestroy = isDestroy;
        this.runId = runId;
        this.runComment = runComment;
        this.commonInputs = commonInputs;
    }

    public static TerraformRunInputsBuilder builder() {
        return new TerraformRunInputsBuilder();
    }

    @NotNull
    public String getWorkspaceId() {
        return workspaceId;
    }


    @NotNull
    public String getRunMessage() {
        return runMessage;
    }

    @NotNull
    public String getIsDestroy() {
        return isDestroy;
    }

    @NotNull
    public String getRunId() {
        return runId;
    }

    @NotNull
    public String getRunComment() {
        return runComment;
    }

    @NotNull
    public TerraformCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class TerraformRunInputsBuilder {
        private String workspaceId = EMPTY;
        private String runMessage = EMPTY;
        private String isDestroy = EMPTY;
        private String runId = EMPTY;
        private String runComment = EMPTY;

        private TerraformCommonInputs commonInputs;

        TerraformRunInputsBuilder() {
        }

        @NotNull
        public TerraformRunInputs.TerraformRunInputsBuilder workspaceId(@NotNull final String workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }

        @NotNull
        public TerraformRunInputs.TerraformRunInputsBuilder runMessage(@NotNull final String runMessage) {
            this.runMessage = runMessage;
            return this;
        }

        @NotNull
        public TerraformRunInputs.TerraformRunInputsBuilder isDestroy(@NotNull final String isDestroy) {
            this.isDestroy = isDestroy;
            return this;
        }

        @NotNull
        public TerraformRunInputs.TerraformRunInputsBuilder runId(@NotNull final String runId) {
            this.runId = runId;
            return this;
        }

        @NotNull
        public TerraformRunInputs.TerraformRunInputsBuilder runComment(@NotNull final String runComment) {
            this.runComment = runComment;
            return this;
        }


        @NotNull
        public TerraformRunInputs.TerraformRunInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public TerraformRunInputs build() {
            return new TerraformRunInputs(workspaceId, runMessage, isDestroy, runId, runComment, commonInputs);
        }
    }

}
