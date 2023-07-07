/*
 * Copyright 2021-2023 Open Text
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
import io.cloudslang.content.microsoftAD.entities.CommonUserInputs;

import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPost;
import static io.cloudslang.content.microsoftAD.utils.Constants.USERS_URL;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CreateUserService {

    public static Map<String, String> createUser(CommonUserInputs commonUserInputs) {

        if (!commonUserInputs.getBody().equals(EMPTY))
            return httpPost(commonUserInputs.getCommonInputs(), USERS_URL, commonUserInputs.getBody());

        JsonObject passwordProfile = new JsonObject();
        passwordProfile.addProperty(FORCE_CHANGE_PASSWORD, commonUserInputs.getForceChangePassword());
        passwordProfile.addProperty(PASSWORD, commonUserInputs.getPassword());

        JsonObject body = new JsonObject();
        body.addProperty(ACCOUNT_ENABLED, commonUserInputs.getAccountEnabled());
        body.addProperty(DISPLAY_NAME, commonUserInputs.getDisplayName());
        if (!commonUserInputs.getOnPremisesImmutableId().equals(EMPTY))
            body.addProperty(ON_PREMISES_IMMUTABLE_ID, commonUserInputs.getOnPremisesImmutableId());
        body.addProperty(MAIL_NICKNAME, commonUserInputs.getMailNickname());
        body.addProperty(USER_PRINCIPAL_NAME, commonUserInputs.getCommonInputs().getUserPrincipalName());
        body.add(PASSWORD_PROFILE, passwordProfile);

        return httpPost(commonUserInputs.getCommonInputs(), USERS_URL, body.toString());
    }
}
