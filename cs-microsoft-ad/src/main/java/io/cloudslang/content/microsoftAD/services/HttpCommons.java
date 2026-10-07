/*
 * Copyright 2021-2025 Open Text
 * This program and the accompanying materials
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

package io.cloudslang.content.microsoftAD.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.microsoftAD.utils.Constants.APPLICATION_JSON;
import static io.cloudslang.content.microsoftAD.utils.Constants.BEARER;
import static io.cloudslang.content.microsoftAD.utils.Constants.AUTHORIZATION;
import static io.cloudslang.content.microsoftAD.utils.Constants.UTF8;
import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class HttpCommons {

    public static Map<String, String> httpPost(AzureActiveDirectoryCommonInputs commonInputs, String url, String body) {
        return execute(commonInputs, url, "POST", body);
    }

    public static Map<String, String> httpDelete(AzureActiveDirectoryCommonInputs commonInputs, String url) {
        return execute(commonInputs, url, "DELETE", EMPTY);
    }

    public static Map<String, String> httpGet(AzureActiveDirectoryCommonInputs commonInputs, String url) {
        return execute(commonInputs, url, "GET", EMPTY);
    }

    public static Map<String, String> httpPatch(AzureActiveDirectoryCommonInputs commonInputs, String url, String body) {
        return execute(commonInputs, url, "PATCH", body);
    }

    private static Map<String, String> execute(AzureActiveDirectoryCommonInputs commonInputs, String url,
                                               String method, String body) {
        final HttpClientInputs httpClientInputs = HttpClientInputs.builder()
                .url(url)
                .method(method)
                .headers(AUTHORIZATION + BEARER + commonInputs.getAuthToken())
                .contentType(APPLICATION_JSON)
                .body(body)
                .requestCharacterSet(UTF8)
                .responseCharacterSet(UTF8)
                .followRedirects(TRUE)
                .proxyHost(commonInputs.getProxyHost())
                .proxyPort(commonInputs.getProxyPort())
                .proxyUsername(commonInputs.getProxyUsername())
                .proxyPassword(commonInputs.getProxyPassword())
                .trustAllRoots(commonInputs.getTrustAllRoots())
                .x509HostnameVerifier(commonInputs.getX509HostnameVerifier())
                .trustKeystore(commonInputs.getTrustKeystore())
                .trustPassword(commonInputs.getTrustPassword())
                .connectTimeout(commonInputs.getConnectTimeout())
                .responseTimeout(commonInputs.getSocketTimeout())
                .executionTimeout("0")
                .keepAlive(commonInputs.getKeepAlive())
                .connectionsMaxPerRoute(commonInputs.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(commonInputs.getConnectionsMaxTotal())
                .build();

        try {
            return HttpClientService.execute(httpClientInputs);
        } catch (Exception e) {
            final Map<String, String> result = new HashMap<>();
            result.put(STATUS_CODE, EMPTY);
            result.put(RETURN_RESULT, e.getMessage());
            result.put(EXCEPTION, e.toString());
            return result;
        }
    }
}
