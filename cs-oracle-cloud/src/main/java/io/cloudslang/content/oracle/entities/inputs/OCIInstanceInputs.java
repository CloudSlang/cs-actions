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

package io.cloudslang.content.oracle.entities.inputs;

import org.jetbrains.annotations.NotNull;

public class OCIInstanceInputs {
    private final String compartmentOcid;
    private final OCICommonInputs commonInputs;

    @java.beans.ConstructorProperties({"compartmentOcid","commonInputs"})

    public OCIInstanceInputs(String compartmentOcid, OCICommonInputs commonInputs){
        this.compartmentOcid = compartmentOcid;
        this.commonInputs = commonInputs;
    }
    @NotNull
    public static OCIInstanceInputsBuilder builder() {
        return new OCIInstanceInputsBuilder();
    }

    @NotNull
    public String getCompartmentOcid() {return compartmentOcid;}

    @NotNull
    public OCICommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class OCIInstanceInputsBuilder {
        private String compartmentOcid;
        private OCICommonInputs commonInputs;

        OCIInstanceInputsBuilder() {

        }

        @NotNull
        public OCIInstanceInputs.OCIInstanceInputsBuilder compartmentOcid(@NotNull final String compartmentOcid) {
            this.compartmentOcid = compartmentOcid;
            return this;
        }

        @NotNull
        public OCIInstanceInputsBuilder commonInputs(@NotNull final OCICommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public OCIInstanceInputs build() {
            return new OCIInstanceInputs(compartmentOcid, commonInputs);
        }

    }
}
