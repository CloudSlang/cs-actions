/*
 * Copyright 2021-2023 Open Text
 * This program and the accompanying materials
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

public class SFTPRenameInputs implements IHasFTPOperation {
    private final SFTPCommonInputs sftpCommonInputs;
    private final String remotePath;
    private final String remoteFile;
    private final String newRemotePath;
    private final String newRemoteFile;

    @java.beans.ConstructorProperties({SFTP_COMMON_INPUTS, REMOTE_PATH,REMOTE_FILE,NEW_REMOTE_PATH,NEW_REMOTE_FILE})
    public SFTPRenameInputs(SFTPCommonInputs sftpCommonInputs, String remotePath,String remoteFile, String newRemotePath,String newRemoteFile) {
        this.sftpCommonInputs = sftpCommonInputs;
        this.remotePath = remotePath;
        this.remoteFile = remoteFile;
        this.newRemotePath = newRemotePath;
        this.newRemoteFile = newRemoteFile;
    }

    @NotNull
    public static SFTPRenameInputsBuilder builder() {
        return new SFTPRenameInputsBuilder();
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
    public String getRemoteFile() {
        return remoteFile;
    }

    @NotNull

    public String getNewRemotePath() {
        return newRemotePath;
    }


    @NotNull
    public String getNewRemoteFile() {
        return newRemoteFile;
    }

    public static class SFTPRenameInputsBuilder {
        private SFTPCommonInputs sftpCommonInputs;
        private String remotePath = EMPTY;
        private String remoteFile = EMPTY;
        private String newRemotePath = EMPTY;
        private String newRemoteFile = EMPTY;

        SFTPRenameInputsBuilder() {
        }

        @NotNull
        public SFTPRenameInputs.SFTPRenameInputsBuilder sftpCommonInputs(@NotNull final SFTPCommonInputs sftpCommonInputs) {
            this.sftpCommonInputs = sftpCommonInputs;
            return this;
        }

        @NotNull
        public SFTPRenameInputs.SFTPRenameInputsBuilder remotePath(@NotNull final String remotePath) {
            this.remotePath = remotePath;
            return this;
        }

        @NotNull
        public SFTPRenameInputs.SFTPRenameInputsBuilder remoteFile(@NotNull final String remoteFile) {
            this.remoteFile = remoteFile;
            return this;
        }

        @NotNull
        public SFTPRenameInputs.SFTPRenameInputsBuilder newRemotePath(@NotNull final String newRemotePath) {
            this.newRemotePath = newRemotePath;
            return this;
        }

        @NotNull
        public SFTPRenameInputs.SFTPRenameInputsBuilder newRemoteFile(@NotNull final String newRemoteFile) {
            this.newRemoteFile = newRemoteFile;
            return this;
        }

        public SFTPRenameInputs build() {
            return new SFTPRenameInputs(sftpCommonInputs, remotePath,remoteFile,newRemotePath,newRemoteFile);
        }

    }
}
