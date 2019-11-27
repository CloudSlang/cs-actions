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

public class CreateWorkspaceInputs {
    private final String workspaceName;
    private final String vcsRepoId;
    private final String oauthTokenId;
    private final String requestBody;
    private final TerraformCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"workspaceName", "vcsRepoId", "oauthTokenId", "requestBody", "commonInputs"})
    public CreateWorkspaceInputs(String workspaceName, String vcsRepoId, String oauthTokenId, String requestBody,
                                 TerraformCommonInputs commonInputs) {
        this.workspaceName = workspaceName;
        this.vcsRepoId = vcsRepoId;
        this.oauthTokenId = oauthTokenId;
        this.requestBody = requestBody;
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
    public String getVcsRepoId() {
        return vcsRepoId;
    }

    @NotNull
    public String getOauthTokenId() {
        return oauthTokenId;
    }

    @NotNull
    public String getRequestBody() {
        return requestBody;
    }


    @NotNull
    public TerraformCommonInputs getCommonInputs() {
        return this.commonInputs;
    }


    public static class CreateWorkspaceInputsBuilder {
        private String workspaceName;
        private String vcsRepoId;
        private String oauthTokenId;
        private String requestBody;
        private TerraformCommonInputs commonInputs;

        CreateWorkspaceInputsBuilder() {

        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder workspaceName(@NotNull final String workspaceName) {
            this.workspaceName = workspaceName;
            return this;
        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder vcsRepoId(@NotNull final String vcsRepoId) {
            this.vcsRepoId = vcsRepoId;
            return this;
        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder oauthTokenId(@NotNull final String oauthTokenId) {
            this.oauthTokenId = oauthTokenId;
            return this;
        }

        @NotNull
        public CreateWorkspaceInputs.CreateWorkspaceInputsBuilder requestBody(@NotNull final String requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public CreateWorkspaceInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public CreateWorkspaceInputs build() {
            return new CreateWorkspaceInputs(workspaceName, vcsRepoId, oauthTokenId, requestBody, commonInputs);
        }

    }

}
