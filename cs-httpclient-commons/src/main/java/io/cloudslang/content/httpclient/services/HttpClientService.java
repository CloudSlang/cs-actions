/*
 * (c) Copyright 2022 Micro Focus
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
package io.cloudslang.content.httpclient.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.StandardAuthScheme;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.httpclient.utils.Descriptions.HTTPClient.INVALID_STRING_ARRAY_INPUT_DESC;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class HttpClientService {
    public static Map<String, String> execute(HttpClientInputs httpClientInputs) {

        try {
            URI uri = URI.create(httpClientInputs.getHost());
            HttpUriRequestBase httpRequest = new HttpUriRequestBase(httpClientInputs.getMethod(), uri);
            SSLConnectionSocketFactory socketFactory = CustomSSLSocketFactory.createSSLSocketFactory(httpClientInputs);
            CredentialsProvider credentialsProvider = CustomCredentialsProvider.getCredentialsProvider(httpClientInputs,uri);
            RequestConfig requestConfig = CustomRequestConfig.getDefaultRequestConfig(httpClientInputs);

            final HttpClientContext context = HttpClientContext.create();
            // Contextual attributes set the local context level will take
            // precedence over those set at the client level.
            context.setCookieStore(new BasicCookieStore());
            context.setCredentialsProvider(credentialsProvider);

            CloseableHttpClient httpclient = HttpClients.custom()
                    .setDefaultCredentialsProvider(credentialsProvider)
                    //.setConnectionManager(cm)
                    .setDefaultRequestConfig(requestConfig)

                    .build();

            try (final CloseableHttpResponse response = httpclient.execute(httpRequest, context)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                System.out.println(EntityUtils.toString(response.getEntity()));

                // Once the request has been executed the local context can
                // be used to examine updated state and various objects affected
                // by the request execution.

                // Last executed request
                context.getRequest();
                // Execution route
                context.getHttpRoute();
                // Auth exchanges
                context.getAuthExchanges();
                // Cookie origin
                context.getCookieOrigin();
                // Cookie spec used
                context.getCookieSpec();
                // User security token
                context.getUserToken();
            }

            return null;

        } catch (Exception e) {
            Map<String, String> result = new HashMap<>();
            result.put(STATUS_CODE, EMPTY);
            result.put(RETURN_RESULT, INVALID_STRING_ARRAY_INPUT_DESC);
            result.put(EXCEPTION, e.toString());
            return result;
        }

    }
}
