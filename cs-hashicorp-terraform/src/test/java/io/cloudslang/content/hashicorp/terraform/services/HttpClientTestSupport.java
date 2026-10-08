/*
 * Copyright 2022-2024 Open Text
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

package io.cloudslang.content.hashicorp.terraform.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.junit.jupiter.api.function.Executable;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

final class HttpClientTestSupport {

    private HttpClientTestSupport() {
    }

    static void assertHttpClientException(Executable serviceCall) {
        try (MockedStatic<HttpClientService> httpClient = mockStatic(HttpClientService.class)) {
            httpClient.when(() -> HttpClientService.execute(any(HttpClientInputs.class)))
                    .thenThrow(new IllegalArgumentException("Invalid test request"));

            assertThrows(IllegalArgumentException.class, serviceCall);
            httpClient.verify(() -> HttpClientService.execute(any(HttpClientInputs.class)));
        }
    }
}
