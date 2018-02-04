/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.urldecoder;

import io.cloudslang.content.httpclient.actions.URLDecoderAction;
import org.junit.Test;

import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Map;

public class UrlDecoderTest {
    private final URLDecoderAction u = new URLDecoderAction();

    @Test
    public void testWithIncompleteByteEncodingCharacterSet() {
        final Map<String, String> result = u.execute("%x", "");
        assertEquals(FAILURE, result.get (RETURN_CODE));
        assertTrue(result.get(RETURN_RESULT).contains("URLDecoder: Incomplete trailing escape (%) pattern"));

    }

    @Test
    public void testWithEmptyCharacterSet() {
        final Map<String, String> result = u.execute("https%3A%2F%2Fmywebsite%2Fdocs%2Fenglish%2Fsite%2Fmybook.do", "");
        assertEquals("https://mywebsite/docs/english/site/mybook.do", result.get(RETURN_RESULT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));

    }
}
