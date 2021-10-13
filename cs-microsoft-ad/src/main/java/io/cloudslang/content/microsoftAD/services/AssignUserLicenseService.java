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


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.cloudslang.content.microsoftAD.entities.AssignUserLicenseInputs;
import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPost;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.AssignUserLicense.INVALID_JSON_INPUT_DESC;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class AssignUserLicenseService {

    public static Map<String, String> assignUserLicense(AssignUserLicenseInputs licenseInputs) {

        AzureActiveDirectoryCommonInputs commonInputs = licenseInputs.getCommonInputs();
        JsonObject body = new JsonObject();
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        try {
            JsonArray jsonArray = (JsonArray) jsonParser.parse(licenseInputs.getBody());
            body.add(ADD_LICENSES, jsonArray);
            body.add(REMOVE_LICENSES, new JsonArray());

            return httpPost(commonInputs, getUserUrl(commonInputs.getUserPrincipalName(), commonInputs.getUserId()), body.toString());
        }catch(Exception e)
        {
            Map<String, String> result = new HashMap<>();
            result.put(STATUS_CODE, EMPTY);
            result.put(RETURN_RESULT, INVALID_JSON_INPUT_DESC);
            result.put(EXCEPTION, e.toString());
            return result;
        }

    }

    @NotNull
    private static String getUserUrl(String userPrincipalName, String userId) {
        String finalUrl;
        if (!StringUtils.isEmpty(userPrincipalName))
            finalUrl = USERS_URL + FORWARD_SLASH + userPrincipalName + ASSIGN_LICENSE_URL;
        else
            finalUrl = USERS_URL + FORWARD_SLASH + userId + ASSIGN_LICENSE_URL;
        return finalUrl;
    }


}


