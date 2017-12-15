/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.dca.actions.authentication;

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

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.dca.utils.DefaultValues.*;
import static io.cloudslang.content.dca.utils.OutputNames.AUTH_TOKEN;
import static io.cloudslang.content.dca.utils.Utilities.*;
import static io.cloudslang.content.httpclient.HttpClientInputs.*;
import static io.cloudslang.content.httpclient.build.auth.AuthTypes.BASIC;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.join;

public class GetAuthenticationToken {

    @Action(name = "Get Authentication Token",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION),
                    @Output(AUTH_TOKEN)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ERROR)
            }
    )
    public static Map<String, String> execute(
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
        validator.validatePort(idmPortInp);
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

        try {
            final Map<String, String> resultMap = new CSHttpClient().execute(httpClientInputs);
            resultMap.put(AUTH_TOKEN, resultMap.get(RETURN_RESULT));
            return resultMap;
        } catch (Exception e) {
            return getFailureResultsMap("Failed to get authentication token.", e);
        }
    }

}
