package io.cloudslang.content.hashicorp.terraform.entities;

import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import org.jetbrains.annotations.NotNull;

public class GetWorkspaceDetailsInputs {

    private final String workspaceName;
    private final Inputs commonInputs;

    @java.beans.ConstructorProperties({"workspaceName", "commonInputs"})
    public GetWorkspaceDetailsInputs(String workspaceName, Inputs commonInputs) {
        this.workspaceName = workspaceName;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static GetWorkspaceDetailsInputs.GetWorkspaceDetailsInputsBuilder builder() {
        return new GetWorkspaceDetailsInputs.GetWorkspaceDetailsInputsBuilder();
    }

    @NotNull
    public String getWorkspaceName() {
        return workspaceName;
    }

    @NotNull
    public Inputs getCommonInputs() {
        return this.commonInputs;
    }


    public static class GetWorkspaceDetailsInputsBuilder {
        private String workspaceName;
        private Inputs commonInputs;

        GetWorkspaceDetailsInputsBuilder() {

        }

        @NotNull
        public GetWorkspaceDetailsInputs.GetWorkspaceDetailsInputsBuilder workspaceName(@NotNull final String workspaceName) {
            this.workspaceName = workspaceName;
            return this;
        }

               public GetWorkspaceDetailsInputs.GetWorkspaceDetailsInputsBuilder commonInputs(@NotNull final Inputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public GetWorkspaceDetailsInputs build() {
            return new GetWorkspaceDetailsInputs(workspaceName, commonInputs);
        }

    }

}
