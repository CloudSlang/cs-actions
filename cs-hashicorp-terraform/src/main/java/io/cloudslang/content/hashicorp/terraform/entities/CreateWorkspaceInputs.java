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

public class CreateWorkspaceInputs {

    public static final String WORKSPACE_NAME = "workspaceName";
    public static final String WORKSPACE_DESCRIPTION = "workspaceDescription";
    public static final String VCS_REPO_ID = "vcsRepoId";
    public static final String VCS_BRANCH_NAME = "vcsBranchName";
    public static final String VCS_DEFAULT_BRANCH = "vcsDefaultBranch";

    private final String workspaceName;
    private final String workspaceDescription;
    private final String vcsRepoId;
    private final String vcsBranchName;
    private final String  isDefaultBranch;
    private final String oauthTokenId;
    private final Inputs commonInputs;

    @java.beans.ConstructorProperties({"workspaceName", "workspaceDescription", "vcsRepoId", "vcsBranchName", "isDefaultBranch", "oauthTokenId", "commonInputs"})
    public CreateWorkspaceInputs(String workspaceName, String workspaceDescription, String vcsRepoId, String vcsBranchName, String  isDefaultBranch, String oauthTokenId,
                                 Inputs commonInputs) {
        this.workspaceName = workspaceName;
        this.workspaceDescription = workspaceDescription;
        this.vcsRepoId = vcsRepoId;
        this.vcsBranchName = vcsBranchName;
        this.isDefaultBranch = isDefaultBranch;
        this.oauthTokenId = oauthTokenId;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static CreateWorkspaceInputsBuilder builder() {
        return new CreateWorkspaceInputsBuilder();
    }

    @NotNull
    public String getWorkspaceName() {
        return workspaceName;
    }

    @NotNull
    public String getWorkspaceDescription() {
        return workspaceDescription;
    }

    @NotNull
    public String getVcsRepoId() {
        return vcsRepoId;
    }

    @NotNull
    public String getVcsBranch() {
        return vcsBranchName;
    }

    public String isDefaultBranch() {
        return isDefaultBranch;
    }

    @NotNull
    public String getOauthTokenId() {
        return oauthTokenId;
    }

    @NotNull
    public Inputs getCommonInputs() {
        return this.commonInputs;
    }


    public static class CreateWorkspaceInputsBuilder {
        private String workspaceName;
        private String workspaceDescription;
        private String vcsRepoId;
        private String vcsBranchName;
        private String isDefaultBranch;
        private String oauthTokenId;
        private Inputs commonInputs;

        CreateWorkspaceInputsBuilder() {

        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder workspaceName(@NotNull final String workspaceName) {
            this.workspaceName = workspaceName;
            return this;
        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder workspaceDescription(@NotNull final String workspaceDescription) {
            this.workspaceDescription = workspaceDescription;
            return this;
        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder vcsRepoId(@NotNull final String vcsRepoId) {
            this.vcsRepoId = vcsRepoId;
            return this;
        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder vcsBranchName(@NotNull final String vcsBranchName) {
            this.vcsBranchName = vcsBranchName;
            return this;
        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder isDefaultBranch(@NotNull final String  isDefaultBranch) {
            this.isDefaultBranch = isDefaultBranch;
            return this;
        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder oauthTokenId(@NotNull final String oauthTokenId) {
            this.oauthTokenId = oauthTokenId;
            return this;
        }

        public CreateWorkspaceInputsBuilder commonInputs(@NotNull final Inputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public CreateWorkspaceInputs build() {
            return new CreateWorkspaceInputs(workspaceName, workspaceDescription, vcsRepoId, vcsBranchName, isDefaultBranch, oauthTokenId, commonInputs);
        }

    }

}
