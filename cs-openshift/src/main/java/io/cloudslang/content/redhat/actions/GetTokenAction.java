/*
 * (c) Copyright 2022 Micro Focus, L.P.
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


package io.cloudslang.content.redhat.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.actions.HttpClientGetAction;
import io.cloudslang.content.redhat.entities.HttpInput;
import io.cloudslang.content.utils.OutputUtilities;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.SESSION_CONNECTION_POOL_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.SESSION_COOKIES_DESC;

import static io.cloudslang.content.redhat.services.OpenshiftService.processAuthTokenResult;
import static io.cloudslang.content.redhat.utils.Constants.CommonConstants.*;
import static io.cloudslang.content.redhat.utils.Descriptions.Common.*;
import static io.cloudslang.content.redhat.utils.Descriptions.GetTokenAction.AUTH_TOKEN_DESC;
import static io.cloudslang.content.redhat.utils.Descriptions.GetTokenAction.*;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.AUTH_TOKEN;

public class GetTokenAction {
    @Action(name = GET_TOKEN_NAME,
            description = GET_TOKEN_NAME_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = AUTH_TOKEN, description = AUTH_TOKEN_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = HOST, required = true, description = HOST_DESC) String host,
                                       @Param(value = USERNAME, required = true, description = USERNAME_DESC) String username,
                                       @Param(value = PASSWORD, required = true, encrypted = true, description = PASSWORD_DESC) String password,

                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD,encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,
                                       @Param(value = TLS_VERSION, description = TLS_VERSION_DESC) String tlsVersion,
                                       @Param(value = ALLOWED_CIPHERS, description = ALLOWED_CIPHERS_DESC) String allowedCyphers,
                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_HOSTNAME_VERIFIER_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD,encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,
                                       @Param(value = KEYSTORE, description = KEYSTORE_DESC) String keystore,
                                       @Param(value = KEYSTORE_PASSWORD,encrypted = true, description = KEYSTORE_PASSWORD_DESC) String keystorePassword,
                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String executionTimeout,
                                       @Param(value = SESSION_COOKIES, description = SESSION_COOKIES_DESC) SerializableSessionObject sessionCookies,
                                       @Param(value = SESSION_CONNECTION_POOL, description = SESSION_CONNECTION_POOL_DESC) GlobalSessionObject sessionConnectionPool) {
        try {

            String auth = username + COLON_PUNCTUATION + password;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));

            HttpInput input = HttpInput.builder()
                    .host(host)
                    .proxyHost(proxyHost)
                    .proxyPassword(proxyPassword)
                    .proxyPort(proxyPort)
                    .proxyPassword(proxyPassword)
                    .keystorePassword(keystorePassword)
                    .proxyUsername(proxyUsername)
                    .allowedCyphers(allowedCyphers)
                    .trustKeystore(trustKeystore)
                    .keystore(keystore)
                    .tlsVersion(tlsVersion)
                    .trustAllRoots(trustAllRoots)
                    .trustPassword(trustPassword)
                    .x509HostnameVerifier(x509HostnameVerifier)
                    .username(username)
                    .password(password)
                    .connectTimeout(connectTimeout)
                    .executionTimeout(executionTimeout)
                    .build();

            Map<String, String> result = new HttpClientGetAction().execute(
                    input.getHost() +AUTHORIZE_TOKEN_URL,
                    ANONYMOUS,
                    EMPTY_STRING,
                    EMPTY_STRING,
                    TRUE,
                    input.getProxyHost(),
                    input.getProxyPort(),
                    input.getProxyUsername(),
                    input.getProxyPassword(),
                    input.getTlsVersion(),
                    input.getAllowedCyphers(),
                    input.getTrustAllRoots(),
                    input.getX509HostnameVerifier(),
                    input.getTrustKeystore(),
                    input.getTrustPassword(),
                    input.getKeystore(),
                    input.getKeystorePassword(),
                    FALSE,
                    CONNECTION_MAX_PER_ROUTE,
                    CONNECTIONS_MAX_TOTAL_VALUE,
                    TRUE,
                    TRUE,
                    AUTHORIZATION_BASIC + new String(encodedAuth),
                    EMPTY_STRING,
                    EMPTY_STRING,
                    QUERY_PARAM,
                    TRUE,
                    FALSE,
                    input.getConnectTimeout(),
                    EMPTY_STRING,
                    input.getExecutionTimeout(),
                    sessionCookies,
                    sessionConnectionPool);


            processAuthTokenResult(result,input,sessionCookies,sessionConnectionPool);

            return result;
        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }
    }

}
