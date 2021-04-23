package io.cloudslang.content.azure.actions.utils;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.azure.entities.AuthorizationTokenUsingWebAPIInputs;
import io.cloudslang.content.azure.services.AuthenticationTokenUsingWebAPIImpl;
import io.cloudslang.content.constants.ReturnCodes;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;

import static io.cloudslang.content.azure.utils.AuthorizationInputNames.CLIENT_ID;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_HOST;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_PASSWORD;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_PORT;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_USERNAME;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.RESOURCE;
import static io.cloudslang.content.azure.utils.Constants.Common.*;
import static io.cloudslang.content.azure.utils.Constants.DEFAULT_PROXY_PORT;
import static io.cloudslang.content.azure.utils.Constants.DEFAULT_RESOURCE;
import static io.cloudslang.content.azure.utils.Descriptions.Common.*;
import static io.cloudslang.content.azure.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.azure.utils.Outputs.CommonOutputs.AUTH_TOKEN;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.TRUST_PASSWORD;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class GetAuthTokenUsingWebAPI {
    @Action(name = "Get the authorization token for Azure",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(AUTH_TOKEN),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR)
            })
    public Map<String, String> execute(@Param(value = TENANT_ID, required = true, description = TENANT_ID_DESC) String tenantId,
                                       @Param(value = CLIENT_ID, required = true, description = CLIENT_ID_DESC) String clientId,
                                       @Param(value = CLIENT_SECRET, required = true, encrypted = true, description = CLIENT_SECRET_DESC) String clientSecret,
                                       @Param(value = RESOURCE, description = RESOURCE_DESC) String resource,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,
                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword) {

        resource = defaultIfEmpty(resource, DEFAULT_RESOURCE);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
        trustKeystore = defaultIfEmpty(trustKeystore, DEFAULT_JAVA_KEYSTORE);
        trustPassword = defaultIfEmpty(trustPassword, CHANGEIT);
        byte[] bytes = clientSecret.getBytes(StandardCharsets.UTF_8);

        String clientSecretEncode = new String(bytes, StandardCharsets.UTF_8);
        try {
            return AuthenticationTokenUsingWebAPIImpl.getAuthToken(AuthorizationTokenUsingWebAPIInputs.builder()
                    .tenantId(tenantId)
                    .clientSecret(clientSecretEncode)
                    .clientId(clientId)
                    .resource(resource)
                    .proxyPort(proxyPort)
                    .proxyHost(proxyHost)
                    .proxyUsername(proxyUsername)
                    .proxyPassword(proxyPassword)
                    .trustAllRoots(trustAllRoots)
                    .x509HostnameVerifier(x509HostnameVerifier)
                    .trustKeystore(trustKeystore)
                    .trustPassword(trustPassword)
                    .build());

//            return getSuccessResultsMap(result.getAccessTokenType() + SPACE + result.getAccessToken());
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}


