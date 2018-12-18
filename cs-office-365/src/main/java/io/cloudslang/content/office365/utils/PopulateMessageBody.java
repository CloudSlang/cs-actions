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

import static io.cloudslang.content.office365.utils.Constants.CONTENT_TYPE;

public class PopulateMessageBody {

    public static String populateMessageBody(Office365CommonInputs commonInputs, CreateMessageInputs createMessageInputs, String delimiter) {

        final CreateMessageBody messageBody = new CreateMessageBody();
        final List<String> categoriesList = new ArrayList<>();
        final Body body = new Body();
        final From fromVal = new From();
        final Sender senderVal = new Sender();
        final EmailAddress senderEmailAddress = new EmailAddress();
        final EmailAddress emailAddress = new EmailAddress();

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

         messageBody.setBccRecipients(PopulateMessageBodyService.recipientsCollection(createMessageInputs.getBccRecipients(), delimiter));
         messageBody.setCcRecipients(PopulateMessageBodyService.recipientsCollection(createMessageInputs.getCcRecipients(), delimiter));

        emailAddress.setAddress(createMessageInputs.getFrom());
        fromVal.setEmailAddress(emailAddress);
        messageBody.setFrom(fromVal);

        messageBody.setReplyTo(PopulateMessageBodyService.recipientsCollection(createMessageInputs.getReplyTo(), delimiter));

        senderEmailAddress.setAddress(createMessageInputs.getSender());
        senderVal.setEmailAddress(senderEmailAddress);
        messageBody.setSender(senderVal);

        messageBody.setToRecipients(PopulateMessageBodyService.recipientsCollection(createMessageInputs.getToRecipients(), delimiter));

        body.setContentType(CONTENT_TYPE);
        body.setContent(createMessageInputs.getBody());
        messageBody.setBody(body);

        return new Gson().toJson(messageBody);
    }
}
