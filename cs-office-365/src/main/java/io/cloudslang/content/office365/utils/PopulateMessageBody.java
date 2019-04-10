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
import java.util.*;
import static io.cloudslang.content.office365.utils.Constants.CONTENT_TYPE;

public class PopulateMessageBody {

    public static String populateMessageBody(Office365CommonInputs commonInputs, CreateMessageInputs createMessageInputs, String delimiter) {

        final CreateMessageBody messageBody = new CreateMessageBody();
        final List<String> categoriesList = new ArrayList<>();
        final Body body = new Body();
        String bccRecipients;
        String ccRecipients;
        String replyTo;

        String[] categoryList = createMessageInputs.getCategories().split(delimiter);
        Collections.addAll(categoriesList, categoryList);
        messageBody.setCategories(categoriesList);

        bccRecipients = createMessageInputs.getBccRecipients();
        if (bccRecipients.isEmpty()){
            messageBody.setBccRecipients(null);
        }
        else {
        messageBody.setBccRecipients(recipientsCollection(bccRecipients, delimiter));
        }

        ccRecipients = createMessageInputs.getCcRecipients();
        if (ccRecipients.isEmpty()){
            messageBody.setCcRecipients(null);
        }
        else{
        messageBody.setCcRecipients(recipientsCollection(ccRecipients, delimiter));
        }

        replyTo = createMessageInputs.getReplyTo();
        if (replyTo.isEmpty()){
            messageBody.setReplyTo(null);
        }
        else {
        messageBody.setReplyTo(recipientsCollection(replyTo, delimiter));
        }

        messageBody.setToRecipients(recipientsCollection(createMessageInputs.getToRecipients(), delimiter));

        body.setContentType(CONTENT_TYPE);
        body.setContent(createMessageInputs.getBody());
        messageBody.setBody(body);

        populateMessage(messageBody, createMessageInputs, commonInputs);

        return new Gson().toJson(messageBody);
    }

    private static List<Recipient> recipientsCollection(String emailInput, String delimiter) {
        final List<Recipient> finalList = new ArrayList<>();
        String[] list = emailInput.split(delimiter);

        if (!emailInput.contains(delimiter)){
            setEmailAddress(finalList, emailInput);
        } else {
            for (String item : list) {
                setEmailAddress(finalList, item);
            }
        }
        return finalList;
    }

    private static void setEmailAddress(List<Recipient> finalList, String email){
        final EmailAddress emailAddress = new EmailAddress();
        final Recipient recipient = new Recipient();
        emailAddress.setAddress(email);
        recipient.setEmailAddress(emailAddress);
        finalList.add(recipient);
    }

    private static void populateMessage(CreateMessageBody messageBody, CreateMessageInputs createMessageInputs, Office365CommonInputs commonInputs) {

        final EmailAddress emailAddress = new EmailAddress();
        final EmailAddress senderEmailAddress = new EmailAddress();
        final From fromVal = new From();
        final Sender senderVal = new Sender();

        messageBody.setImportance(createMessageInputs.getImportance());
        messageBody.setInferenceClassification(createMessageInputs.getInferenceClassification());
        messageBody.setInternetMessageId(createMessageInputs.getInternetMessageId());
        messageBody.setIsRead(Boolean.valueOf(createMessageInputs.getIsRead()));
        messageBody.setIsDeliveryReceiptRequested(Boolean.valueOf(createMessageInputs.getIsDeliveryReceiptRequested()));
        messageBody.setIsReadReceiptRequested(Boolean.valueOf(createMessageInputs.getIsReadReceiptRequested()));
        messageBody.setSubject(createMessageInputs.getSubject());
        messageBody.setId(commonInputs.getUserId());

        emailAddress.setAddress(createMessageInputs.getFrom());
        fromVal.setEmailAddress(emailAddress);
        messageBody.setFrom(fromVal);

        senderEmailAddress.setAddress(createMessageInputs.getSender());
        senderVal.setEmailAddress(senderEmailAddress);
        messageBody.setSender(senderVal);

    }
}
