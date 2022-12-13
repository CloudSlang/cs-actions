package io.cloudslang.content.redhat.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.actions.HttpClientDeleteAction;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.SESSION_CONNECTION_POOL_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.SESSION_COOKIES_DESC;
import static io.cloudslang.content.redhat.services.OpenshiftService.processHttpResult;
import static io.cloudslang.content.redhat.utils.Constants.CommonConstants.*;
import static io.cloudslang.content.redhat.utils.Descriptions.Common.*;
import static io.cloudslang.content.redhat.utils.Descriptions.DeletePod.*;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.AUTH_TOKEN;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.STATUS_CODE;

public class DeletePod {

    @Action(name = DELETE_POD,
            description = DELETE_POD_DESCRIPTION,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = HOST, required = true, description = HOST_DESC) String host,
                                       @Param(value = AUTH_TOKEN, required = true, description = AUTH_TOKEN_DESCRIPTION, encrypted = true) String authToken,
                                       @Param(value = NAMESPACE, required = true, description = NAMESPACE_DESCRIPTION) String namespace,
                                       @Param(value = POD_NAME, required = true, description = POD_NAME_DESCRIPTION) String podName,

                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,
                                       @Param(value = TLS_VERSION, description = TLS_VERSION_DESC) String tlsVersion,
                                       @Param(value = ALLOWED_CIPHERS, description = ALLOWED_CIPHERS_DESC) String allowedCyphers,
                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_HOSTNAME_VERIFIER_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,
                                       @Param(value = KEYSTORE, description = KEYSTORE_DESC) String keystore,
                                       @Param(value = KEYSTORE_PASSWORD, encrypted = true, description = KEYSTORE_PASSWORD_DESC) String keystorePassword,
                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String executionTimeout,
                                       @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONNECTIONS_MAX_PER_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = CONNECTIONS_MAX_TOTAL_DESC) String connectionsMaxTotal,
                                       @Param(value = SESSION_COOKIES, description = SESSION_COOKIES_DESC) SerializableSessionObject sessionCookies,
                                       @Param(value = SESSION_CONNECTION_POOL, description = SESSION_CONNECTION_POOL_DESC) GlobalSessionObject sessionConnectionPool) {
        try {

            Map<String, String> result = new HttpClientDeleteAction().execute(
                    host + POD_ENDPOINT_1 + namespace + POD_ENDPOINT_2 + FORWARD_SLASH + podName,
                    ANONYMOUS,
                    EMPTY_STRING,
                    EMPTY_STRING,
                    TRUE,
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
                    TRUE,
                    AUTHORIZATION_BEARER + authToken,
                    EMPTY_STRING,
                    EMPTY_STRING,
                    connectTimeout,
                    EMPTY_STRING,
                    executionTimeout,
                    sessionCookies,
                    sessionConnectionPool);

            processHttpResult(result);
            return result;
        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }
    }

}
