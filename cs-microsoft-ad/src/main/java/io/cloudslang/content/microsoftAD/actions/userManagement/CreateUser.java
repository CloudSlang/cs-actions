package io.cloudslang.content.microsoftAD.actions.userManagement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.microsoftAD.utils.Outputs;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.RESPONSE_CHARACTER_SET;
import static io.cloudslang.content.microsoftAD.utils.Constants.BOOLEAN_FALSE;
import static io.cloudslang.content.microsoftAD.utils.Constants.NEW_LINE;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.Common.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.CreateUser.*;
import static io.cloudslang.content.microsoftAD.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.AUTH_TOKEN;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CreateUser.*;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CreateUser.PASSWORD;
import static io.cloudslang.content.microsoftAD.utils.InputsValidation.addVerifyBoolean;
import static io.cloudslang.content.microsoftAD.utils.InputsValidation.addVerifyNotNullOrEmpty;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class CreateUser {

    @Action(name = DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = CREATE_USER_RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = ""),
                    @Output(value = Outputs.CommonOutputs.STATUS_CODE, description = "")
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = "SUCCESS_DESC"),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = "FAILURE_DESC")
            })

    public Map<String, String> execute(@Param(value = AUTH_TOKEN, required = true, description = AUTH_TOKEN_DESC) String authToken,

                                       @Param(value = ACCOUNT_ENABLED, description = ACCOUNT_ENABLED_DESC) String accountEnabled,
                                       @Param(value = DISPLAY_NAME, description = DISPLAY_NAME_DESC) String displayName,
                                       @Param(value = ON_PREMISES_IMMUTABLE_ID, description = ON_PREMISES_IMMUTABLE_ID_DESC) String onPremisesImmutableId,
                                       @Param(value = MAIL_NICKNAME, description = MAIL_NICKNAME_DESC) String mailNickname,
                                       @Param(value = FORCE_CHANGE_PASSWORD, description = FORCE_CHANGE_PASSWORD_DESC) String forceChangePassword,
                                       @Param(value = PASSWORD, encrypted = true, required = true, description = PASSWORD_DESC) String password,
                                       @Param(value = USER_PRINCIPAL_NAME, required = true, description = USER_PRINCIPAL_NAME_DESC) String userPrincipalName,

                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,

                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,

                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = SOCKET_TIMEOUT, description = SOCKET_TIMEOUT_DESC) String socketTimeout,
                                       @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONN_MAX_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = CONN_MAX_TOTAL_DESC) String connectionsMaxTotal,
                                       @Param(value = RESPONSE_CHARACTER_SET, description = CONN_MAX_TOTAL_DESC) String responseCharacterSet) {

        accountEnabled = defaultIfEmpty(accountEnabled, BOOLEAN_FALSE);
        onPremisesImmutableId = defaultIfEmpty(onPremisesImmutableId, BOOLEAN_FALSE);
        forceChangePassword = defaultIfEmpty(forceChangePassword, BOOLEAN_FALSE);

        List<String> exceptionMessages = new ArrayList<>();

        addVerifyNotNullOrEmpty(exceptionMessages, accountEnabled, ACCOUNT_ENABLED);
        addVerifyNotNullOrEmpty(exceptionMessages, accountEnabled, ACCOUNT_ENABLED);
        addVerifyBoolean(exceptionMessages, accountEnabled, ACCOUNT_ENABLED);
        addVerifyNotNullOrEmpty(exceptionMessages, displayName, DISPLAY_NAME);
        addVerifyNotNullOrEmpty(exceptionMessages, mailNickname, MAIL_NICKNAME);
        addVerifyNotNullOrEmpty(exceptionMessages, userPrincipalName, USER_PRINCIPAL_NAME);
        addVerifyNotNullOrEmpty(exceptionMessages, forceChangePassword, FORCE_CHANGE_PASSWORD);
        addVerifyNotNullOrEmpty(exceptionMessages, password, PASSWORD);

        if (exceptionMessages.size() > 0)
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));

        Map<String, String> results = new HashMap<>();

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            //HttpHost proxy = new HttpHost("www.proxy.com", 8080, "http");

            HttpPost httpPost = new HttpPost("https://graph.microsoft.com/v1.0/users");
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + authToken);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            JsonObject passwordProfile = new JsonObject();
            passwordProfile.addProperty(FORCE_CHANGE_PASSWORD, forceChangePassword);
            passwordProfile.addProperty(PASSWORD, password);

            JsonObject body = new JsonObject();
            body.addProperty(ACCOUNT_ENABLED, accountEnabled);
            body.addProperty(DISPLAY_NAME, displayName);
            body.addProperty(ON_PREMISES_IMMUTABLE_ID, onPremisesImmutableId);
            body.addProperty(MAIL_NICKNAME, mailNickname);
            body.addProperty(USER_PRINCIPAL_NAME, userPrincipalName);
            body.add("passwordProfile", passwordProfile);

            System.out.println(body);
            httpPost.setEntity(new StringEntity(body.toString()));

            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                results.put("return_result", EntityUtils.toString(response.getEntity()));
                return getOperationResults(results, SUCCESS, SUCCESS);
            }
        } catch (IOException e) {
            return new HashMap<>();
        }
    }
}
