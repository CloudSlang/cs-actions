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
package io.cloudslang.content.actions;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import org.junit.Test;

import java.util.Map;

import static io.cloudslang.content.utils.Constants.Descriptions.HAS_MORE;
import static io.cloudslang.content.utils.Constants.Descriptions.NO_MORE;
import static io.cloudslang.content.utils.Constants.OutputNames.RESULT_STRING;
import static io.cloudslang.content.utils.Constants.OutputNames.RESULT_TEXT;
import static org.junit.Assert.assertEquals;

public class ListIteratorActionTest {

    private static final String LIST_STRING = "Maria, Ioana, Matei, Gicu, George";
    private static final String LIST_INTEGER = "12, 11, 10, 154, 22, 1";
    private static final String EMPTY_LIST = ",,,";

    @Test
    public void testListIteratorSuccess() {
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Map<String, String> result = new ListIteratorAction().execute(LIST_STRING, ",", globalSessionObject);
        assertEquals(HAS_MORE, result.get(RESULT_TEXT));
        assertEquals("Maria", result.get(RESULT_STRING));
        assertEquals("0", result.get("index"));
    }

    @Test
    public void testListIteratorSuccessFullList() {
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Map<String, String> result = new ListIteratorAction().execute(LIST_STRING, "ceva", globalSessionObject);
        assertEquals(HAS_MORE, result.get(RESULT_TEXT));
        assertEquals(LIST_STRING, result.get(RESULT_STRING));
        assertEquals("0", result.get("index"));
    }

    @Test
    public void testListIteratorFails() {
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Map<String, String> result = new ListIteratorAction().execute(LIST_STRING, "", globalSessionObject);
        assertEquals(HAS_MORE, result.get(RESULT_TEXT));
        assertEquals("Maria", result.get(RESULT_STRING));
    }

    @Test
    public void testListIteratorEmptyList() {
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Map<String, String> result = new ListIteratorAction().execute("", ";", globalSessionObject);
        assertEquals("failed", result.get(RESULT_TEXT));
        assertEquals("list has null or 0 length", result.get(RESULT_STRING));
    }

    @Test
    public void testListIteratorEmptyListAndDefaultSeparator() {
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Map<String, String> result = new ListIteratorAction().execute("", "", globalSessionObject);
        assertEquals("failed", result.get(RESULT_TEXT));
        assertEquals("list has null or 0 length", result.get(RESULT_STRING));
    }

    @Test
    public void testListIteratorEmptyListAndSeparator() {
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Map<String, String> result = new ListIteratorAction().execute(EMPTY_LIST, ",", globalSessionObject);
        assertEquals(NO_MORE, result.get(RESULT_TEXT));
    }

    @Test
    public void testListIteratorEmptyListWithDifferentSeparator() {
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Map<String, String> result = new ListIteratorAction().execute("", ";", globalSessionObject);
        assertEquals("failed", result.get(RESULT_TEXT));
        assertEquals("list has null or 0 length", result.get(RESULT_STRING));
    }

    @Test
    public void testListIteratorIntegerListAndStringSeparator() {
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Map<String, String> result = new ListIteratorAction().execute(LIST_INTEGER, "alabala", globalSessionObject);
        assertEquals(HAS_MORE, result.get(RESULT_TEXT));
        assertEquals(LIST_INTEGER, result.get(RESULT_STRING));
    }

    @Test
    public void testListIteratorRandomListWordAndSeparator() {
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Map<String, String> result = new ListIteratorAction().execute("ThisIsOnlyAWord, , , , ,,,", ",", globalSessionObject);
        assertEquals(HAS_MORE, result.get(RESULT_TEXT));
        assertEquals("ThisIsOnlyAWord", result.get(RESULT_STRING));
    }
}
