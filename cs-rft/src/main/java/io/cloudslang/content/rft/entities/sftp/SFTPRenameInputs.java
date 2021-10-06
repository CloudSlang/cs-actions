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

public class SFTPRenameInputs implements IHasFTPOperation {
    private final SFTPCommonInputs sftpCommonInputs;
    private final String remotePath;
    private final String newRemotePath;

    @java.beans.ConstructorProperties({SFTP_COMMON_INPUTS, REMOTE_PATH,NEW_REMOTE_PATH})
    public SFTPRenameInputs(SFTPCommonInputs sftpCommonInputs, String remotePath, String newRemotePath) {
        this.sftpCommonInputs = sftpCommonInputs;
        this.remotePath = remotePath;
        this.newRemotePath = newRemotePath;
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
    public String getNewRemotePath() {
        return newRemotePath;
    }

    public static class SFTPRenameInputsBuilder {
        private SFTPCommonInputs sftpCommonInputs;
        private String remotePath = EMPTY;
        private String newRemotePath = EMPTY;

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
        public SFTPRenameInputs.SFTPRenameInputsBuilder newRemotePath(@NotNull final String newRemotePath) {
            this.newRemotePath = newRemotePath;
            return this;
        }

        public SFTPRenameInputs build() {
            return new SFTPRenameInputs(sftpCommonInputs, remotePath,newRemotePath);
        }

    }
}
