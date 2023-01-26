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
package io.cloudslang.content.cyberark.actions.accounts;

import com.hp.oo.sdk.content.annotations.*;
import com.hp.oo.sdk.content.plugin.ActionMetadata.*;
import com.hp.oo.sdk.content.plugin.*;
import io.cloudslang.content.constants.*;
import io.cloudslang.content.httpclient.actions.*;
import io.cloudslang.content.utils.*;

import java.util.*;

import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.ALLOWED_CIPHERS;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.CONNECTIONS_MAX_PER_ROUTE;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.CONNECTIONS_MAX_TOTAL;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.CONNECT_TIMEOUT;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.EXECUTION_TIMEOUT;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.HOST;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.KEEP_ALIVE;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.KEYSTORE;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.KEYSTORE_PASSWORD;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.PROTOCOL;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.PROXY_HOST;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.PROXY_PASSWORD;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.PROXY_PORT;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.PROXY_USERNAME;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.TLS_VERSION;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.TRUST_ALL_ROOTS;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.TRUST_KEYSTORE;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.TRUST_PASSWORD;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.X509_HOSTNAME_VERIFIER;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.*;
import static io.cloudslang.content.cyberark.utils.Constants.GetAccountDetails.*;
import static io.cloudslang.content.cyberark.utils.Constants.OtherConstants.CONTENT_TYPE;
import static io.cloudslang.content.cyberark.utils.Constants.OtherConstants.*;
import static io.cloudslang.content.cyberark.utils.CyberarkUtils.*;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.*;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.*;
import static org.apache.commons.lang3.StringUtils.*;

public class GetAccountDetails {

    @Action(name = GET_ACCOUNT_DETAILS,
            description = GET_ACCOUNT_DETAILS_DESCRIPTION,
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(STATUS_CODE),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(
            @Param(value = HOST, description = HOST_DESCRIPTION, required = true) String hostname,
            @Param(value = PROTOCOL, description = PROTOCOL_DESCRIPTION) String protocol,
            @Param(value = AUTH_TOKEN, description = AUTH_TOKEN_DESCRIPTION, required = true) String authToken,
            @Param(value = ACCOUNT_ID, description = ACCOUNT_ID_DESCRIPTION) String accountId,
            @Param(value = PROXY_HOST, description = PROXY_HOST_DESCRIPTION) String proxyHost,
            @Param(value = PROXY_PORT, description = PROXY_PORT_DESCRIPTION) String proxyPort,
            @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESCRIPTION) String proxyUsername,
            @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESCRIPTION) String proxyPassword,
            @Param(value = TLS_VERSION, description = TLS_VERSION_DESCRIPTION) String tlsVersion,
            @Param(value = ALLOWED_CIPHERS, description = ALLOWED_CIPHERS_DESCRIPTION) String allowedCiphers,
            @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESCRIPTION) String trustAllRoots,
            @Param(value = X509_HOSTNAME_VERIFIER, description = X509_HOSTNAME_VERIFIER_DESCRIPTION) String x509HostnameVerifier,
            @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESCRIPTION) String trustKeystore,
            @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESCRIPTION) String trustPassword,
            @Param(value = KEYSTORE, description = KEYSTORE_DESCRIPTION) String keystore,
            @Param(value = KEYSTORE_PASSWORD, encrypted = true, description = KEYSTORE_PASSWORD_DESCRIPTION) String keystorePassword,
            @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESCRIPTION) String connectTimeout,
            @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESCRIPTION) String executionTimeout,
            @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESCRIPTION) String keepAlive,
            @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONNECTIONS_MAX_PER_ROUTE_DESCRIPTION) String connectionsMaxPerRoute,
            @Param(value = CONNECTIONS_MAX_TOTAL, description = CONNECTIONS_MAX_TOTAL_DESCRIPTION) String connectionsMaxTotal,
            @Param(value = SESSION_COOKIES, description = SESSION_COOKIES_DESC) SerializableSessionObject sessionCookies,
            @Param(value = SESSION_CONNECTION_POOL, description = SESSION_CONNECTION_POOL_DESC) GlobalSessionObject sessionConnectionPool) {

        try {

            validateProtocol(protocol);

            Map<String, String> result = new HttpClientGetAction().execute(
                    protocol + PROTOCOL_DELIMITER + hostname + GET_ACCOUNT_DETAILS_ENDPOINT + accountId,
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
                    keystore,
                    keystorePassword,
                    keepAlive,
                    connectionsMaxPerRoute,
                    connectionsMaxTotal,
                    EMPTY,
                    EMPTY,
                    CONTENT_TYPE + APPLICATION_JSON + COMMA + AUTHORIZATION + authToken,
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

            processHttpResult(result);
            return result;

        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }
    }
}