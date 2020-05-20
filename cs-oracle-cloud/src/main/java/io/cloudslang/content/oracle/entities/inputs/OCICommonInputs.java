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

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class OCICommonInputs {

    private final String tenancyOcid;
    private final String userOcid;
    private final String fingerPrint;
    private final String privateKeyFilename;


    @java.beans.ConstructorProperties({"tenancyOcid", "userOcid", "fingerPrint", "privateKeyFilename"})
    private OCICommonInputs(String tenancyOcid, String userOcid, String fingerPrint, String privateKeyFilename) {
        this.tenancyOcid = tenancyOcid;
        this.userOcid = userOcid;
        this.fingerPrint = fingerPrint;
        this.privateKeyFilename = privateKeyFilename;
    }

    @NotNull
    public static OCICommonInputs.OCICommonInputsBuilder builder() {
        return new OCICommonInputs.OCICommonInputsBuilder();
    }

    @NotNull
    public String getTenancyOcid() {
        return tenancyOcid;
    }

    @NotNull
    public String getUserOcid() {
        return userOcid;
    }

    @NotNull
    public String getFingerPrint() {
        return fingerPrint;
    }

    @NotNull
    public String getPrivateKeyFilename() {
        return privateKeyFilename;
    }


    public static class OCICommonInputsBuilder {
        private String tenancyOcid = EMPTY;
        private String userOcid = EMPTY;
        private String fingerPrint = EMPTY;
        private String privateKeyFilename = EMPTY;

        OCICommonInputsBuilder() {
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder tenancyOcId(@NotNull final String tenancyOcId) {
            this.tenancyOcid = tenancyOcId;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder userOcid(@NotNull final String userOcid) {
            this.userOcid = userOcid;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder fingerPrint(@NotNull final String fingerPrint) {
            this.fingerPrint = fingerPrint;
            return this;
        }

        @NotNull
        public OCICommonInputs.OCICommonInputsBuilder privateKeyFilename(@NotNull final String privateKeyFilename) {
            this.privateKeyFilename = privateKeyFilename;
            return this;
        }



        public OCICommonInputs build() {
            return new OCICommonInputs(tenancyOcid, userOcid, fingerPrint, privateKeyFilename);
        }
    }
}