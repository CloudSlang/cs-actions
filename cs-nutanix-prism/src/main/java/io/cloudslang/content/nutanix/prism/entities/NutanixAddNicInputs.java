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
package io.cloudslang.content.nutanix.prism.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NutanixAddNicInputs {
    private final String vmUUID;
    private final String networkUUID;
    private final String requestedIPAddress;
    private final String vlanId;
    private final String isConnected;

    private final NutanixCommonInputs commonInputs;



    @java.beans.ConstructorProperties({ "vmUUID","networkUUID", "requestedIPAddress","vlanId","isConnected","commonInputs"})
    public NutanixAddNicInputs(String vmUUID, String networkUUID, String requestedIPAddress, String vlanId,
                               String isConnected, NutanixCommonInputs commonInputs) {

        this.vmUUID = vmUUID;
        this.networkUUID = networkUUID;
        this.requestedIPAddress = requestedIPAddress;
        this.vlanId = vlanId;
        this.isConnected = isConnected;
        this.commonInputs = commonInputs;

    }

    public static NutanixAddNicInputsBuilder builder()
    {
        return new NutanixAddNicInputsBuilder();
    }

    public String getVmUUID() {
        return vmUUID;
    }

    @NotNull
    public String getNetworkUUID() {
        return networkUUID;
    }

    @NotNull
    public String getVlanid() { return vlanId; }

    @NotNull
    public String getRequestedIPAddress() {
        return requestedIPAddress;
    }

    @NotNull
    public String getIsConnected() {
        return isConnected;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class NutanixAddNicInputsBuilder
    {
        private String vmUUID = EMPTY;
        private String networkUUID = EMPTY;
        private String requestedIPAddress = EMPTY;
        private String vlanId = EMPTY;
        private String isConnected = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixAddNicInputsBuilder()
        {

        }

        @NotNull
        public NutanixAddNicInputs.NutanixAddNicInputsBuilder commonInputs(@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public NutanixAddNicInputs.NutanixAddNicInputsBuilder networkUUID(@NotNull final String networkUUID) {
            this.networkUUID = networkUUID;
            return this;
        }

        @NotNull
        public NutanixAddNicInputs.NutanixAddNicInputsBuilder vmUUID(@NotNull final String vmUUID) {
            this.vmUUID = vmUUID;
            return this;
        }

        @NotNull
        public NutanixAddNicInputs.NutanixAddNicInputsBuilder requestedIPAddress(@NotNull final String requestedIPAddress) {
            this.requestedIPAddress = requestedIPAddress;
            return this;
        }

        @NotNull
        public NutanixAddNicInputs.NutanixAddNicInputsBuilder vlanId(@NotNull final String vlanId) {
            this.vlanId = vlanId;
            return this;
        }


        @NotNull
        public NutanixAddNicInputs.NutanixAddNicInputsBuilder isConnected(@NotNull final String isConnected) {
            this.isConnected = isConnected;
            return this;
        }
        public NutanixAddNicInputs build() {
            return new NutanixAddNicInputs(vmUUID,
                    networkUUID, requestedIPAddress,vlanId, isConnected,commonInputs);
        }

    }


}
