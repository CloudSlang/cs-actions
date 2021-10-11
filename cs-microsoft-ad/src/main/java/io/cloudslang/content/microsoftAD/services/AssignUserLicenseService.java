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
import io.cloudslang.content.microsoftAD.actions.licenseManagement.AssignUserLicense;
import io.cloudslang.content.microsoftAD.entities.AssignUserLicenseInputs;
import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import io.cloudslang.content.microsoftAD.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPatch;
import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPost;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;

public class AssignUserLicenseService {

    public static Map<String, String> assignUserLicense(AssignUserLicenseInputs licenseInputs) {

        AzureActiveDirectoryCommonInputs commonInputs = licenseInputs.getCommonInputs();
        return httpPost(commonInputs, getUserUrl(commonInputs.getUserPrincipalName(), commonInputs.getUserId()), licenseInputs.getBody().toString());

    }



    @NotNull
    private static String getUserUrl(String userPrincipalName, String userId) {
        String finalUrl;
        if (!StringUtils.isEmpty(userPrincipalName))
            finalUrl = ASSIGN_USER_LICENSE_URL_PRE + userPrincipalName + ASSIGN_USER_LICENSE_URL_POST;
        else
            finalUrl = ASSIGN_USER_LICENSE_URL_PRE + userId + ASSIGN_USER_LICENSE_URL_POST;
        return finalUrl;
    }


}


