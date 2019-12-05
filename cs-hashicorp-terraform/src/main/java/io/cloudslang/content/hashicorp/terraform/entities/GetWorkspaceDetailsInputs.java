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

public class GetWorkspaceDetailsInputs {

    private final String workspaceName;
    private final TerraformCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"workspaceName", "commonInputs"})
    public GetWorkspaceDetailsInputs(String workspaceName, TerraformCommonInputs commonInputs) {
        this.workspaceName = workspaceName;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static GetWorkspaceDetailsInputs.GetWorkspaceDetailsInputsBuilder builder() {
        return new GetWorkspaceDetailsInputs.GetWorkspaceDetailsInputsBuilder();
    }

    @NotNull
    public String getWorkspaceName() {
        return workspaceName;
    }

    @NotNull
    public TerraformCommonInputs getCommonInputs() {
        return this.commonInputs;
    }


    public static class GetWorkspaceDetailsInputsBuilder {
        private String workspaceName;
        private TerraformCommonInputs commonInputs;

        GetWorkspaceDetailsInputsBuilder() {

        }

        @NotNull
        public GetWorkspaceDetailsInputs.GetWorkspaceDetailsInputsBuilder workspaceName(@NotNull final String workspaceName) {
            this.workspaceName = workspaceName;
            return this;
        }

               public GetWorkspaceDetailsInputs.GetWorkspaceDetailsInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public GetWorkspaceDetailsInputs build() {
            return new GetWorkspaceDetailsInputs(workspaceName, commonInputs);
        }

    }

}
