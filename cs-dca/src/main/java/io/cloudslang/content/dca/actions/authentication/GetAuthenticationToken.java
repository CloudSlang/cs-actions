/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
import io.cloudslang.content.dca.utils.InputNames;
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
import static io.cloudslang.content.dca.utils.OutputNames.AUTH_TOKEN;
import static io.cloudslang.content.dca.utils.OutputNames.REFRESH_TOKEN;
import static io.cloudslang.content.dca.utils.Utilities.*;
import static io.cloudslang.content.httpclient.CSHttpClient.STATUS_CODE;
import static io.cloudslang.content.httpclient.HttpClientInputs.*;
import static io.cloudslang.content.httpclient.build.auth.AuthTypes.BASIC;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.lang.Integer.parseInt;
import static java.lang.System.lineSeparator;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.join;

public class GetAuthenticationToken {

    @Action(name = "Get Authentication Token",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION),
                    @Output(AUTH_TOKEN),
                    @Output(REFRESH_TOKEN)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ERROR)
            }
    )
    public Map<String, String> execute(
            @Param(value = InputNames.IDM_HOST, required = true) final String idmHostInp,
            @Param(InputNames.IDM_PORT) final String idmPortInp,
            @Param(InputNames.PROTOCOL) final String protocolInp,
            @Param(value = InputNames.IDM_USERNAME, required = true) final String idmUsername,
            @Param(value = InputNames.IDM_PASSWORD, required = true, encrypted = true) final String idmPassword,
            @Param(value = InputNames.DCA_USERNAME, required = true) final String dcaUsername,
            @Param(value = InputNames.DCA_PASSWORD, required = true, encrypted = true) final String dcaPassword,
            @Param(InputNames.DCA_TENANT_NAME) final String dcaTenantInp,
            @Param(PREEMPTIVE_AUTH) final String preemptiveAuth,
            @Param(PROXY_HOST) final String proxyHost,
            @Param(PROXY_PORT) final String proxyPort,
            @Param(PROXY_USERNAME) final String proxyUsername,
            @Param(value = PROXY_PASSWORD, encrypted = true) final String proxyPassword,
            @Param(TRUST_ALL_ROOTS) final String trustAllRoots,
            @Param(X509_HOSTNAME_VERIFIER) final String x509HostnameVerifier,
            @Param(TRUST_KEYSTORE) final String trustKeystoreInp,
            @Param(value = TRUST_PASSWORD, encrypted = true) final String trustPasswordInp,
            @Param(KEYSTORE) final String keystoreInp,
            @Param(value = KEYSTORE_PASSWORD, encrypted = true) final String keystorePasswordInp,
            @Param(CONNECT_TIMEOUT) final String connectTimeout,
            @Param(SOCKET_TIMEOUT) final String socketTimeout,
            @Param(USE_COOKIES) final String useCookies,
            @Param(KEEP_ALIVE) final String keepAlive,
            @Param(CONNECTIONS_MAX_PER_ROUTE) final String connectionsMaxPerRoot,
            @Param(CONNECTIONS_MAX_TOTAL) final String connectionsMaxTotal
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
        final Validator validator = new Validator();
        validator.validatePort(idmPortStr);
        validator.validateProtocol(protocolStr);

        if (!validator.getValidationErrorList().isEmpty()) {
            return getFailureResultsMap(join(validator.getValidationErrorList(), lineSeparator()));
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
