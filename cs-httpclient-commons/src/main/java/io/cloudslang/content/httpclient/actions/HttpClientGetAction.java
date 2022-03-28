/*
 * (c) Copyright 2022 Micro Focus
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
package io.cloudslang.content.httpclient.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.utils.Constants.HTTP_CLIENT_GET_ACTION;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.ALLOWED_CIPHERS_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.AUTH_TYPE_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.CONNECTIONS_MAX_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.CONNECTIONS_MAX_PER_ROUTE_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.CONNECT_TIMEOUT_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.CONTENT_TYPE_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.EXCEPTION_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.EXECUTION_TIMEOUT_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.FAILURE_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.HEADERS_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.HOST_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.HTTP_CLIENT_ACTION_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.KEEP_ALIVE_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.KEYSTORE_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.KEYSTORE_PASSWORD_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.PROTOCOL_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.PROXY_HOST_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.PROXY_PASSWORD_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.PROXY_PORT_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.PROXY_USERNAME_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.QUERY_PARAMS_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.RETURN_CODE_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.RETURN_RESULT_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.STATUS_CODE_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.SUCCESS_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.TLS_VERSION_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.TRUST_ALL_ROOTS_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.TRUST_KEYSTORE_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.TRUST_PASSWORD_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.X509_HOSTNAME_VERIFIER_DESC;
import static io.cloudslang.content.httpclient.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.httpclient.utils.HttpUtils.parseApiExceptionMessage;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.ALLOWED_CIPHERS;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.AUTH_TYPE;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.CONNECTIONS_MAX_PER_ROUTE;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.CONNECTIONS_MAX_TOTAL;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.CONNECT_TIMEOUT;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.CONTENT_TYPE;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.EXECUTION_TIMEOUT;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.HEADERS;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.HOST;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.KEEP_ALIVE;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.KEYSTORE;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.KEYSTORE_PASSWORD;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.PASSWORD;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.PROTOCOL;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.PROXY_HOST;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.PROXY_PASSWORD;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.PROXY_PORT;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.PROXY_USERNAME;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.QUERY_PARAMS;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.TLS_VERSION;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.TRUST_ALL_ROOTS;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.TRUST_KEYSTORE;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.TRUST_PASSWORD;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.USERNAME;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.X509_HOSTNAME_VERIFIER;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;

public class HttpClientGetAction {
    @Action(name = HTTP_CLIENT_GET_ACTION,
            description = HTTP_CLIENT_ACTION_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = HOST, description = HOST_DESC) String host,
                                       @Param(value = PROTOCOL, required = true, description = PROTOCOL_DESC) String protocol,

                                       @Param(value = AUTH_TYPE, required = true, description = AUTH_TYPE_DESC) String authType,
                                       @Param(value = USERNAME, required = true, description = PROTOCOL_DESC) String username,
                                       @Param(value = PASSWORD, required = true, description = PROTOCOL_DESC) String password,

                                       @Param(value = PROXY_HOST, required = true, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, required = true, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, required = true, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, required = true, description = PROXY_PASSWORD_DESC) String proxyPassword,
                                       @Param(value = TLS_VERSION, required = true, description = TLS_VERSION_DESC) String tlsVersion,
                                       @Param(value = ALLOWED_CIPHERS, required = true, description = ALLOWED_CIPHERS_DESC) String allowedCiphers,
                                       @Param(value = TRUST_ALL_ROOTS, required = true, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, required = true, description = X509_HOSTNAME_VERIFIER_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, required = true, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, required = true, description = TRUST_PASSWORD_DESC) String trustPassword,
                                       @Param(value = KEYSTORE, required = true, description = KEYSTORE_DESC) String keystore,
                                       @Param(value = KEYSTORE_PASSWORD, required = true, description = KEYSTORE_PASSWORD_DESC) String keystorePassword,
                                       @Param(value = CONNECT_TIMEOUT, required = true, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = EXECUTION_TIMEOUT, required = true, description = EXECUTION_TIMEOUT_DESC) String executionTimeout,
                                       @Param(value = KEEP_ALIVE, required = true, description = KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, required = true, description = CONNECTIONS_MAX_PER_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, required = true, description = CONNECTIONS_MAX_DESC) String connectionsMaxTotal,

                                       @Param(value = HEADERS, required = true, description = HEADERS_DESC) String headers,
                                       @Param(value = QUERY_PARAMS, required = true, description = QUERY_PARAMS_DESC) String queryParams,
                                       @Param(value = CONTENT_TYPE, required = true, description = CONTENT_TYPE_DESC) String contentType) {

//        final List<String> exceptionMessages = verifyAuthorizationInputs(loginType, clientId, clientSecret, username, password, proxyPort);
//        if (!exceptionMessages.isEmpty()) {
//            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
//        }

        try {
            Map<String, String> result = HttpClientService.execute(HttpClientInputs.builder()
                    .host(host)
                    //.protocol(protocol)

                    .authType(authType)
                    .username(username)
                    .password(password)
                    .proxyHost(proxyHost)
                    .proxyPort(proxyPort)
                    .proxyUsername(proxyUsername)
                    .proxyPassword(proxyPassword)
                    .tlsVersion(tlsVersion)
                    .allowedCiphers(allowedCiphers)
                    .trustAllRoots(trustAllRoots)
                    .x509HostnameVerifier(x509HostnameVerifier)
                    .trustKeystore(trustKeystore)
                    .trustPassword(trustPassword)
                    .keystore(keystore)
                    .keystorePassword(keystore)
                    .connectTimeout(connectTimeout)
                    .executionTimeout(executionTimeout)
                    .keepAlive(keepAlive)
                    .connectionsMaxPerRoute(connectionsMaxPerRoute)
                    .connectionsMaxTotal(connectionsMaxTotal)

                    .headers(headers)
                    .queryParams(queryParams)
                    .contentType(contentType)

                    .build());

            Map<String, String> finalResult = getOperationResults(result, result.get(RETURN_RESULT), result.get(RETURN_RESULT));
            parseApiExceptionMessage(finalResult);

            return finalResult;

        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
