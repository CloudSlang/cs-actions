package io.cloudslang.content.office365.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.office365.utils.InputsValidation.verifyCommonInputs;
import static io.cloudslang.content.office365.utils.InputsValidation.verifyGetMessageInputs;
import static org.junit.Assert.assertEquals;

public class InputsValidationUtils {

    private List<String> exceptionMessages = new ArrayList<>();

    @Test
    public void verifyCommonInputsValid() {
        exceptionMessages = verifyCommonInputs("userPrincipalName", "userId", "8080", "true",
                "2", "10", "false", "2", "20");
        assertEquals(exceptionMessages.size(), 0);
    }

    @Test
    public void verifyCommonInputsEmptyUserAuthInputs() {
        exceptionMessages = verifyCommonInputs("", "", "8080", "true",
                "2", "10", "false", "2", "20");
        assertEquals(exceptionMessages.size(), 1);
        assertEquals(exceptionMessages.get(0), "The userPrincipalName or userId is required for login.");
    }

    @Test
    public void verifyCommonInputsInvalidBooleanAndNumber() {
        exceptionMessages = verifyCommonInputs("userPrincipalName", "userId", "8080", "invalid",
                "2", "invalid", "false", "2", "20");
        assertEquals(exceptionMessages.size(), 2);
        assertEquals(exceptionMessages.get(0), "The invalid for trustAllRoots input is not a valid boolean value.");
        assertEquals(exceptionMessages.get(1), "The invalid for socketTimeout input is not a valid number value.");
    }

    @Test
    public void verifyCommonInputsEmptyAuthInputUserId() {
        exceptionMessages = verifyCommonInputs("userPrincipalName", "", "8080", "true",
                "2", "10", "false", "2", "20");
        assertEquals(exceptionMessages.size(), 0);
    }

    @Test
    public void verifyCommonInputsEmptyAuthInputUserPrincipalName() {
        exceptionMessages = verifyCommonInputs("", "userId", "8080", "true",
                "2", "10", "false", "2", "20");
        assertEquals(exceptionMessages.size(), 0);
    }

    @Test
    public void verifyGetMessageInputsInvalid() {
        exceptionMessages = verifyGetMessageInputs("", "", "userId", "8080", "true",
                "2", "10", "false", "2", "20");
        assertEquals(exceptionMessages.size(), 1);
        assertEquals(exceptionMessages.get(0), "The messageId can't be null or empty.");
    }


    @Test
    public void verifyGetMessageInputsValid() {
        exceptionMessages = verifyGetMessageInputs("messageId", "", "userId", "8080", "true",
                "2", "10", "false", "2", "20");
        assertEquals(exceptionMessages.size(), 0);
    }

}
