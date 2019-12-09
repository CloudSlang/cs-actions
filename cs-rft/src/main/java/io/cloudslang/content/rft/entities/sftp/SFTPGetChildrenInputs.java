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

public class SFTPGetChildrenInputs implements IHasFTPOperation {
    public final SFTPCommonInputs sftpCommonInputs;
    private final String remotePath;
    private final String delimiter;

    public SFTPGetChildrenInputs(SFTPCommonInputs sftpCommonInputs,String remotePath,String delimiter){
        this.sftpCommonInputs = sftpCommonInputs;
        this.remotePath = remotePath;
        this.delimiter = delimiter;
    }

    @NotNull
    public static SFTPGetChildrenInputsBuilder builder(){ return new SFTPGetChildrenInputsBuilder(); }

    @NotNull
    public  SFTPCommonInputs getSftpCommonInputs() {
        return sftpCommonInputs;
    }

    @NotNull
    public String getRemotePath(){ return remotePath;}

    @NotNull
    public String getDelimiter(){ return delimiter;}

    public static class SFTPGetChildrenInputsBuilder{
        private SFTPCommonInputs sftpCommonInputs;
        private String remotePath = EMPTY;
        private String delimiter = EMPTY;

        SFTPGetChildrenInputsBuilder(){
        }

        @NotNull
        public SFTPGetChildrenInputs.SFTPGetChildrenInputsBuilder sftpCommonInputs(@NotNull final SFTPCommonInputs sftpCommonInputs){
            this.sftpCommonInputs = sftpCommonInputs;
            return this;
        }

        @NotNull
        public SFTPGetChildrenInputs.SFTPGetChildrenInputsBuilder remotePath(@NotNull final String remotePath){
            this.remotePath = remotePath;
            return this;
        }

        @NotNull
        public SFTPGetChildrenInputs.SFTPGetChildrenInputsBuilder delimiter(@NotNull final String delimiter){
            this.delimiter = delimiter;
            return this;
        }

        public SFTPGetChildrenInputs build() { return new SFTPGetChildrenInputs(sftpCommonInputs,remotePath,delimiter); }
    }
}
