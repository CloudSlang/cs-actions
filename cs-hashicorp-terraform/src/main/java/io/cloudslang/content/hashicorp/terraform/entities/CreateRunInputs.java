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

import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CreateRunInputs {

    public  static final String RUN_MESSAGE="runMessage";
    public  static final String IS_DESTROY="isDestroy";


    private String workspaceId;
    private  String workspaceName;
    private String runMessage;
    private boolean isDestroy;
    private Inputs commonInputs;

    @java.beans.ConstructorProperties({"workspaceId","workspaceName", "runMessage", "isDestroy"})
    public CreateRunInputs(String workspaceId,String workspaceName, String runMessage, boolean isDestroy, Inputs commonInputs) {
        this.workspaceId = workspaceId;
        this.workspaceName=workspaceName;
        this.runMessage = runMessage;
        this.isDestroy = isDestroy;
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
    public String getWorkspaceName() { return workspaceName; }

    @NotNull
    public String getRunMessage() {
        return runMessage;
    }

    @NotNull
    public boolean isDestroy() { return isDestroy; }

    @NotNull
    public Inputs getCommonInputs() {
        return commonInputs;
    }

    public static class CreateRunInputsBuilder {
        private String workspaceId = EMPTY;
        private String workspaceName=EMPTY;
        private String runMessage = EMPTY;
        private boolean isDestroy = Boolean.FALSE;

        private Inputs commonInputs;

        CreateRunInputsBuilder() {
        }

        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder workspaceId(@NotNull final String workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }

        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder workspaceName(@NotNull final String workspaceName) {
            this.workspaceName = workspaceName;
            return this;
        }

        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder runMessage(@NotNull final String runMessage) {
            this.runMessage = runMessage;
            return this;
        }

        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder isDestroy(@NotNull final boolean isDestroy) {
            this.isDestroy = isDestroy;
            return this;
        }


        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder commonInputs(@NotNull final Inputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public CreateRunInputs build() {
            return new CreateRunInputs(workspaceId,workspaceName, runMessage, isDestroy, commonInputs);
        }
    }

}
