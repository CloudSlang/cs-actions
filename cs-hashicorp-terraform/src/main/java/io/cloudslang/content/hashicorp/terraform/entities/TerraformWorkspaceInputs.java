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

public class TerraformWorkspaceInputs {

    private final String workspaceName;
    private final String workspaceDescription;
    private final String autoApply;
    private final String fileTriggersEnabled;
    private final String workingDirectory;
    private final String triggerPrefixes;
    private final String queueAllRuns;
    private final String speculativeEnabled;
    private final String ingressSubmodules;
    private final String vcsRepoId;
    private final String vcsBranchName;
    private final String oauthTokenId;
    private final TerraformCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"workspaceName", "workspaceDescription", "autoApply", "fileTriggersEnabled", "workingDirectory", "triggerPrefixes", "queueAllRuns",
            "speculativeEnabled", "ingressSubmodules", "vcsRepoId", "vcsBranchName", "oauthTokenId", "commonInputs"})
    public TerraformWorkspaceInputs(String workspaceName, String workspaceDescription, String autoApply, String fileTriggersEnabled, String workingDirectory, String triggerPrefixes, String queueAllRuns, String speculativeEnabled,
                                    String ingressSubmodules, String vcsRepoId, String vcsBranchName, String oauthTokenId, TerraformCommonInputs commonInputs) {
        this.workspaceName = workspaceName;
        this.workspaceDescription = workspaceDescription;
        this.autoApply = autoApply;
        this.fileTriggersEnabled = fileTriggersEnabled;
        this.workingDirectory = workingDirectory;
        this.triggerPrefixes = triggerPrefixes;
        this.queueAllRuns = queueAllRuns;
        this.speculativeEnabled = speculativeEnabled;
        this.ingressSubmodules = ingressSubmodules;
        this.vcsRepoId = vcsRepoId;
        this.vcsBranchName = vcsBranchName;
        this.oauthTokenId = oauthTokenId;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static TerraformWorkspaceInputsBuilder builder() {
        return new TerraformWorkspaceInputsBuilder();
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
    public String getAutoApply() {
        return autoApply;
    }

    @NotNull
    public String getFileTriggersEnabled() {
        return fileTriggersEnabled;
    }

    @NotNull
    public String getWorkingDirectory() {
        return workingDirectory;
    }

    @NotNull
    public String getTriggerPrefixes() {
        return triggerPrefixes;
    }

    @NotNull
    public String getQueueAllRuns() {
        return queueAllRuns;
    }

    @NotNull
    public String getSpeculativeEnabled() {
        return speculativeEnabled;
    }

    @NotNull
    public String getIngressSubmodules() {
        return ingressSubmodules;
    }

    @NotNull
    public String getVcsRepoId() {
        return vcsRepoId;
    }

    @NotNull
    public String getVcsBranch() {
        return vcsBranchName;
    }

    @NotNull
    public String getOauthTokenId() {
        return oauthTokenId;
    }

    @NotNull
    public TerraformCommonInputs getCommonInputs() {
        return this.commonInputs;
    }


    public static class TerraformWorkspaceInputsBuilder {
        private String workspaceName;
        private String workspaceDescription;
        private String autoApply;
        private String fileTriggersEnabled;
        private String workingDirectory;
        private String triggerPrefixes;
        private String queueAllRuns;
        private String speculativeEnabled;
        private String ingressSubmodules;
        private String vcsRepoId;
        private String vcsBranchName;
        private String oauthTokenId;
        private TerraformCommonInputs commonInputs;

        TerraformWorkspaceInputsBuilder() {

        }

        @NotNull
        public TerraformWorkspaceInputs.TerraformWorkspaceInputsBuilder workspaceName(@NotNull final String workspaceName) {
            this.workspaceName = workspaceName;
            return this;
        }

        @NotNull
        public TerraformWorkspaceInputs.TerraformWorkspaceInputsBuilder workspaceDescription(@NotNull final String workspaceDescription) {
            this.workspaceDescription = workspaceDescription;
            return this;
        }

        @NotNull
        public TerraformWorkspaceInputs.TerraformWorkspaceInputsBuilder autoApply(@NotNull final String autoApply) {
            this.autoApply = autoApply;
            return this;
        }

        @NotNull
        public TerraformWorkspaceInputs.TerraformWorkspaceInputsBuilder fileTriggersEnabled(@NotNull final String fileTriggersEnabled) {
            this.fileTriggersEnabled = fileTriggersEnabled;
            return this;
        }

        @NotNull
        public TerraformWorkspaceInputs.TerraformWorkspaceInputsBuilder workingDirectory(@NotNull final String workingDirectory) {
            this.workingDirectory = workingDirectory;
            return this;
        }

        @NotNull
        public TerraformWorkspaceInputs.TerraformWorkspaceInputsBuilder triggerPrefixes(@NotNull final String triggerPrefixes) {
            this.triggerPrefixes = triggerPrefixes;
            return this;
        }

        @NotNull
        public TerraformWorkspaceInputs.TerraformWorkspaceInputsBuilder queueAllRuns(@NotNull final String queueAllRuns) {
            this.queueAllRuns = queueAllRuns;
            return this;
        }

        @NotNull
        public TerraformWorkspaceInputs.TerraformWorkspaceInputsBuilder speculativeEnabled(@NotNull final String speculativeEnabled) {
            this.speculativeEnabled = speculativeEnabled;
            return this;
        }

        @NotNull
        public TerraformWorkspaceInputs.TerraformWorkspaceInputsBuilder ingressSubmodules(@NotNull final String ingressSubmodules) {
            this.ingressSubmodules = ingressSubmodules;
            return this;
        }

        @NotNull
        public TerraformWorkspaceInputs.TerraformWorkspaceInputsBuilder vcsRepoId(@NotNull final String vcsRepoId) {
            this.vcsRepoId = vcsRepoId;
            return this;
        }

        @NotNull
        public TerraformWorkspaceInputs.TerraformWorkspaceInputsBuilder vcsBranchName(@NotNull final String vcsBranchName) {
            this.vcsBranchName = vcsBranchName;
            return this;
        }

        @NotNull
        public TerraformWorkspaceInputs.TerraformWorkspaceInputsBuilder oauthTokenId(@NotNull final String oauthTokenId) {
            this.oauthTokenId = oauthTokenId;
            return this;
        }

        public TerraformWorkspaceInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public TerraformWorkspaceInputs build() {
            return new TerraformWorkspaceInputs(workspaceName, workspaceDescription, autoApply, fileTriggersEnabled, workingDirectory, triggerPrefixes, queueAllRuns, speculativeEnabled, ingressSubmodules, vcsRepoId, vcsBranchName, oauthTokenId, commonInputs);
        }

    }

}
