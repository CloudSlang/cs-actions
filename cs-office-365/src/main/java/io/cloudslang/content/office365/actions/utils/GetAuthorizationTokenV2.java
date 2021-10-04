package io.cloudslang.content.office365.actions.utils;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.office365.entities.AuthorizationTokenInputs;
import io.cloudslang.content.office365.services.AuthorizationTokenV2Impl;
import io.cloudslang.content.utils.NumberUtilities;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.Constants.NEW_LINE;
import static io.cloudslang.content.office365.utils.Descriptions.Common.*;
import static io.cloudslang.content.office365.utils.Descriptions.GetAuthorizationToken.*;
import static io.cloudslang.content.office365.utils.Descriptions.GetAuthorizationToken.RETURN_CODE_DESC;
import static io.cloudslang.content.office365.utils.Inputs.AuthorizationInputs.*;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.office365.utils.InputsValidation.verifyAuthorizationInputs;
import static io.cloudslang.content.office365.utils.Outputs.AuthorizationOutputs.AUTH_TOKEN;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class GetAuthorizationTokenV2 {
    /**
     * @param loginType      Login method according to application type
     *                       Valid values: 'API', 'Native'
     *                       Default: 'API'
     * @param clientId       Service Client ID
     * @param clientSecret   Service Client Secret
     * @param username       Office 365 username
     * @param password       Office 365 password
     * @param loginAuthority The authority URL. Usually, the format for this input is:
     *                       'https://login.windows.net/TENANT_NAME/oauth2/token' where TENANT_NAME is your application
     *                       tenant.
     * @param scope          The scope URL. The scope consists of a series of resource permissions separated " +
     *                       "by a comma (,), i.e.: 'https://graph.microsoft.com/User.Read, https://graph.microsoft.com/Sites.Read'. " +
     *                       "The 'https://graph.microsoft.com/.default' scope means that the user consent to all of the configured permissions " +
     *                       "present on the specific Azure AD Application. For the 'API' loginType, '/.default' scope should be used. \n" +
     *                       "Default: 'https://graph.microsoft.com/.default'
     * @param proxyHost      Proxy server used to access the web site
     * @param proxyPort      Proxy server port
     *                       Default: '8080'
     * @param proxyUsername  User name used when connecting to the proxy
     * @param proxyPassword  The proxy server password associated with the proxyUsername input value
     * @return The authorization token for Office 365
     */
    @Action(name = "Get the authorization token for Office 365 V2",
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
            final IAuthenticationResult result = AuthorizationTokenV2Impl.getToken(AuthorizationTokenInputs.builder()
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
