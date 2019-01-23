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
import io.cloudslang.content.office365.entities.*;
import io.cloudslang.content.office365.utils.PopulateUpdateUserBody;
import io.cloudslang.content.office365.utils.PopulateUserBody;
import org.apache.commons.lang3.StringUtils;
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
        httpClientInputs.setUrl(MANAGE_USER_REQUEST_URL);

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
    public static Map<String, String> listUsers(@NotNull final CreateUserInputs createUserInputs) {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = createUserInputs.getCommonInputs();
        httpClientInputs.setUrl(MANAGE_USER_REQUEST_URL);

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setContentType(APPLICATION_JSON);

        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        if (!StringUtils.isEmpty(createUserInputs.getoDataQuery())) {
            httpClientInputs.setQueryParams(getQueryParams(createUserInputs.getoDataQuery().replaceAll("\\s+", "")));
        }

        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> getUser(@NotNull final GetUserInputs getUserInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = getUserInputs.getCommonInputs();
        httpClientInputs.setUrl(getUserUrl(commonInputs.getUserPrincipalName(), commonInputs.getUserId(), getUserInputs.getoDataQuery()));

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setContentType(APPLICATION_JSON);

        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String getUserUrl(String userPrincipalName, String userId, String oDataQuery) throws Exception {
        String finalUrl;
        if (!StringUtils.isEmpty(userPrincipalName))
            finalUrl = GET_USER_REQUEST_URL + userPrincipalName;
        else
            finalUrl = GET_USER_REQUEST_URL + userId;
        if (!StringUtils.isEmpty(oDataQuery))
            finalUrl = finalUrl + SELECT_PATH + oDataQuery.replaceAll("\\s+", "");
        return finalUrl;
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

    private static String updateUserUrl(@NotNull final String userPrincipalNameToUpdate, @NotNull final String userIdToUpdate) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(updateUserPath(userPrincipalNameToUpdate, userIdToUpdate));

        return uriBuilder.build().toURL().toString();
    }


    @NotNull
    public static Map<String, String> deleteUser(@NotNull final DeleteUserInputs deleteUserInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = deleteUserInputs.getCommonInputs();
        httpClientInputs.setUrl(getDeleteUserUrl(deleteUserInputs));

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(DELETE);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setContentType(APPLICATION_JSON);

        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static String getDeleteUserUrl(@NotNull final DeleteUserInputs deleteUserInputs) {
        String finalUrl = DELETE_USER_REQUEST_URL;

        if (!deleteUserInputs.getCommonInputs().getUserPrincipalName().isEmpty())
            finalUrl = finalUrl + deleteUserInputs.getCommonInputs().getUserPrincipalName();
        else
            finalUrl = finalUrl + deleteUserInputs.getCommonInputs().getUserId();

        return finalUrl;
    }

    @NotNull
    public static Map<String, String> ListAttachment(@NotNull final ListAttachmentsInputs getListAttachmentInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = getListAttachmentInputs.getCommonInputs();
        httpClientInputs.setUrl(ListAttachmentsUrl(commonInputs.getUserPrincipalName(), commonInputs.getUserId(), getListAttachmentInputs.getMessageId()));

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setContentType(APPLICATION_JSON);

        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String ListAttachmentsUrl(String userPrincipalName, String userId, String messageId) throws Exception {
        String finalUrl;
        if (!StringUtils.isEmpty(userPrincipalName))
            finalUrl = GET_USER_REQUEST_URL + userPrincipalName;
        else
            finalUrl = GET_USER_REQUEST_URL + userId;
        if (!StringUtils.isEmpty(messageId))
            finalUrl = finalUrl + MESSAGES_PATH + messageId;
        finalUrl = finalUrl + ATTACHMENTS;
        return finalUrl;
    }


}
