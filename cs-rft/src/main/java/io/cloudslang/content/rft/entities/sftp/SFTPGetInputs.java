/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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

import io.cloudslang.content.rft.actions.sftp.SFTPGet;
import io.cloudslang.content.rft.utils.Inputs;
import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SFTPGetInputs implements IHasFTPOperation {
    private final SFTPCommonInputs sftpCommonInputs;
    private final String remoteFile;
    private final String localLocation;

    @java.beans.ConstructorProperties({"sfptCommonInputs","remoteFile","localLocation"})

    public SFTPGetInputs(SFTPCommonInputs sftpCommonInputs, String remoteFile, String localLocation) {
        this.sftpCommonInputs = sftpCommonInputs;
        this.remoteFile = remoteFile;
        this.localLocation = localLocation;
    }

    @NotNull
    public static SFTPGetInputsBuilder builder(){
        return new SFTPGetInputsBuilder();
    }

    @NotNull
    public SFTPCommonInputs getSftpCommonInputs(){
        return this.sftpCommonInputs;
    }

    @NotNull
    public String getRemoteFile(){
        return remoteFile;
    }

    @NotNull
    public String getLocalLocation(){
        return localLocation;
    }

    public static class SFTPGetInputsBuilder{
        private SFTPCommonInputs sftpCommonInputs;
        private String remoteFile = EMPTY;
        private String localLocation = EMPTY;

        SFTPGetInputsBuilder(){
        }

        @NotNull
        public SFTPGetInputs.SFTPGetInputsBuilder sftpCommonInputs(@NotNull final SFTPCommonInputs sftpCommonInputs){
            this.sftpCommonInputs  = sftpCommonInputs;
            return this;
        }

        @NotNull
        public SFTPGetInputs.SFTPGetInputsBuilder remoteFile(@NotNull final String remoteFile){
            this.remoteFile = remoteFile;
            return this;
        }

        @NotNull
        public SFTPGetInputs.SFTPGetInputsBuilder localLocation(@NotNull final String localLocation){
            this.localLocation = localLocation;
            return this;
        }

        public SFTPGetInputs build(){
            return new SFTPGetInputs(sftpCommonInputs,remoteFile,localLocation);
        }


    }
}
