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
package io.cloudslang.content.dca.actions.templates;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.dca.utils.Validator;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;

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
import static io.cloudslang.content.dca.utils.Descriptions.GetDeployment.*;
import static io.cloudslang.content.dca.utils.InputNames.*;
import static io.cloudslang.content.dca.utils.OutputNames.STATUS;
import static io.cloudslang.content.dca.utils.Utilities.*;
import static io.cloudslang.content.httpclient.services.HttpClientService.STATUS_CODE;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.lang.Integer.parseInt;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class GetDeployment {

    @Action(name = "Get Deployment",
            description = GET_DEPLOYMENT_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = STATUS, description = STATUS_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_RESPONSE_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ERROR, description = FAILURE_RESPONSE_DESC)
            })
    public Map<String, String> execute(
            @Param(value = DCA_HOST, required = true, description = DCA_HOST_DESC) final String host,
            @Param(value = DCA_PORT, description = DCA_PORT_DESC) final String portInp,
            @Param(value = PROTOCOL, description = DCA_PROTOCOL_DESC) final String protocolInp,
            @Param(value = AUTH_TOKEN, required = true, encrypted = true, description = AUTH_TOKEN_DESC) final String authToken,
            @Param(value = REFRESH_TOKEN, encrypted = true, description = REFRESH_TOKEN_DESC) final String refreshToken,
            @Param(value = DEPLOYMENT_UUID, required = true, description = DEPLOYMENT_UUID_DESC) final String deploymentUuid,

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
        // defaults
        final String port = defaultIfEmpty(portInp, DEFAULT_DCA_PORT);
        final String protocol = defaultIfEmpty(protocolInp, HTTPS);

        final String trustKeystore = defaultIfEmpty(trustKeystoreInp, DEFAULT_JAVA_KEYSTORE);
        final String trustPassword = defaultIfEmpty(trustPasswordInp, DEFAULT_JAVA_KEYSTORE_PASSWORD);
        final String keystore = defaultIfEmpty(keystoreInp, DEFAULT_JAVA_KEYSTORE);
        final String keystorePassword = defaultIfEmpty(keystorePasswordInp, DEFAULT_JAVA_KEYSTORE_PASSWORD);

        // validation
        final Validator validator = new Validator()
                .validatePort(port, DCA_PORT)
                .validateProtocol(protocol, PROTOCOL);

        if (validator.hasErrors()) {
            return getFailureResultsMap(validator.getErrors());
        }

        final HttpClientInputs httpClientInputs = new HttpClientInputs();

        httpClientInputs.setUrl(getDcaDeploymentUrl(protocol, host, port, deploymentUuid));

        httpClientInputs.setHeaders(getAuthHeaders(authToken, refreshToken));

        setProxy(httpClientInputs, proxyHost, proxyPort, proxyUsername, proxyPassword);

        setSecurityInputs(httpClientInputs, trustAllRoots, x509HostnameVerifier,
                trustKeystore, trustPassword, keystore, keystorePassword);

        setConnectionParameters(httpClientInputs, connectTimeout, socketTimeout, useCookies, keepAlive,
                connectionsMaxPerRoot, connectionsMaxTotal);

        httpClientInputs.setFollowRedirects(TRUE);
        httpClientInputs.setMethod(GET);

        try {
            final ObjectMapper mapper = new ObjectMapper();

            final Map<String, String> httpClientResult = new HttpClientService().execute(httpClientInputs);

            final JsonNode result = mapper.readTree(httpClientResult.get(RETURN_RESULT));

            if (parseInt(httpClientResult.get(STATUS_CODE)) != HTTP_OK) {
                return getFailureResultsMap(result.toString());
            }

            final Map<String, String> successResultsMap = getSuccessResultsMap(httpClientResult.get(RETURN_RESULT));
            successResultsMap.put(STATUS, result.get(STATUS).asText());
            return successResultsMap;
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }

}

