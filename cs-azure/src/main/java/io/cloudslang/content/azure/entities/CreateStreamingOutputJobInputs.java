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


public class CreateStreamingOutputJobInputs {

    private final String jobName;
    private final String outputName;
    private final String accountName;
    private final AzureCommonInputs azureCommonInputs;
    private final String accountKey;

    public String getAccountKey() {
        return accountKey;
    }


    public String getJobName() {
        return jobName;
    }

    public String getOutputName() {
        return outputName;
    }

    public String getAccountName() {
        return accountName;
    }

    public AzureCommonInputs getAzureCommonInputs() {
        return azureCommonInputs;
    }



    @java.beans.ConstructorProperties({"jobName", "outputName", "accountName", "azureCommonInputs","accountKey"})
    public CreateStreamingOutputJobInputs(String jobName, String outputName, String accountName,AzureCommonInputs azureCommonInputs,String accountKey)
    {
        this.jobName = jobName;
        this.outputName = outputName;
        this.accountName = accountName;
        this.azureCommonInputs = azureCommonInputs;
        this.accountKey = accountKey;
    }

    @NotNull
    public static CreateStreamingOutputJobInputs.CreateStreamingOutputJobInputsBuilder builder() {
        return new CreateStreamingOutputJobInputs.CreateStreamingOutputJobInputsBuilder();
    }

    public static final class CreateStreamingOutputJobInputsBuilder{

        private  String jobName = EMPTY;
        private  String outputName = EMPTY;
        private  String accountName = EMPTY;
        private  AzureCommonInputs azureCommonInputs;
        private  String accountKey = EMPTY;


        CreateStreamingOutputJobInputsBuilder() {
        }

        @NotNull
        public CreateStreamingOutputJobInputsBuilder jobName(@NotNull final String jobName)
        {
            this.jobName = jobName;
            return this;

        }

        @NotNull
        public CreateStreamingOutputJobInputsBuilder accountKey(@NotNull final String accountKey)
        {
            this.accountKey = accountKey;
            return this;

        }

        @NotNull
        public CreateStreamingOutputJobInputsBuilder outputName(@NotNull final String outputName)
        {
            this.outputName = outputName;
            return this;

        }
        @NotNull
        public CreateStreamingOutputJobInputsBuilder accountName(@NotNull final String accountName)
        {
            this.accountName = accountName;
            return this;

        }
        @NotNull
        public CreateStreamingOutputJobInputsBuilder azureCommonInputs(@NotNull final AzureCommonInputs azureCommonInputs)
        {
            this.azureCommonInputs = azureCommonInputs;
            return this;

        }

        public CreateStreamingOutputJobInputs build(){

            return new CreateStreamingOutputJobInputs (jobName,outputName,accountName,azureCommonInputs,accountKey);
        }
    }

}
