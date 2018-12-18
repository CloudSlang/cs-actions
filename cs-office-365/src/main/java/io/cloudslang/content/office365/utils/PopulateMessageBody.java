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
package io.cloudslang.content.office365.utils;

import com.google.gson.Gson;
import io.cloudslang.content.office365.entities.CreateMessageInputs;
import io.cloudslang.content.office365.entities.Office365CommonInputs;
import io.cloudslang.content.office365.entities.createMessageModels.*;

import java.util.ArrayList;
import java.util.List;

public class PopulateMessageBody {

    public static String populateMessageBody(Office365CommonInputs commonInputs, CreateMessageInputs createMessageInputs, String delimiter) {

        CreateMessageBody messageBody = new CreateMessageBody();
        List<String> categoriesList = new ArrayList<>();
        List<BccRecipient> bccList = new ArrayList<>();
        List<CcRecipient> ccList = new ArrayList<>();
        List<ToRecipient> toList = new ArrayList<>();
        List<ReplyTo> replyToList = new ArrayList<>();
        Body body = new Body();
        From fromVal = new From();
        Sender senderVal = new Sender();
        ToRecipient toRecipient = new ToRecipient();
        EmailAddress toEmailAddress = new EmailAddress();
        EmailAddress senderEmailAddress = new EmailAddress();
        EmailAddress emailAddress = new EmailAddress();
        EmailAddress bccemailAddress = new EmailAddress();
        EmailAddress ccemailAddress = new EmailAddress();
        EmailAddress replyEmailAddress = new EmailAddress();
        BccRecipient bccRecipient = new BccRecipient();
        CcRecipient ccRecipient = new CcRecipient();
        ReplyTo replyTo = new ReplyTo();


        messageBody.setImportance(createMessageInputs.getImportance());
        messageBody.setInferenceClassification(createMessageInputs.getInferenceClassification());
        messageBody.setInternetMessageId(createMessageInputs.getInternetMessageId());
        messageBody.setIsRead(Boolean.valueOf(createMessageInputs.getIsRead()));
        messageBody.setIsDeliveryReceiptRequested(Boolean.valueOf(createMessageInputs.getIsDeliveryReceiptRequested()));
        messageBody.setIsReadReceiptRequested(Boolean.valueOf(createMessageInputs.getIsReadReceiptRequested()));
        messageBody.setSubject(createMessageInputs.getSubject());
        messageBody.setId(commonInputs.getUserId());


        String[] categoryList = createMessageInputs.getCategories().split(delimiter);
        for (String category : categoryList) {
            categoriesList.add(category);
        }
        messageBody.setCategories(categoriesList);

        String[] list = createMessageInputs.getBccRecipients().split(delimiter);
        for (String item : list) {
            bccemailAddress.setAddress(item);
            bccRecipient.setEmailAddress(bccemailAddress);
            bccList.add(bccRecipient);
        }
        messageBody.setBccRecipients(bccList);

        String[] array = createMessageInputs.getCcRecipients().split(delimiter);
        for (String item : array) {
            ccemailAddress.setAddress(item);
            ccRecipient.setEmailAddress(ccemailAddress);
            ccList.add(ccRecipient);
        }
        messageBody.setCcRecipients(ccList);

        emailAddress.setAddress(createMessageInputs.getFrom());
        fromVal.setEmailAddress(emailAddress);
        messageBody.setFrom(fromVal);

        String[] replyParts = createMessageInputs.getReplyTo().split(delimiter);
        for (String item : replyParts) {
            replyEmailAddress.setAddress(item);
            replyTo.setEmailAddress(replyEmailAddress);
            replyToList.add(replyTo);
        }
        messageBody.setReplyTo(replyToList);

        senderEmailAddress.setAddress(createMessageInputs.getSender());
        senderVal.setEmailAddress(senderEmailAddress);
        messageBody.setSender(senderVal);


        String[] toParts = createMessageInputs.getToRecipients().split(delimiter);
        for (String item : toParts) {
            toEmailAddress.setAddress(item);
            toRecipient.setEmailAddress(toEmailAddress);
            toList.add(toRecipient);
        }
        messageBody.setToRecipients(toList);


        body.setContentType("HTML");
        body.setContent(createMessageInputs.getBody());
        messageBody.setBody(body);

        return new Gson().toJson(messageBody);
    }
}
