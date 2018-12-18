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

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.List;

@Generated("net.hexar.json2pojo")

public class CreateMessageBody {

    @SerializedName("bccRecipients")
    private List<BccRecipient> mBccRecipients;
    @SerializedName("body")
    private Body mBody;
    @SerializedName("categories")
    private List<String> mCategories;
    @SerializedName("ccRecipients")
    private List<CcRecipient> mCcRecipients;
    @SerializedName("from")
    private From mFrom;
    @SerializedName("id")
    private String mId;
    @SerializedName("importance")
    private String mImportance;
    @SerializedName("inferenceClassification")
    private String mInferenceClassification;
    @SerializedName("internetMessageId")
    private String mInternetMessageId;
    @SerializedName("isDeliveryReceiptRequested")
    private Boolean mIsDeliveryReceiptRequested;
    @SerializedName("isDraft")
    private Boolean mIsDraft;
    @SerializedName("isRead")
    private Boolean mIsRead;
    @SerializedName("isReadReceiptRequested")
    private Boolean mIsReadReceiptRequested;
    @SerializedName("replyTo")
    private List<ReplyTo> mReplyTo;
    @SerializedName("sender")
    private Sender mSender;
    @SerializedName("subject")
    private String mSubject;
    @SerializedName("toRecipients")
    private List<ToRecipient> mToRecipients;

    public List<BccRecipient> getBccRecipients() {
        return mBccRecipients;
    }

    public void setBccRecipients(List<BccRecipient> bccRecipients) {
        mBccRecipients = bccRecipients;
    }

    public Body getBody() {
        return mBody;
    }

    public void setBody(Body body) {
        mBody = body;
    }

    public List<String> getCategories() {
        return mCategories;
    }

    public void setCategories(List<String> categories) {
        mCategories = categories;
    }

    public List<CcRecipient> getCcRecipients() {
        return mCcRecipients;
    }

    public void setCcRecipients(List<CcRecipient> ccRecipients) {
        mCcRecipients = ccRecipients;
    }

    public From getFrom() {
        return mFrom;
    }

    public void setFrom(From from) {
        mFrom = from;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getImportance() {
        return mImportance;
    }

    public void setImportance(String importance) {
        mImportance = importance;
    }

    public String getInferenceClassification() {
        return mInferenceClassification;
    }

    public void setInferenceClassification(String inferenceClassification) {
        mInferenceClassification = inferenceClassification;
    }

    public String getInternetMessageId() {
        return mInternetMessageId;
    }

    public void setInternetMessageId(String internetMessageId) {
        mInternetMessageId = internetMessageId;
    }

    public Boolean getIsDeliveryReceiptRequested() {
        return mIsDeliveryReceiptRequested;
    }

    public void setIsDeliveryReceiptRequested(Boolean isDeliveryReceiptRequested) {
        mIsDeliveryReceiptRequested = isDeliveryReceiptRequested;
    }

    public Boolean getIsDraft() {
        return mIsDraft;
    }

    public void setIsDraft(Boolean isDraft) {
        mIsDraft = isDraft;
    }

    public Boolean getIsRead() {
        return mIsRead;
    }

    public void setIsRead(Boolean isRead) {
        mIsRead = isRead;
    }

    public Boolean getIsReadReceiptRequested() {
        return mIsReadReceiptRequested;
    }

    public void setIsReadReceiptRequested(Boolean isReadReceiptRequested) {
        mIsReadReceiptRequested = isReadReceiptRequested;
    }

    public List<ReplyTo> getReplyTo() {
        return mReplyTo;
    }

    public void setReplyTo(List<ReplyTo> replyTo) {
        mReplyTo = replyTo;
    }

    public Sender getSender() {
        return mSender;
    }

    public void setSender(Sender sender) {
        mSender = sender;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public List<ToRecipient> getToRecipients() {
        return mToRecipients;
    }

    public void setToRecipients(List<ToRecipient> toRecipients) {
        mToRecipients = toRecipients;
    }

}
