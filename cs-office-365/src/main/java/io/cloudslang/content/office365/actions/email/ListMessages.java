package io.cloudslang.content.office365.actions.email;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.office365.entities.ListMessagesInputs;
import io.cloudslang.content.office365.entities.Office365CommonInputs;
import io.cloudslang.content.office365.utils.Inputs;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.CONNECTIONS_MAX_PER_ROUTE;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.CONNECTIONS_MAX_TOTAL;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.CONNECT_TIMEOUT;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.KEEP_ALIVE;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.RESPONSE_CHARACTER_SET;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.SOCKET_TIMEOUT;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.TRUST_ALL_ROOTS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.TRUST_KEYSTORE;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.TRUST_PASSWORD;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.X509_HOSTNAME_VERIFIER;
import static io.cloudslang.content.office365.services.EmailServiceImpl.listMessages;
import static io.cloudslang.content.office365.utils.Constants.BOOLEAN_FALSE;
import static io.cloudslang.content.office365.utils.Constants.CHANGEIT;
import static io.cloudslang.content.office365.utils.Constants.CONNECTIONS_MAX_PER_ROUTE_CONST;
import static io.cloudslang.content.office365.utils.Constants.CONNECTIONS_MAX_TOTAL_CONST;
import static io.cloudslang.content.office365.utils.Constants.DEFAULT_JAVA_KEYSTORE;
import static io.cloudslang.content.office365.utils.Constants.DEFAULT_PROXY_PORT;
import static io.cloudslang.content.office365.utils.Constants.NEW_LINE;
import static io.cloudslang.content.office365.utils.Constants.STRICT;
import static io.cloudslang.content.office365.utils.Constants.UTF8;
import static io.cloudslang.content.office365.utils.Constants.ZERO;
import static io.cloudslang.content.office365.utils.Descriptions.Common.CONNECT_TIMEOUT_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.CONN_MAX_ROUTE_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.CONN_MAX_TOTAL_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.KEEP_ALIVE_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.PROXY_HOST_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.PROXY_PASSWORD_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.PROXY_PORT_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.PROXY_USERNAME_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.RESPONSC_CHARACTER_SET_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.SOCKET_TIMEOUT_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.TRUST_ALL_ROOTS_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.TRUST_KEYSTORE_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.TRUST_PASSWORD_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.X509_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.GetAuthorizationToken.RETURN_CODE_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.ListMessages.AUTH_TOKEN;
import static io.cloudslang.content.office365.utils.Descriptions.ListMessages.EXCEPTION_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.ListMessages.FAILURE_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.ListMessages.RETURN_RESULT_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.ListMessages.SUCCESS_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.ListMessages.USER_ID;
import static io.cloudslang.content.office365.utils.Descriptions.ListMessages.USER_PRINCIPAL_NAME;
import static io.cloudslang.content.office365.utils.InputsValidation.verifyListMessagesInputs;
import static io.cloudslang.content.office365.utils.Outputs.CommonOutputs.DOCUMENT;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class ListMessages {

    @Action(name = "Get the messages in the signed-in user's mailbox (including the Deleted Items and Clutter folders).",
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = AUTH_TOKEN, description = AUTH_TOKEN) String authToken,
                                       @Param(value = USER_PRINCIPAL_NAME, required = true, description = USER_PRINCIPAL_NAME) String userPrincipalName,
                                       @Param(value = USER_ID, encrypted = true, description = USER_ID) String userId,

                                       @Param(value = Inputs.CommonInputs.PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = Inputs.CommonInputs.PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = Inputs.CommonInputs.PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = Inputs.CommonInputs.PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,

                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,

                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = SOCKET_TIMEOUT, description = SOCKET_TIMEOUT_DESC) String socketTimeout,
                                       @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONN_MAX_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = CONN_MAX_TOTAL_DESC) String connectionsMaxTotal,
                                       @Param(value = RESPONSE_CHARACTER_SET, description = RESPONSC_CHARACTER_SET_DESC) String responseCharacterSet) {

        userPrincipalName = defaultIfEmpty(userPrincipalName, EMPTY);
        userId = defaultIfEmpty(userId, EMPTY);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
        trustKeystore = defaultIfEmpty(trustKeystore, DEFAULT_JAVA_KEYSTORE);
        trustPassword = defaultIfEmpty(trustPassword, CHANGEIT);
        connectTimeout = defaultIfEmpty(connectTimeout, ZERO);
        socketTimeout = defaultIfEmpty(socketTimeout, ZERO);
        keepAlive = defaultIfEmpty(keepAlive, BOOLEAN_FALSE);
        connectionsMaxPerRoute = defaultIfEmpty(connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE_CONST);
        connectionsMaxTotal = defaultIfEmpty(connectionsMaxTotal, CONNECTIONS_MAX_TOTAL_CONST);
        responseCharacterSet = defaultIfEmpty(responseCharacterSet, UTF8);

        final List<String> exceptionMessages = verifyListMessagesInputs(userPrincipalName, userId, proxyPort, trustAllRoots,
                                                                        connectTimeout, socketTimeout, keepAlive,
                                                                        connectionsMaxPerRoute, connectionsMaxTotal);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            final Map<String, String> result = getSuccessResultsMap(listMessages(ListMessagesInputs.builder()
                    .commonInputs(Office365CommonInputs.builder()
                            .authToken(authToken)
                            .connectionsMaxPerRoute(connectionsMaxPerRoute)
                            .connectionsMaxTotal(connectionsMaxTotal)
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
                            .userId(userId)
                            .userPrincipalName(userPrincipalName)
                            .x509HostnameVerifier(x509HostnameVerifier)
                            .trustKeystore(trustKeystore)
                            .trustPassword(trustPassword)
                            .build())
                    .build()));
            result.put(DOCUMENT, result.get(RETURN_RESULT));
            return result;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
