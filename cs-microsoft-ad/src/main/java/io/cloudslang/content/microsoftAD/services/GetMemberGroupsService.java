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
import io.cloudslang.content.microsoftAD.entities.GetMemberGroupsInputs;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPost;
import static io.cloudslang.content.microsoftAD.utils.Constants.GET_MEMBER_GROUPS;
import static io.cloudslang.content.microsoftAD.utils.Constants.GET_MEMBER_GROUPS_REQUEST_URL;
import static io.cloudslang.content.microsoftAD.utils.Inputs.IsUserInGroup.SECURITY_ENABLED_ONLY;
import static org.apache.commons.lang3.StringUtils.EMPTY;


public class GetMemberGroupsService {

    public static Map<String, String> getMemberGroups(GetMemberGroupsInputs getMemberGroupsInputs) {

        if (!getMemberGroupsInputs.getBody().equals(EMPTY))
            return httpPost(getMemberGroupsInputs.getCommonInputs(), getUrl(getMemberGroupsInputs.getCommonInputs().getUserId()), getMemberGroupsInputs.getBody());

        JsonObject body = new JsonObject();
        body.addProperty(SECURITY_ENABLED_ONLY, getMemberGroupsInputs.getSecurityEnabledOnly());

        return httpPost(getMemberGroupsInputs.getCommonInputs(), getUrl(getMemberGroupsInputs.getCommonInputs().getUserId()), body.toString());
    }

    @NotNull
    private static String getUrl(String userId) {
        return GET_MEMBER_GROUPS_REQUEST_URL + userId + GET_MEMBER_GROUPS;
    }

}
