/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * (c) Copyright 2020 Micro Focus, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.content.sitescope.utils;

import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.sitescope.constants.ExceptionMsgs;
import io.cloudslang.content.sitescope.constants.Outputs;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

import static java.net.Proxy.Type.HTTP;

public class HttpUtils {
    @NotNull
    public static Proxy getProxy(@NotNull final String proxyHost, final int proxyPort, @NotNull final String proxyUser, @NotNull final String proxyPassword) {
        if (StringUtilities.isBlank(proxyHost)) {
            return Proxy.NO_PROXY;
        }
        if (StringUtilities.isNotEmpty(proxyUser)) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                }
            });
        }
        return new Proxy(HTTP, InetSocketAddress.createUnresolved(proxyHost, proxyPort));
    }


    public static void setProxy(@NotNull final HttpClientInputs httpClientInputs,
                                @NotNull final String proxyHost,
                                @NotNull final String proxyPort,
                                @NotNull final String proxyUsername,
                                @NotNull final String proxyPassword) {
        httpClientInputs.setProxyHost(proxyHost);
        httpClientInputs.setProxyPort(proxyPort);
        httpClientInputs.setProxyUsername(proxyUsername);
        httpClientInputs.setProxyPassword(proxyPassword);
    }


    public static void setSecurityInputs(@NotNull final HttpClientInputs httpClientInputs,
                                         @NotNull final String trustAllRoots,
                                         @NotNull final String x509HostnameVerifier,
                                         @NotNull final String keystore,
                                         @NotNull final String keystorePassword,
                                         @NotNull final String trustKeystore,
                                         @NotNull final String trustPassword) {
        httpClientInputs.setTrustAllRoots(trustAllRoots);
        httpClientInputs.setX509HostnameVerifier(x509HostnameVerifier);
        httpClientInputs.setKeystore(keystore);
        httpClientInputs.setKeystorePassword(keystorePassword);
        httpClientInputs.setTrustKeystore(trustKeystore);
        httpClientInputs.setTrustPassword(trustPassword);
    }


    public static void setConnectionParameters(HttpClientInputs httpClientInputs,
                                               @NotNull final String connectTimeout,
                                               @NotNull final String socketTimeout,
                                               @NotNull final String keepAlive,
                                               @NotNull final String connectionsMaxPerRoot,
                                               @NotNull final String connectionsMaxTotal,
                                               @NotNull final String responseCharacterSet) {
        httpClientInputs.setConnectTimeout(connectTimeout);
        httpClientInputs.setSocketTimeout(socketTimeout);
        httpClientInputs.setKeepAlive(keepAlive);
        httpClientInputs.setConnectionsMaxPerRoute(connectionsMaxPerRoot);
        httpClientInputs.setConnectionsMaxTotal(connectionsMaxTotal);
        httpClientInputs.setResponseCharacterSet(responseCharacterSet);
    }


    public static Map<String, String> convertToSitescopeResultsMap(@NotNull final Map<String, String> httpClientOutputs,
                                                                   @NotNull final String successMsg) throws Exception {
        Map<String, String> sitescopeOutputs = new HashMap<>();

        if (!httpClientOutputs.containsKey(Outputs.STATUS_CODE)) {
            sitescopeOutputs.put(Outputs.RETURN_RESULT, httpClientOutputs.get(Outputs.RETURN_RESULT));
            sitescopeOutputs.put(Outputs.RETURN_CODE, ReturnCodes.FAILURE);
            sitescopeOutputs.put(Outputs.EXCEPTION, httpClientOutputs.get(Outputs.EXCEPTION));
            return sitescopeOutputs;
        }

        if (httpClientOutputs.get(Outputs.STATUS_CODE).equals(String.valueOf(200))) {
            String errMsg = httpClientOutputs.get("returnResult");
            if (errMsg.contains("errorMessage")) {
                sitescopeOutputs.put(Outputs.RETURN_RESULT, httpClientOutputs.get(Outputs.RETURN_RESULT));
                sitescopeOutputs.put(Outputs.STATUS_CODE, httpClientOutputs.get(Outputs.STATUS_CODE));
                sitescopeOutputs.put(Outputs.RETURN_CODE, ReturnCodes.FAILURE);
            } else {
                sitescopeOutputs.put(Outputs.RETURN_RESULT, httpClientOutputs.get(Outputs.RETURN_RESULT));
                sitescopeOutputs.put(Outputs.STATUS_CODE, httpClientOutputs.get(Outputs.STATUS_CODE));
                sitescopeOutputs.put(Outputs.RETURN_CODE, ReturnCodes.SUCCESS);
            }
            return sitescopeOutputs;
        }

        if (httpClientOutputs.get(Outputs.STATUS_CODE).equals(String.valueOf(204))) {
            sitescopeOutputs.put(Outputs.RETURN_RESULT, successMsg);
            sitescopeOutputs.put(Outputs.STATUS_CODE, httpClientOutputs.get(Outputs.STATUS_CODE));
            sitescopeOutputs.put(Outputs.RETURN_CODE, ReturnCodes.SUCCESS);
            return sitescopeOutputs;
        }

        if (httpClientOutputs.get(Outputs.STATUS_CODE).equals(String.valueOf(400))) {
            sitescopeOutputs.put(Outputs.RETURN_RESULT, httpClientOutputs.get(Outputs.RETURN_RESULT));
            sitescopeOutputs.put(Outputs.STATUS_CODE, httpClientOutputs.get(Outputs.STATUS_CODE));
            sitescopeOutputs.put(Outputs.RETURN_CODE, ReturnCodes.FAILURE);
            return sitescopeOutputs;
        }

        if (httpClientOutputs.get(Outputs.STATUS_CODE).equals(String.valueOf(500))) {
            sitescopeOutputs.put(Outputs.RETURN_RESULT, httpClientOutputs.get(Outputs.RETURN_RESULT));
            sitescopeOutputs.put(Outputs.STATUS_CODE, httpClientOutputs.get(Outputs.STATUS_CODE));
            sitescopeOutputs.put(Outputs.RETURN_CODE, ReturnCodes.FAILURE);
            return sitescopeOutputs;
        }

        if (StringUtils.isEmpty(httpClientOutputs.get(Outputs.RETURN_RESULT))) {
            String errMsg = httpClientOutputs.get("reasonPhrase");
            sitescopeOutputs.put(Outputs.RETURN_RESULT, errMsg);
            sitescopeOutputs.put(Outputs.STATUS_CODE, httpClientOutputs.get(Outputs.STATUS_CODE));
            sitescopeOutputs.put(Outputs.RETURN_CODE, ReturnCodes.FAILURE);
            sitescopeOutputs.put(Outputs.EXCEPTION, ExceptionUtils.getStackTrace(new Exception(errMsg)));
            return sitescopeOutputs;
        }

        try {
            String errMsg = JsonPath.read(httpClientOutputs.get(Outputs.RETURN_RESULT), "$.message");
            sitescopeOutputs.put(Outputs.RETURN_RESULT, errMsg);
            sitescopeOutputs.put(Outputs.STATUS_CODE, httpClientOutputs.get(Outputs.STATUS_CODE));
            sitescopeOutputs.put(Outputs.RETURN_CODE, ReturnCodes.FAILURE);
            sitescopeOutputs.put(Outputs.EXCEPTION, ExceptionUtils.getStackTrace(new Exception(errMsg)));
        } catch (Exception ex) {
            throw new Exception(ExceptionMsgs.EXCEPTION_WHILE_PARSING_RESPONSE);
        }
        return sitescopeOutputs;
    }
}
