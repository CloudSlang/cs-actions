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


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.cloudslang.content.microsoftAD.entities.AssignUserLicenseInputs;
import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import io.cloudslang.content.microsoftAD.entities.RemoveUserLicenseInputs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPost;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.AssignUserLicense.INVALID_JSON_INPUT_DESC;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.RemoveUserLicense.INVALID_STRING_ARRAY_INPUT_DESC;
import static org.apache.commons.lang3.CharEncoding.UTF_8;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class AssignRemoveUserLicenseService {

    public static Map<String, String> assignUserLicense(AssignUserLicenseInputs assignedLicenseInputs) {

        AzureActiveDirectoryCommonInputs commonInputs = assignedLicenseInputs.getCommonInputs();
        JsonObject body = new JsonObject();
        try {

            JsonParser jsonParser = new JsonParser();
            JsonArray jsonArray = (JsonArray) jsonParser.parse(assignedLicenseInputs.getAssignedLicenses());
            body.add(ADD_LICENSES, jsonArray);
            body.add(REMOVE_LICENSES, new JsonArray());

            return httpPost(commonInputs, getUserUrl(commonInputs.getUserPrincipalName(),
                                                    commonInputs.getUserId()),
                                                    body.toString());

        }catch(Exception e)
        {
            Map<String, String> result = new HashMap<>();
            result.put(STATUS_CODE, EMPTY);
            result.put(RETURN_RESULT, INVALID_JSON_INPUT_DESC);
            result.put(EXCEPTION, e.toString());
            return result;
        }

    }

    public static Map<String, String> removeUserLicense(RemoveUserLicenseInputs removedLicenseInputs) {

        AzureActiveDirectoryCommonInputs commonInputs = removedLicenseInputs.getCommonInputs();
        JsonObject body = new JsonObject();
        try {

            Gson gson = new Gson();
            List<String> removedLicenses = Arrays.asList(removedLicenseInputs.getRemovedLicenses().split(",", -1));
            JsonArray removedLicensesJson = gson.toJsonTree(removedLicenses).getAsJsonArray();
            body.add(ADD_LICENSES, new JsonArray());
            body.add(REMOVE_LICENSES, removedLicensesJson);

            return httpPost(commonInputs, getUserUrl(commonInputs.getUserPrincipalName(),
                                                        commonInputs.getUserId()),
                                                        body.toString());

        }catch(Exception e)
        {
            Map<String, String> result = new HashMap<>();
            result.put(STATUS_CODE, EMPTY);
            result.put(RETURN_RESULT, INVALID_STRING_ARRAY_INPUT_DESC);
            result.put(EXCEPTION, e.toString());
            return result;
        }

    }

    @NotNull
    private static String getUserUrl(String userPrincipalName, String userId) {
        String finalUrl;
        if (!StringUtils.isEmpty(userPrincipalName)){
            try {
                finalUrl = USERS_URL + FORWARD_SLASH + URLEncoder.encode(userPrincipalName.trim(), UTF_8) + ASSIGN_LICENSE_URL;
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        else
            finalUrl = USERS_URL + FORWARD_SLASH + userId.trim() + ASSIGN_LICENSE_URL;
        return finalUrl;
    }


}


