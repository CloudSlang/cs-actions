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

package io.cloudslang.content.office365.services;

import com.microsoft.graph.models.UploadSession;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UploadBigFileImplTest {

    @Test
    void getUploadSessionPopulatesGraphSdkSession() {
        Map<String, String> result = Map.of(
                STATUS_CODE, "200",
                RETURN_RESULT, """
                        {
                          "uploadUrl": "https://example.test/upload",
                          "expirationDateTime": "2026-10-21T10:30:00Z",
                          "nextExpectedRanges": ["0-"]
                        }
                        """);

        UploadSession uploadSession = UploadBigFileImpl.getUploadSession(result);

        assertEquals("https://example.test/upload", uploadSession.getUploadUrl());
        assertEquals("0-", uploadSession.getNextExpectedRanges().get(0));
        assertNotNull(uploadSession.getExpirationDateTime());
    }
}
