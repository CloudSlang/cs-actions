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

 public class TerraformOrganizationInputs {

     private final String organizationDescription;
     private final String email;
     private final String sessionTimeout;
     private final String sessionRemember;
     private final String collaboratorAuthPolicy;
     private final String costEstimationEnabled;
     private final String ownersTeamSamlRoleId;
     private final TerraformCommonInputs commonInputs;

     @java.beans.ConstructorProperties({"organizationDescription", "email", "sessionTimeout", "sessionRemember", "collaboratorAuthPolicy", "costEstimationEnabled", "ownersTeamSamlRoleId",
             "commonInputs"})
     public TerraformOrganizationInputs(String organizationDescription, String email, String sessionTimeout, String sessionRemember, String collaboratorAuthPolicy, String costEstimationEnabled, String ownersTeamSamlRoleId,
                                        TerraformCommonInputs commonInputs) {
         this.organizationDescription = organizationDescription;
         this.email = email;
         this.sessionTimeout = sessionTimeout;
         this.sessionRemember = sessionRemember;
         this.collaboratorAuthPolicy = collaboratorAuthPolicy;
         this.costEstimationEnabled = costEstimationEnabled;
         this.ownersTeamSamlRoleId = ownersTeamSamlRoleId;
         this.commonInputs = commonInputs;
     }

     @NotNull
     public static io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs.TerraformOrganizationInputsBuilder builder() {
         return new io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs.TerraformOrganizationInputsBuilder();
     }

     @NotNull
     public String getOrganizationDescription() {
         return organizationDescription;
     }

     @NotNull
     public String getEmail() {
         return email;
     }

     @NotNull
     public String getSessionTimeout() {
         return sessionTimeout;
     }

     @NotNull
     public String getSessionRemember() {
         return sessionRemember;
     }

     @NotNull
     public String getCollaboratorAuthPolicy() {
         return collaboratorAuthPolicy;
     }

     @NotNull
     public String getCostEstimationEnabled() {
         return costEstimationEnabled;
     }

     @NotNull
     public String getOwnersTeamSamlRoleId() {
         return ownersTeamSamlRoleId;
     }

     @NotNull
     public TerraformCommonInputs getCommonInputs() {
         return this.commonInputs;
     }


     public static class TerraformOrganizationInputsBuilder {
         private String organizationDescription;
         private String email;
         private String sessionTimeout;
         private String sessionRemember;
         private String collaboratorAuthPolicy;
         private String costEstimationEnabled;
         private String ownersTeamSamlRoleId;
         private TerraformCommonInputs commonInputs;

         TerraformOrganizationInputsBuilder() {

         }

         @NotNull
         public io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs.TerraformOrganizationInputsBuilder organizationDescription(@NotNull final String organizationDescription) {
             this.organizationDescription = organizationDescription;
             return this;
         }

         @NotNull
         public io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs.TerraformOrganizationInputsBuilder email(@NotNull final String email) {
             this.email = email;
             return this;
         }

         @NotNull
         public io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs.TerraformOrganizationInputsBuilder sessionTimeout(@NotNull final String sessionTimeout) {
             this.sessionTimeout = sessionTimeout;
             return this;
         }

         @NotNull
         public io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs.TerraformOrganizationInputsBuilder sessionRemember(@NotNull final String sessionRemember) {
             this.sessionRemember = sessionRemember;
             return this;
         }

         @NotNull
         public io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs.TerraformOrganizationInputsBuilder collaboratorAuthPolicy(@NotNull final String collaboratorAuthPolicy) {
             this.collaboratorAuthPolicy = collaboratorAuthPolicy;
             return this;
         }

         @NotNull
         public io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs.TerraformOrganizationInputsBuilder costEstimationEnabled(@NotNull final String costEstimationEnabled) {
             this.costEstimationEnabled = costEstimationEnabled;
             return this;
         }

         @NotNull
         public io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs.TerraformOrganizationInputsBuilder ownersTeamSamlRoleId(@NotNull final String ownersTeamSamlRoleId) {
             this.ownersTeamSamlRoleId = ownersTeamSamlRoleId;
             return this;
         }


         public io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs.TerraformOrganizationInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
             this.commonInputs = commonInputs;
             return this;
         }

         public io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs build() {
             return new io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs(organizationDescription, email, sessionTimeout, sessionRemember, collaboratorAuthPolicy, costEstimationEnabled, ownersTeamSamlRoleId, commonInputs);
         }

     }

 }

