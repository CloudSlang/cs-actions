/*
 * Copyright 2020-2023 Open Text
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

package io.cloudslang.content.sitescope.actions.templates;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.sitescope.entities.UpdateTemplateInputs;
import io.cloudslang.content.sitescope.entities.SiteScopeCommonInputs;
import io.cloudslang.content.sitescope.services.UpdateTemplateService;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.sitescope.constants.Constants.*;
import static io.cloudslang.content.sitescope.constants.Descriptions.Common.*;
import static io.cloudslang.content.sitescope.constants.Descriptions.DeleteMonitorGroupAction.RETURN_RESULT_DESC;
import static io.cloudslang.content.sitescope.constants.Descriptions.GetGroupPropertiesAction.FAILURE_DESC;
import static io.cloudslang.content.sitescope.constants.Descriptions.GetGroupPropertiesAction.SUCCESS_DESC;
import static io.cloudslang.content.sitescope.constants.Descriptions.UpdateTemplateAction.PROPERTIES_DESC;
import static io.cloudslang.content.sitescope.constants.Descriptions.UpdateTemplateAction.UPDATE_TEMPLATE_DESC;
import static io.cloudslang.content.sitescope.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.sitescope.constants.Inputs.UpdateTemplate.FULL_PATH_TO_TEMPLATE;
import static io.cloudslang.content.sitescope.constants.Inputs.UpdateTemplate.PROPERTIES;
import static io.cloudslang.content.sitescope.constants.Outputs.STATUS_CODE;
import static io.cloudslang.content.sitescope.utils.InputsValidation.verifyCommonInputs;
import static io.cloudslang.content.sitescope.utils.InputsValidation.verifyUpdateTemplateInputs;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class UpdateTemplateAction {


    @Action(name = "Update Template", description = UPDATE_TEMPLATE_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESCRIPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = HOST, required = true, description = HOST_DESC) String host,
                                       @Param(value = PORT, required = true, description = PORT_DESC) String port,
                                       @Param(value = PROTOCOL, required = true, description = PROTOCOL_DESC) String protocol,
                                       @Param(value = USERNAME, description = USERNAME_DESC) String username,
                                       @Param(value = PASSWORD, encrypted = true, description = PASSWORD_DESC) String password,
                                       @Param(value = FULL_PATH_TO_TEMPLATE, description = FULL_PATH_TO_TEMPLATE_DESC) String fullPathToTemplate,
                                       @Param(value = DELIMITER, description = DELIMITER_DESC) String delimiter,
                                       @Param(value = PROPERTIES, description = PROPERTIES_DESC) String properties,
                                       @Param(value = IDENTIFIER, description = IDENTIFIER_DESC) String identifier,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,
                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
                                       @Param(value = KEYSTORE, description = KEYSTORE_DESC) String keystore,
                                       @Param(value = KEYSTORE_PASSWORD, encrypted = true, description = KEYSTORE_PASSWORD_DESC) String keystorePassword,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,
                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = SOCKET_TIMEOUT, description = SOCKET_TIMEOUT_DESC) String socketTimeout,
                                       @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONN_MAX_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = CONN_MAX_TOTAL_DESC) String connectionsMaxTotal,
                                       @Param(value = RESPONSE_CHARACTER_SET, description = RESPONSE_CHARACTER_SET_DESC) String responseCharacterSet) {

        username = defaultIfEmpty(username, EMPTY);
        password = defaultIfEmpty(password, EMPTY);
        delimiter = defaultIfEmpty(delimiter, DEFAULT_DELIMITER);
        identifier = defaultIfEmpty(identifier, EMPTY);
        properties = defaultIfEmpty(properties, EMPTY);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
        keystore = defaultIfEmpty(keystore, DEFAULT_JAVA_KEYSTORE);
        keystorePassword = defaultIfEmpty(keystorePassword, CHANGEIT);
        trustKeystore = defaultIfEmpty(trustKeystore, DEFAULT_JAVA_TRUST_KEYSTORE);
        trustPassword = defaultIfEmpty(trustPassword, CHANGEIT);
        connectTimeout = defaultIfEmpty(connectTimeout, ZERO);
        socketTimeout = defaultIfEmpty(socketTimeout, ZERO);
        keepAlive = defaultIfEmpty(keepAlive, BOOLEAN_FALSE);
        connectionsMaxPerRoute = defaultIfEmpty(connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE_CONST);
        connectionsMaxTotal = defaultIfEmpty(connectionsMaxTotal, CONNECTIONS_MAX_TOTAL_CONST);
        responseCharacterSet = defaultIfEmpty(responseCharacterSet, UTF8);

        final UpdateTemplateService service = new UpdateTemplateService();
        Map<String, String> result;
        final List<String> exceptionMessage = verifyCommonInputs(port, proxyPort, trustAllRoots,
                connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal);

        exceptionMessage.addAll(verifyUpdateTemplateInputs(fullPathToTemplate));
        if (!exceptionMessage.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessage, NEW_LINE));
        }

        try {
            UpdateTemplateInputs inputs = new UpdateTemplateInputs.UpdateTemplateInputsBuilder()
                    .fullPathToTemplate(fullPathToTemplate)
                    .delimiter(delimiter)
                    .identifier(identifier)
                    .properties(properties)
                    .commonInputs(SiteScopeCommonInputs.builder()
                            .host(host)
                            .port(port)
                            .protocol(protocol)
                            .username(username)
                            .password(password)
                            .proxyHost(proxyHost)
                            .proxyPort(proxyPort)
                            .proxyUsername(proxyUsername)
                            .proxyPassword(proxyPassword)
                            .connectionsMaxTotal(connectionsMaxTotal)
                            .connectionsMaxPerRoute(connectionsMaxPerRoute)
                            .keepAlive(keepAlive)
                            .responseCharacterSet(responseCharacterSet)
                            .connectTimeout(connectTimeout)
                            .trustAllRoots(trustAllRoots)
                            .x509HostnameVerifier(x509HostnameVerifier)
                            .keystore(keystore)
                            .keystorePassword(keystorePassword)
                            .trustKeystore(trustKeystore)
                            .trustPassword(trustPassword)
                            .build())
                    .build();

            result = service.execute(inputs);

            return result;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
