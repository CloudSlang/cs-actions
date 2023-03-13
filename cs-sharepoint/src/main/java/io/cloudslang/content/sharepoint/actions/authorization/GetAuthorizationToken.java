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
package io.cloudslang.content.sharepoint.actions.authorization;

import java.util.List;
import java.util.Map;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.sharepoint.entities.GetAuthorizationTokenInputs;
import io.cloudslang.content.sharepoint.services.GetAuthorizationTokenImpl;
import io.cloudslang.content.utils.NumberUtilities;
import io.cloudslang.content.utils.StringUtilities;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.*;
import static io.cloudslang.content.sharepoint.utils.Constants.*;
import static io.cloudslang.content.sharepoint.utils.Descriptions.Common.*;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetAuthorizationToken.*;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetAuthorizationToken.RETURN_CODE_DESC;
import static io.cloudslang.content.sharepoint.utils.Inputs.AuthorizationInputs.*;
import static io.cloudslang.content.sharepoint.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.sharepoint.utils.InputsValidation.verifyAuthorizationInputs;
import static io.cloudslang.content.sharepoint.utils.Outputs.*;
import static io.cloudslang.content.sharepoint.utils.Outputs.EXCEPTION;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class GetAuthorizationToken {
    @Action(name = "Get the authorization token for Office 365 Sharepoint",
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
    public Map<String, String> execute(@Param(value = LOGIN_TYPE, description = LOGIN_TYPE_DESC) String loginType,
                                       @Param(value = CLIENT_ID, required = true, description = CLIENT_ID_DESC) String clientId,
                                       @Param(value = CLIENT_SECRET, encrypted = true, description = CLIENT_SECRET_DESC) String clientSecret,
                                       @Param(value = USERNAME, description = USERNAME_DESC) String username,
                                       @Param(value = PASSWORD, encrypted = true, description = PASSWORD_DESC) String password,
                                       @Param(value = LOGIN_AUTHORITY, required = true, description = LOGIN_AUTHORITY_DESC) String loginAuthority,
                                       @Param(value = SCOPE, description = SCOPE_DESC) String scope,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword) {
        loginType = defaultIfEmpty(loginType, DEFAULT_LOGIN_TYPE);
        clientId = defaultIfEmpty(clientId, EMPTY);
        clientSecret = defaultIfEmpty(clientSecret, EMPTY);
        username = defaultIfEmpty(username, EMPTY);
        password = defaultIfEmpty(password, EMPTY);
        scope = defaultIfEmpty(scope, DEFAULT_SCOPE);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        final List<String> exceptionMessages = verifyAuthorizationInputs(loginType, clientId, clientSecret, username, password, proxyPort);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            final IAuthenticationResult result = GetAuthorizationTokenImpl.getToken(GetAuthorizationTokenInputs.builder()
                    .loginType(loginType)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(username)
                    .password(password)
                    .authority(loginAuthority)
                    .scope(scope)
                    .proxyHost(proxyHost)
                    .proxyPort(NumberUtilities.toInteger(proxyPort))
                    .proxyUsername(proxyUsername)
                    .proxyPassword(proxyPassword)
                    .build());

            final Map<String, String> successResultsMap = getSuccessResultsMap(result.accessToken());
            successResultsMap.put(AUTH_TOKEN, result.accessToken());
            return successResultsMap;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
