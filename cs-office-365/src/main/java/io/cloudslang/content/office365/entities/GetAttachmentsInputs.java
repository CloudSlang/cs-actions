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

public class GetAttachmentsInputs {
    private final String messageId;
    private final String attachmentId;
    private final String filePath;

    private final Office365CommonInputs commonInputs;

    @java.beans.ConstructorProperties({"messageId", "attachmentId", "filePath", "commonInputs"})
    public GetAttachmentsInputs(String messageId, String attachmentId, String filePath, Office365CommonInputs commonInputs) {
        this.messageId = messageId;
        this.attachmentId = attachmentId;
        this.filePath = filePath;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static GetAttachmentsInputsBuilder builder() {
        return new GetAttachmentsInputsBuilder();
    }

    @NotNull
    public String getMessageId() {
        return messageId;
    }

    @NotNull
    public String getAttachmentId() {
        return attachmentId;
    }

    @NotNull
    public String getoFilePath() {
        return this.filePath;
    }

    @NotNull
    public Office365CommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class GetAttachmentsInputsBuilder {
        private String messageId = EMPTY;
        private String attachmentId = EMPTY;
        private String filePath = EMPTY;
        private Office365CommonInputs commonInputs;

        GetAttachmentsInputsBuilder() {
        }

        @NotNull
        public GetAttachmentsInputs.GetAttachmentsInputsBuilder messageId(@NotNull final String messageId) {
            this.messageId = messageId;
            return this;
        }
        @NotNull
        public GetAttachmentsInputs.GetAttachmentsInputsBuilder attachmentId(@NotNull final String attachmentId) {
            this.attachmentId = attachmentId;
            return this;
        }
        @NotNull
        public GetAttachmentsInputs.GetAttachmentsInputsBuilder filePath(@NotNull final String filePath) {
            this.filePath = filePath;
            return this;
        }

        @NotNull
        public GetAttachmentsInputs.GetAttachmentsInputsBuilder commonInputs(@NotNull final Office365CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public GetAttachmentsInputs build() {
            return new GetAttachmentsInputs(messageId, attachmentId, filePath, commonInputs);
        }
    }

}
