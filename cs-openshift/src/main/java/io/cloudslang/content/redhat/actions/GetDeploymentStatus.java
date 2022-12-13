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
import io.cloudslang.content.httpclient.actions.HttpClientGetAction;
import io.cloudslang.content.utils.OutputUtilities;

import static io.cloudslang.content.redhat.services.OpenshiftService.*;
import static io.cloudslang.content.redhat.utils.Constants.CommonConstants.*;

import java.util.Map;

import static io.cloudslang.content.redhat.utils.Descriptions.GetDeploymentStatus.FAILURE_DESC;
import static io.cloudslang.content.redhat.utils.Descriptions.GetDeploymentStatus.STATUS_CODE_DESC;
import static io.cloudslang.content.redhat.utils.Descriptions.GetDeploymentStatus.SUCCESS_DESC;
import static io.cloudslang.content.redhat.utils.Descriptions.GetDeploymentStatus.RETURN_RESULT_DESC;
import static io.cloudslang.content.redhat.utils.Descriptions.GetPodList.FAILURE_RETURN_RESULT;
import static io.cloudslang.content.redhat.utils.Descriptions.GetPodList.NEGATIVE_RETURN_CODE;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.*;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.EXCEPTION;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.redhat.utils.Descriptions.Common.*;
import static io.cloudslang.content.redhat.utils.Descriptions.GetDeploymentStatus.*;

import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.SESSION_CONNECTION_POOL_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.SESSION_COOKIES_DESC;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.SESSION_CONNECTION_POOL;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.SESSION_COOKIES;

public class GetDeploymentStatus {

    @Action(name = GET_DEPLOYMENT_STATUS,
            description = GET_DEPLOYMENT_STATUS_DESC,
            outputs = {
                    //Common outputs
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    //Specific outputs - general
                    @Output(value = DOCUMENT_OUTPUT, description = DOCUMENT_OUTPUT_DESC),

                    @Output(value = KIND_OUTPUT, description = KIND_OUTPUT_DESC),
                    @Output(value = NAME_OUTPUT, description = NAME_OUTPUT_DESC),
                    @Output(value = NAMESPACE_OUTPUT, description = NAMESPACE_OUTPUT_DESC),
                    @Output(value = UID_OUTPUT, description = UID_OUTPUT_DESC),

                    @Output(value = OBSERVED_GENERATION_OUTPUT, description = OBSERVED_GENERATION_OUTPUT_DESC),
                    @Output(value = REPLICAS_OUTPUT, description = REPLICAS_OUTPUT_DESC),
                    @Output(value = UPDATED_REPLICAS_OUTPUT, description = UPDATED_REPLICAS_OUTPUT_DESC),
                    @Output(value = UNAVAILABLE_REPLICAS_OUTPUT, description = UNAVAILABLE_REPLICAS_OUTPUT_DESC),
                    @Output(value = CONDITIONS_OUTPUT, description = CONDITIONS_OUTPUT_DESC)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESC)
            })

    public Map<String, String> execute(
            //Common Inputs
            @Param(value = HOST, description = HOST_DESC, required = true) String host,
            @Param(value = AUTH_TOKEN, description = AUTH_TOKEN_DESC, required = true) String authToken,
            //Specific inputs
            @Param(value = NAME, description = NAME_DESC, required = true) String name,
            @Param(value = NAMESPACE, description = NAMESPACE_DESC, required = true) String namespace,
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

            Map<String, String> result = new HttpClientGetAction().execute(
                    host + GET_DEPLOYMENT_STATUS_ENDPOINT_1 + namespace +
                            GET_DEPLOYMENT_STATUS_ENDPOINT_2 + name +
                            GET_DEPLOYMENT_STATUS_ENDPOINT_3,
                    ANONYMOUS,
                    EMPTY,
                    EMPTY,
                    EMPTY,
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
                    keystore,
                    keystorePassword,
                    keepAlive,
                    connectionsMaxPerRoute,
                    connectionsMaxTotal,
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
                    sessionCookies,
                    sessionConnectionPool
            );

            if (Integer.parseInt(result.get(RETURN_CODE)) != -1) {
                if (Integer.parseInt(result.get(STATUS_CODE)) >= 200 && Integer.parseInt(result.get(STATUS_CODE)) < 300)
                    processHttpGetDeploymentStatusResult(result);
                else {
                    result.put(RETURN_CODE, NEGATIVE_RETURN_CODE);
                    result.put(EXCEPTION, result.get(RETURN_RESULT));
                    result.put(RETURN_RESULT, FAILURE_RETURN_RESULT);
                }
            }

            return result;

        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }
    }

}
