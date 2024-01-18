/*
 * Copyright 2024 Open Text
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

package io.cloudslang.content.sharepoint.actions.entities;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.actions.HttpClientGetAction;
import io.cloudslang.content.sharepoint.utils.Descriptions;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Arrays;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.sharepoint.services.SharepointService.processHostWithQuery;
import static io.cloudslang.content.sharepoint.services.SharepointService.processHttpSearchForEntities;
import static io.cloudslang.content.sharepoint.utils.Constants.*;
import static io.cloudslang.content.sharepoint.utils.Descriptions.Common.AUTH_TOKEN_DESC;
import static io.cloudslang.content.sharepoint.utils.Descriptions.Common.STATUS_CODE_DESC;
import static io.cloudslang.content.sharepoint.utils.Descriptions.Common.*;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetAllDrives.*;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetEntitiesFromDrive.ENTITY_IDS_DESC;
import static io.cloudslang.content.sharepoint.utils.Descriptions.SearchForEntities.EXCEPTION_DESC;
import static io.cloudslang.content.sharepoint.utils.Descriptions.SearchForEntities.SITE_ID_DESC;
import static io.cloudslang.content.sharepoint.utils.Descriptions.SearchForEntities.*;
import static io.cloudslang.content.sharepoint.utils.Inputs.CommonInputs.AUTH_TOKEN;
import static io.cloudslang.content.sharepoint.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.sharepoint.utils.Inputs.SearchForEntities.OPTIONAL_PARAMETERS;
import static io.cloudslang.content.sharepoint.utils.Inputs.SearchForEntities.SEARCH_TEXT;
import static io.cloudslang.content.sharepoint.utils.Outputs.*;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SearchForEntities {

    @Action(name = SEARCH_FOR_ENTITIES,
            description = SEARCH_FOR_ENTITIES_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = ENTITY_IDS, description = ENTITY_IDS_DESC),
                    @Output(value = NEXT_LINK, description = NEXT_LINK_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, isOnFail = true, description = FAILURE_DESC)
            })
    public Map<String, String> execute(
            @Param(value = AUTH_TOKEN, description = AUTH_TOKEN_DESC, required = true, encrypted = true) String authToken,
            @Param(value = DRIVE_ID, encrypted = true, description = DRIVE_ID_DESC) String driveId,
            @Param(value = GROUP_ID, encrypted = true, description = GROUP_ID_DESC) String groupId,
            @Param(value = SITE_ID, encrypted = true, description = SITE_ID_DESC) String siteId,
            @Param(value = USER_ID, encrypted = true, description = USER_ID_DESC) String userId,
            @Param(value = SEARCH_TEXT, description = SEARCH_TEXT_DESC) String searchText,
            @Param(value = OPTIONAL_PARAMETERS, description = OPTIONAL_PARAMETERS_DESC) String optionalParameters,

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
            @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String executionTimeout) {

        try {

            Map<String, String> result = new HttpClientGetAction().execute(
                    processHostWithQuery(Arrays.asList(driveId, groupId, siteId, userId), searchText, optionalParameters),

                    ANONYMOUS,
                    EMPTY,
                    EMPTY,
                    String.valueOf(TRUE),

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

                    null,
                    null
            );

            processHttpSearchForEntities(result, EXCEPTION_DESC);
            return result;

        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }
    }
}
