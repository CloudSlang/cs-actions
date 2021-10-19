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
package io.cloudslang.content.microsoftAD.actions.licenseManagement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import io.cloudslang.content.microsoftAD.entities.GetUserInputs;
import io.cloudslang.content.microsoftAD.utils.Inputs;
import io.cloudslang.content.microsoftAD.utils.Outputs.OutputNames;
import io.cloudslang.content.utils.StringUtilities;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.microsoftAD.services.GetSubscribedSkusService.getSubscribedSkus;
import static io.cloudslang.content.microsoftAD.services.GetUserService.getUser;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.Common.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.ListAvailableSkus.*;
import static io.cloudslang.content.microsoftAD.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.microsoftAD.utils.HttpUtils.parseApiExceptionMessage;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.AUTH_TOKEN;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.USER_PRINCIPAL_NAME;
import static io.cloudslang.content.microsoftAD.utils.InputsValidation.verifyCommonUserInputs;
import static io.cloudslang.content.microsoftAD.utils.InputsValidation.verifyGetInputs;
import static io.cloudslang.content.microsoftAD.utils.Outputs.OutputNames.AVAILABLE_SKUS_LIST;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.*;

public class ListAvailableSkus {
    @Action(name = LIST_AVAILABLE_SKUS_NAME,
            description = LIST_AVAILABLE_SKUS_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = LIST_AVAILABLE_SKUS_RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = OutputNames.STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = AVAILABLE_SKUS_LIST, description = AVAILABLE_SKUS_LIST_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = LIST_AVAILABLE_SKUS_SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = LIST_AVAILABLE_SKUS_FAILURE_DESC)
            })

    public Map<String, String> execute(@Param(value = AUTH_TOKEN, required = true, description = AUTH_TOKEN_DESC) String authToken,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,

                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,

                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = SOCKET_TIMEOUT, description = SOCKET_TIMEOUT_DESC) String socketTimeout,
                                       @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONN_MAX_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = CONN_MAX_TOTAL_DESC) String connectionsMaxTotal) {


        //inputs validation
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
        trustKeystore = defaultIfEmpty(trustKeystore, DEFAULT_JAVA_KEYSTORE);
        trustPassword = defaultIfEmpty(trustPassword, CHANGEIT);
        connectTimeout = defaultIfEmpty(connectTimeout, ZERO);
        socketTimeout = defaultIfEmpty(socketTimeout, ZERO);
        keepAlive = defaultIfEmpty(keepAlive, BOOLEAN_FALSE);
        connectionsMaxPerRoute = defaultIfEmpty(connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE_CONST);
        connectionsMaxTotal = defaultIfEmpty(connectionsMaxTotal, CONNECTIONS_MAX_TOTAL_CONST);


        final List<String> exceptionMessages = verifyCommonUserInputs(proxyPort, trustAllRoots, x509HostnameVerifier,
                connectTimeout, socketTimeout, keepAlive,
                connectionsMaxPerRoute, connectionsMaxTotal);

        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            final Map<String, String> result = getSubscribedSkus(AzureActiveDirectoryCommonInputs.builder()
                            .authToken(authToken)
                            .proxyHost(proxyHost)
                            .proxyPort(proxyPort)
                            .proxyUsername(proxyUsername)
                            .proxyPassword(proxyPassword)
                            .connectionsMaxTotal(connectionsMaxTotal)
                            .connectionsMaxPerRoute(connectionsMaxPerRoute)
                            .keepAlive(keepAlive)
                            .connectTimeout(connectTimeout)
                            .socketTimeout(socketTimeout)
                            .trustAllRoots(trustAllRoots)
                            .userId("")
                            .userPrincipalName("")
                            .x509HostnameVerifier(x509HostnameVerifier)
                            .trustKeystore(trustKeystore)
                            .trustPassword(trustPassword)
                            .build());

            final String returnMessage = result.get(RETURN_RESULT);
            final Map<String, String> results = getOperationResults(result, LIST_AVAILABLE_SKUS_SUCCESS_DESC, result.get(RETURN_RESULT));

            if (!result.get(STATUS_CODE).isEmpty()) {
                final Integer statusCode = Integer.parseInt(result.get(STATUS_CODE));


                if (statusCode >= 200 && statusCode < 300) {
                    try
                    {
                        final JsonParser parser = new JsonParser();
                        final JsonObject responseJson = parser.parse(returnMessage).getAsJsonObject();
                        JsonArray jsonArray = responseJson.get(VALUE).getAsJsonArray();
                        StringBuilder skuListString = new StringBuilder();
                        for(int i=0;i<jsonArray.size();i++)
                        {
                                JsonObject skuIdObject = jsonArray.get(i).getAsJsonObject();
                                String skuIdString = skuIdObject.get(SKUID).getAsString();
                                skuListString.append(skuIdString.substring(1, skuIdString.length()-1) + SIMPLE_COMMA);

                        }
                        if(skuListString.toString().length() > 0)
                            results.put(AVAILABLE_SKUS_LIST, skuListString.toString()
                                .substring(0,skuListString.toString().length()-1)); //take last comma
                        else
                            results.put(AVAILABLE_SKUS_LIST, EMPTY);
                    }
                    catch(Exception e)
                    {
                        results.put(AVAILABLE_SKUS_LIST, EMPTY);
                    }
                }
            }

            parseApiExceptionMessage(results);

            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
