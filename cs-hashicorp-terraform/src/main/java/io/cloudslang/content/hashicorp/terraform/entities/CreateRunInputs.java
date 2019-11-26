package io.cloudslang.content.hashicorp.terraform.entities;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class CreateRunInputs {
    private String workspaceId;
    private String runMessage;
    private String isDestroy;
    private String body;
    private TerraformCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"workspaceId", "runMessage", "isDestroy"})
    public CreateRunInputs(String workspaceId, String runMessage, String isDestroy, TerraformCommonInputs commonInputs) {
        this.workspaceId = workspaceId;
        this.runMessage = runMessage;
        this.isDestroy = isDestroy;
        this.commonInputs = commonInputs;
    }

    public static CreateRunInputsBuilder builder() {
        return new CreateRunInputsBuilder();
    }

    @NotNull
    public String getWorkspaceId() {
        return workspaceId;
    }

    @NotNull
    public String getRunMessage() {
        return runMessage;
    }

    @NotNull
    public String getIsDestroy() {
        return isDestroy;
    }

    @NotNull
    public String getBody() {
        return body;
    }

    @NotNull
    public TerraformCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class CreateRunInputsBuilder {
        private String workspaceId = StringUtils.EMPTY;
        private String runMessage = StringUtils.EMPTY;
        private String isDestroy = StringUtils.EMPTY;
        private String body = StringUtils.EMPTY;

        private TerraformCommonInputs commonInputs;

        CreateRunInputsBuilder() {
        }

        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder workspaceId(@NotNull final String workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }

        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder runMessage(@NotNull final String runMessage) {
            this.runMessage = runMessage;
            return this;
        }

        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder isDestroy(@NotNull final String isDestroy) {
            this.isDestroy = isDestroy;
            return this;
        }

        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder body(@NotNull final String body) {
            this.body = body;
            return this;
        }

        @NotNull
        public CreateRunInputs.CreateRunInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public CreateRunInputs build() {
            return new CreateRunInputs(workspaceId, runMessage, isDestroy, commonInputs);
        }
    }

}
