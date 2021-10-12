package io.cloudslang.content.microsoftAD.services;

import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import io.cloudslang.content.microsoftAD.entities.CommonUserInputs;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPatch;
import static io.cloudslang.content.microsoftAD.utils.Constants.FORWARD_SLASH;
import static io.cloudslang.content.microsoftAD.utils.Constants.USERS_URL;

public class ResetUserPasswordService {

    public Map<String, String> resetUserPassword(CommonUserInputs commonUserInputs) {
        return httpPatch(commonUserInputs, getResetUserPasswordUrl())
    }

    @NotNull
    public static String getResetUserPasswordUrl(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) {
        String finalUrl = USERS_URL;

        if (!commonInputs.getUserPrincipalName().isEmpty())
            finalUrl = finalUrl + FORWARD_SLASH + commonInputs.getUserPrincipalName();
        else
            finalUrl = finalUrl + FORWARD_SLASH + commonInputs.getUserId();

        return finalUrl;
    }
}
