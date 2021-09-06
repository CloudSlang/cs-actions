package io.cloudslang.content.microsoftAD.entities;

import org.jetbrains.annotations.NotNull;

public class DeleteUserInputs {
    private final AzureActiveDirectoryCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"commonInputs"})
    public DeleteUserInputs(AzureActiveDirectoryCommonInputs commonInputs) {
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static DeleteUserInputsBuilder builder() {
        return new DeleteUserInputsBuilder();
    }

    @NotNull
    public AzureActiveDirectoryCommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class DeleteUserInputsBuilder {
        private AzureActiveDirectoryCommonInputs commonInputs;

        DeleteUserInputsBuilder() {
        }

        @NotNull
        public DeleteUserInputs.DeleteUserInputsBuilder commonInputs(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public DeleteUserInputs build() {
            return new DeleteUserInputs(commonInputs);
        }
    }
}
