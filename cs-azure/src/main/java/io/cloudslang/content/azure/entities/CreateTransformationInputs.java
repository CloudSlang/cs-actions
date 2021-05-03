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
