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

public class DeleteMessageInputs {
    private final String messageId;
    private final Office365CommonInputs commonInputs;
    private final String folderId;

    @java.beans.ConstructorProperties({"messageId", "folderId", "commonInputs"})
    public DeleteMessageInputs(String messageId, String folderId, Office365CommonInputs commonInputs) {
        this.messageId = messageId;
        this.commonInputs = commonInputs;
        this.folderId = folderId;
    }

    @NotNull
    public static DeleteMessageInputsBuilder builder() {
        return new DeleteMessageInputsBuilder();
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
    public Office365CommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class DeleteMessageInputsBuilder {
        private String messageId = EMPTY;
        private Office365CommonInputs commonInputs;
        private String folderId = EMPTY;

        DeleteMessageInputsBuilder() {
        }

        @NotNull
        public DeleteMessageInputs.DeleteMessageInputsBuilder messageId(@NotNull final String messageId) {
            this.messageId = messageId;
            return this;
        }

        public DeleteMessageInputsBuilder folderId(@NotNull final String folderId) {
            this.folderId = folderId;
            return this;
        }

        @NotNull
        public DeleteMessageInputs.DeleteMessageInputsBuilder commonInputs(@NotNull final Office365CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public DeleteMessageInputs build() {
            return new DeleteMessageInputs(messageId, folderId, commonInputs);
        }
    }

}
