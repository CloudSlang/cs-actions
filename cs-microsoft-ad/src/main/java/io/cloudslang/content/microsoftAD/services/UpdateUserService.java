/*
 * Copyright 2021-2025 Open Text
 * This program and the accompanying materials
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

package io.cloudslang.content.microsoftAD.services;

import com.google.gson.JsonObject;
import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import io.cloudslang.content.microsoftAD.entities.UpdateUserInputs;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPatch;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.ACCOUNT_ENABLED;
import static org.apache.commons.lang3.CharEncoding.UTF_8;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class UpdateUserService {

    public static Map<String, String> updateUser(UpdateUserInputs updateUserInputs) {

        Map<String, String> result = new HashMap<>();
        String UPDATE_USERS_URL = getUpdateUserUrl(updateUserInputs);

        if (!updateUserInputs.getUserCommonInputs().getBody().equals(EMPTY))
            return httpPatch(updateUserInputs.getUserCommonInputs().getCommonInputs(), UPDATE_USERS_URL, updateUserInputs.getUserCommonInputs().getBody());

        JsonObject body = new JsonObject();
        if (!updateUserInputs.getUserCommonInputs().getAccountEnabled().isEmpty())
            body.addProperty(ACCOUNT_ENABLED, updateUserInputs.getUserCommonInputs().getAccountEnabled());
        if (!updateUserInputs.getUserCommonInputs().getDisplayName().isEmpty())
            body.addProperty(DISPLAY_NAME, updateUserInputs.getUserCommonInputs().getDisplayName());
        if (!updateUserInputs.getUserCommonInputs().getOnPremisesImmutableId().isEmpty())
            body.addProperty(ON_PREMISES_IMMUTABLE_ID, updateUserInputs.getUserCommonInputs().getOnPremisesImmutableId());
        if (!updateUserInputs.getUserCommonInputs().getMailNickname().isEmpty())
            body.addProperty(MAIL_NICKNAME, updateUserInputs.getUserCommonInputs().getMailNickname());
        if (!updateUserInputs.getUpdatedUserPrincipalName().isEmpty())
            body.addProperty(USER_PRINCIPAL_NAME, updateUserInputs.getUpdatedUserPrincipalName());

        if (!updateUserInputs.getUserCommonInputs().getPassword().isEmpty()) {
            JsonObject passwordProfile = new JsonObject();

            if (!updateUserInputs.getUserCommonInputs().getForceChangePassword().isEmpty())
                passwordProfile.addProperty(FORCE_CHANGE_PASSWORD, updateUserInputs.getUserCommonInputs().getForceChangePassword());
            else
                passwordProfile.addProperty(FORCE_CHANGE_PASSWORD, Boolean.FALSE);

            passwordProfile.addProperty(PASSWORD, updateUserInputs.getUserCommonInputs().getPassword());
            body.add(PASSWORD_PROFILE, passwordProfile);
        }

        if (body.size() == 0) {
            result.put(RETURN_RESULT, EXCEPTION_NO_PROPERTIES_SET);
            result.put(STATUS_CODE, CODE_404);
            return result;
        }

        return httpPatch(updateUserInputs.getUserCommonInputs().getCommonInputs(), UPDATE_USERS_URL, body.toString());
    }

    @NotNull
    public static String getUpdateUserUrl(@NotNull final UpdateUserInputs updateUserInputs) {

        String UPDATE_USERS_URL = USERS_URL + FORWARD_SLASH;

        if (!(updateUserInputs.getUserCommonInputs().getCommonInputs().getUserId().isEmpty()))
            UPDATE_USERS_URL += updateUserInputs.getUserCommonInputs().getCommonInputs().getUserId().trim();
        else
        {
            try {
                UPDATE_USERS_URL += URLEncoder.encode(updateUserInputs.getUserCommonInputs().getCommonInputs().getUserPrincipalName().trim(), UTF_8);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        return UPDATE_USERS_URL;
    }
}
