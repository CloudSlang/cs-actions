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

public class CreateMessageInputs {

    private final String userPrincipalName;
    private final String userId;
    private final String folderId;
    private final String bccRecipients;
    private final String categories;
    private final String ccRecipients;
    private final String from;
    private final String importance;
    private final String inferenceClassification;
    private final String internetMessageId;
    private final String isRead;
    private final String replyTo;
    private final String sender;
    private final String toRecipients;
    private final String body;
    private final String isDeliveryReceiptRequested;
    private final String isReadReceiptRequested;
    private final String subject;

    private final Office365CommonInputs commonInputs;

    @java.beans.ConstructorProperties({"userPrincipalName", "userId", "folderId", "bccRecipients", "categories",
            "ccRecipients", "from", "importance", "inferenceClassification", "internetMessageId", "isRead", "replyTo",
            "sender", "toRecipients", "body", "isDeliveryReceiptRequested", "isReadReceiptRequested", "subject", "commonInputs"})
    public CreateMessageInputs(String userPrincipalName, String userId, String folderId, String bccRecipients, String categories,
                               String ccRecipients, String from, String importance, String inferenceClassification, String internetMessageId,
                               String isRead, String replyTo, String sender, String toRecipients, String body, String isDeliveryReceiptRequested,
                               String isReadReceiptRequested, String subject,
                               Office365CommonInputs commonInputs) {
        this.userPrincipalName = userPrincipalName;
        this.userId = userId;
        this.folderId = folderId;
        this.bccRecipients = bccRecipients;
        this.categories = categories;
        this.ccRecipients = ccRecipients;
        this.from = from;
        this.importance = importance;
        this.inferenceClassification = inferenceClassification;
        this.internetMessageId = internetMessageId;
        this.isRead = isRead;
        this.replyTo = replyTo;
        this.sender = sender;
        this.toRecipients = toRecipients;
        this.body = body;
        this.isDeliveryReceiptRequested = isDeliveryReceiptRequested;
        this.isReadReceiptRequested = isReadReceiptRequested;
        this.subject = subject;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static CreateMessageInputsBuilder builder() {
        return new CreateMessageInputsBuilder();
    }

    @NotNull
    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    @NotNull
    public String getFolderId() {
        return folderId;
    }

    @NotNull
    public String getUserId() {
        return userId;
    }

    @NotNull
    public String getBccRecipients() { return bccRecipients; }

    @NotNull
    public String getCategories() { return categories; }

    @NotNull
    public String getCcRecipients() { return ccRecipients; }

    @NotNull
    public String getFrom() { return from; }

    @NotNull
    public String getImportance() { return importance; }

    @NotNull
    public String getInferenceClassification() { return inferenceClassification; }

    @NotNull
    public String getInternetMessageId() { return internetMessageId; }

    @NotNull
    public String getIsRead() { return isRead; }

    @NotNull
    public String getReplyTo() { return replyTo; }

    @NotNull
    public String getSender() { return sender; }

    @NotNull
    public String getToRecipients() { return toRecipients; }

    @NotNull
    public String getBody() { return body; }

    @NotNull
    public String getIsDeliveryReceiptRequested() { return isDeliveryReceiptRequested; }

    @NotNull
    public String getIsReadReceiptRequested() { return isReadReceiptRequested; }

    @NotNull
    public String getSubject() { return subject; }

    @NotNull
    public Office365CommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class CreateMessageInputsBuilder {
        private String userPrincipalName = EMPTY;
        private String userId = EMPTY;
        private String folderId = EMPTY;
        private String bccRecipients = EMPTY;
        private String categories = EMPTY;
        private String ccRecipients = EMPTY;
        private String from = EMPTY;
        private String importance = EMPTY;
        private String inferenceClassification = EMPTY;
        private String internetMessageId = EMPTY;
        private String isRead = EMPTY;
        private String replyTo = EMPTY;
        private String sender = EMPTY;
        private String toRecipients = EMPTY;
        private String body = EMPTY;
        private String isDeliveryReceiptRequested = EMPTY;
        private String isReadReceiptRequested = EMPTY;
        private String subject = EMPTY;
        private Office365CommonInputs commonInputs;

        CreateMessageInputsBuilder() {
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder userPrincipalName(@NotNull final String userPrincipalName) {
            this.userPrincipalName = userPrincipalName;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder userId(@NotNull final String userId) {
            this.userId = userId;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder folderId(@NotNull final String folderId) {
            this.folderId = folderId;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder bccRecipients(@NotNull final String bccRecipients) {
            this.bccRecipients = bccRecipients;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder categories(@NotNull final String categories) {
            this.categories = categories;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder ccRecipients(@NotNull final String ccRecipients) {
            this.ccRecipients = ccRecipients;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder from(@NotNull final String from) {
            this.from = from;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder importance(@NotNull final String importance) {
            this.importance = importance;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder inferenceClassification(@NotNull final String inferenceClassification) {
            this.inferenceClassification = inferenceClassification;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder internetMessageId(@NotNull final String internetMessageId) {
            this.internetMessageId = internetMessageId;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder isRead(@NotNull final String isRead) {
            this.isRead = isRead;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder replyTo(@NotNull final String replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder sender(@NotNull final String sender) {
            this.sender = sender;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder toRecipients(@NotNull final String toRecipients) {
            this.toRecipients = toRecipients;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder body(@NotNull final String body) {
            this.body = body;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder isDeliveryReceiptRequested(@NotNull final String isDeliveryReceiptRequested) {
            this.isDeliveryReceiptRequested = isDeliveryReceiptRequested;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder isReadReceiptRequested(@NotNull final String isReadReceiptRequested) {
            this.isReadReceiptRequested = isReadReceiptRequested;
            return this;
        }

        @NotNull
        public CreateMessageInputs.CreateMessageInputsBuilder subject(@NotNull final String subject) {
            this.subject = subject;
            return this;
        }

        public CreateMessageInputs.CreateMessageInputsBuilder commonInputs(@NotNull final Office365CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public CreateMessageInputs build() {
            return new CreateMessageInputs(userPrincipalName, userId, folderId, bccRecipients, categories,
                    ccRecipients, from, importance, inferenceClassification, internetMessageId,
                    isRead, replyTo, sender, toRecipients, body, isDeliveryReceiptRequested,
                    isReadReceiptRequested, subject, commonInputs);
        }
    }

}
