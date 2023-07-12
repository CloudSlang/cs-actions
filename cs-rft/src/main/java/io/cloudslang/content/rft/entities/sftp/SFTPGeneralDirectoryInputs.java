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

public class SFTPGeneralDirectoryInputs implements IHasFTPOperation {
    private final SFTPCommonInputs sftpCommonInputs;
    private final String remotePath;

    @java.beans.ConstructorProperties({SFTP_COMMON_INPUTS, REMOTE_PATH})
    public SFTPGeneralDirectoryInputs(SFTPCommonInputs sftpCommonInputs, String remotePath) {
        this.sftpCommonInputs = sftpCommonInputs;
        this.remotePath = remotePath;
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
    public String getRemotePath() {
        return remotePath;
    }
    

    public static class SFTPGeneralDirectoryInputsBuilder {
        private SFTPCommonInputs sftpCommonInputs;
        private String remotePath = EMPTY;

        SFTPGeneralDirectoryInputsBuilder() {
        }

        @NotNull
        public SFTPGeneralDirectoryInputs.SFTPGeneralDirectoryInputsBuilder sftpCommonInputs(@NotNull final SFTPCommonInputs sftpCommonInputs) {
            this.sftpCommonInputs = sftpCommonInputs;
            return this;
        }

        @NotNull
        public SFTPGeneralDirectoryInputs.SFTPGeneralDirectoryInputsBuilder remotePath(@NotNull final String remotePath) {
            this.remotePath = remotePath;
            return this;
        }

        public SFTPGeneralDirectoryInputs build(){
            return new SFTPGeneralDirectoryInputs(sftpCommonInputs, remotePath);

        }


    }

}
