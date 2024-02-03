/*
 * Copyright 2021-2024 Open Text
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

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SFTPUploadFileInputs implements IHasFTPOperation {
    private final SFTPCommonInputs sftpCommonInputs;
    private final String remotePath;
    private final String localPath;
    private final String localFile;

    public SFTPUploadFileInputs(SFTPCommonInputs sftpCommonInputs, String remotePath, String localPath, String localFile) {
        this.sftpCommonInputs = sftpCommonInputs;
        this.remotePath = remotePath;
        this.localPath = localPath;
        this.localFile = localFile;
    }

    @NotNull
    public static SFTPPutInputsBuilder builder() {
        return new SFTPPutInputsBuilder();
    }

    @NotNull
    public SFTPCommonInputs getSftpCommonInputs() {
        return sftpCommonInputs;
    }

    @NotNull
    public String getRemotePath() {
        return remotePath;
    }

    @NotNull
    public String getLocalPath() {
        return localPath;
    }

    @NotNull
    public String getLocalFile() {
        return localFile;
    }

    public static class SFTPPutInputsBuilder {
        private SFTPCommonInputs sftpCommonInputs;
        private String remotePath = EMPTY;
        private String localPath = EMPTY;
        private String localFile = EMPTY;

        SFTPPutInputsBuilder() {
        }

        @NotNull
        public SFTPUploadFileInputs.SFTPPutInputsBuilder sftpCommonInputs(@NotNull final SFTPCommonInputs sftpCommonInputs) {
            this.sftpCommonInputs = sftpCommonInputs;
            return this;
        }

        @NotNull
        public SFTPUploadFileInputs.SFTPPutInputsBuilder remotePath(@NotNull final String remotePath) {
            this.remotePath = remotePath;
            return this;
        }

        @NotNull
        public SFTPUploadFileInputs.SFTPPutInputsBuilder localPath(@NotNull final String localPath) {
            this.localPath = localPath;
            return this;
        }

        @NotNull
        public SFTPUploadFileInputs.SFTPPutInputsBuilder localFile(@NotNull final String localFile) {
            this.localFile = localFile;
            return this;
        }

        public SFTPUploadFileInputs build() {
            return new SFTPUploadFileInputs(sftpCommonInputs, remotePath, localPath, localFile);
        }

    }
}
