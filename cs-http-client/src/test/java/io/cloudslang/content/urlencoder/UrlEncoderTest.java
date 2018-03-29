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
package io.cloudslang.content.urlencoder;

import io.cloudslang.content.httpclient.actions.URLEncoderAction;
import org.junit.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static junit.framework.Assert.assertEquals;

public class UrlEncoderTest {
    private final URLEncoderAction u = new URLEncoderAction();

    @Test
    public void testWithUnknownEncodingCharacterSet() {
        final Map<String, String> result = u.execute("https://mywebsite/docs/english/site/mybook.do", "testCharSet");
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals("testCharSet", result.get(RETURN_RESULT));

    }

    @Test
    public void testWithEmptyCharacterSet() {
        final Map<String, String> result = u.execute("https://mywebsite/docs/english/site/mybook.do", "");
        assertEquals("https%3A%2F%2Fmywebsite%2Fdocs%2Fenglish%2Fsite%2Fmybook.do", result.get(RETURN_RESULT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));

    }

}
