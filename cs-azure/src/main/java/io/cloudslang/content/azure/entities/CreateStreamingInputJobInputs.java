/*
 * Copyright 2024 Open Text
 * This program and the accompanying materials
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

public class CreateStreamingInputJobInputs {
    private final String jobName;
    private final String inputName;
    private final String accountName;
    private final AzureCommonInputs azureCommonInputs;
    private final String accountKey;
    private final String sourceType;
    private final String containerName;

    @java.beans.ConstructorProperties({"jobName", "inputName", "accountName", "azureCommonInputs", "accountKey", "sourceType", "containerName"})
    public CreateStreamingInputJobInputs(String jobName, String inputName, String accountName, AzureCommonInputs azureCommonInputs, String accountKey, String sourceType, String containerName) {
        this.jobName = jobName;
        this.inputName = inputName;
        this.accountName = accountName;
        this.azureCommonInputs = azureCommonInputs;
        this.accountKey = accountKey;
        this.sourceType = sourceType;
        this.containerName = containerName;
    }

    @NotNull
    public static CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder builder() {
        return new CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder();
    }

    public String getContainerName() { return containerName; }

    public String getInputName() {
        return inputName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public String getJobName() {
        return jobName;
    }

    public String getAccountName() {
        return accountName;
    }

    public AzureCommonInputs getAzureCommonInputs() {
        return azureCommonInputs;
    }

    public static final class CreateStreamingInputJobInputsBuilder {

        private String jobName = EMPTY;
        private String inputName = EMPTY;
        private String accountName = EMPTY;
        private AzureCommonInputs azureCommonInputs;
        private String accountKey = EMPTY;
        private String sourceType = EMPTY;
        private String containerName = EMPTY;


        CreateStreamingInputJobInputsBuilder() {
        }

        @NotNull
        public CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder jobName(@NotNull final String jobName) {
            this.jobName = jobName;
            return this;
        }

        @NotNull
        public CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder containerName(@NotNull final String containerName) {
            this.containerName = containerName;
            return this;
        }

        @NotNull
        public CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder accountKey(@NotNull final String accountKey) {
            this.accountKey = accountKey;
            return this;

        }

        @NotNull
        public CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder inputName(@NotNull final String inputName) {
            this.inputName = inputName;
            return this;

        }

        @NotNull
        public CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder accountName(@NotNull final String accountName) {
            this.accountName = accountName;
            return this;

        }

        @NotNull
        public CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder azureCommonInputs(@NotNull final AzureCommonInputs azureCommonInputs) {
            this.azureCommonInputs = azureCommonInputs;
            return this;

        }

        @NotNull
        public CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder sourceType(@NotNull final String sourceType) {
            this.sourceType = sourceType;
            return this;

        }

        public CreateStreamingInputJobInputs build() {

            return new CreateStreamingInputJobInputs(jobName, inputName, accountName, azureCommonInputs, accountKey, sourceType,containerName);
        }
    }
}
