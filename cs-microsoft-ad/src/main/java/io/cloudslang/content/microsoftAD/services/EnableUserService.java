package io.cloudslang.content.microsoftAD.services;

import com.google.gson.JsonObject;
import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import io.cloudslang.content.microsoftAD.utils.Constants;

import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPatch;
import static io.cloudslang.content.microsoftAD.utils.Constants.UPDATE_USER_URL;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class EnableUserService {

    public static Map<String, String> enabledUser(AzureActiveDirectoryCommonInputs commonInputs, boolean enabled) {

        JsonObject body = new JsonObject();
        body.addProperty(Constants.ACCOUNT_ENABLED, enabled);

        if (!commonInputs.getUserPrincipalName().equals(EMPTY))
            return httpPatch(commonInputs, UPDATE_USER_URL + commonInputs.getUserPrincipalName(), body.toString());
        else
            return httpPatch(commonInputs, UPDATE_USER_URL + commonInputs.getUserId(), body.toString());
    }
}