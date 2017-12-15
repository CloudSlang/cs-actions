/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.dca.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.HttpClientInputs;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;

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
            @Param(value = HttpClientInputs.URL, required = true) String url,
            @Param(HttpClientInputs.AUTH_TYPE) String authType,
            @Param(HttpClientInputs.PREEMPTIVE_AUTH) String preemptiveAuth,
            @Param(HttpClientInputs.USERNAME) String username,
            @Param(HttpClientInputs.PASSWORD) String password,
            @Param(HttpClientInputs.KERBEROS_CONFIG_FILE) String kerberosConfFile,
            @Param(HttpClientInputs.KERBEROS_LOGIN_CONFIG_FILE) String kerberosLoginConfFile,
            @Param(HttpClientInputs.KERBEROS_SKIP_PORT_CHECK) String kerberosSkipPortForLookup,
            @Param(HttpClientInputs.PROXY_HOST) String proxyHost,
            @Param(HttpClientInputs.PROXY_PORT) String proxyPort,
            @Param(HttpClientInputs.PROXY_USERNAME) String proxyUsername,
            @Param(HttpClientInputs.PROXY_PASSWORD) String proxyPassword,
            @Param(HttpClientInputs.TRUST_ALL_ROOTS) String trustAllRoots,
            @Param(HttpClientInputs.X509_HOSTNAME_VERIFIER) String x509HostnameVerifier,
            @Param(HttpClientInputs.TRUST_KEYSTORE) String trustKeystore,
            @Param(HttpClientInputs.TRUST_PASSWORD) String trustPassword,
            @Param(HttpClientInputs.KEYSTORE) String keystore,
            @Param(HttpClientInputs.KEYSTORE_PASSWORD) String keystorePassword,
            @Param(HttpClientInputs.CONNECT_TIMEOUT) String connectTimeout,
            @Param(HttpClientInputs.SOCKET_TIMEOUT) String socketTimeout,
            @Param(HttpClientInputs.USE_COOKIES) String useCookies,
            @Param(HttpClientInputs.KEEP_ALIVE) String keepAlive,
            @Param(HttpClientInputs.CONNECTIONS_MAX_PER_ROUTE) String connectionsMaxPerRoot,
            @Param(HttpClientInputs.CONNECTIONS_MAX_TOTAL) String connectionsMaxTotal,
            @Param(HttpClientInputs.HEADERS) String headers,
            @Param(HttpClientInputs.RESPONSE_CHARACTER_SET) String responseCharacterSet,
            @Param(HttpClientInputs.DESTINATION_FILE) String destinationFile,
            @Param(HttpClientInputs.FOLLOW_REDIRECTS) String followRedirects,
            @Param(HttpClientInputs.QUERY_PARAMS) String queryParams,
            @Param(HttpClientInputs.QUERY_PARAMS_ARE_URLENCODED) String queryParamsAreURLEncoded,
            @Param(HttpClientInputs.QUERY_PARAMS_ARE_FORM_ENCODED) String queryParamsAreFormEncoded,
            @Param(HttpClientInputs.FORM_PARAMS) String formParams,
            @Param(HttpClientInputs.FORM_PARAMS_ARE_URLENCODED) String formParamsAreURLEncoded,
            @Param(HttpClientInputs.SOURCE_FILE) String sourceFile,
            @Param(HttpClientInputs.BODY) String body,
            @Param(HttpClientInputs.CONTENT_TYPE) String contentType,
            @Param(HttpClientInputs.REQUEST_CHARACTER_SET) String requestCharacterSet,
            @Param(HttpClientInputs.MULTIPART_BODIES) String multipartBodies,
            @Param(HttpClientInputs.MULTIPART_BODIES_CONTENT_TYPE) String multipartBodiesContentType,
            @Param(HttpClientInputs.MULTIPART_FILES) String multipartFiles,
            @Param(HttpClientInputs.MULTIPART_FILES_CONTENT_TYPE) String multipartFilesContentType,
            @Param(HttpClientInputs.MULTIPART_VALUES_ARE_URLENCODED) String multipartValuesAreURLEncoded,
            @Param(HttpClientInputs.CHUNKED_REQUEST_ENTITY) String chunkedRequestEntity,
            @Param(value = HttpClientInputs.METHOD, required = true) String method,
            @Param(HttpClientInputs.SESSION_COOKIES) SerializableSessionObject httpClientCookieSession,
            @Param(HttpClientInputs.SESSION_CONNECTION_POOL) GlobalSessionObject httpClientPoolingConnectionManager
    ) {
        return getFailureResultsMap(new NotImplementedException());
    }

}
