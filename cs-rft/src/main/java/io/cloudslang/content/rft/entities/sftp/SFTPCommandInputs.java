package io.cloudslang.content.rft.entities.sftp;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SFTPCommandInputs implements IHasFTPOperation {
    private final SFTPCommonInputs sftpCommonInputs;
    private final String remotePath;
    private final String mode;

    @java.beans.ConstructorProperties({"sfptCommonInputs", "remotePath", "mode"})
    public SFTPCommandInputs(SFTPCommonInputs sftpCommonInputs, String remotePath, String mode) {
        this.sftpCommonInputs = sftpCommonInputs;
        this.mode = mode;
        this.remotePath = remotePath;
    }

    @NotNull
    public static SFTPCommandInputsBuilder builder() {
        return new SFTPCommandInputsBuilder();
    }

    @NotNull
    public SFTPCommonInputs getSftpCommonInputs() {
        return this.sftpCommonInputs;
    }

    @NotNull
    public String getremotePath() {
        return remotePath;
    }

    @NotNull
    public String getMode() {
        return mode;
    }

    public static class SFTPCommandInputsBuilder {
        private SFTPCommonInputs sftpCommonInputs;
        private String remotePath = EMPTY;
        private String mode = EMPTY;

        SFTPCommandInputsBuilder() {
        }

        @NotNull
        public SFTPCommandInputs.SFTPCommandInputsBuilder sftpCommonInputs(@NotNull final SFTPCommonInputs sftpCommonInputs) {
            this.sftpCommonInputs = sftpCommonInputs;
            return this;
        }

        @NotNull
        public SFTPCommandInputs.SFTPCommandInputsBuilder remotePath(@NotNull final String remotePath) {
            this.remotePath = remotePath;
            return this;
        }

        @NotNull
        public SFTPCommandInputs.SFTPCommandInputsBuilder mode(@NotNull final String mode) {
            this.mode = mode;
            return this;
        }

        public SFTPCommandInputs build(){
            return new SFTPCommandInputs(sftpCommonInputs,remotePath,mode);
        }


    }

}
