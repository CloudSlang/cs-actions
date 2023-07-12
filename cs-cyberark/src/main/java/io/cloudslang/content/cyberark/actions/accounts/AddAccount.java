/*
 * Copyright 2022-2023 Open Text
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

package io.cloudslang.content.cyberark.actions.accounts;


import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.cyberark.utils.StringUtils;
import io.cloudslang.content.httpclient.actions.HttpClientPostAction;
import io.cloudslang.content.utils.OutputUtilities;
import net.minidev.json.JSONObject;

import java.util.Map;

import static io.cloudslang.content.cyberark.utils.Constants.AddAccountConstants.*;
import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.*;
import static io.cloudslang.content.cyberark.utils.Constants.OtherConstants.*;
import static io.cloudslang.content.cyberark.utils.CyberarkUtils.processHttpResult;
import static io.cloudslang.content.cyberark.utils.CyberarkUtils.validateProtocol;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.SESSION_CONNECTION_POOL_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.SESSION_COOKIES_DESC;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.SESSION_CONNECTION_POOL;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.SESSION_COOKIES;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class AddAccount {

    @Action(name = ADD_ACCOUNT,
            description = ADD_ACCOUNT_DESCRIPTION,
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
            @Param(value = HOST, description = HOST_DESCRIPTION, required = true) String hostName,
            @Param(value = PROTOCOL, description = PROTOCOL_DESCRIPTION) String protocol,
            @Param(value = AUTH_TOKEN, description = AUTH_TOKEN_DESCRIPTION, required = true) String authToken,
            @Param(value = NAME, description = NAME_DESCRIPTION) String name,
            @Param(value = ADDRESS, description = ADDRESS_DESCRIPTION, required = true) String address,
            @Param(value = USERNAME, description = USERNAME_DESCRIPTION,required = true) String userName,
            @Param(value = PLATFORM_ID, description = PLATFORM_ID_DESCRIPTION, required = true) String platformId,
            @Param(value = SAFE_NAME, description = SAFE_NAME_DESCRIPTION, required = true) String safeName,
            @Param(value = SECRET_TYPE, description = SECRET_TYPE_DESCRIPTION) String secretType,
            @Param(value = SECRET, description = SECRET_DESCRIPTION) String secret,
            @Param(value = PLATFORM_ACCOUNT_PROPERTIES, description = PLATFORM_ACCOUNT_PROPERTIES_DESCRIPTION) String platformAccountProperties,
            @Param(value = SECRET_MANAGEMENT, description = SECRET_MANAGEMENT_DESCRIPTION) String secretManagement,
            @Param(value = REMOTE_MACHINE_ACCESS, description = REMOTE_MACHINE_ACCESS_DESCRIPTION) String remoteMachineAccess,
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
            
            JSONObject body = new JSONObject();

            body.put(PLATFORM_ID, platformId);
            body.put(SAFE_NAME, safeName);
            body.put(ADDRESS, address);
            body.put(USERNAME, userName);

            if (!StringUtils.isEmpty(name))
                body.put(NAME, name);

            if (!StringUtils.isEmpty(secretType))
                body.put(SECRET_TYPE, secretType);

            if (!StringUtils.isEmpty(secret))
                body.put(SECRET, secret);

            if (!StringUtils.isEmpty(platformAccountProperties))
                body.put(PLATFORM_ACCOUNT_PROPERTIES, platformAccountProperties);

            if (!StringUtils.isEmpty(secretManagement))
                body.put(SECRET_MANAGEMENT, secretManagement);

            if (!StringUtils.isEmpty(remoteMachineAccess))
                body.put(REMOTE_MACHINE_ACCESS, remoteMachineAccess);

                        
            Map<String, String> result = new HttpClientPostAction().execute(
                    protocol + PROTOCOL_DELIMITER + hostName + ADD_ACCOUNT_ENDPOINT,
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
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    body.toString(),
                    APPLICATION_JSON,
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
