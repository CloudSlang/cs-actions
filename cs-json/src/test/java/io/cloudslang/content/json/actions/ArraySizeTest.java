/*
 * (c) Copyright 2018 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.json.actions;

import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by vranau
 * Date 2/11/2015.
 */
public class ArraySizeTest {
    private static final String RETURN_RESULT = "returnResult";

    private final ArraySize arraySize = new ArraySize();

    @Test
    public void testWithBasicArray() {
        String array = "[ \"apple\", \"banana\" ]";
        final Map<String, String> execute = arraySize.execute(array);
        assertEquals("2", execute.get(RETURN_RESULT));
    }

    @Test
    public void testWithArrayWithArray() {
        String array = "[ \"apple\", \"pencil\", [ 0, 3, 5, -199 ] ]";
        final Map<String, String> execute = arraySize.execute(array);
        assertEquals("3", execute.get(RETURN_RESULT));
    }

    @Test
    public void testWithArrayWithNullElementsInArray() {
        String array = "[ \"apple\", null, \"banana\", null ] ";
        final Map<String, String> execute = arraySize.execute(array);
        assertEquals("4", execute.get(RETURN_RESULT));
    }

    @Test
    public void testWithEmptyArray() {
        String array = "[]";
        final Map<String, String> execute = arraySize.execute(array);
        assertEquals("0", execute.get(RETURN_RESULT));
    }

    @Test
    public void testWithBadFormatArray() {
        String array = "[";
        Map<String, String> execute = arraySize.execute(array);
        assertTrue(execute.get(RETURN_RESULT).toLowerCase().startsWith("invalid jsonobject provided!"));

        array = "[,`~!@#$%^&*()_+}{-=[]:|<>?<>]";
        execute = arraySize.execute(array);
        assertTrue(execute.get(RETURN_RESULT).toLowerCase().startsWith("invalid jsonobject provided!"));

        assertTrue(execute.get("exception").toLowerCase().startsWith("unexpected character (',' "));
        assertEquals("-1", execute.get("returnCode"));
    }

    @Test
    public void testWithEmptyInput() {
        String array = "";
        final Map<String, String> execute = arraySize.execute(array);
        assertEquals("The input value is not a valid JavaScript array!", execute.get(RETURN_RESULT));
        assertEquals("The input value is not a valid JavaScript array!", execute.get("exception"));
        assertEquals("-1", execute.get("returnCode"));
    }

    @Test
    public void testWithNullInput() {
        final Map<String, String> execute = arraySize.execute(null);
        assertEquals("The input value is not a valid JavaScript array!", execute.get(RETURN_RESULT));
        assertEquals("The input value is not a valid JavaScript array!", execute.get("exception"));
        assertEquals("-1", execute.get("returnCode"));
    }

    @Test
    public void testWithJsonObject() {
        String array = " { \"city\" : \"Palo Alto\", \n" +
                "     \"state\" : \"CA\" }";
        final Map<String, String> execute = arraySize.execute(array);
        assertEquals("The input value is not a valid JavaScript array!", execute.get(RETURN_RESULT));
    }

    @Test
    public void testWithJsonObjectWithArrayInside() {
        String array = " { \"city\" : \"[Palo Alto, CA]\", \n" +
                "     \"state\" : \"CA\" }";
        final Map<String, String> execute = arraySize.execute(array);
        assertEquals("The input value is not a valid JavaScript array!", execute.get(RETURN_RESULT));
    }
}
