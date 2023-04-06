/*
 * (c) Copyright 2023 Micro Focus, L.P.
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
package io.cloudslang.content.sharepoint.actions.drives;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.actions.HttpClientGetAction;
import io.cloudslang.content.sharepoint.utils.Descriptions;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.sharepoint.services.SharepointService.processHttpGetRootDrive;
import static io.cloudslang.content.sharepoint.utils.Constants.*;
import static io.cloudslang.content.sharepoint.utils.Descriptions.Common.*;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetRootDrive.NAME;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetRootDrive.*;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetRootDrive.EXCEPTION_DESC;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetRootDrive.STATUS_CODE_DESC;
import static io.cloudslang.content.sharepoint.utils.Inputs.CommonInputs.AUTH_TOKEN;
import static io.cloudslang.content.sharepoint.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.sharepoint.utils.InputsValidation.verifyCommonInputs;
import static io.cloudslang.content.sharepoint.utils.Outputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class GetRootDrive {
    @Action(name = NAME,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = WEB_URL, description = WEB_URL_DESC),
                    @Output(value = DRIVE_NAME, description = DRIVE_NAME_DESC),
                    @Output(value = DRIVE_TYPE, description = DRIVE_TYPE_DESC),
                    @Output(value = DRIVE_ID, description = DRIVE_ID_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(
            @Param(value = AUTH_TOKEN, description = AUTH_TOKEN_DESC, required = true, encrypted = true) String authToken,
            @Param(value = SITE_ID, description = SITE_ID_DESC, required = true) String siteId,

            @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
            @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
            @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
            @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,

            @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
            @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
            @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
            @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,
            @Param(value = TLS_VERSION, description = TLS_VERSION_DESCRIPTION) String tlsVersion,
            @Param(value = ALLOWED_CIPHERS, description = ALLOWED_CIPHERS_DESCRIPTION) String allowedCiphers,
            @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
            @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String executionTimeout,
            @Param(value = SESSION_COOKIES, description = Descriptions.Common.SESSION_COOKIES_DESC) SerializableSessionObject sessionCookies,
            @Param(value = SESSION_CONNECTION_POOL, description = Descriptions.Common.SESSION_CONNECTION_POOL_DESC)
            GlobalSessionObject sessionConnectionPool) {

        try {

            proxyHost = defaultIfEmpty(proxyHost, EMPTY);
            proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
            proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
            proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
            trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
            x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
            connectTimeout = defaultIfEmpty(connectTimeout, DEFAULT_TIMEOUT);
            executionTimeout = defaultIfEmpty(executionTimeout, DEFAULT_TIMEOUT);

            final List<String> exceptionMessages = verifyCommonInputs(proxyPort, trustAllRoots, x509HostnameVerifier, connectTimeout, executionTimeout);
            if (!exceptionMessages.isEmpty())
                return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));

            Map<String, String> result = new HttpClientGetAction().execute(
                    GRAPH_API_ENDPOINT + SITES_ENDPOINT + siteId + ROOT_DRIVE_ENDPOINT,
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
                    EMPTY,
                    EMPTY,
                    FALSE,
                    CONNECTIONS_MAX_PER_ROUTE_CONST,
                    CONNECTIONS_MAX_TOTAL_CONST,
                    EMPTY,
                    EMPTY,
                    AUTHORIZATION_BEARER + authToken,
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

            processHttpGetRootDrive(result, EXCEPTION_DESC);
            return result;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
