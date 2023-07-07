


package io.cloudslang.content.hashicorp.terraform.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class TerraformStateVersionInputs {

    private final String workspaceId;
    private final TerraformCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"workspaceId", "commonInputs"})
    public TerraformStateVersionInputs(String workspaceId, TerraformCommonInputs commonInputs) {
        this.workspaceId = workspaceId;
        this.commonInputs = commonInputs;
    }

    public static TerraformStateVersionInputs.TerraformStateVersionInputsBuilder builder() {
        return new TerraformStateVersionInputs.TerraformStateVersionInputsBuilder();
    }

    @NotNull
    public String getWorkspaceId() {
        return workspaceId;
    }

    @NotNull
    public TerraformCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class TerraformStateVersionInputsBuilder {
        private String workspaceId = EMPTY;
        private TerraformCommonInputs commonInputs;

        TerraformStateVersionInputsBuilder() {
        }

        @NotNull
        public TerraformStateVersionInputs.TerraformStateVersionInputsBuilder workspaceId(@NotNull final String workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }

        @NotNull
        public TerraformStateVersionInputs.TerraformStateVersionInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public TerraformStateVersionInputs build() {
            return new TerraformStateVersionInputs(workspaceId, commonInputs);
        }

    }
}
