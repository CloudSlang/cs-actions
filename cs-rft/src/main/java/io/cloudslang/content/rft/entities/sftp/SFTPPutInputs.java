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

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SFTPPutInputs implements IHasFTPOperation {
    private final SFTPCommonInputs sftpCommonInputs;
    private final String remoteLocation;
    private final String localFile;

    public SFTPPutInputs(SFTPCommonInputs sftpCommonInputs, String remoteLocation, String localFile) {
        this.sftpCommonInputs = sftpCommonInputs;
        this.remoteLocation = remoteLocation;
        this.localFile = localFile;
    }

    @NotNull
    public static SFTPPutInputsBuilder builder(){
        return new SFTPPutInputsBuilder();
    }

    @NotNull
    public  SFTPCommonInputs getSftpCommonInputs() {
        return sftpCommonInputs;
    }

    @NotNull
    public String getRemoteLocation(){ return remoteLocation;}

    @NotNull
    public String getLocalFile(){ return localFile;}

    public static class SFTPPutInputsBuilder{
        private SFTPCommonInputs sftpCommonInputs;
        private String remoteLocation = EMPTY;
        private String localFile = EMPTY;

        SFTPPutInputsBuilder(){
        }

        @NotNull
        public SFTPPutInputs.SFTPPutInputsBuilder sftpCommonInputs(@NotNull final SFTPCommonInputs sftpCommonInputs){
            this.sftpCommonInputs  = sftpCommonInputs;
            return this;
        }

        @NotNull
        public SFTPPutInputs.SFTPPutInputsBuilder remoteLocation(@NotNull final String remoteLocation){
            this.remoteLocation = remoteLocation;
            return this;
        }

        @NotNull
        public SFTPPutInputs.SFTPPutInputsBuilder localFile(@NotNull final String localFile){
            this.localFile = localFile;
            return this;
        }

        public SFTPPutInputs build(){
            return new SFTPPutInputs(sftpCommonInputs,remoteLocation,localFile);
        }


    }
}
