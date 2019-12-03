package io.cloudslang.content.hashicorp.terraform.entities;

import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import org.jetbrains.annotations.NotNull;

public class GetRunDetailsInputs {

    public static final String RUN_ID = "runId";
    public static final String RUN_ID_DESC = "runIdDescription";

    private final String runId;
    private final Inputs commonInputs;

    @java.beans.ConstructorProperties({"runId", "commonInputs"})
    public GetRunDetailsInputs(String runId, Inputs commonInputs) {
        this.runId = runId;
        this.commonInputs = commonInputs;
    }


    @NotNull
    public static GetRunDetailsInputs.GetRunDetailsInputsBuilder builder() {
        return new GetRunDetailsInputs.GetRunDetailsInputsBuilder();
    }

    @NotNull
    public String getRunId() {
        return runId;
    }

    @NotNull
    public Inputs getCommonInputs() {
        return this.commonInputs;
    }


    public static class GetRunDetailsInputsBuilder {
        private String runId;
        private Inputs commonInputs;

        GetRunDetailsInputsBuilder() {

        }

        @NotNull
        public GetRunDetailsInputs.GetRunDetailsInputsBuilder runId(@NotNull final String runId) {
            this.runId = runId;
            return this;
        }

        public GetRunDetailsInputs.GetRunDetailsInputsBuilder commonInputs(@NotNull final Inputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public GetRunDetailsInputs build() {
            return new GetRunDetailsInputs(runId, commonInputs);
        }

    }
}
