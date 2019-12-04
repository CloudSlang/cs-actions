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

public class ApplyRunInputs {


    private String runId;
    private String runComment;
    private TerraformCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"runId", "runComment"})
    public ApplyRunInputs(String runId, String runComment, TerraformCommonInputs commonInputs) {
        this.runId = runId;
        this.runComment = runComment;
        this.commonInputs = commonInputs;
    }

    public static ApplyRunInputs.ApplyRunInputsBuilder builder() {
        return new ApplyRunInputs.ApplyRunInputsBuilder();
    }

    @NotNull
    public String getRunId() {
        return runId;
    }

    @NotNull
    public String getRunComment() {
        return runComment;
    }

    @NotNull
    public TerraformCommonInputs getCommonInputs() {
        return commonInputs;
    }


    public static class ApplyRunInputsBuilder {
        private String runId = EMPTY;
        private String runComment = EMPTY;

        private TerraformCommonInputs commonInputs;

        ApplyRunInputsBuilder() {
        }

        @NotNull
        public ApplyRunInputs.ApplyRunInputsBuilder runId(@NotNull final String runId) {
            this.runId = runId;
            return this;
        }

        @NotNull
        public ApplyRunInputs.ApplyRunInputsBuilder runComment(@NotNull final String runComment) {
            this.runComment = runComment;
            return this;
        }


        @NotNull
        public ApplyRunInputs.ApplyRunInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public ApplyRunInputs build() {
            return new ApplyRunInputs(runId, runComment, commonInputs);
        }
    }


}
