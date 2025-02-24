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

import io.cloudslang.content.microsoftAD.entities.GetUserLicenseDetailsInputs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpGet;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;

public class GetUserLicenseDetailsService {

    public static Map<String, String> getUserLicenseDetails(GetUserLicenseDetailsInputs getUserLicenseDetailsInputs) {

        return httpGet(getUserLicenseDetailsInputs.getCommonInputs(),
                getUrl(getUserLicenseDetailsInputs.getQueryParams(),
                        getUserLicenseDetailsInputs.getCommonInputs().getUserId())
        );
    }

    @NotNull
    private static String getUrl(String queryParams, String userId) {

        String finalUrl = USERS_URL + FORWARD_SLASH;

        if (!StringUtils.isEmpty(queryParams))
            finalUrl += userId.trim() + FORWARD_SLASH + LICENSE_DETAILS + QUERY + QUERY_PARAMS_SELECT + queryParams;
        else
            finalUrl += userId.trim() + FORWARD_SLASH + LICENSE_DETAILS;

        return finalUrl;
    }

}
