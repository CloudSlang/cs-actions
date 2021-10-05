package io.cloudslang.content.microsoftAD.services;

import com.google.gson.JsonObject;
import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import io.cloudslang.content.microsoftAD.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPatch;
import static io.cloudslang.content.microsoftAD.utils.Constants.GET_USER_REQUEST_URL;
import static io.cloudslang.content.microsoftAD.utils.Constants.UPDATE_USER_URL;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class EnableDisableUserService {

    public static Map<String, String> enableDisableUser(AzureActiveDirectoryCommonInputs commonInputs, boolean enabled) {

        JsonObject body = new JsonObject();
        body.addProperty(Constants.ACCOUNT_ENABLED, enabled);
        return httpPatch(commonInputs, getUserUrl(commonInputs.getUserPrincipalName(), commonInputs.getUserId()), body.toString());

    }

    @NotNull
    private static String getUserUrl(String userPrincipalName, String userId) {
        String finalUrl;
        if (!StringUtils.isEmpty(userPrincipalName))
            finalUrl = UPDATE_USER_URL + userPrincipalName;
        else
            finalUrl = UPDATE_USER_URL + userId;
        return finalUrl;
    }


}


