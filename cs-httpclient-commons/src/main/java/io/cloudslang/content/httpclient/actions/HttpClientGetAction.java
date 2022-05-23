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
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.httpclient.utils.ErrorHandler;
import io.cloudslang.content.utils.StringUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.utils.Constants.*;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.*;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.*;
import static io.cloudslang.content.httpclient.utils.InputsValidator.verifyHttpCommonInputs;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class HttpClientGetAction {
    @Action(name = HTTP_CLIENT_GET_ACTION,
            description = HTTP_CLIENT_ACTION_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RESPONSE_HEADERS, description = RESPONSE_HEADERS_DESC),
                    @Output(value = FINAL_LOCATION, description = FINAL_LOCATION_DESC),
                    @Output(value = REASON_PHRASE, description = REASON_PHRASE_DESC),
                    @Output(value = PROTOCOL_VERSION, description = PROTOCOL_VERSION_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = HOST, required = true, description = HOST_DESC) String host,

                                       @Param(value = AUTH_TYPE, description = AUTH_TYPE_DESC) String authType,
                                       @Param(value = USERNAME, description = PROTOCOL_DESC) String username,
                                       @Param(value = PASSWORD, encrypted = true, description = PROTOCOL_DESC) String password,
                                       @Param(value = PREEMPTIVE_AUTH, description = PREEMPTIVE_AUTH_DESC) String preemptiveAuth,

                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,

                                       @Param(value = TLS_VERSION, description = TLS_VERSION_DESC) String tlsVersion,
                                       @Param(value = ALLOWED_CIPHERS, description = ALLOWED_CIPHERS_DESC) String allowedCiphers,
                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_HOSTNAME_VERIFIER_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,
                                       @Param(value = KEYSTORE, description = KEYSTORE_DESC) String keystore,
                                       @Param(value = KEYSTORE_PASSWORD, encrypted = true, description = KEYSTORE_PASSWORD_DESC) String keystorePassword,

                                       @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONNECTIONS_MAX_PER_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = CONNECTIONS_MAX_DESC) String connectionsMaxTotal,

                                       @Param(value = USE_COOKIES, description = USE_COOKIES_DESC) String useCookies,
                                       @Param(value = FOLLOW_REDIRECTS, description = FOLLOW_REDIRECTS_DESC) String followRedirects,
                                       @Param(value = HEADERS, description = HEADERS_DESC) String headers,
                                       @Param(value = DESTINATION_FILE, description = DESTINATION_FILE_DESC) String destinationFile,

                                       @Param(value = RESPONSE_CHARACTER_SET, description = RESPONSE_CHARACTER_SET_DESC) String responseCharacterSet,
                                       @Param(value = QUERY_PARAMS, description = QUERY_PARAMS_DESC) String queryParams,
                                       @Param(value = QUERY_PARAMS_ARE_URLENCODED, description = QUERY_PARAMS_ARE_URLENCODED_DESC) String queryParamsAreURLEncoded,
                                       @Param(value = QUERY_PARAMS_ARE_FORM_ENCODED, description = QUERY_PARAMS_ARE_FORM_ENCODED_DESC) String queryParamsAreFormEncoded,

                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = RESPONSE_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String responseTimeout,
                                       @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String executionTimeout,

                                       @Param(value = SESSION_COOKIES, description = SESSION_COOKIES_DESC) SerializableSessionObject sessionCookies,
                                       @Param(value = SESSION_CONNECTION_POOL, description = SESSION_CONNECTION_POOL_DESC) GlobalSessionObject sessionConnectionPool) {

        preemptiveAuth = defaultIfEmpty(preemptiveAuth, BOOLEAN_TRUE);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);

        tlsVersion = defaultIfEmpty(tlsVersion, TLSv12);
        allowedCiphers = defaultIfEmpty(allowedCiphers, DEFAULT_ALLOWED_CIPHERS);

        keepAlive = defaultIfEmpty(keepAlive, BOOLEAN_FALSE);
        connectionsMaxPerRoute = defaultIfEmpty(connectionsMaxPerRoute, DEFAULT_CONNECTIONS_MAX_PER_ROUTE);
        connectionsMaxTotal = defaultIfEmpty(connectionsMaxTotal, DEFAULT_CONNECTIONS_MAX_TOTAL);

        responseCharacterSet = defaultIfEmpty(responseCharacterSet, UTF_8);
        queryParamsAreFormEncoded = defaultIfEmpty(queryParamsAreFormEncoded, BOOLEAN_TRUE);
        queryParamsAreURLEncoded = defaultIfEmpty(queryParamsAreURLEncoded, BOOLEAN_FALSE);

        useCookies = defaultIfEmpty(useCookies, BOOLEAN_TRUE);
        followRedirects = defaultIfEmpty(followRedirects, BOOLEAN_TRUE);

        connectTimeout = defaultIfEmpty(connectTimeout, DEFAULT_CONNECT_TIMEOUT);
        responseTimeout = defaultIfEmpty(responseTimeout, DEFAULT_CONNECT_TIMEOUT);
        executionTimeout = defaultIfEmpty(executionTimeout, DEFAULT_EXECUTION_TIMEOUT);

        final List<String> exceptionMessages = verifyHttpCommonInputs(authType, preemptiveAuth, proxyPort, tlsVersion,
                trustAllRoots, x509HostnameVerifier, useCookies, keepAlive, connectionsMaxPerRoute,
                connectionsMaxTotal, followRedirects, connectTimeout, responseTimeout, connectTimeout);
        if (!exceptionMessages.isEmpty())
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));

        HttpClientInputs httpClientInputs = HttpClientInputs.builder()
                .host(host)
                .method(GET)

                .authType(authType)
                .username(username)
                .password(password)
                .preemptiveAuth(preemptiveAuth)

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
                .keystorePassword(keystorePassword)

                .keepAlive(keepAlive)
                .connectionsMaxPerRoute(connectionsMaxPerRoute)
                .connectionsMaxTotal(connectionsMaxTotal)

                .useCookies(useCookies)
                .followRedirects(followRedirects)
                .headers(headers)
                .destinationFile(destinationFile)

                .responseCharacterSet(responseCharacterSet)
                .queryParams(queryParams)
                .queryParamsAreFormEncoded(queryParamsAreFormEncoded)
                .queryParamsAreURLEncoded(queryParamsAreURLEncoded)

                .connectTimeout(connectTimeout)
                .responseTimeout(responseTimeout)
                .executionTimeout(executionTimeout)

                .cookieStoreSessionObject(sessionCookies)
                .connectionPoolSessionObject(sessionConnectionPool)

                .build();

        try {
            return HttpClientService.execute(httpClientInputs);
        } catch (Exception exception) {
            return ErrorHandler.handleErrors(exception, new HashMap<>());
        }
    }
}
