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
        private String name;
        String terraform_version;
        String description;
        @JsonProperty("auto-apply")
        String autoApply;
        @JsonProperty("file-triggers-enabled")
        String fileTriggersEnabled;
        @JsonProperty("working-directory")
        String workingDirectory;
        @JsonProperty("trigger-prefixes")
        List<String> triggerPrefixes;
        @JsonProperty("queue-all-runs")
        String queueAllRuns;
        @JsonProperty("speculative-enabled")
        String speculativeEnabled;
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

        public String getAutoApply() {
            return autoApply;
        }

        public void setAutoApply(String autoApply) {
            this.autoApply = autoApply;
        }

        public String getFileTriggersEnabled() {
            return fileTriggersEnabled;
        }

        public void setFileTriggersEnabled(String fileTriggersEnabled) {
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

        public String getQueueAllRuns() {
            return queueAllRuns;
        }

        public void setQueueAllRuns(String queueAllRuns) {
            this.queueAllRuns = queueAllRuns;
        }

        public String getSpeculativeEnabled() {
            return speculativeEnabled;
        }

        public void setSpeculativeEnabled(String speculativeEnabled) {
            this.speculativeEnabled = speculativeEnabled;
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
        @JsonProperty("ingress-submodules")
        String ingressSubmodules;

        public String getIdentifier() {
            return identifier;
        }

        public String getOauthTokenId() {
            return oauthTokenId;
        }

        public String getBranch() {
            return branch;
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

        public String getIngressSubmodules() {
            return ingressSubmodules;
        }

        public void setIngressSubmodules(String ingressSubmodules) {
            this.ingressSubmodules = ingressSubmodules;
        }
    }
}
