package io.cloudslang.content.office365.utils;

import io.cloudslang.content.office365.entities.createMessageModels.EmailAddress;
import io.cloudslang.content.office365.entities.createMessageModels.Recipient;

import java.util.LinkedList;
import java.util.List;

public class PopulateMessageBodyService {

    public static List<Recipient> recipientsCollection(String emailInput, String delimiter) {
        List<Recipient> finalList = new LinkedList<>();

        String[] list = emailInput.split(delimiter);
        for (String item : list) {
            EmailAddress emailAddress = new EmailAddress();
            Recipient recipient = new Recipient();
            emailAddress.setAddress(item);
            recipient.setEmailAddress(emailAddress);
            finalList.add(recipient);
        }
        return finalList;
    }
}
