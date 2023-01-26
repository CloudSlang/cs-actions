/*
 * (c) Copyright 2021 Micro Focus
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.rft.entities.sftp;

import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.rft.utils.Constants.SFTP_COMMON_INPUTS;
import static io.cloudslang.content.rft.utils.Inputs.SFTPInputs.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SFTPCommandInputs implements IHasFTPOperation {
    private final SFTPCommonInputs sftpCommonInputs;
    private final String remotePath;
    private final String newRemotePath;
    private final String mode;
    private final String commandType;
    private final String uid;
    private final String gid;

    @java.beans.ConstructorProperties({SFTP_COMMON_INPUTS, REMOTE_PATH, MODE, COMMAND_TYPE, GID, UID})
    public SFTPCommandInputs(SFTPCommonInputs sftpCommonInputs, String remotePath,String newRemotePath, String mode, String commandType, String gid, String uid) {
        this.sftpCommonInputs = sftpCommonInputs;
        this.mode = mode;
        this.remotePath = remotePath;
        this.newRemotePath = newRemotePath;
        this.commandType = commandType;
        this.gid = gid;
        this.uid = uid;
    }

    @NotNull
    public static SFTPGeneralDirectoryInputsBuilder builder() {
        return new SFTPGeneralDirectoryInputsBuilder();
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
    public String getnewRemotePath() {
        return newRemotePath;
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

    public static class SFTPGeneralDirectoryInputsBuilder {
        private SFTPCommonInputs sftpCommonInputs;
        private String remotePath = EMPTY;
        private String newRemotePath = EMPTY;
        private String mode = EMPTY;
        private String commandType = EMPTY;
        private String gid = EMPTY;
        private String uid = EMPTY;

        SFTPGeneralDirectoryInputsBuilder() {
        }

        @NotNull
        public SFTPCommandInputs.SFTPGeneralDirectoryInputsBuilder sftpCommonInputs(@NotNull final SFTPCommonInputs sftpCommonInputs) {
            this.sftpCommonInputs = sftpCommonInputs;
            return this;
        }

        @NotNull
        public SFTPCommandInputs.SFTPGeneralDirectoryInputsBuilder remotePath(@NotNull final String remotePath) {
            this.remotePath = remotePath;
            return this;
        }

        @NotNull
        public SFTPCommandInputs.SFTPGeneralDirectoryInputsBuilder newRemotePath(@NotNull final String newRemotePath) {
            this.newRemotePath = newRemotePath;
            return this;
        }

        @NotNull
        public SFTPCommandInputs.SFTPGeneralDirectoryInputsBuilder mode(@NotNull final String mode) {
            this.mode = mode;
            return this;
        }

        @NotNull
        public SFTPCommandInputs.SFTPGeneralDirectoryInputsBuilder commandType(@NotNull final String commandType) {
            this.commandType = commandType;
            return this;
        }

        @NotNull
        public SFTPCommandInputs.SFTPGeneralDirectoryInputsBuilder gid(@NotNull final String gid) {
            this.gid = gid;
            return this;
        }

        @NotNull
        public SFTPCommandInputs.SFTPGeneralDirectoryInputsBuilder uid(@NotNull final String uid) {
            this.uid = uid;
            return this;
        }

        public SFTPCommandInputs build(){
            return new SFTPCommandInputs(sftpCommonInputs,remotePath,newRemotePath ,mode, commandType, gid, uid);
        }


    }

}
