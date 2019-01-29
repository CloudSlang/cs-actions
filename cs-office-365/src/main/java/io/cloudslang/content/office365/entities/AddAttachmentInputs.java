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

public class AddAttachmentInputs {

    private final String messageId;
    private final String filePath;
    private final String contentName;
    private final String contentBytes;
    private final Office365CommonInputs commonInputs;

    @java.beans.ConstructorProperties({"messageId", "filePath", "contentName", "contentBytes", "commonInputs"})
    public AddAttachmentInputs(String messageId, String filePath, String contentName, String contentBytes, Office365CommonInputs commonInputs) {
        this.messageId = messageId;
        this.filePath = filePath;
        this.contentName = contentName;
        this.contentBytes = contentBytes;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static AddAttachmentInputsBuilder builder() {
        return new AddAttachmentInputsBuilder();
    }

    @NotNull
    public String getMessageId() {
        return messageId;
    }

    @NotNull
    public String getFilePath() {
        return filePath;
    }

    @NotNull
    public String getContentName() {
        return contentName;
    }

    @NotNull
    public String getContentBytes() {
        return contentBytes;
    }

    @NotNull
    public Office365CommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class AddAttachmentInputsBuilder {

        private String messageId = EMPTY;
        private String filePath = EMPTY;
        private String contentName = EMPTY;
        private String contentBytes = EMPTY;
        private Office365CommonInputs commonInputs;

        AddAttachmentInputsBuilder() {
        }

        @NotNull
        public AddAttachmentInputs.AddAttachmentInputsBuilder messageId(@NotNull final String messageId) {
            this.messageId = messageId;
            return this;
        }

        @NotNull
        public AddAttachmentInputs.AddAttachmentInputsBuilder filePath(@NotNull final String filePath) {
            this.filePath = filePath;
            return this;
        }

        @NotNull
        public AddAttachmentInputs.AddAttachmentInputsBuilder contentName(@NotNull final String contentName) {
            this.contentName = contentName;
            return this;
        }

        @NotNull
        public AddAttachmentInputs.AddAttachmentInputsBuilder contentBytes(@NotNull final String contentBytes) {
            this.contentBytes = contentBytes;
            return this;
        }

        @NotNull
        public AddAttachmentInputs.AddAttachmentInputsBuilder commonInputs(@NotNull final Office365CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public AddAttachmentInputs build() {
            return new AddAttachmentInputs(messageId, filePath, contentName, contentBytes, commonInputs);
        }
    }

}
