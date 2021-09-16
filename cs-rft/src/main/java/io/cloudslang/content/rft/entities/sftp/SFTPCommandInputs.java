package io.cloudslang.content.rft.entities.sftp;

import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.rft.utils.Constants.SFTP_COMMON_INPUTS;
import static io.cloudslang.content.rft.utils.Inputs.SFTPInputs.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SFTPCommandInputs implements IHasFTPOperation {
    private final SFTPCommonInputs sftpCommonInputs;
    private final String remotePath;
    private final String mode;
    private final String commandType;
    private final String uid;
    private final String gid;

    @java.beans.ConstructorProperties({SFTP_COMMON_INPUTS, REMOTE_PATH, MODE, COMMAND_TYPE, GID, UID})
    public SFTPCommandInputs(SFTPCommonInputs sftpCommonInputs, String remotePath, String mode, String commandType, String gid, String uid) {
        this.sftpCommonInputs = sftpCommonInputs;
        this.mode = mode;
        this.remotePath = remotePath;
        this.commandType = commandType;
        this.gid = gid;
        this.uid = uid;
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

    @NotNull
    public String getCommandType() {
        return commandType;
    }

    @NotNull
    public String getUid() {
        return uid;
    }

    @NotNull
    public String getGid() {
        return gid;
    }

    public static class SFTPCommandInputsBuilder {
        private SFTPCommonInputs sftpCommonInputs;
        private String remotePath = EMPTY;
        private String mode = EMPTY;
        private String commandType = EMPTY;
        private String gid = EMPTY;
        private String uid = EMPTY;

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

        @NotNull
        public SFTPCommandInputs.SFTPCommandInputsBuilder commandType(@NotNull final String commandType) {
            this.commandType = commandType;
            return this;
        }

        @NotNull
        public SFTPCommandInputs.SFTPCommandInputsBuilder gid(@NotNull final String gid) {
            this.gid = gid;
            return this;
        }

        @NotNull
        public SFTPCommandInputs.SFTPCommandInputsBuilder uid(@NotNull final String uid) {
            this.uid = uid;
            return this;
        }

        public SFTPCommandInputs build(){
            return new SFTPCommandInputs(sftpCommonInputs,remotePath,mode, commandType, gid, uid);
        }


    }

}
