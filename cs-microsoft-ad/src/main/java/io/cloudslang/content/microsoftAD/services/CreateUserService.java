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
package io.cloudslang.content.microsoftAD.services;

import com.google.gson.JsonObject;
import io.cloudslang.content.microsoftAD.entities.CreateUserInputs;

import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPost;
import static io.cloudslang.content.microsoftAD.utils.Constants.CREATE_USER_REQUEST_URL;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CreateUser.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CreateUserService {

    public static Map<String, String> createUser(CreateUserInputs createUserInputs) {

        if (!createUserInputs.getBody().equals(EMPTY))
            return httpPost(createUserInputs.getCommonInputs(), CREATE_USER_REQUEST_URL, createUserInputs.getBody());

        JsonObject passwordProfile = new JsonObject();
        passwordProfile.addProperty(FORCE_CHANGE_PASSWORD, createUserInputs.getForceChangePassword());
        passwordProfile.addProperty(PASSWORD, createUserInputs.getPassword());

        JsonObject body = new JsonObject();
        body.addProperty(ACCOUNT_ENABLED, createUserInputs.getAccountEnabled());
        body.addProperty(DISPLAY_NAME, createUserInputs.getDisplayName());
        if (!createUserInputs.getOnPremisesImmutableId().equals(EMPTY))
            body.addProperty(ON_PREMISES_IMMUTABLE_ID, createUserInputs.getOnPremisesImmutableId());
        body.addProperty(MAIL_NICKNAME, createUserInputs.getMailNickname());
        body.addProperty(USER_PRINCIPAL_NAME, createUserInputs.getUserPrincipalName());
        body.add(PASSWORD_PROFILE, passwordProfile);

        return httpPost(createUserInputs.getCommonInputs(), CREATE_USER_REQUEST_URL, body.toString());
    }
}
