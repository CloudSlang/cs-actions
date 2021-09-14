/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
/*
 * (c) Copyright 2021 Micro Focus, L.P.
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
package io.cloudslang.content.azure.entities;

import org.jetbrains.annotations.NotNull;

public class AzureComputeCommonInputs {

    private final AzureCommonInputs azureCommonInputs;
    private final String azureProtocol;
    private final String azureHost;
    private final String vmName;

    @java.beans.ConstructorProperties({"azureCommonInputs", "azureProtocol", "azureHost", "vmName"})
    public AzureComputeCommonInputs(AzureCommonInputs azureCommonInputs, String azureProtocol, String azureHost,String vmName) {
        this.azureCommonInputs = azureCommonInputs;
        this.azureProtocol = azureProtocol;
        this.azureHost = azureHost;
        this.vmName = vmName;

    }

    @NotNull
    public static AzureComputeCommonInputs.AzureComputeCommonInputsBuilder builder() {
        return new AzureComputeCommonInputs.AzureComputeCommonInputsBuilder();
    }
    public AzureCommonInputs getAzureCommonInputs() {
        return azureCommonInputs;
    }
    public String getAzureProtocol() {
        return azureProtocol;
    }

    public String getAzureHost() {
        return azureHost;
    }

    public String getVmName() {
        return vmName;
    }

    public static final class AzureComputeCommonInputsBuilder {
        private AzureCommonInputs azureCommonInputs;
        private String azureProtocol;
        private String azureHost;

        private String vmName;


        private AzureComputeCommonInputsBuilder() {
        }

        @NotNull
        public AzureComputeCommonInputs.AzureComputeCommonInputsBuilder azureCommonInputs(AzureCommonInputs azureCommonInputs) {
            this.azureCommonInputs = azureCommonInputs;
            return this;
        }


        @NotNull
        public AzureComputeCommonInputs.AzureComputeCommonInputsBuilder azureProtocol(String azureProtocol) {
            this.azureProtocol = azureProtocol;
            return this;
        }

        @NotNull
        public AzureComputeCommonInputs.AzureComputeCommonInputsBuilder azureHost(String azureHost) {
            this.azureHost = azureHost;
            return this;
        }


        @NotNull
        public AzureComputeCommonInputs.AzureComputeCommonInputsBuilder vmName(String vmName) {
            this.vmName = vmName;
            return this;
        }



        @NotNull
        public AzureComputeCommonInputs build() {
            return new AzureComputeCommonInputs(azureCommonInputs, azureProtocol, azureHost, vmName);
        }
    }
}
