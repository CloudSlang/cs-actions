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

package io.cloudslang.content.hashicorp.terraform.entities;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class TerraformHttpClientInputsTest {
    @Test
    void preservesSessionObjectsWhenBuildingRequestsForBatchOperations() {
        TerraformHttpClientInputs inputs = new TerraformHttpClientInputs();

        HttpClientInputs firstRequest = inputs.build();
        HttpClientInputs secondRequest = inputs.build();

        assertSame(firstRequest.getCookieStoreSessionObject(), secondRequest.getCookieStoreSessionObject());
        assertSame(firstRequest.getConnectionPoolSessionObject(), secondRequest.getConnectionPoolSessionObject());
    }
}
