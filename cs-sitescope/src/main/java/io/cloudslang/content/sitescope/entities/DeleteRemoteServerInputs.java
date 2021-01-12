package io.cloudslang.content.sitescope.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class DeleteRemoteServerInputs {
    private String platform;
    private String remoteName;
    private SiteScopeCommonInputs commonInputs;

    private DeleteRemoteServerInputs(String platform, String remoteName, SiteScopeCommonInputs commonInputs) {
        this.platform = platform;
        this.remoteName = remoteName;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static DeleteRemoteServerInputs.DeleteRemoteServerInputsBuilder builder() {
        return new DeleteRemoteServerInputs.DeleteRemoteServerInputsBuilder();
    }

    @NotNull
    public String getPlatform() {
        return platform;
    }

    @NotNull
    public String getRemoteName() {
        return remoteName;
    }

    @NotNull
    public SiteScopeCommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class DeleteRemoteServerInputsBuilder {
        private String platform = EMPTY;
        private String remoteName = EMPTY;
        private SiteScopeCommonInputs commonInputs;

        public DeleteRemoteServerInputsBuilder() {

        }

        @NotNull
        public DeleteRemoteServerInputs.DeleteRemoteServerInputsBuilder platform(@NotNull final String platform) {
            this.platform = platform;
            return this;
        }

        @NotNull
        public DeleteRemoteServerInputs.DeleteRemoteServerInputsBuilder remoteName(@NotNull final String remoteName) {
            this.remoteName = remoteName;
            return this;
        }

        @NotNull
        public DeleteRemoteServerInputs.DeleteRemoteServerInputsBuilder commonInputs(@NotNull final SiteScopeCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public DeleteRemoteServerInputs build() {
            return new DeleteRemoteServerInputs(platform, remoteName, commonInputs);
        }
    }
}
