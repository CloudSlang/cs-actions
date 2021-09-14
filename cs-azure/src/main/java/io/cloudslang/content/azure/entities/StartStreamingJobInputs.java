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

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class StartStreamingJobInputs {

    private final String outputStartMode;
    private final String outputStartTime;

    private final AzureCommonInputs azureCommonInputs;

    @java.beans.ConstructorProperties({"outputStartMode", "outputStartTime", "azureCommonInputs"})
    public StartStreamingJobInputs(String outputStartMode, String outputStartTime, AzureCommonInputs azureCommonInputs) {

        this.outputStartMode = outputStartMode;
        this.outputStartTime = outputStartTime;
        this.azureCommonInputs = azureCommonInputs;
    }

    @NotNull
    public static StartStreamingJobInputs.StartStreamingJobInputsBuilder builder() {
        return new StartStreamingJobInputs.StartStreamingJobInputsBuilder();
    }


    @NotNull
    public String getOutputStartMode() {
        return outputStartMode;
    }

    @NotNull
    public String getOutputStartTime() {
        return outputStartTime;
    }

    @NotNull
    public AzureCommonInputs getAzureCommonInputs(){
        return azureCommonInputs;
    }


    public static final class StartStreamingJobInputsBuilder {
        private String outputStartMode = EMPTY;;
        private String outputStartTime = EMPTY;
        private AzureCommonInputs azureCommonInputs;



        private StartStreamingJobInputsBuilder() {
        }

        @NotNull
        public StartStreamingJobInputsBuilder outputStartMode(String outputStartMode) {
            this.outputStartMode = outputStartMode;
            return this;
        }
        @NotNull
        public StartStreamingJobInputsBuilder outputStartTime(String outputStartTime) {
            this.outputStartTime = outputStartTime;
            return this;
        }
        @NotNull
        public StartStreamingJobInputsBuilder azureCommonInputs(AzureCommonInputs azureCommonInputs) {
            this.azureCommonInputs = azureCommonInputs;
            return this;
        }

        public StartStreamingJobInputs build() {

            return new StartStreamingJobInputs(outputStartMode, outputStartTime, azureCommonInputs);
        }
    }
}
