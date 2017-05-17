/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.actions.nodes;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.couchbase.entities.inputs.CommonInputs;
import io.cloudslang.content.couchbase.entities.inputs.NodeInputs;
import io.cloudslang.content.couchbase.execute.CouchbaseService;
import io.cloudslang.content.httpclient.HttpClientInputs;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.NODES;
import static io.cloudslang.content.couchbase.entities.constants.Constants.NodeActions.SET_RECOVERY_TYPE;
import static io.cloudslang.content.couchbase.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.couchbase.entities.constants.Inputs.NodeInputs.INTERNAL_NODE_IP_ADDRESS;
import static io.cloudslang.content.couchbase.entities.constants.Inputs.NodeInputs.RECOVERY_TYPE;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getHttpClientInputs;
import static io.cloudslang.content.httpclient.HttpClientInputs.CONNECT_TIMEOUT;
import static io.cloudslang.content.httpclient.HttpClientInputs.KEEP_ALIVE;
import static io.cloudslang.content.httpclient.HttpClientInputs.KEYSTORE;
import static io.cloudslang.content.httpclient.HttpClientInputs.KEYSTORE_PASSWORD;
import static io.cloudslang.content.httpclient.HttpClientInputs.PASSWORD;
import static io.cloudslang.content.httpclient.HttpClientInputs.PROXY_HOST;
import static io.cloudslang.content.httpclient.HttpClientInputs.PROXY_PASSWORD;
import static io.cloudslang.content.httpclient.HttpClientInputs.PROXY_PORT;
import static io.cloudslang.content.httpclient.HttpClientInputs.PROXY_USERNAME;
import static io.cloudslang.content.httpclient.HttpClientInputs.SOCKET_TIMEOUT;
import static io.cloudslang.content.httpclient.HttpClientInputs.TRUST_ALL_ROOTS;
import static io.cloudslang.content.httpclient.HttpClientInputs.TRUST_KEYSTORE;
import static io.cloudslang.content.httpclient.HttpClientInputs.TRUST_PASSWORD;
import static io.cloudslang.content.httpclient.HttpClientInputs.USERNAME;
import static io.cloudslang.content.httpclient.HttpClientInputs.USE_COOKIES;
import static io.cloudslang.content.httpclient.HttpClientInputs.X509_HOSTNAME_VERIFIER;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.http.client.methods.HttpPost.METHOD_NAME;

/**
 * Created by TusaM
 * 5/17/2017.
 */
public class SetRecoveryType {
    /**
     * Sets the recovery type to be performed for a node.
     * Note: Recovery options are delta node recovery or full recovery. The recovery type for a node is set after the node
     * is failed over and before rebalance. Rebalance is required to apply the recovery.
     * https://developer.couchbase.com/documentation/server/4.6/rest-api/rest-node-recovery-incremental.html
     *
     * @param endpoint              Endpoint to which request will be sent. A valid endpoint will be formatted as it shows
     *                              in bellow example.
     *                              Example: "http://somewhere.couchbase.com:8091"
     * @param username              Username used in basic authentication.
     * @param password              Password associated with "username" input to be used in basic authentication.
     * @param proxyHost             Optional - proxy server used to connect to Couchbase API. If empty no proxy will be
     *                              used.
     * @param proxyPort             Optional - proxy server port. You must either specify values for both proxyHost and
     *                              proxyPort inputs or leave them both empty.
     * @param proxyUsername         Optional - proxy server user name.
     * @param proxyPassword         Optional - proxy server password associated with the proxyUsername input value.
     * @param trustAllRoots         Optional - specifies whether to enable weak security over SSL/TSL. A certificate is
     *                              trusted even if no trusted certification authority issued it.
     *                              Valid values: "true", "false"
     *                              Default value: "true"
     * @param x509HostnameVerifier  Optional - specifies the way the server hostname must match a domain name in the subject's
     *                              Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to "allow_all"
     *                              to skip any checking. For the value "browser_compatible" the hostname verifier works
     *                              the same way as Curl and Firefox. The hostname must match either the first CN, or any
     *                              of the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts.
     *                              The only difference between "browser_compatible" and "strict" is that a wildcard (such
     *                              as "*.foo.com") with "browser_compatible" matches all subdomains, including "a.b.foo.com".
     *                              Valid values: "strict", "browser_compatible", "allow_all"
     *                              Default value: "allow_all"
     * @param trustKeystore         Optional - pathname of the Java TrustStore file. This contains certificates from other
     *                              parties that you expect to communicate with, or from Certificate Authorities that you
     *                              trust to identify other parties. If the protocol (specified by the "url") is not "https"
     *                              or if trustAllRoots is "true" this input is ignored.
     *                              Default value: ../java/lib/security/cacerts
     *                              Format: Java KeyStore (JKS)
     * @param trustPassword         Optional - password associated with the TrustStore file. If trustAllRoots is "false"
     *                              and trustKeystore is empty, trustPassword default will be supplied.
     *                              Default value: "changeit"
     * @param keystore              Optional - pathname of the Java KeyStore file. You only need this if the server requires
     *                              client authentication. If the protocol (specified by the "url") is not "https" or if
     *                              trustAllRoots is "true" this input is ignored.
     *                              Format: Java KeyStore (JKS)
     *                              Default value: ../java/lib/security/cacerts.
     * @param keystorePassword      Optional - password associated with the KeyStore file. If trustAllRoots is "false" and
     *                              keystore is empty, keystorePassword default will be supplied.
     *                              Default value: "changeit"
     * @param connectTimeout        Optional - time to wait for a connection to be established, in seconds. A timeout value
     *                              of "0" represents an infinite timeout.
     *                              Default value: "0"
     * @param socketTimeout         Optional - timeout for waiting for data (a maximum period inactivity between two
     *                              consecutive data packets), in seconds. A socketTimeout value of "0" represents an
     *                              infinite timeout.
     *                              Default value: "0"
     * @param useCookies            Optional - specifies whether to enable cookie tracking or not. Cookies are stored between
     *                              consecutive calls in a serializable session object therefore they will be available
     *                              on a branch level. If you specify a non-boolean value, the default value is used.
     *                              Valid values: "true", "false"
     *                              Default value: "true"
     * @param keepAlive             Optional - specifies whether to create a shared connection that will be used in subsequent
     *                              calls. If keepAlive is "false", the already open connection will be used and after
     *                              execution it will close it.
     *                              Valid values: "true", "false"
     *                              Default value: "true"
     * @param internalNodeIpAddress Indicates a specific node to set recovery for.
     *                              Example: "ns_2@10.0.0.2"
     * @param recoveryType          Recovery type to be performed at rebalancing nodes.
     *                              Valid values: "full", "delta"
     *                              Default value: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Set Recovery Type",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = ENDPOINT, required = true) String endpoint,
                                       @Param(value = USERNAME, required = true) String username,
                                       @Param(value = PASSWORD, required = true, encrypted = true) String password,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                       @Param(value = TRUST_ALL_ROOTS) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true) String trustPassword,
                                       @Param(value = KEYSTORE) String keystore,
                                       @Param(value = KEYSTORE_PASSWORD, encrypted = true) String keystorePassword,
                                       @Param(value = CONNECT_TIMEOUT) String connectTimeout,
                                       @Param(value = SOCKET_TIMEOUT) String socketTimeout,
                                       @Param(value = USE_COOKIES) String useCookies,
                                       @Param(value = KEEP_ALIVE) String keepAlive,
                                       @Param(value = INTERNAL_NODE_IP_ADDRESS) String internalNodeIpAddress,
                                       @Param(value = RECOVERY_TYPE) String recoveryType) {
        try {
            final HttpClientInputs httpClientInputs = getHttpClientInputs(username, password, proxyHost, proxyPort,
                    proxyUsername, proxyPassword, trustAllRoots, x509HostnameVerifier, trustKeystore, trustPassword,
                    keystore, keystorePassword, connectTimeout, socketTimeout, useCookies, keepAlive, METHOD_NAME);

            final CommonInputs commonInputs = new CommonInputs.Builder()
                    .withAction(SET_RECOVERY_TYPE)
                    .withApi(NODES)
                    .withEndpoint(endpoint)
                    .build();

            final NodeInputs nodeInputs = new NodeInputs.Builder()
                    .withInternalNodeIpAddress(internalNodeIpAddress)
                    .withRecoveryType(recoveryType)
                    .build();

            return new CouchbaseService().execute(httpClientInputs, commonInputs, nodeInputs);
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}