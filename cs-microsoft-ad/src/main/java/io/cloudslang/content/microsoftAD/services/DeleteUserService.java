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

import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpDelete;
import static io.cloudslang.content.microsoftAD.utils.Constants.FORWARD_SLASH;
import static io.cloudslang.content.microsoftAD.utils.Constants.USERS_URL;
import static org.apache.commons.lang3.CharEncoding.UTF_8;


public class DeleteUserService {

    public static Map<String, String> deleteUser(@NotNull final AzureActiveDirectoryCommonInputs deleteUserInputs) throws Exception {

        return httpDelete(deleteUserInputs, getDeleteUserUrl(deleteUserInputs));

    }


    @NotNull
    public static String getDeleteUserUrl(@NotNull final AzureActiveDirectoryCommonInputs deleteUserInputs) {
        String finalUrl = USERS_URL + FORWARD_SLASH;

        if (!deleteUserInputs.getUserPrincipalName().isEmpty())
        {
            try {
                finalUrl += URLEncoder.encode(deleteUserInputs.getUserPrincipalName().trim(), UTF_8);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        else
            finalUrl += deleteUserInputs.getUserId().trim();

        return finalUrl;
    }

}
