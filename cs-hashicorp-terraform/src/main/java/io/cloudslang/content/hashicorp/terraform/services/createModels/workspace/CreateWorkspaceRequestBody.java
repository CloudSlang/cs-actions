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

package io.cloudslang.content.hashicorp.terraform.services.createModels.workspace;

import com.fasterxml.jackson.annotation.JsonProperty;

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
        boolean isDefaultBranch;

        public String getIdentifier() {
            return identifier;
        }

        public String getOauthTokenId() {
            return oauthTokenId;
        }

        public String getBranch() {
            return branch;
        }

        public boolean isDefaultBranch() {
            return isDefaultBranch;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public void setOauthTokenId(String oauthTokenId) {
            this.oauthTokenId = oauthTokenId;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public void setIsDefaultBranch(boolean isDefaultBranch) {
            this.isDefaultBranch = isDefaultBranch;
        }

    }
}
