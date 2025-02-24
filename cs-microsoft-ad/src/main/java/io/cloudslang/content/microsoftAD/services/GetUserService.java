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

import io.cloudslang.content.microsoftAD.entities.GetUserInputs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpGet;
import static io.cloudslang.content.microsoftAD.utils.Constants.FORWARD_SLASH;
import static io.cloudslang.content.microsoftAD.utils.Constants.USERS_URL;
import static org.apache.commons.lang3.CharEncoding.UTF_8;


public class GetUserService {

    public static Map<String, String> getUser(@NotNull final GetUserInputs getUserInputs) throws Exception {

        return httpGet(getUserInputs.getCommonInputs(), getUserUrl(getUserInputs.getODataQuery(), getUserInputs.getCommonInputs().getUserPrincipalName(), getUserInputs.getCommonInputs().getUserId()));

    }


    @NotNull
    private static String getUserUrl(String oDdataQuery, String userPrincipalName, String userId) {
        String finalUrl =  USERS_URL + FORWARD_SLASH;
        if (!StringUtils.isEmpty(userPrincipalName)){
            try {
                finalUrl += URLEncoder.encode(userPrincipalName.trim(), UTF_8);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        else
            finalUrl = finalUrl + userId.trim();

        if (!StringUtils.isEmpty(oDdataQuery))
            finalUrl += "?" + oDdataQuery;

        return finalUrl;
    }

}
