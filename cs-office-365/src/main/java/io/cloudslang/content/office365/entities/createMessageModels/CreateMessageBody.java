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

package io.cloudslang.content.office365.entities.createMessageModels;

import java.util.List;

public class CreateMessageBody {

    private List<Recipient> bccRecipients;
    private Body body;
    private List<String> categories;
    private List<Recipient> ccRecipients;
    private From from;
    private String id;
    private String importance;
    private String inferenceClassification;
    private String internetMessageId;
    private Boolean isDeliveryReceiptRequested;
    private Boolean isDraft;
    private Boolean isRead;
    private Boolean isReadReceiptRequested;
    private List<Recipient> replyTo;
    private Sender sender;
    private String subject;
    private List<Recipient> toRecipients;

    public List<Recipient> getBccRecipients() {
        return bccRecipients;
    }

    public void setBccRecipients(List<Recipient> bccRecipients) {
        this.bccRecipients = bccRecipients;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Recipient> getCcRecipients() {
        return ccRecipients;
    }

    public void setCcRecipients(List<Recipient> ccRecipients) {
        this.ccRecipients = ccRecipients;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getInferenceClassification() {
        return inferenceClassification;
    }

    public void setInferenceClassification(String inferenceClassification) {
        this.inferenceClassification = inferenceClassification;
    }

    public String getInternetMessageId() {
        return internetMessageId;
    }

    public void setInternetMessageId(String internetMessageId) {
        this.internetMessageId = internetMessageId;
    }

    public Boolean getIsDeliveryReceiptRequested() {
        return isDeliveryReceiptRequested;
    }

    public void setIsDeliveryReceiptRequested(Boolean isDeliveryReceiptRequested) {
        this.isDeliveryReceiptRequested = isDeliveryReceiptRequested;
    }

    public Boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Boolean isDraft) {
        this.isDraft = isDraft;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Boolean getIsReadReceiptRequested() {
        return isReadReceiptRequested;
    }

    public void setIsReadReceiptRequested(Boolean isReadReceiptRequested) {
        this.isReadReceiptRequested = isReadReceiptRequested;
    }

    public List<Recipient> getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(List<Recipient> replyTo) {
        this.replyTo = replyTo;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Recipient> getToRecipients() {
        return toRecipients;
    }

    public void setToRecipients(List<Recipient> toRecipients) {
        this.toRecipients = toRecipients;
    }

}
