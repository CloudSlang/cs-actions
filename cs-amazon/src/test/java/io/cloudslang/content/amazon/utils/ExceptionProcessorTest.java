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

package io.cloudslang.content.amazon.utils;

import org.junit.Test;

import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by persdana
 * 7/13/2015.
 */
public class ExceptionProcessorTest {
    private static final String STACK_TRACE = "java.lang.Exception: abc";

    @Test
    public void testGetExceptionResult() {
        Map<String, String> result = ExceptionProcessor.getExceptionResult(new Exception("abc"));
        String exception = result.get("exception");

        assertEquals("abc", result.get("returnResult"));
        assertEquals("-1", result.get("returnCode"));
        assertTrue(exception.contains(STACK_TRACE));
    }
}
