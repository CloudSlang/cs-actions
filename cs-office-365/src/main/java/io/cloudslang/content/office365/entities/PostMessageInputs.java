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

public class PostMessageInputs {

    private final String messageId;
    private final String body;

    private final Office365CommonInputs commonInputs;

    @java.beans.ConstructorProperties({"messageId", "body", "commonInputs"})
    public PostMessageInputs(String messageId, String body, Office365CommonInputs commonInputs) {
        this.messageId = messageId;
        this.body = body;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static PostMessageInputs.PostSendMessageBuilder builder() {
        return new PostMessageInputs.PostSendMessageBuilder();
    }

    @NotNull
    public String getMessageId() {
        return messageId;
    }

    @NotNull
    public String getBody(){return body;}

    @NotNull
    public Office365CommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class PostSendMessageBuilder {
        private String messageId = EMPTY;
        private String body = EMPTY;
        private Office365CommonInputs commonInputs;

        PostSendMessageBuilder() {
        }

        @NotNull
        public PostMessageInputs.PostSendMessageBuilder messageId(@NotNull final String messageId) {
            this.messageId = messageId;
            return this;
        }

        @NotNull
        public PostMessageInputs.PostSendMessageBuilder body(@NotNull final String body) {
            this.body = body;
            return this;
        }

        @NotNull
        public PostMessageInputs.PostSendMessageBuilder commonInputs(@NotNull final Office365CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public PostMessageInputs build() {
            return new PostMessageInputs(messageId, body, commonInputs);
        }
    }
}

