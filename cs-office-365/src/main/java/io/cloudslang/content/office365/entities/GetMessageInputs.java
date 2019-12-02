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

package io.cloudslang.content.office365.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class GetMessageInputs {
    private final String messageId;
    private final String folderId;
    private final String selectQuery;
    private final String oDataQuery;
    private final String filePath;

    private final Office365CommonInputs commonInputs;

    @java.beans.ConstructorProperties({"messageId", "folderId", "selectQuery", "oDataQuery", "filePath", "commonInputs"})
    public GetMessageInputs(String messageId, String folderId, String selectQuery, String oDataQuery, String filePath, Office365CommonInputs commonInputs) {
        this.messageId = messageId;
        this.folderId = folderId;
        this.selectQuery = selectQuery;
        this.oDataQuery = oDataQuery;
        this.filePath = filePath;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static GetMessageInputsBuilder builder() {
        return new GetMessageInputsBuilder();
    }

    @NotNull
    public String getMessageId() {
        return messageId;
    }

    @NotNull
    public String getFolderId() {
        return folderId;
    }

    @NotNull
    public String getSelectQuery() {
        return selectQuery;
    }

    @NotNull
    public String getoDataQuery() {
        return this.oDataQuery;
    }

    @NotNull
    public String getfilePath() {
        return this.filePath;
    }

    @NotNull
    public Office365CommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class GetMessageInputsBuilder {
        private String messageId = EMPTY;
        private String folderId = EMPTY;
        private String selectQuery = EMPTY;
        private String oDataQuery = EMPTY;
        private String filePath = EMPTY;
        private Office365CommonInputs commonInputs;

        GetMessageInputsBuilder() {
        }

        @NotNull
        public GetMessageInputs.GetMessageInputsBuilder messageId(@NotNull final String messageId) {
            this.messageId = messageId;
            return this;
        }
        @NotNull
        public GetMessageInputs.GetMessageInputsBuilder folderId(@NotNull final String folderId) {
            this.folderId = folderId;
            return this;
        }
        @NotNull
        public GetMessageInputs.GetMessageInputsBuilder selectQuery(@NotNull final String selectQuery) {
            this.selectQuery = selectQuery;
            return this;
        }
        @NotNull
        public GetMessageInputs.GetMessageInputsBuilder oDataQuery(@NotNull final String oDataQuery) {
            this.oDataQuery = oDataQuery;
            return this;
        }
        @NotNull
        public GetMessageInputs.GetMessageInputsBuilder filePath(@NotNull final String filePath) {
            this.filePath = filePath;
            return this;
        }

        @NotNull
        public GetMessageInputs.GetMessageInputsBuilder commonInputs(@NotNull final Office365CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public GetMessageInputs build() {
            return new GetMessageInputs(messageId, folderId, selectQuery, oDataQuery, filePath, commonInputs);
        }
    }

}
