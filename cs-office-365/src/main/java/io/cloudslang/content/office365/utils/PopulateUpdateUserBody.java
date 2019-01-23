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

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.cloudslang.content.office365.entities.CreateUserInputs;

import static io.cloudslang.content.office365.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class PopulateUpdateUserBody {

    public static String populateUpdateUserBody(CreateUserInputs createUserInputs) {
        final JsonObject updateUserBody = new JsonObject();
        updateUserBody.addProperty(ACCOUNT_ENABLED_BODY, Boolean.getBoolean(createUserInputs.getAccountEnabled()));
        updateUserBody.add(PASSWORD_PROFILE_BODY, updatePasswordProfile(createUserInputs.getForceChangePassword(), createUserInputs.getAssignedPassword()));

        if (!isEmpty(createUserInputs.getMailNickname()))
            updateUserBody.addProperty(MAIL_NICKNAME_BODY, createUserInputs.getMailNickname());
        if (!isEmpty(createUserInputs.getDisplayName()))
            updateUserBody.addProperty(DISPLAY_NAME_BODY, createUserInputs.getDisplayName());
        if (!isEmpty(createUserInputs.getUserPrincipalName()))
            updateUserBody.addProperty(USER_PRINCIPAL_NAME_BODY, createUserInputs.getUserPrincipalName());
        if (!isEmpty(createUserInputs.getOnPremisesImmutableId()))
            updateUserBody.addProperty(ON_PREMISES_IMMUTABLE_ID_BODY, createUserInputs.getOnPremisesImmutableId());

        return new GsonBuilder().create().toJson(updateUserBody);
    }

    private static JsonObject updatePasswordProfile(String forceChangePassword, String password) {
        final JsonObject passwordProfile = new JsonObject();
        passwordProfile.addProperty(FORCE_CHANGE_PASSWORD_NEXT_SIGN_IN_BODY, Boolean.getBoolean(forceChangePassword));
        if (!isEmpty(password)) {
            passwordProfile.addProperty(PASSWORD_BODY, password);
        }
        return passwordProfile;
    }
}
