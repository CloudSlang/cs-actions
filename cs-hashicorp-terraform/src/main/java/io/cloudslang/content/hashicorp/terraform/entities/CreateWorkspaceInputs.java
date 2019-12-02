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
    public static final String AUTO_APPLY = "autoApply";
    public static final String FILE_TRIGGERS_ENABLED = "fileTriggersEnabled";
    public static final String WORKING_DIRECTORY = "workingDirectory";
    public static final String TRIGGER_PREFIXES = "triggerPrefixes";
    public static final String QUEUE_ALL_RUNS = "queueAllRuns";
    public static final String SPECULATIVE_ENABLED = "speculativeEnabled";
    public static final String INGRESS_SUBMODULES = "ingressSubmodules";
    public static final String VCS_REPO_ID = "vcsRepoId";
    public static final String VCS_BRANCH_NAME = "vcsBranchName";


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
    private final Inputs commonInputs;

    @java.beans.ConstructorProperties({"workspaceName", "workspaceDescription","autoApply","fileTriggersEnabled", "workingDirectory", "triggerPrefixes", "queueAllRuns",
            "speculativeEnabled","ingressSubmodules","vcsRepoId", "vcsBranchName", "oauthTokenId", "commonInputs"})
    public CreateWorkspaceInputs(String workspaceName, String workspaceDescription,String autoApply, String fileTriggersEnabled,String workingDirectory,String triggerPrefixes, String queueAllRuns,String speculativeEnabled,
                                 String ingressSubmodules,String vcsRepoId, String vcsBranchName, String oauthTokenId, Inputs commonInputs) {
        this.workspaceName = workspaceName;
        this.workspaceDescription = workspaceDescription;
        this.autoApply = autoApply;
        this.fileTriggersEnabled =fileTriggersEnabled;
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
    public Inputs getCommonInputs() {
        return this.commonInputs;
    }


    public static class CreateWorkspaceInputsBuilder {
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
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder autoApply(@NotNull final String autoApply) {
            this.autoApply = autoApply;
            return this;
        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder fileTriggersEnabled(@NotNull final String fileTriggersEnabled) {
            this.fileTriggersEnabled = fileTriggersEnabled;
            return this;
        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder workingDirectory(@NotNull final String workingDirectory) {
            this.workingDirectory = workingDirectory;
            return this;
        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder triggerPrefixes(@NotNull final String triggerPrefixes) {
            this.triggerPrefixes = triggerPrefixes;
            return this;
        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder queueAllRuns(@NotNull final String queueAllRuns) {
            this.queueAllRuns = queueAllRuns;
            return this;
        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder speculativeEnabled(@NotNull final String speculativeEnabled) {
            this.speculativeEnabled = speculativeEnabled;
            return this;
        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder ingressSubmodules(@NotNull final String ingressSubmodules) {
            this.ingressSubmodules = ingressSubmodules;
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
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder oauthTokenId(@NotNull final String oauthTokenId) {
            this.oauthTokenId = oauthTokenId;
            return this;
        }

        public CreateWorkspaceInputsBuilder commonInputs(@NotNull final Inputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public CreateWorkspaceInputs build() {
            return new CreateWorkspaceInputs(workspaceName, workspaceDescription,autoApply,fileTriggersEnabled,workingDirectory,triggerPrefixes,queueAllRuns,speculativeEnabled,ingressSubmodules,vcsRepoId, vcsBranchName, oauthTokenId, commonInputs);
        }

    }

}
