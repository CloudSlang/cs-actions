/*
 * (c) Copyright 2019 Micro Focus, L.P.
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

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class CreateRunInputs {
    private String workspaceId;
    private String runMessage;
    private String isDestroy;
    private String body;
    private TerraformCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"workspaceId", "runMessage", "isDestroy","body"})
    public CreateRunInputs(String workspaceId, String runMessage, String isDestroy,String body, TerraformCommonInputs commonInputs) {
        this.workspaceId = workspaceId;
        this.runMessage = runMessage;
        this.isDestroy = isDestroy;
        this.body = body;
        this.commonInputs = commonInputs;
    }

    public static CreateRunInputsBuilder builder() {
        return new CreateRunInputsBuilder();
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
    public String getBody() { return body; }

    @NotNull
    public TerraformCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class CreateRunInputsBuilder {
        private String workspaceId = StringUtils.EMPTY;
        private String runMessage = StringUtils.EMPTY;
        private String isDestroy = StringUtils.EMPTY;
        private String body = StringUtils.EMPTY;

        private TerraformCommonInputs commonInputs;

        CreateRunInputsBuilder() {
        }

        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder workspaceId(@NotNull final String workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }

        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder runMessage(@NotNull final String runMessage) {
            this.runMessage = runMessage;
            return this;
        }

        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder isDestroy(@NotNull final String isDestroy) {
            this.isDestroy = isDestroy;
            return this;
        }

        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder body(@NotNull final String body) {
            this.body = body;
            return this;
        }

        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public CreateRunInputs build() {
            return new CreateRunInputs(workspaceId, runMessage, isDestroy,body, commonInputs);
        }
    }

}
