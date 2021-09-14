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

public class CreateTransformationInputs {
    private final String transformationName;
    private final String query;
    private final String streamingUnits;
    private final AzureCommonInputs azureCommonInputs;

    @java.beans.ConstructorProperties({"transformationName", "query", "streamingUnits","azureCommonInputs"})
    public CreateTransformationInputs(String transformationName, String query, String streamingUnits, AzureCommonInputs azureCommonInputs) {
        this.transformationName = transformationName;
        this.query = query;
        this.streamingUnits = streamingUnits;
        this.azureCommonInputs = azureCommonInputs;
    }

    @NotNull
    public static CreateTransformationInputs.CreateTransformationInputsBuilder builder() {
        return new CreateTransformationInputs.CreateTransformationInputsBuilder();
    }

    @NotNull
    public String getTransformationName() {
        return transformationName;
    }

    @NotNull
    public String getQuery() {
        return query;
    }

    @NotNull
    public String getStreamingUnits() {
        return streamingUnits;
    }

    @NotNull
    public AzureCommonInputs getAzureCommonInputs(){
        return azureCommonInputs;
    }


    public static final class CreateTransformationInputsBuilder {
        private String transformationName = EMPTY;
        private  String query= EMPTY;
        private  String streamingUnits = EMPTY;
        private AzureCommonInputs azureCommonInputs;



        private CreateTransformationInputsBuilder() {
        }

        @NotNull
        public CreateTransformationInputs.CreateTransformationInputsBuilder transformationName(String transformationName) {
            this.transformationName = transformationName;
            return this;
        }

        @NotNull
        public CreateTransformationInputs.CreateTransformationInputsBuilder query(String query) {
            this.query = query;
            return this;
        }

        @NotNull
        public CreateTransformationInputs.CreateTransformationInputsBuilder streamingUnits(String streamingUnits) {
            this.streamingUnits = streamingUnits;
            return this;
        }

        @NotNull
        public CreateTransformationInputs.CreateTransformationInputsBuilder azureCommonInputs(AzureCommonInputs azureCommonInputs) {
            this.azureCommonInputs = azureCommonInputs;
            return this;
        }

        public CreateTransformationInputs build() {

            return new CreateTransformationInputs(transformationName, query, streamingUnits, azureCommonInputs);
        }

    }
}
