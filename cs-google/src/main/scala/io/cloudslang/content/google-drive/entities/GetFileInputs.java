/*
 * (c) Copyright 2019 Micro Focus, L.P.
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
package io.cloudslang.content.google-drive.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class GetFileInputs {
    private final String fileId;
    private final String acknowledgeAbuse;
    private final String supportsTeamDrives;

    private final GoogleDriveCommonInputs commonInputs;
    @java.beans.ConstructorProperties({"fileId", "acknowledgeAbuse", "supportsTeamDrives"})

    public CreateFileInputs (String fileId, String acknowledgeAbuse, String supportsTeamDrives) {
        this.fileId = fileId;
        this.acknowledgeAbuse = acknowledgeAbuse;
        this.supportsTeamDrives = supportsTeamDrives;
    }

    @NotNull
    public static GetFileInputsBuilder builder() {return new GetFileInputsBuilder();}

    @NotNull
    public String fileId {return fileId;}

    @NotNull
    public String acknowledgeAbuse {return acknowledgeAbuse;}

    @NotNull
    public String supportsTeamDrives {return supportsTeamDrives;}

    @NotNull
    public GoogleDriveCommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class CreateFileInputsBuilder {
        private String fileId = EMPTY;
        private String acknowledgeAbuse = EMPTY;
        private String supportsTeamDrives = EMPTY;
        private GoogleDriveCommonInputs commonInputs;

        GetFileInputsBuilder() {
        }

        @NotNull
        public GetFileInputs.GetFileInputsBuilder fileId(@NotNull final String fileId) {
            this.fileId = fileId;
            return this;
        }

        @NotNull
        public GetFileInputs.GetFileInputsBuilder acknowledgeAbuse(@NotNull final String acknowledgeAbuse) {
            this.acknowledgeAbuse = acknowledgeAbuse;
            return this;
        }

        @NotNull
        public GetFileInputs.GetFileInputsBuilder supportsTeamDrives(@NotNull final String supportsTeamDrives) {
            this.supportsTeamDrives = supportsTeamDrives;
            return this;
        }

        @NotNull
        public GetFileInputs.GetFileInputsBuilder commonInputs(@NotNull final GoogleDriveCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }
        public GetFileInputs build() {
            return new GetFileInputs(fileId, acknowledgeAbuse, supportsTeamDrives, commonInputs);
        }
    }

}
