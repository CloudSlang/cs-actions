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
import io.cloudslang.content.microsoftAD.entities.GetMemberGroupsInputs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPost;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;
import static org.apache.commons.lang3.CharEncoding.UTF_8;


public class GetMemberGroupsService {

    public static Map<String, String> getMemberGroups(GetMemberGroupsInputs getMemberGroupsInputs) {

        JsonObject body = new JsonObject();
        body.addProperty(SECURITY_ENABLED_GROUPS, getMemberGroupsInputs.getSecurityEnabledGroups());

        return httpPost(getMemberGroupsInputs.getCommonInputs(),
                getUrl(getMemberGroupsInputs.getCommonInputs().getUserPrincipalName(),
                        getMemberGroupsInputs.getCommonInputs().getUserId()),
                body.toString());
    }

    @NotNull
    private static String getUrl(String userPrincipalName, String userId) {

        String finalUrl = USERS_URL + FORWARD_SLASH;

        if (!StringUtils.isEmpty(userPrincipalName)) {
            try {
                finalUrl += URLEncoder.encode(userPrincipalName.trim(), UTF_8) + GET_MEMBER_GROUPS;
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else
            finalUrl += userId.trim() + GET_MEMBER_GROUPS;

        return finalUrl;
    }

}
