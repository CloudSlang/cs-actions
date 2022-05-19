package io.cloudslang.content.cyberark.actions.accounts;


import com.hp.oo.sdk.content.annotations.*;
import com.hp.oo.sdk.content.plugin.ActionMetadata.*;
import com.hp.oo.sdk.content.plugin.*;
import io.cloudslang.content.constants.*;
import io.cloudslang.content.httpclient.actions.*;
import io.cloudslang.content.utils.*;

import java.util.*;

import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.*;
import static io.cloudslang.content.cyberark.utils.Constants.GetAccountsConstants.*;
import static io.cloudslang.content.cyberark.utils.Constants.OtherConstants.*;
import static io.cloudslang.content.cyberark.utils.CyberarkUtils.*;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.*;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.*;
import static org.apache.commons.lang3.StringUtils.*;

public class GetAccounts {

    @Action(name = GET_ACCOUNTS,
            description = GET_ACCOUNTS_DESCRIPTION,
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(STATUS_CODE),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(
            @Param(value = HOST, description = HOST_DESCRIPTION, required = true) String hostname,
            @Param(value = PROTOCOL, description = PROTOCOL_DESCRIPTION) String protocol,
            @Param(value = AUTH_TOKEN, description = AUTH_TOKEN_DESCRIPTION, required = true) String authToken,
            @Param(value = SEARCH, description = SEARCH_DESCRIPTION) String search,
            @Param(value = SEARCH_TYPE, description = SEARCH_TYPE_DESCRIPTION) String searchType,
            @Param(value = SORT, description = SORT_DESCRIPTION) String sort,
            @Param(value = LIMIT, description = LIMIT_DESCRIPTION) String limit,
            @Param(value = FILTER, description = FILTER_DESCRIPTION) String filter,
            @Param(value = SAVED_FILTER, description = SAVED_FILTER_DESCRIPTION) String savedFilter,
            @Param(value = PROXY_HOST, description = PROXY_HOST_DESCRIPTION) String proxyHost,
            @Param(value = PROXY_PORT, description = PROXY_PORT_DESCRIPTION) String proxyPort,
            @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESCRIPTION) String proxyUsername,
            @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESCRIPTION) String proxyPassword,
            @Param(value = TLS_VERSION, description = TLS_VERSION_DESCRIPTION) String tlsVersion,
            @Param(value = ALLOWED_CYPHERS, description = ALLOWED_CYPHERS_DESCRIPTION) String allowedCyphers,
            @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESCRIPTION) String trustAllRoots,
            @Param(value = X509_HOSTNAME_VERIFIER, description = X509_HOSTNAME_VERIFIER_DESCRIPTION) String x509HostnameVerifier,
            @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESCRIPTION) String trustKeystore,
            @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESCRIPTION) String trustPassword,
            @Param(value = KEYSTORE, description = KEYSTORE_DESCRIPTION) String keystore,
            @Param(value = KEYSTORE_PASSWORD, encrypted = true, description = KEYSTORE_PASSWORD_DESCRIPTION) String keystorePassword,
            @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESCRIPTION) String connectTimeout,
            @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESCRIPTION) String executionTimeout,
            @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESCRIPTION) String keepAlive,
            @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONNECTIONS_MAX_PER_ROUTE_DESCRIPTION) String connectionsMaxPerRoute,
            @Param(value = CONNECTIONS_MAX_TOTAL, description = CONNECTIONS_MAX_TOTAL_DESCRIPTION) String connectionsMaxTotal,
            @Param(value = SESSION_COOKIES, description = SESSION_COOKIES_DESC) SerializableSessionObject sessionCookies,
            @Param(value = SESSION_CONNECTION_POOL, description = SESSION_CONNECTION_POOL_DESC) GlobalSessionObject sessionConnectionPool) {

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(SEARCH, search);
        queryParams.put(SEARCH_TYPE, searchType);
        queryParams.put(SORT, sort);
        queryParams.put(LIMIT, limit);
        queryParams.put(FILTER, filter);
        queryParams.put(SAVED_FILTER, savedFilter);

        try {

            validateProtocol(protocol);

            Map<String, String> result = new HttpClientGetAction().execute(
                    protocol + PROTOCOL_DELIMITER + hostname + GET_ACCOUNTS_ENDPOINT,
                    ANONYMOUS,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    proxyHost,
                    proxyPort,
                    proxyUsername,
                    proxyPassword,
                    tlsVersion,
                    allowedCyphers,
                    trustAllRoots,
                    x509HostnameVerifier,
                    trustKeystore,
                    trustPassword,
                    keystore,
                    keystorePassword,
                    keepAlive,
                    connectionsMaxPerRoute,
                    connectionsMaxTotal,
                    EMPTY,
                    EMPTY,
                    CONTENT_TYPE + APPLICATION_JSON + COMMA + AUTHORIZATION + authToken,
                    EMPTY,
                    EMPTY,
                    getQueryParamsString(queryParams),
                    EMPTY,
                    EMPTY,
                    connectTimeout,
                    EMPTY,
                    executionTimeout,
                    sessionCookies,
                    sessionConnectionPool
            );

            removeUnusedHttpResults(result);
            return result;

        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }
    }
}
