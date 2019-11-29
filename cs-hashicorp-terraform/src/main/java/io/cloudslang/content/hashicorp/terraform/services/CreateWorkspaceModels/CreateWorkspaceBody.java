package io.cloudslang.content.hashicorp.terraform.services.CreateWorkspaceModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateWorkspaceBody {

    CreateWorkspaceData data;


    public CreateWorkspaceData getData() {
        return data;
    }

    public void setData(CreateWorkspaceData data) {
        this.data = data;
    }

    public class CreateWorkspaceData {
        Attributes attributes;
        String type;

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
        String name;
        String terraform_version;
        String description;
        @JsonProperty("vcs-repo")
        VCSRepo vcsRepo;

        public String getName() {
            return name;
        }

        public String getTerraform_version() {
            return terraform_version;
        }

        public String getDescription() {
            return description;
        }

        public VCSRepo getVcsRepo() {
            return vcsRepo;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTerraform_version(String terraform_version) {
            this.terraform_version = terraform_version;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setVcsRepo(VCSRepo vcsRepo) {
            this.vcsRepo = vcsRepo;
        }

    }
    public class VCSRepo {

        String identifier;
        @JsonProperty("oauth-token-id")
        String oauthTokenId;
        String branch;
        @JsonProperty("default-branch")
        String isDefaultBranch;

        public String getIdentifier() {
            return identifier;
        }

        public String getOauthTokenId() {
            return oauthTokenId;
        }

        public String getBranch() {
            return branch;
        }

        public String getIsDefaultBranch() {
            return isDefaultBranch;
        }

        public void setIdentifier(String identifier) { this.identifier = identifier; }

        public void setOauthTokenId(String oauthTokenId) {
            this.oauthTokenId = oauthTokenId;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public void setIsDefaultBranch(String  isDefaultBranch) {
            this.isDefaultBranch = isDefaultBranch;
        }

    }
}
