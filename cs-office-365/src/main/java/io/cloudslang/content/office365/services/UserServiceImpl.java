/*
 * (c) Copyright 2019 Micro Focus, L.P.
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

package io.cloudslang.content.office365.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.office365.entities.CreateUserInputs;
import io.cloudslang.content.office365.entities.Office365CommonInputs;
import io.cloudslang.content.office365.utils.PopulateUpdateUserBody;
import io.cloudslang.content.office365.utils.PopulateUserBody;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.HttpUtils.*;

public class UserServiceImpl {


    @NotNull
    public static Map<String, String> createUser(@NotNull final CreateUserInputs createUserInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = createUserInputs.getCommonInputs();
        httpClientInputs.setUrl(CREATE_USER_REQUEST_URL);

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setContentType(APPLICATION_JSON);
        httpClientInputs.setBody(PopulateUserBody.populateUserBody(createUserInputs));

        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> updateUser(@NotNull final CreateUserInputs createUserInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = createUserInputs.getCommonInputs();
        httpClientInputs.setUrl(updateUserUrl(createUserInputs.getCommonInputs().getUserPrincipalName(), createUserInputs.getCommonInputs().getUserId()));

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(PATCH);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setContentType(APPLICATION_JSON);
        httpClientInputs.setBody(PopulateUpdateUserBody.populateUpdateUserBody(createUserInputs));

        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        return new HttpClientService().execute(httpClientInputs);
    }

    private static String updateUserUrl(String userPrincipalNameToUpdate, String userIdToUpdate) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(updateUserPath(userPrincipalNameToUpdate, userIdToUpdate));

        return uriBuilder.build().toURL().toString();
    }

}
