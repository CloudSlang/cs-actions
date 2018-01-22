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
package io.cloudslang.content.dca.actions.templates;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.constants.BooleanValues;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.dca.models.DcaDeploymentModel;
import io.cloudslang.content.dca.models.DcaResourceModel;
import io.cloudslang.content.dca.utils.InputNames;
import io.cloudslang.content.dca.utils.Validator;
import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.httpclient.HttpClientInputs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.dca.utils.Constants.*;
import static io.cloudslang.content.dca.utils.DefaultValues.*;
import static io.cloudslang.content.dca.utils.Utilities.*;
import static io.cloudslang.content.httpclient.CSHttpClient.STATUS_CODE;
import static io.cloudslang.content.utils.BooleanUtilities.toBoolean;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.lineSeparator;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.join;

public class DeployTemplate {

    @Action(name = "Deploy Template",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ERROR)
            })
    public Map<String, String> execute(
            @Param(value = InputNames.DCA_HOST, required = true) String host,
            @Param(value = InputNames.DCA_PORT) String portInp,
            @Param(value = InputNames.PROTOCOL) String protocolInp,
            @Param(value = InputNames.AUTH_TOKEN, required = true) String authToken,
            @Param(value = InputNames.REFRESH_TOKEN) String refreshToken,
            @Param(value = InputNames.DEPLOYMENT_NAME) String deploymentName,
            @Param(value = InputNames.DEPLOYMENT_DESCRIPTION) String deploymentDesc,
            @Param(value = InputNames.DEPLOYMENT_TEMPLATE_ID) String deploymentTemplateId,
            @Param(value = InputNames.DEPLOYMENT_RESOURCES_JSON) String deploymentResources,

            @Param(InputNames.ASYNC) String asyncInp,
            @Param(InputNames.TIMEOUT) String timeoutInp,
            @Param(InputNames.POLLING_INTERVAL) String pollingIntervalInp,

            @Param(HttpClientInputs.PROXY_HOST) String proxyHost,
            @Param(HttpClientInputs.PROXY_PORT) String proxyPort,
            @Param(HttpClientInputs.PROXY_USERNAME) String proxyUsername,
            @Param(HttpClientInputs.PROXY_PASSWORD) String proxyPassword,
            @Param(HttpClientInputs.TRUST_ALL_ROOTS) String trustAllRoots,
            @Param(HttpClientInputs.X509_HOSTNAME_VERIFIER) String x509HostnameVerifier,
            @Param(HttpClientInputs.TRUST_KEYSTORE) String trustKeystoreInp,
            @Param(HttpClientInputs.TRUST_PASSWORD) String trustPasswordInp,
            @Param(HttpClientInputs.KEYSTORE) String keystoreInp,
            @Param(HttpClientInputs.KEYSTORE_PASSWORD) String keystorePasswordInp,
            @Param(HttpClientInputs.CONNECT_TIMEOUT) String connectTimeout,
            @Param(HttpClientInputs.SOCKET_TIMEOUT) String socketTimeout,
            @Param(HttpClientInputs.USE_COOKIES) String useCookies,
            @Param(HttpClientInputs.KEEP_ALIVE) String keepAlive,
            @Param(HttpClientInputs.CONNECTIONS_MAX_PER_ROUTE) String connectionsMaxPerRoot,
            @Param(HttpClientInputs.CONNECTIONS_MAX_TOTAL) String connectionsMaxTotal
    ) {
        // defaults
        final String port = defaultIfEmpty(portInp, DEFAULT_DCA_PORT);
        final String protocol = defaultIfEmpty(protocolInp, HTTPS);

        final String asyncStr = defaultIfEmpty(asyncInp, BooleanValues.TRUE);
        final String timeoutStr = defaultIfEmpty(timeoutInp, DEFAULT_TIMEOUT);
        final String pollingIntervalStr = defaultIfEmpty(pollingIntervalInp, DEFAULT_POLLING_INTERVAL);

        final String trustKeystore = defaultIfEmpty(trustKeystoreInp, DEFAULT_JAVA_KEYSTORE);
        final String trustPassword = defaultIfEmpty(trustPasswordInp, DEFAULT_JAVA_KEYSTORE_PASSWORD);
        final String keystore = defaultIfEmpty(keystoreInp, DEFAULT_JAVA_KEYSTORE);
        final String keystorePassword = defaultIfEmpty(keystorePasswordInp, DEFAULT_JAVA_KEYSTORE_PASSWORD);

        // validation
        final Validator validator = new Validator();
        validator.validatePort(port);
        validator.validateProtocol(protocol);

        validator.validateBoolean(asyncStr);
        validator.validateInt(timeoutStr);
        validator.validateInt(pollingIntervalStr);

        if (!validator.getValidationErrorList().isEmpty()) {
            return getFailureResultsMap(join(validator.getValidationErrorList(), lineSeparator()));
        }

        final HttpClientInputs httpClientInputs = new HttpClientInputs();

        httpClientInputs.setUrl(getDcaDeployUrl(protocol, host, port));

        httpClientInputs.setHeaders(getAuthHeaders(authToken, refreshToken));

        setProxy(httpClientInputs, proxyHost, proxyPort, proxyUsername, proxyPassword);

        setSecurityInputs(httpClientInputs, trustAllRoots, x509HostnameVerifier,
                trustKeystore, trustPassword, keystore, keystorePassword);

        setConnectionParameters(httpClientInputs, connectTimeout, socketTimeout, useCookies, keepAlive,
                connectionsMaxPerRoot, connectionsMaxTotal);

        httpClientInputs.setContentType(APPLICATION_JSON);
//        httpClientInputs.setResponseCharacterSet(UTF_8.toString());
//        httpClientInputs.setRequestCharacterSet(UTF_8.toString());
        httpClientInputs.setFollowRedirects(BooleanValues.TRUE);
        httpClientInputs.setMethod(POST);

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final List<DcaResourceModel> resources = mapper.readValue(deploymentResources,
                    mapper.getTypeFactory().constructCollectionType(List.class, DcaResourceModel.class));

            final DcaDeploymentModel dcaDeploymentModel =
                    new DcaDeploymentModel(deploymentName, deploymentDesc, deploymentTemplateId, resources);
            httpClientInputs.setBody(dcaDeploymentModel.toJson());

            final Map<String, String> httpClientResult = new CSHttpClient().execute(httpClientInputs);

            final Map resultMap = mapper.readValue(httpClientResult.get(RETURN_RESULT), Map.class);

            if (Integer.parseInt(httpClientResult.get(STATUS_CODE)) != HTTP_OK) {
                return getFailureResultsMap(resultMap.get("message").toString());
            }

            final boolean async = toBoolean(asyncStr);
            final int timeout = Integer.parseInt(timeoutStr);
            final int pollingInterval = Integer.parseInt(pollingIntervalStr);

            if (async) {
                return getSuccessResultsMap(httpClientResult.get(RETURN_RESULT));
            } else {

                final long finishTime = currentTimeMillis() + SECONDS.toMillis(timeout);
                final String deploymentUuid = resultMap.get("uuid").toString();

                while (true) {
                    Thread.sleep(SECONDS.toMillis(pollingInterval));

                    final String getDeploymentResult = new GetDeployment().execute(host, port, protocolInp,
                            authToken, refreshToken, deploymentUuid, proxyHost, proxyPort, proxyUsername, proxyPassword,
                            trustAllRoots, x509HostnameVerifier, trustKeystoreInp, trustPasswordInp, keystoreInp,
                            keystorePasswordInp, connectTimeout, socketTimeout, useCookies, keepAlive,
                            connectionsMaxPerRoot, connectionsMaxTotal).get(RETURN_RESULT);

                    final Map getMap = mapper.readValue(getDeploymentResult, Map.class);

                    final String status = getMap.get("status").toString();

                    if (status.equalsIgnoreCase("SUCCESS")) {
                        return getSuccessResultsMap(getDeploymentResult);
                    }

                    if (status.equalsIgnoreCase("FAILED")) {
                        return getFailureResultsMap(getDeploymentResult, new Exception("Failed to deploy template!"));
                    }

                    if (currentTimeMillis() > finishTime) {
                        throw new TimeoutException("Deploy Template timeout.");
                    }
                }
            }
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }

}
