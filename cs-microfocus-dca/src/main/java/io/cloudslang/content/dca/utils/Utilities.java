/*
 * (c) Copyright 2018 Micro Focus, L.P.
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
package io.cloudslang.content.dca.utils;

import io.cloudslang.content.dca.models.DcaAuthModel;
import io.cloudslang.content.httpclient.HttpClientInputs;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.splitPreserveAllTokens;

public class Utilities {

    public static final String DCA_CM_CREDENTIAL_PATH = "/credential";

    @NotNull
    public static String getIdmUrl(@NotNull final String protocol,
                                   @NotNull final String idmHostInp,
                                   @NotNull final String idmPort) {
        final URIBuilder uriBuilder = getUriBuilder(protocol, idmHostInp, idmPort);
        uriBuilder.setPath(Constants.IDM_TOKENS_PATH);

        try {
            return uriBuilder.build().toURL().toString();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static String getDcaDeployUrl(@NotNull final String protocol,
                                         @NotNull final String dcaHost,
                                         @NotNull final String dcaPort) {
        final URIBuilder uriBuilder = getUriBuilder(protocol, dcaHost, dcaPort);
        uriBuilder.setPath(Constants.DCA_DEPLOYMENT_PATH);

        try {
            return uriBuilder.build().toURL().toString();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static String getDcaDeploymentUrl(@NotNull final String protocol,
                                             @NotNull final String dcaHost,
                                             @NotNull final String dcaPort,
                                             @NotNull final String deploymentUuid) {
        final URIBuilder uriBuilder = getUriBuilder(protocol, dcaHost, dcaPort);
        uriBuilder.setPath(String.format("%s/%s", Constants.DCA_DEPLOYMENT_PATH, deploymentUuid));

        try {
            return uriBuilder.build().toURL().toString();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static String getDcaCMCredentialUrl(@NotNull final String protocol,
                                             @NotNull final String dcaHost,
                                             @NotNull final String dcaPort,
                                             @NotNull final String credentialUuid) {
        final URIBuilder uriBuilder = getUriBuilder(protocol, dcaHost, dcaPort);
        uriBuilder.setPath(String.format("%s/%s", DCA_CM_CREDENTIAL_PATH, credentialUuid));

        try {
            return uriBuilder.build().toURL().toString();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static String getDcaResourceUrl(@NotNull final String protocol,
                                           @NotNull final String dcaHost,
                                           @NotNull final String dcaPort,
                                           @NotNull final String resourceUuid) {
        final URIBuilder uriBuilder = getUriBuilder(protocol, dcaHost, dcaPort);
        uriBuilder.setPath(String.format("%s/%s", Constants.DCA_RESOURCE_PATH, resourceUuid));

        try {
            return uriBuilder.build().toURL().toString();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static URIBuilder getUriBuilder(@NotNull String protocol, @NotNull String dcaHost, @NotNull String dcaPort) {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost(dcaHost);
        uriBuilder.setPort(Integer.valueOf(dcaPort));
        uriBuilder.setScheme(protocol);
        return uriBuilder;
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
                                         @NotNull final String trustKeystore,
                                         @NotNull final String trustPassword,
                                         @NotNull final String keystore,
                                         @NotNull final String keystorePassword) {
        httpClientInputs.setTrustAllRoots(trustAllRoots);
        httpClientInputs.setX509HostnameVerifier(x509HostnameVerifier);
        httpClientInputs.setTrustKeystore(trustKeystore);
        httpClientInputs.setTrustPassword(trustPassword);
        httpClientInputs.setKeystore(keystore);
        httpClientInputs.setKeystorePassword(keystorePassword);
    }

    public static void setDcaCredentials(@NotNull final HttpClientInputs httpClientInputs,
                                         @NotNull final String dcaUsername,
                                         @NotNull final String dcaPassword,
                                         @NotNull final String dcaTenant) {
        final DcaAuthModel dcaAuthModel = new DcaAuthModel(dcaTenant);
        dcaAuthModel.setCredentials(dcaUsername, dcaPassword);
        httpClientInputs.setBody(dcaAuthModel.toJson());
        httpClientInputs.setPreemptiveAuth("true");
    }

    public static void setIdmAuthentication(@NotNull final HttpClientInputs httpClientInputs,
                                            @NotNull final String authType,
                                            @NotNull final String idmUsername,
                                            @NotNull final String idmPassword,
                                            @NotNull final String preemptiveAuth) {
        httpClientInputs.setAuthType(authType); // todo check if IDM supports other authType
        httpClientInputs.setPreemptiveAuth(preemptiveAuth);
        httpClientInputs.setUsername(idmUsername);
        httpClientInputs.setPassword(idmPassword);
    }

    public static void setConnectionParameters(HttpClientInputs httpClientInputs,
                                               @NotNull final String connectTimeout,
                                               @NotNull final String socketTimeout,
                                               @NotNull final String useCookies,
                                               @NotNull final String keepAlive,
                                               @NotNull final String connectionsMaxPerRoot,
                                               @NotNull final String connectionsMaxTotal) {
        httpClientInputs.setConnectTimeout(connectTimeout);
        httpClientInputs.setSocketTimeout(socketTimeout);
        httpClientInputs.setUseCookies(useCookies);
        httpClientInputs.setKeepAlive(keepAlive);
        httpClientInputs.setConnectionsMaxPerRoute(connectionsMaxPerRoot);
        httpClientInputs.setConnectionsMaxTotal(connectionsMaxTotal);
    }

    @NotNull
    public static List<String> splitToList(@NotNull final String string, @NotNull final String delimiter) {
        final List<String> tokens = asList(splitPreserveAllTokens(string, delimiter));
        if (tokens.isEmpty()) {
            // to make sure that if empty string is added it will be put in the array
            return asList(EMPTY);
        }
        return tokens;
    }

    @NotNull
    public static String getAuthHeaders(@NotNull final String authToken, @NotNull final String refreshToken) {
        final StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append(Constants.X_AUTH_TOKEN_HEADER).append(authToken).append(Constants.HEADERS_DELIMITER);
//        headerBuilder.append("Content-Type:").append(APPLICATION_JSON).append(Constants.HEADERS_DELIMITER);
        if (StringUtils.isNotEmpty(refreshToken)) {
            headerBuilder.append(Constants.REFRESH_TOKEN_HEADER).append(refreshToken).append(Constants.HEADERS_DELIMITER);
        }
        return headerBuilder.toString();
    }
}
