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

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class TerraformStateVersionInputs {

    private final String workspaceId;
    private final TerraformCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"workspaceId", "commonInputs"})
    public TerraformStateVersionInputs(String workspaceId, TerraformCommonInputs commonInputs) {
        this.workspaceId = workspaceId;
        this.commonInputs = commonInputs;
    }

    public static TerraformStateVersionInputs.TerraformStateVersionInputsBuilder builder() {
        return new TerraformStateVersionInputs.TerraformStateVersionInputsBuilder();
    }

    @NotNull
    public String getWorkspaceId() {
        return workspaceId;
    }

    @NotNull
    public TerraformCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class TerraformStateVersionInputsBuilder {
        private String workspaceId = EMPTY;
        private TerraformCommonInputs commonInputs;

        TerraformStateVersionInputsBuilder() {
        }

        @NotNull
        public TerraformStateVersionInputs.TerraformStateVersionInputsBuilder workspaceId(@NotNull final String workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }

        @NotNull
        public TerraformStateVersionInputs.TerraformStateVersionInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public TerraformStateVersionInputs build() {
            return new TerraformStateVersionInputs(workspaceId, commonInputs);
        }

    }
}
