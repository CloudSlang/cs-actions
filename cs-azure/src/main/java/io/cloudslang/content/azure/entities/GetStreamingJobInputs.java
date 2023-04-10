/*
 * (c) Copyright 2023 EntIT Software LLC, a Micro Focus company, L.P.
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

public class GetStreamingJobInputs {

    private final String expand;

    private final AzureCommonInputs azureCommonInputs;

    @java.beans.ConstructorProperties({"expand", "azureCommonInputs"})
    public GetStreamingJobInputs(String expand, AzureCommonInputs azureCommonInputs) {

        this.expand = expand;
        this.azureCommonInputs = azureCommonInputs;
    }

    @NotNull
    public static GetStreamingJobInputs.GetStreamingJobInputsBuilder builder() {
        return new GetStreamingJobInputs.GetStreamingJobInputsBuilder();
    }


    @NotNull
    public String getExpand() {
        return expand;
    }

    @NotNull
    public AzureCommonInputs getAzureCommonInputs(){
        return azureCommonInputs;
    }


    public static final class GetStreamingJobInputsBuilder {
        private String expand = EMPTY;;
        private AzureCommonInputs azureCommonInputs;



        private GetStreamingJobInputsBuilder() {
        }

        @NotNull
        public GetStreamingJobInputs.GetStreamingJobInputsBuilder expand(String expand) {
            this.expand = expand;
            return this;
        }

        @NotNull
        public GetStreamingJobInputs.GetStreamingJobInputsBuilder azureCommonInputs(AzureCommonInputs azureCommonInputs) {
            this.azureCommonInputs = azureCommonInputs;
            return this;
        }

        public GetStreamingJobInputs build() {

            return new GetStreamingJobInputs(expand, azureCommonInputs);
        }
    }
}
