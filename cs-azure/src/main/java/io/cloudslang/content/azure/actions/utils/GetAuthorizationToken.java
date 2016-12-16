/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.azure.actions.utils;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.microsoft.aad.adal4j.AuthenticationResult;
import io.cloudslang.content.azure.entities.AuthorizationTokenInputs;
import io.cloudslang.content.azure.services.AuthorizationTokenImpl;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.NumberUtilities;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.LOGIN_AUTHORITY;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.CLIENT_ID;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PASSWORD;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_HOST;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_PASSWORD;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_PORT;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_USERNAME;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.RESOURCE;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.USERNAME;
import static io.cloudslang.content.azure.utils.Constants.DEFAULT_AUTHORITY;
import static io.cloudslang.content.azure.utils.Constants.DEFAULT_CLIENT_ID;
import static io.cloudslang.content.azure.utils.Constants.DEFAULT_PROXY_PORT;
import static io.cloudslang.content.azure.utils.Constants.DEFAULT_RESOURCE;
import static io.cloudslang.content.azure.utils.Constants.NEW_LINE;
import static io.cloudslang.content.azure.utils.InputsValidation.verifyAuthorizationInputs;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by victor on 27.09.2016.
 */
public class GetAuthorizationToken {

    /**
     * @param username       Azure username
     * @param password       Azure password
     * @param clientId       Service Client ID
     *                       Default: '9ba1a5c7-f17a-4de9-a1f1-6178c8d51223'
     * @param loginAuthority The authority URL
     *                       Default: 'https://login.windows.net/common'
     * @param resource       The resource URL
     *                       Default: 'https://management.azure.com'
     * @param proxyHost      Proxy server used to access the web site
     * @param proxyPort      Proxy server port
     *                       Default: '8080'
     * @param proxyUsername  User name used when connecting to the proxy
     * @param proxyPassword  The proxy server password associated with the proxyUsername input value
     * @return The authorization Bearer token for Azure
     */
    @Action(name = "Get the authorization token for Azure",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR)
            })
    public Map<String, String> execute(@Param(value = USERNAME, required = true) String username,
                                       @Param(value = PASSWORD, required = true, encrypted = true) String password,
                                       @Param(value = CLIENT_ID) String clientId,
                                       @Param(value = LOGIN_AUTHORITY) String loginAuthority,
                                       @Param(value = RESOURCE) String resource,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword) {
        clientId = defaultIfEmpty(clientId, DEFAULT_CLIENT_ID);
        loginAuthority = defaultIfEmpty(loginAuthority, DEFAULT_AUTHORITY);
        resource = defaultIfEmpty(resource, DEFAULT_RESOURCE);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        final List<String> exceptionMessages = verifyAuthorizationInputs(username, password, clientId, proxyPort);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            final AuthenticationResult result = AuthorizationTokenImpl.getToken(AuthorizationTokenInputs.builder()
                    .username(username)
                    .password(password)
                    .clientId(clientId)
                    .authority(loginAuthority)
                    .resource(resource)
                    .proxyHost(proxyHost)
                    .proxyPort(NumberUtilities.toInteger(proxyPort))
                    .proxyUsername(proxyUsername)
                    .proxyPassword(proxyPassword)
                    .build());
            return getSuccessResultsMap(result.getAccessTokenType() + SPACE + result.getAccessToken());
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
