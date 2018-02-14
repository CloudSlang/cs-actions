/*
 * (c) Copyright 2018 Micro Focus, L.P.
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
package io.cloudslang.content.dca.actions.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.dca.utils.Validator;
import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.httpclient.HttpClientInputs;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.dca.utils.Constants.*;
import static io.cloudslang.content.dca.utils.DefaultValues.*;
import static io.cloudslang.content.dca.utils.Descriptions.Common.*;
import static io.cloudslang.content.dca.utils.Descriptions.GetAuthenticationToken.*;
import static io.cloudslang.content.dca.utils.InputNames.*;
import static io.cloudslang.content.dca.utils.OutputNames.AUTH_TOKEN;
import static io.cloudslang.content.dca.utils.OutputNames.REFRESH_TOKEN;
import static io.cloudslang.content.dca.utils.Utilities.*;
import static io.cloudslang.content.httpclient.CSHttpClient.STATUS_CODE;
import static io.cloudslang.content.httpclient.HttpClientInputs.*;
import static io.cloudslang.content.httpclient.build.auth.AuthTypes.BASIC;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.lang.Integer.parseInt;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.join;

public class GetAuthenticationToken {

    @Action(name = "Get Authentication Token",
            description = GET_AUTH_TOKEN_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = AUTH_TOKEN, description = AUTH_TOKEN_OUT_DESC),
                    @Output(value = REFRESH_TOKEN, description = REFRESH_TOKEN_OUT_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_RESPONSE_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ERROR, description = FAILURE_RESPONSE_DESC)
            }
    )
    public Map<String, String> execute(
            @Param(value = IDM_HOST, required = true, description = IDM_HOST_DESC) final String idmHostInp,
            @Param(value = IDM_PORT, description = IDM_PORT_DESC) final String idmPortInp,
            @Param(value = PROTOCOL, description = PROTOCOL_DESC) final String protocolInp,

            @Param(value = IDM_USERNAME, required = true, description = IDM_USERNAME_DESC) final String idmUsername,
            @Param(value = IDM_PASSWORD, required = true, encrypted = true,
                    description = IDM_PASSWORD_DESC) final String idmPassword,

            @Param(value = DCA_USERNAME, required = true, description = DCA_USERNAME_DESC) final String dcaUsername,
            @Param(value = DCA_PASSWORD, required = true, encrypted = true,
                    description = DCA_PASSWORD_DESC) final String dcaPassword,
            @Param(value = DCA_TENANT_NAME, description = DCA_TENANT_DESC) final String dcaTenantInp,

            @Param(value = PREEMPTIVE_AUTH, description = PREEMPTIVE_AUTH_DESC) final String preemptiveAuth,
            @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) final String proxyHost,
            @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) final String proxyPort,
            @Param(value = PROXY_USERNAME, description = PROXY_USER_DESC) final String proxyUsername,
            @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASS_DESC) final String proxyPassword,

            @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) final String trustAllRoots,
            @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) final String x509HostnameVerifier,
            @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) final String trustKeystoreInp,
            @Param(value = TRUST_PASSWORD, encrypted = true,
                    description = TRUST_PASSWORD_DESC) final String trustPasswordInp,
            @Param(value = KEYSTORE, description = KEYSTORE_DESC) final String keystoreInp,
            @Param(value = KEYSTORE_PASSWORD, encrypted = true, description = KEYSTORE_PASS_DESC) final String keystorePasswordInp,

            @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) final String connectTimeout,
            @Param(value = SOCKET_TIMEOUT, description = SOCKET_TIMEOUT_DESC) final String socketTimeout,
            @Param(value = USE_COOKIES, description = USE_COOKIES_DESC) final String useCookies,
            @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) final String keepAlive,
            @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONN_MAX_ROUTE_DESC) final String connectionsMaxPerRoot,
            @Param(value = CONNECTIONS_MAX_TOTAL, description = CONN_MAX_TOTAL_DESC) final String connectionsMaxTotal
    ) {
        // SETUP DEFAULTS
        // default IDM port is 5443
        final String idmPortStr = defaultIfEmpty(idmPortInp, DEFAULT_IDM_PORT);
        final String protocolStr = defaultIfEmpty(protocolInp, DEFAULT_IDM_PROTOCOL);

        // todo check if tenant type needs to be validated
        final String dcaTenant = defaultIfEmpty(dcaTenantInp, DEFAULT_TENANT);

        final String trustKeystore = defaultIfEmpty(trustKeystoreInp, DEFAULT_JAVA_KEYSTORE);
        final String trustPassword = defaultIfEmpty(trustPasswordInp, DEFAULT_JAVA_KEYSTORE_PASSWORD);
        final String keystore = defaultIfEmpty(keystoreInp, DEFAULT_JAVA_KEYSTORE);
        final String keystorePassword = defaultIfEmpty(keystorePasswordInp, DEFAULT_JAVA_KEYSTORE_PASSWORD);

        // VALIDATION
        final Validator validator = new Validator()
                .validatePort(idmPortStr, IDM_PORT)
                .validateProtocol(protocolStr, PROTOCOL);

        if (validator.hasErrors()) {
            return getFailureResultsMap(validator.getErrors());
        }

        // SETUP HTTP INPUTS
        final HttpClientInputs httpClientInputs = new HttpClientInputs();

        httpClientInputs.setUrl(getIdmUrl(protocolInp, idmHostInp, idmPortStr));

        setIdmAuthentication(httpClientInputs, BASIC, idmUsername, idmPassword, preemptiveAuth);

        setProxy(httpClientInputs, proxyHost, proxyPort, proxyUsername, proxyPassword);

        setSecurityInputs(httpClientInputs, trustAllRoots, x509HostnameVerifier,
                trustKeystore, trustPassword, keystore, keystorePassword);

        setDcaCredentials(httpClientInputs, dcaUsername, dcaPassword, dcaTenant);

        setConnectionParameters(httpClientInputs, connectTimeout, socketTimeout, useCookies, keepAlive,
                connectionsMaxPerRoot, connectionsMaxTotal);

        httpClientInputs.setContentType(APPLICATION_JSON);
        httpClientInputs.setResponseCharacterSet(UTF_8.toString());
        httpClientInputs.setRequestCharacterSet(UTF_8.toString());
        httpClientInputs.setFollowRedirects(TRUE);
        httpClientInputs.setMethod(POST);

        try {
            final Map<String, String> httpClientResultMap = new CSHttpClient().execute(httpClientInputs);
            final ObjectMapper mapper = new ObjectMapper();
            final Map responseMap = mapper.readValue(httpClientResultMap.get(RETURN_RESULT), Map.class);

            if (parseInt(httpClientResultMap.get(STATUS_CODE)) == HTTP_OK) {
                final String authToken = ((LinkedHashMap) responseMap.get("token")).get("id").toString();

                final String refreshToken = responseMap.get(REFRESH_TOKEN).toString();

                final Map<String, String> resultMap = getSuccessResultsMap(authToken);

                resultMap.put(AUTH_TOKEN, authToken);
                resultMap.put(REFRESH_TOKEN, refreshToken);

                return resultMap;
            } else {
                return getFailureResultsMap(join(responseMap.get("errors"), NEW_LINE));
            }
        } catch (Exception e) {
            return getFailureResultsMap("Failed to get authentication token.", e);
        }
    }

}
