/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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



package io.cloudslang.content.utilities;

import io.cloudslang.content.utilities.actions.DefaultIfEmpty;
import org.junit.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by moldovai on 8/22/2017.
 */
public class DefaultIfEmptyTest {

    private final DefaultIfEmpty d = new DefaultIfEmpty();

    @Test
    public void testWithBlankString() {
        final Map<String, String> result = d.execute("   ", "string", "  ");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testWithEmptyString() {
        final Map<String, String> result = d.execute("", "string", "");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testWithTrimString() {
        final Map<String, String> result = d.execute("", "string", "astring");
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertTrue(result.get(RETURN_RESULT).contains("The provided string cannot be converted to a boolean value"));
    }

    @Test
    public void testWithTrimInitialString() {
        final Map<String, String> result = d.execute("initial", "string", "astring");
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertTrue(result.get(RETURN_RESULT).contains("The provided string cannot be converted to a boolean value"));
    }

    @Test
    public void testHavingInitialString() {
        final Map<String, String> result = d.execute("initialstr", "string", "");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("initialstr", result.get(RETURN_RESULT));
    }

    @Test
    public void testWithTrimTrue() {
        final Map<String, String> result = d.execute("   ", "string", "true");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testWithTrimFalse() {
        final Map<String, String> result = d.execute("", "string", "false");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingInitialStringAndTrimTrue() {
        final Map<String, String> result = d.execute("initialstr", "string", "true");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("initialstr", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingInitialStringAndTrimFalse() {
        final Map<String, String> result = d.execute("initialstr", "string", "false");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("initialstr", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingTrimNull() {
        final Map<String, String> result = d.execute("", "string", null);
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingInitialStringNull() {
        final Map<String, String> result = d.execute(null, "string", "false");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingTrimVariableWrittenInLowerAndUpperCase() {
        final Map<String, String> result = d.execute(null, "string", "    fAlSE  ");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingTrimVariableInUpperCase() {
        final Map<String, String> result = d.execute("initialstr", "string", "  TRUE     ");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("initialstr", result.get(RETURN_RESULT));
    }
}