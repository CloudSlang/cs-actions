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
import java.util.Collections;
import java.util.List;

import static io.cloudslang.content.office365.utils.Constants.CONTENT_TYPE;

public class PopulateMessageBody {

    public static String populateMessageBody(Office365CommonInputs commonInputs, CreateMessageInputs createMessageInputs, String delimiter) {

        final CreateMessageBody messageBody = new CreateMessageBody();
        final List<String> categoriesList = new ArrayList<>();
        final Body body = new Body();

        String[] categoryList = createMessageInputs.getCategories().split(delimiter);
        Collections.addAll(categoriesList, categoryList);
        messageBody.setCategories(categoriesList);

        messageBody.setBccRecipients(recipientsCollection(createMessageInputs.getBccRecipients(), delimiter));
        messageBody.setCcRecipients(recipientsCollection(createMessageInputs.getCcRecipients(), delimiter));
        messageBody.setReplyTo(recipientsCollection(createMessageInputs.getReplyTo(), delimiter));
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

        for (String item : list) {
            final EmailAddress emailAddress = new EmailAddress();
            final Recipient recipient = new Recipient();
            emailAddress.setAddress(item);
            recipient.setEmailAddress(emailAddress);
            finalList.add(recipient);
        }
        return finalList;
    }

    private static void populateMessage(CreateMessageBody messageBody, CreateMessageInputs createMessageInputs, Office365CommonInputs commonInputs) {

        final EmailAddress emailAddress = new EmailAddress();
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

        emailAddress.setAddress(createMessageInputs.getSender());
        senderVal.setEmailAddress(emailAddress);
        messageBody.setSender(senderVal);

    }
}
