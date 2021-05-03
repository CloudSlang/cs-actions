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



    @java.beans.ConstructorProperties({"jobName", "inputName", "accountName", "azureCommonInputs","accountKey","sourceType"})
    public CreateStreamingInputJobInputs(String jobName, String inputName, String accountName,AzureCommonInputs azureCommonInputs,String accountKey, String sourceType)
    {
        this.jobName = jobName;
        this.inputName = inputName;
        this.accountName = accountName;
        this.azureCommonInputs = azureCommonInputs;
        this.accountKey = accountKey;
        this.sourceType = sourceType;
    }

    @NotNull
    public static CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder builder() {
        return new CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder();
    }

    public static final class CreateStreamingInputJobInputsBuilder{

        private  String jobName = EMPTY;
        private  String inputName = EMPTY;
        private  String accountName = EMPTY;
        private  AzureCommonInputs azureCommonInputs;
        private  String accountKey = EMPTY;
        private String sourceType= EMPTY;


        CreateStreamingInputJobInputsBuilder()
        {

        }

        @NotNull
        public CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder jobName(@NotNull final String jobName)
        {
            this.jobName = jobName;
            return this;

        }

        @NotNull
        public CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder accountKey(@NotNull final String accountKey)
        {
            this.accountKey = accountKey;
            return this;

        }

        @NotNull
        public CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder inputName(@NotNull final String inputName)
        {
            this.inputName = inputName;
            return this;

        }
        @NotNull
        public CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder accountName(@NotNull final String accountName)
        {
            this.accountName = accountName;
            return this;

        }
        @NotNull
        public CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder azureCommonInputs(@NotNull final AzureCommonInputs azureCommonInputs)
        {
            this.azureCommonInputs = azureCommonInputs;
            return this;

        }

        @NotNull
        public CreateStreamingInputJobInputs.CreateStreamingInputJobInputsBuilder sourceType(@NotNull final String sourceType)
        {
            this.sourceType = sourceType;
            return this;

        }

        public CreateStreamingInputJobInputs build(){

            return new CreateStreamingInputJobInputs (jobName,inputName,accountName,azureCommonInputs,accountKey,sourceType);
        }
    }
}
