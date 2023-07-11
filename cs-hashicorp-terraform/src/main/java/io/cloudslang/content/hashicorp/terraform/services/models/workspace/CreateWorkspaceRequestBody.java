

package io.cloudslang.content.hashicorp.terraform.services.models.workspace;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CreateWorkspaceRequestBody {

    CreateWorkspaceData data;


    public CreateWorkspaceData getData() {
        return data;
    }

    public void setData(CreateWorkspaceData data) {
        this.data = data;
    }

    public class CreateWorkspaceData {
        Attributes attributes;
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Attributes getAttributes() {
            return attributes;
        }

        public void setAttributes(Attributes attributes) {
            this.attributes = attributes;
        }


    }

    public class Attributes {
        String terraform_version;
        String description;
        @JsonProperty("auto-apply")
        boolean autoApply;
        @JsonProperty("file-triggers-enabled")
        boolean fileTriggersEnabled;
        @JsonProperty("working-directory")
        String workingDirectory;
        @JsonProperty("trigger-prefixes")
        List<String> triggerPrefixes;
        @JsonProperty("queue-all-runs")
        boolean queueAllRuns;
        @JsonProperty("speculative-enabled")
        boolean speculativeEnabled;
        @JsonProperty("vcs-repo")
        VCSRepo vcsRepo;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTerraform_version() {
            return terraform_version;
        }

        public void setTerraform_version(String terraform_version) {
            this.terraform_version = terraform_version;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public VCSRepo getVcsRepo() {
            return vcsRepo;
        }

        public void setVcsRepo(VCSRepo vcsRepo) {
            this.vcsRepo = vcsRepo;
        }

        public boolean getAutoApply() {
            return autoApply;
        }

        public void setAutoApply(boolean autoApply) {
            this.autoApply = autoApply;
        }

        public boolean getFileTriggersEnabled() {
            return fileTriggersEnabled;
        }

        public void setFileTriggersEnabled(boolean fileTriggersEnabled) {
            this.fileTriggersEnabled = fileTriggersEnabled;
        }

        public String getWorkingDirectory() {
            return workingDirectory;
        }

        public void setWorkingDirectory(String workingDirectory) {
            this.workingDirectory = workingDirectory;
        }

        public List<String> getTriggerPrefixes() {
            return triggerPrefixes;
        }

        public void setTriggerPrefixes(List<String> triggerPrefixes) {
            this.triggerPrefixes = triggerPrefixes;
        }

        public boolean getQueueAllRuns() {
            return queueAllRuns;
        }

        public void setQueueAllRuns(boolean queueAllRuns) {
            this.queueAllRuns = queueAllRuns;
        }

        public boolean getSpeculativeEnabled() {
            return speculativeEnabled;
        }

        public void setSpeculativeEnabled(boolean speculativeEnabled) {
            this.speculativeEnabled = speculativeEnabled;
        }

    }

    public class VCSRepo {

        String identifier;
        @JsonProperty("oauth-token-id")
        String oauthTokenId;
        String branch;
        @JsonProperty("ingress-submodules")
        boolean ingressSubmodules;

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getOauthTokenId() {
            return oauthTokenId;
        }

        public void setOauthTokenId(String oauthTokenId) {
            this.oauthTokenId = oauthTokenId;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public boolean getIngressSubmodules() {
            return ingressSubmodules;
        }

        public void setIngressSubmodules(boolean ingressSubmodules) {
            this.ingressSubmodules = ingressSubmodules;
        }
    }
}
