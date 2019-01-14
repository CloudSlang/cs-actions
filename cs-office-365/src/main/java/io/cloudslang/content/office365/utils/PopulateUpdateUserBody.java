package io.cloudslang.content.office365.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.cloudslang.content.office365.entities.CreateUserInputs;

import static io.cloudslang.content.office365.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class PopulateUpdateUserBody {

    public static String populateUpdateUserBody(CreateUserInputs createUserInputs) {
        JsonObject updateUserBody = new JsonObject();
        updateUserBody.addProperty(ACCOUNT_ENABLED_BODY, Boolean.getBoolean(createUserInputs.getAccountEnabled()));
        updateUserBody.add(PASSWORD_PROFILE_BODY, updatePasswordProfile(createUserInputs.getForceChangePassword(), createUserInputs.getPassword()));

        if (!isEmpty(createUserInputs.getMailNickname()))
            updateUserBody.addProperty(MAIL_NICKNAME_BODY, createUserInputs.getMailNickname());
        if (!isEmpty(createUserInputs.getDisplayName()))
            updateUserBody.addProperty(DISPLAY_NAME_BODY, createUserInputs.getDisplayName());
        if (!isEmpty(createUserInputs.getUserPrincipalName()))
            updateUserBody.addProperty(USER_PRINCIPAL_NAME_BODY, createUserInputs.getUserPrincipalName());
        if (!isEmpty(createUserInputs.getOnPremisesImmutableId()))
            updateUserBody.addProperty(ON_PREMISES_IMMUTABLE_ID_BODY, createUserInputs.getOnPremisesImmutableId());

        Gson gson = new GsonBuilder().create();
        return gson.toJson(updateUserBody);
    }

    private static JsonObject updatePasswordProfile(String forceChangePassword, String password) {
        JsonObject passwordProfile = new JsonObject();
        passwordProfile.addProperty(FORCE_CHANGE_PASSWORD_NEXT_SIGN_IN_BODY, Boolean.getBoolean(forceChangePassword));
        if (!isEmpty(password)) {
            passwordProfile.addProperty(PASSWORD_BODY, password);
        }
        return passwordProfile;
    }
}
