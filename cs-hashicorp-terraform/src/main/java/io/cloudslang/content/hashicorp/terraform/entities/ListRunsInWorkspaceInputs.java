package io.cloudslang.content.hashicorp.terraform.entities;

import org.jetbrains.annotations.NotNull;

public class ListRunsInWorkspaceInputs {

    private final String workspaceId;
    private final TerraformCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"workspaceId", "commonInputs"})
    public ListRunsInWorkspaceInputs(String workspaceId, TerraformCommonInputs commonInputs) {
        this.workspaceId = workspaceId;
        this.commonInputs = commonInputs;
    }


    @NotNull
    public static ListRunsInWorkspaceInputs.ListRunsInWorkspaceInputsBuilder builder() {
        return new ListRunsInWorkspaceInputs.ListRunsInWorkspaceInputsBuilder();
    }

    @NotNull
    public String getWorkspaceId() {
        return workspaceId;
    }

    @NotNull
    public TerraformCommonInputs getCommonInputs() {
        return this.commonInputs;
    }


    public static class ListRunsInWorkspaceInputsBuilder {
        private String workspaceId;
        private TerraformCommonInputs commonInputs;

        ListRunsInWorkspaceInputsBuilder() {

        }

        @NotNull
        public ListRunsInWorkspaceInputs.ListRunsInWorkspaceInputsBuilder workspaceId(@NotNull final String workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }

        public ListRunsInWorkspaceInputs.ListRunsInWorkspaceInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public ListRunsInWorkspaceInputs build() {
            return new ListRunsInWorkspaceInputs(workspaceId, commonInputs);
        }

    }
}
