/*
 * Copyright 2021-2024 Open Text
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
import io.cloudslang.content.microsoftAD.entities.CommonUserInputs;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPatch;
import static io.cloudslang.content.microsoftAD.utils.Constants.FORWARD_SLASH;
import static io.cloudslang.content.microsoftAD.utils.Constants.USERS_URL;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.*;
import static org.apache.commons.lang3.CharEncoding.UTF_8;

public class ResetUserPasswordService {

    public static Map<String, String> resetUserPassword(CommonUserInputs commonUserInputs) {

        JsonObject passwordProfile = new JsonObject();
        passwordProfile.addProperty(FORCE_CHANGE_PASSWORD, commonUserInputs.getForceChangePassword());
        passwordProfile.addProperty(PASSWORD, commonUserInputs.getPassword());

        JsonObject body = new JsonObject();
        body.add(PASSWORD_PROFILE, passwordProfile);

        return httpPatch(commonUserInputs.getCommonInputs(), getResetUserPasswordUrl(commonUserInputs.getCommonInputs()), body.toString());
    }

    @NotNull
    public static String getResetUserPasswordUrl(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) {

        String finalUrl = USERS_URL + FORWARD_SLASH;

        if (!commonInputs.getUserPrincipalName().isEmpty())
        {
            try {
                finalUrl += URLEncoder.encode(commonInputs.getUserPrincipalName(), UTF_8);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        else
            finalUrl += commonInputs.getUserId().trim();

        return finalUrl;
    }
}
