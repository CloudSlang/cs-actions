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

import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import org.jetbrains.annotations.NotNull;

public class GetRunDetailsInputs {

    public static final String RUN_ID = "runId";
    public static final String RUN_ID_DESC = "runIdDescription";

    private final String runId;
    private final Inputs commonInputs;

    @java.beans.ConstructorProperties({"runId", "commonInputs"})
    public GetRunDetailsInputs(String runId, Inputs commonInputs) {
        this.runId = runId;
        this.commonInputs = commonInputs;
    }


    @NotNull
    public static GetRunDetailsInputs.GetRunDetailsInputsBuilder builder() {
        return new GetRunDetailsInputs.GetRunDetailsInputsBuilder();
    }

    @NotNull
    public String getRunId() {
        return runId;
    }

    @NotNull
    public Inputs getCommonInputs() {
        return this.commonInputs;
    }


    public static class GetRunDetailsInputsBuilder {
        private String runId;
        private Inputs commonInputs;

        GetRunDetailsInputsBuilder() {

        }

        @NotNull
        public GetRunDetailsInputs.GetRunDetailsInputsBuilder runId(@NotNull final String runId) {
            this.runId = runId;
            return this;
        }

        public GetRunDetailsInputs.GetRunDetailsInputsBuilder commonInputs(@NotNull final Inputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public GetRunDetailsInputs build() {
            return new GetRunDetailsInputs(runId, commonInputs);
        }

    }
}
