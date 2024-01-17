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
import io.cloudslang.content.microsoftAD.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPatch;
import static io.cloudslang.content.microsoftAD.utils.Constants.FORWARD_SLASH;
import static io.cloudslang.content.microsoftAD.utils.Constants.USERS_URL;
import static org.apache.commons.lang3.CharEncoding.UTF_8;

public class EnableDisableUserService {

    public static Map<String, String> enableDisableUser(AzureActiveDirectoryCommonInputs commonInputs, boolean enabled) {

        JsonObject body = new JsonObject();
        body.addProperty(Constants.ACCOUNT_ENABLED, enabled);
        return httpPatch(commonInputs, getUserUrl(commonInputs.getUserPrincipalName(), commonInputs.getUserId()), body.toString());

    }

    @NotNull
    private static String getUserUrl(String userPrincipalName, String userId) {
        String finalUrl = USERS_URL + FORWARD_SLASH;
        if (!StringUtils.isEmpty(userPrincipalName)) {
            try {
                finalUrl += URLEncoder.encode(userPrincipalName.trim(), UTF_8);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        else
            finalUrl += userId.trim();
        return finalUrl;
    }


}


