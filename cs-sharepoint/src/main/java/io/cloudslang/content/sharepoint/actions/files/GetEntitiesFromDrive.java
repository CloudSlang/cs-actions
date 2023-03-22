/*
 * (c) Copyright 2023 Micro Focus, L.P.
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
package io.cloudslang.content.sharepoint.actions.files;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.actions.HttpClientGetAction;
import io.cloudslang.content.sharepoint.utils.Constants;
import io.cloudslang.content.sharepoint.utils.Descriptions;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.sharepoint.services.SharepointService.processHttpGetEntitiesFromDrive;
import static io.cloudslang.content.sharepoint.services.SharepointService.processHttpResult;
import static io.cloudslang.content.sharepoint.utils.Constants.*;
import static io.cloudslang.content.sharepoint.utils.Descriptions.Common.*;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetEntitiesFromDrive.NAME;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetEntitiesFromDrive.*;
import static io.cloudslang.content.sharepoint.utils.Inputs.CommonInputs.AUTH_TOKEN;
import static io.cloudslang.content.sharepoint.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.sharepoint.utils.Inputs.GetEntitiesFromDrive.ENTITIES_TYPE;
import static io.cloudslang.content.sharepoint.utils.Inputs.GetEntitiesFromDrive.PATH;
import static io.cloudslang.content.sharepoint.utils.InputsValidation.addVerifyEntitiesType;
import static io.cloudslang.content.sharepoint.utils.InputsValidation.verifyCommonInputs;
import static io.cloudslang.content.sharepoint.utils.Outputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class GetEntitiesFromDrive {
    @Action(name = NAME,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = ENTITY_IDS, description = ENTITY_IDS_DESC),
                    @Output(value = ENTITY_URLS, description = ENTITY_URLS_DESC),
                    @Output(value = ENTITY_TYPES, description = ENTITY_TYPES_DESC),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(
            @Param(value = AUTH_TOKEN, description = AUTH_TOKEN_DESC, required = true, encrypted = true) String authToken,
            @Param(value = DRIVE_ID, description = DRIVE_ID_DESC, required = true) String driveId,
            @Param(value = PATH, description = PATH_DESC) String path,
            @Param(value = ENTITIES_TYPE, description = ENTITIES_TYPE_DESC) String entitiesType,

            @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
            @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
            @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
            @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,

            @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
            @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
            @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
            @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,
            @Param(value = TLS_VERSION, description = TLS_VERSION_DESCRIPTION) String tlsVersion,
            @Param(value = ALLOWED_CIPHERS, description = ALLOWED_CIPHERS_DESCRIPTION) String allowedCiphers,
            @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
            @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String executionTimeout,
            @Param(value = SESSION_COOKIES, description = Descriptions.Common.SESSION_COOKIES_DESC) SerializableSessionObject sessionCookies,
            @Param(value = SESSION_CONNECTION_POOL, description = Descriptions.Common.SESSION_CONNECTION_POOL_DESC)
            GlobalSessionObject sessionConnectionPool) {

        try {

            proxyHost = defaultIfEmpty(proxyHost, EMPTY);
            proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
            proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
            proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
            trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
            x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
            connectTimeout = defaultIfEmpty(connectTimeout, DEFAULT_TIMEOUT);
            executionTimeout = defaultIfEmpty(executionTimeout, DEFAULT_TIMEOUT);
            entitiesType = defaultIfEmpty(entitiesType, ALL);

            List<String> exceptionMessages = verifyCommonInputs(proxyPort, trustAllRoots, x509HostnameVerifier, connectTimeout, executionTimeout);
            exceptionMessages = addVerifyEntitiesType(exceptionMessages, entitiesType, ENTITIES_TYPE);
            if (!exceptionMessages.isEmpty())
                return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));

            String endpoint = path == null || path.isEmpty() ? ROOT_CHILDREN_ENDPOINT : ROOT_PATH_ENDPOINT + path + CHILDREN_PATH_ENDPOINT;

            Map<String, String> result = new HttpClientGetAction().execute(
                    GRAPH_API_ENDPOINT + DRIVES_ENDPOINT + driveId + endpoint,
                    ANONYMOUS,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    proxyHost,
                    proxyPort,
                    proxyUsername,
                    proxyPassword,
                    tlsVersion,
                    allowedCiphers,
                    trustAllRoots,
                    x509HostnameVerifier,
                    trustKeystore,
                    trustPassword,
                    EMPTY,
                    EMPTY,
                    FALSE,
                    CONNECTIONS_MAX_PER_ROUTE_CONST,
                    CONNECTIONS_MAX_TOTAL_CONST,
                    EMPTY,
                    EMPTY,
                    AUTHORIZATION_BEARER + authToken,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    connectTimeout,
                    EMPTY,
                    executionTimeout,
                    sessionCookies,
                    sessionConnectionPool
            );

            processHttpResult(result, EXCEPTION_DESC);

            if (result.get(RETURN_CODE).equals(NEGATIVE_RETURN_CODE))
                return result;

            JsonObject returnResult = JsonParser.parseString(result.get(RETURN_RESULT)).getAsJsonObject();

            String finalProxyHost = proxyHost;
            String finalProxyPort = proxyPort;
            String finalProxyUsername = proxyUsername;
            String finalProxyPassword = proxyPassword;
            String finalX509HostnameVerifier = x509HostnameVerifier;
            String finalTrustAllRoots = trustAllRoots;
            String finalExecutionTimeout = executionTimeout;
            String finalConnectTimeout = connectTimeout;
            String finalEntitiesType = entitiesType;

            StreamSupport
                    .stream(returnResult.get(VALUE).getAsJsonArray().spliterator(), true)
                    .filter(entity -> entity.getAsJsonObject().has(FOLDER))
                    .forEach(folder -> {

                            Map<String, String> folderResult = execute(
                                    authToken,
                                    driveId,
                                    path + SLASH + folder.getAsJsonObject().get(Constants.NAME).getAsString(),
                                    finalEntitiesType,
                                    finalProxyHost,
                                    finalProxyPort,
                                    finalProxyUsername,
                                    finalProxyPassword,
                                    finalTrustAllRoots,
                                    finalX509HostnameVerifier,
                                    trustKeystore,
                                    trustPassword,
                                    tlsVersion,
                                    allowedCiphers,
                                    finalConnectTimeout,
                                    finalExecutionTimeout,
                                    sessionCookies,
                                    sessionConnectionPool
                            );

                            returnResult
                                    .get(VALUE)
                                    .getAsJsonArray()
                                    .addAll(JsonParser.parseString(folderResult.get(RETURN_RESULT)).getAsJsonObject().get(VALUE).getAsJsonArray());
                    });

            result.put(RETURN_RESULT, returnResult.toString());

            processHttpGetEntitiesFromDrive(result, EXCEPTION_DESC, entitiesType);
            return result;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
