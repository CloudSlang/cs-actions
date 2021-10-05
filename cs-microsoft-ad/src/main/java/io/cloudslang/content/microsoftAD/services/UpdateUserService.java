/*
 * (c) Copyright 2021 Micro Focus, L.P.
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
package io.cloudslang.content.microsoftAD.services;

import com.google.gson.JsonObject;
import io.cloudslang.content.microsoftAD.entities.UpdateUserInputs;
import java.util.Map;
import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPost;
import static io.cloudslang.content.microsoftAD.utils.Constants.USERS_URL;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class UpdateUserService {

    public static Map<String, String> updateUser(UpdateUserInputs updateUserInputs) {

        if (!updateUserInputs.getCreateUserCommonInputs().getBody().equals(EMPTY))
            return httpPost(updateUserInputs.getCommonInputs(), USERS_URL, updateUserInputs.getCreateUserCommonInputs().getBody());

        JsonObject passwordProfile = new JsonObject();
        passwordProfile.addProperty(FORCE_CHANGE_PASSWORD, updateUserInputs.getCreateUserCommonInputs().getForceChangePassword());
        passwordProfile.addProperty(PASSWORD, updateUserInputs.getCreateUserCommonInputs().getPassword());

        JsonObject body = new JsonObject();
        body.addProperty(ACCOUNT_ENABLED, updateUserInputs.getCreateUserCommonInputs().getAccountEnabled());
        body.addProperty(DISPLAY_NAME, updateUserInputs.getCreateUserCommonInputs().getDisplayName());
        if (!updateUserInputs.getCreateUserCommonInputs().getOnPremisesImmutableId().equals(EMPTY))
            body.addProperty(ON_PREMISES_IMMUTABLE_ID, updateUserInputs.getCreateUserCommonInputs().getOnPremisesImmutableId());
        body.addProperty(MAIL_NICKNAME, updateUserInputs.getCreateUserCommonInputs().getMailNickname());
        body.addProperty(USER_PRINCIPAL_NAME, updateUserInputs.getUpdatedUserPrincipalName());
        body.add(PASSWORD_PROFILE, passwordProfile);

        return httpPost(updateUserInputs.getCommonInputs(), USERS_URL, body.toString());
    }
}
