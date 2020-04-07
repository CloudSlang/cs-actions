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

package io.cloudslang.content.hashicorp.terraform.services.models.organization;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateOrganizationRequestBody {

    CreateOrganizationData data;


    public CreateOrganizationData getData() {
        return data;
    }

    public void setData(CreateOrganizationData data) {
        this.data = data;
    }

    public class CreateOrganizationData {
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
        String sessionTimeout;
        String sessionRemember;
        String description;
        @JsonProperty("cost-estimation")
        boolean costEstimationEnabled;
        @JsonProperty("owners-team-saml-role-id")
        String ownersTeamSamlRoleId;
        @JsonProperty("collaborator-auth-policy")
        String collaboratorAuthPolicy;

        private String name;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSessionTimeout() {
            return sessionTimeout;
        }

        public void setSessionTimeout(String sessionTimeout) {
            this.sessionTimeout = sessionTimeout;
        }

        public String getSessionRemember() {
            return sessionRemember;
        }

        public void setSessionRemember(String sessionRemember) {
            this.sessionRemember = sessionRemember;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean getCostEstimationEnabled() {
            return costEstimationEnabled;
        }

        public void setCostEstimationEnabled(boolean costEstimationEnabled) {
            this.costEstimationEnabled = costEstimationEnabled;
        }

        public String getCollaboratorAuthPolicy() {
            return collaboratorAuthPolicy;
        }

        public void setCollaboratorAuthPolicy(String collaboratorAuthPolicy) {
            this.collaboratorAuthPolicy = collaboratorAuthPolicy;
        }

        public String getOwnersTeamSamlRoleId() {
            return ownersTeamSamlRoleId;
        }

        public void setOwnersTeamSamlRoleId(String ownersTeamSamlRoleId) {
            this.ownersTeamSamlRoleId = ownersTeamSamlRoleId;
        }
    }
}
