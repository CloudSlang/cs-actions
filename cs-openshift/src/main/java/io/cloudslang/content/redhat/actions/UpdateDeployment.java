package io.cloudslang.content.redhat.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.actions.HttpClientPatchAction;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.SESSION_CONNECTION_POOL_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.SESSION_COOKIES_DESC;
import static io.cloudslang.content.redhat.services.OpenshiftService.*;
import static io.cloudslang.content.redhat.utils.Constants.CommonConstants.*;
import static io.cloudslang.content.redhat.utils.Constants.CommonConstants.AUTH_TOKEN;
import static io.cloudslang.content.redhat.utils.Descriptions.Common.*;
import static io.cloudslang.content.redhat.utils.Descriptions.Common.EXECUTION_TIMEOUT_DESC;
import static io.cloudslang.content.redhat.utils.Descriptions.UpdateDeployment.*;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class UpdateDeployment {
    @Action(name = UPDATE_DEPLOYMENT,
            description = UPDATE_DEPLOYMENT_DESC,
            outputs = {
                    //Common outputs
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                 },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESC)
            })

    public Map<String, String> execute(
            //Specific input
            @Param(value = HOST, description = HOST_DESC, required = true) String host,
            @Param(value = AUTH_TOKEN, description = AUTH_TOKEN_DESC, required = true, encrypted = true) String authToken,
            @Param(value = NAME, description = NAME_DESC, required = true) String name,
            @Param(value = NAMESPACE, description = NAMESPACE_DESC, required = true) String namespace,
            @Param(value = DEFINITION, description = DEFINITION_DESCRIPTION, required = true) String body,


            //Common Inputs
            @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
            @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
            @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
            @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,
            @Param(value = TLS_VERSION, description = TLS_VERSION_DESC) String tlsVersion,
            @Param(value = ALLOWED_CIPHERS, description = ALLOWED_CIPHERS_DESC) String allowedCiphers,
            @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
            @Param(value = X509_HOSTNAME_VERIFIER, description = X509_HOSTNAME_VERIFIER_DESC) String x509HostnameVerifier,
            @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
            @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,
            @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
            @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String executionTimeout,
            @Param(value = SESSION_COOKIES, description = SESSION_COOKIES_DESC) SerializableSessionObject sessionCookies,
            @Param(value = SESSION_CONNECTION_POOL, description = SESSION_CONNECTION_POOL_DESC) GlobalSessionObject sessionConnectionPool) {

        Map<String, String> result = new HashMap<>();

        try {

            result = new HttpClientPatchAction().execute(
                    host + GET_DEPLOYMENT_STATUS_ENDPOINT_1 + namespace + GET_DEPLOYMENT_STATUS_ENDPOINT_2 + name,
                    ANONYMOUS,
                    EMPTY,
                    EMPTY,
                    TRUE,
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
                    CONNECTION_MAX_PER_ROUTE,
                    CONNECTIONS_MAX_TOTAL_VALUE,
                    EMPTY,
                    EMPTY,
                    AUTHORIZATION_BEARER + authToken,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    body,
                    CONTENT_TYPE_PATCH,
                    EMPTY,
                    connectTimeout,
                    EMPTY,
                    executionTimeout,
                    sessionCookies,
                    sessionConnectionPool
            );

            processHttpResult(result);
            return result;
        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }
    }
}
