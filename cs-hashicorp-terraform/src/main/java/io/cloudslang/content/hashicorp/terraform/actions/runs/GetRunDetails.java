package io.cloudslang.content.hashicorp.terraform.actions.runs;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.hashicorp.terraform.entities.GetRunDetailsInputs;
import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import java.util.Map;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.hashicorp.terraform.entities.GetRunDetailsInputs.RUN_ID;
import static io.cloudslang.content.hashicorp.terraform.entities.GetRunDetailsInputs.RUN_ID_DESC;
import static io.cloudslang.content.hashicorp.terraform.services.RunImpl.getRunDetails;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.STATUS_CODE;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.Common.CONN_MAX_TOTAL_DESC;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.Common.RESPONSC_CHARACTER_SET_DESC;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.ListOAuthClient.FAILURE_DESC;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.ListOAuthClient.RETURN_RESULT_DESC;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.ListOAuthClient.SUCCESS_DESC;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.AUTH_TOKEN;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.RESPONSE_CHARACTER_SET;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class GetRunDetails {
    /**
     * Get the details of a Run
     *
     * @param authToken         required - authentication token used to connect to Terraform API.
     *
     * @param runId             required - authentication token used to connect to Terraform API.
     *
     * @param proxyHost         Optional - proxy server used to connect to Terraform API. If empty no proxy will be used.
     *
     * @param proxyPort         Optional - proxy server port. You must either specify values for both proxyHost and
     *                          proxyPort inputs or leave them both empty.
     *                          Default: 8080
     * @param proxyUsername     Optional - proxy server user name.
     *
     * @param proxyPassword     Optional - proxy server password associated with the proxyUsername input value.
     *
     * @param trustAllRoots     Optional - Specifies whether to enable weak security over SSL/TSL.
     *
     * @param x509HostnameVerifier Optional - Specifies the way the server hostname must match a domain name in
     *                                         the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to
     *                                         allow_all to skip any checking. For the value browser_compatible the hostname verifier
     *                                         works the same way as Curl and Firefox. The hostname must match either the first CN, or any of
     *                                         the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only
     *                                         difference between browser_compatible and strict is that a wildcard (such as *.foo.com)
     *                                         with browser_compatible matches all subdomains, including a.b.foo.com
     *                             Default: "strict"
     * @param trustKeystore    Optional - The pathname of the Java TrustStore file. This contains certificates from other parties that you expect to communicate with, or from Certificate Authorities
     *                                    that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https'
     *                                    or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS);
     * @param trustPassword    Optional - The password associated with the TrustStore file. If trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied
     *
     * @param connectTimeout   Optional - The time to wait for a connection to be established in seconds. A timeout value of '0' represents an infinite timeout
     *                         Default: 10000
     *
     * @param socketTimeout    Optional - The timeout for waiting for data (a maximum period " +
     *                                    inactivity between two consecutive data packets), in seconds. A socketTimeout value of '0' represents an infinite timeout
     *                         Default: 0
     * @param keepAlive        Optional - Specifies whether to create a shared connection that will be used in subsequent calls. If keepAlive is false, the already open connection will be used and after" +
     *                                    execution it will close it
     *                         Default: true
     * @param connectionsMaxPerRoute Optional - The maximum limit of connections on a per route basis
     *                               Default: 2
     *
     * @param connectionsMaxTotal    Optional - The maximum limit of connections in total
     *                               Default: 20
     *
     * @param responseCharacterSet   Optional - The character encoding to be used for the HTTP response. If responseCharacterSet is empty, the charset from the 'Content-Type' HTTP response header will be used.If responseCharacterSet is empty and the charset from the HTTP response Content-Type header is empty, the " +
     *                                          default value will be used. You should not use this for method=HEAD or OPTIONS.
     *                               Default : UTF-8
     * @return                  A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     *                          operation, or failure message and the exception if there is one
     */

    @Action(name = "Create Run",
            outputs = {
                    @Output(value = OutputNames.RETURN_RESULT, description = RETURN_RESULT_DESC),
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = AUTH_TOKEN, required = true, description = AUTH_TOKEN_DESC) String authToken,
                                       @Param(value = RUN_ID, required = true, description = RUN_ID_DESC) String runId,
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
                                       @Param(value = RESPONSE_CHARACTER_SET, description = RESPONSC_CHARACTER_SET_DESC) String responseCharacterSet) {

        runId = defaultIfEmpty(runId, EMPTY);
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
        keepAlive = defaultIfEmpty(keepAlive, BOOLEAN_TRUE);
        connectionsMaxPerRoute = defaultIfEmpty(connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE_CONST);
        connectionsMaxTotal = defaultIfEmpty(connectionsMaxTotal, CONNECTIONS_MAX_TOTAL_CONST);
        responseCharacterSet = defaultIfEmpty(responseCharacterSet, UTF8);

        try {
            final Map<String, String> result = getRunDetails(GetRunDetailsInputs.builder()
                    .runId(runId)
                    .commonInputs(Inputs.builder()
                            .authToken(authToken)
                            .proxyHost(proxyHost)
                            .proxyPort(proxyPort)
                            .proxyUsername(proxyUsername)
                            .proxyPassword(proxyPassword)
                            .trustAllRoots(trustAllRoots)
                            .x509HostnameVerifier(x509HostnameVerifier)
                            .trustKeystore(trustKeystore)
                            .trustPassword(trustPassword)
                            .connectTimeout(connectTimeout)
                            .socketTimeout(socketTimeout)
                            .keepAlive(keepAlive)
                            .connectionsMaxPerRoot(connectionsMaxPerRoute)
                            .connectionsMaxTotal(connectionsMaxTotal)
                            .responseCharacterSet(responseCharacterSet)
                            .build())
                    .build());
            final String returnMessage = result.get(RETURN_RESULT);

            final Map<String, String> results = getOperationResults(result, returnMessage, returnMessage, returnMessage);
            final int statusCode = Integer.parseInt(result.get(STATUS_CODE));

            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
