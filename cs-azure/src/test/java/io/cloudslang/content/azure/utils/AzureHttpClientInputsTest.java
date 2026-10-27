/*
 * Copyright 2026-2027 Open Text
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

package io.cloudslang.content.azure.utils;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.azure.utils.Constants.Common.ANONYMOUS;
import static io.cloudslang.content.httpclient.utils.Constants.DEFAULT_CONNECT_TIMEOUT;
import static io.cloudslang.content.httpclient.utils.Constants.DEFAULT_EXECUTION_TIMEOUT;
import static io.cloudslang.content.httpclient.utils.Constants.DEFAULT_RESPONSE_TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AzureHttpClientInputsTest {
    @Test
    void buildsInputsWithAzureRequestAndSecurityValues() {
        AzureHttpClientInputs inputs = new AzureHttpClientInputs();
        inputs.setUrl("https://management.azure.com/subscriptions/123");
        inputs.setAuthType(ANONYMOUS);
        inputs.setMethod("GET");
        inputs.setHeaders("Authorization: Bearer token");
        inputs.setQueryParams("api-version=2020-01-01");
        inputs.setProxyHost("proxy.example");
        inputs.setProxyPort("8080");
        inputs.setTrustAllRoots("false");
        inputs.setTlsVersion("TLSv1.2");
        inputs.setAllowedCyphers("TLS_AES_128_GCM_SHA256");

        HttpClientInputs built = inputs.build();

        assertEquals("https://management.azure.com/subscriptions/123", built.getUrl());
        assertEquals("GET", built.getMethod());
        assertEquals("Authorization: Bearer token", built.getHeaders());
        assertEquals("api-version=2020-01-01", built.getQueryParams());
        assertEquals("proxy.example", built.getProxyHost());
        assertEquals("8080", built.getProxyPort());
        assertEquals("false", built.getTrustAllRoots());
        assertEquals("TLSv1.2", built.getTlsVersion());
        assertEquals("TLS_AES_128_GCM_SHA256", built.getAllowedCiphers());
    }

    @Test
    void suppliesCommonsDefaultsRequiredByDirectExecution() {
        HttpClientInputs built = new AzureHttpClientInputs().build();

        assertEquals("ANONYMOUS", built.getAuthType());
        assertEquals(DEFAULT_CONNECT_TIMEOUT, built.getConnectTimeout());
        assertEquals(DEFAULT_RESPONSE_TIMEOUT, built.getResponseTimeout());
        assertEquals(DEFAULT_EXECUTION_TIMEOUT, built.getExecutionTimeout());
        assertEquals("2", built.getConnectionsMaxPerRoute());
        assertEquals("20", built.getConnectionsMaxTotal());
    }
}
