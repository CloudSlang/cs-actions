/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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



package io.cloudslang.content.json.actions;

import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.*;

/**
 * Created by ioanvranauhp
 * Date 2/5/2015.
 */
public class AddJsonPropertyToObjectTest {

    private static final String RETURN_RESULT = "returnResult";
    public static final String EXCEPTION = "exception";
    public static final String VALIDATE_VALUE_FALSE = "false";
    public static final String VALIDATE_VALUE_TRUE = "true";

    private final AddJsonPropertyToObject addJsonPropertyToObject = new AddJsonPropertyToObject();

    @Test
    public void testExecuteSimpleAll() {
        String jsonObject = "{}";
        String name = "test";
        String value = "1";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_FALSE);
        assertEquals("{\"test\":1}", result.get(RETURN_RESULT));
        assertEquals("0", result.get("returnCode"));
    }

    @Test
    public void testExecuteJsonObjectBad() throws Exception {
        String jsonObject = "{";
        String name = "test";
        String value = "1";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_FALSE);
        assertTrue(result.get(RETURN_RESULT).toLowerCase().startsWith("invalid jsonobject provided!"));
        assertTrue(result.get("exception").toLowerCase().startsWith("unexpected end-of-input: expected close marker for object"));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteJsonObjectBadValidateValue() throws Exception {
        String jsonObject = "{";
        String name = "test";
        String value = "1";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_TRUE);
        assertTrue(result.get(RETURN_RESULT).toLowerCase().startsWith("invalid jsonobject provided!"));
        assertTrue(result.get("exception").toLowerCase().startsWith("unexpected end-of-input: expected close marker for object"));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteNameBad() {
        String jsonObject = "{}";
        String name = "test{\"";
        String value = "1";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_FALSE);
        assertEquals("{\"test{\\\"\":1}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecutePropertyValueBad() {
        String jsonObject = "{}";
        String name = "test";
        String value = "1\"{";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_FALSE);
        assertEquals("{\"test\":\"1\\\"{\"}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecutePropertyValueBadValidateTrue() {
        String jsonObject = "{}";
        String name = "test";
        String value = "1\"{";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_TRUE);
        assertEquals("The value for the property " + name + " it is not a valid JSON object!", result.get(RETURN_RESULT));
        assertTrue(result.get("exception").toLowerCase().startsWith("unexpected character"));
    }

    @Test
    public void testExecutePropertyValueJson() {
        String jsonObject = "{}";
        String name = "test";
        String value = "{\"a\":\"b\"}";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_FALSE);
        assertEquals("{\"test\":{\"a\":\"b\"}}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecutePropertyValueJsonValidateValue() {
        String jsonObject = "{}";
        String name = "test";
        String value = "{\"a\":\"b\"}";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_TRUE);
        assertEquals("{\"test\":{\"a\":\"b\"}}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecutePropertyValueArray() {
        String jsonObject = "{}";
        String name = "test";
        String value = "[1,2,3]";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_FALSE);
        assertEquals("{\"test\":[1,2,3]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecutePropertyValueArrayValidateValue() {
        String jsonObject = "{}";
        String name = "test";
        String value = "[1,2,3]";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_TRUE);
        assertEquals("{\"test\":[1,2,3]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecutePropertyValueBadArrayValidateValue() {
        String jsonObject = "{}";
        String name = "test";
        String value = "[1,2,\"3]";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_TRUE);
        assertEquals("The value for the property " + name + " it is not a valid JSON object!", result.get(RETURN_RESULT));
        assertTrue(result.get("exception").toLowerCase().contains("unexpected end-of-input"));
    }

    @Test
    public void testExecutePropertyValueBadArrayNotValidateValue() {
        String jsonObject = "{}";
        String name = "test";
        String value = "[1,2,\"3]";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_FALSE);
        assertEquals("{\"test\":\"[1,2,\\\"3]\"}", result.get(RETURN_RESULT));
        assertNull(result.get("exception"));
    }

    @Test
    public void testExecuteJsonObjectComplex() {
        String jsonObject = "{\"one\":{\"a\":\"a\",\"B\":\"B\"}, \"two\":\"two\", \"three\":[1,2,3.4]}";
        String name = "test";
        String value = "{\"a\":\"b\"}";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_FALSE);
        assertEquals("{\"one\":{\"a\":\"a\",\"B\":\"B\"},\"two\":\"two\",\"three\":[1,2,3.4],\"test\":{\"a\":\"b\"}}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteJsonObjectComplexValidateValue() {
        String jsonObject = "{\"one\":{\"a\":\"a\",\"B\":\"B\"}, \"two\":\"two\", \"three\":[1,2,3.4]}";
        String name = "test";
        String value = "{\"a\":\"b\"}";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_TRUE);
        assertEquals("{\"one\":{\"a\":\"a\",\"B\":\"B\"},\"two\":\"two\",\"three\":[1,2,3.4],\"test\":{\"a\":\"b\"}}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteJsonObjectComplexPropertyString() {
        String jsonObject = "{\"one\":{\"a\":\"a\",\"B\":\"B\"}, \"two\":\"two\", \"three\":[1,2,3.4]}";
        String name = "test";
        String value = "a";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_FALSE);
        assertEquals("{\"one\":{\"a\":\"a\",\"B\":\"B\"},\"two\":\"two\",\"three\":[1,2,3.4],\"test\":\"a\"}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteJsonObjectComplexPropertyStringValidateValue() {
        String jsonObject = "{\"one\":{\"a\":\"a\",\"B\":\"B\"}, \"two\":\"two\", \"three\":[1,2,3.4]}";
        String name = "test";
        String value = "a";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_TRUE);
        assertEquals("The value for the property " + name + " it is not a valid JSON object!", result.get(RETURN_RESULT));
        assertTrue(result.get("exception").toLowerCase().contains("unrecognized token 'a'"));
    }

    @Test
    public void testExecuteJsonObjectSpecialChars() {
        String jsonObject = "{\"one\":{\"a\":\"a\",\"B\":\"B\"}, \"two\":\"two\", \"three;/?:@&=+,$\":[1,2,3.4]}";
        String name = "tes;/?:@&=+,$t";
        String value = "{\"a\":\"b;/?:@&=+,$\"}";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_FALSE);
        assertEquals("{\"one\":{\"a\":\"a\",\"B\":\"B\"},\"two\":\"two\",\"three;/?:@&=+,$\":[1,2,3.4],\"" +
                "tes;/?:@&=+,$t\":{\"a\":\"b;/?:@&=+,$\"}}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteJsonObjectSpecialCharsValidateValue() {
        String jsonObject = "{\"one\":{\"a\":\"a\",\"B\":\"B\"}, \"two\":\"two\", \"three;/?:@&=+,$\":[1,2,3.4]}";
        String name = "tes;/?:@&=+,$t";
        String value = "{\"a\":\"b;/?:@&=+,$\"}";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_TRUE);
        assertEquals("{\"one\":{\"a\":\"a\",\"B\":\"B\"},\"two\":\"two\",\"three;/?:@&=+,$\":[1,2,3.4],\"" +
                "tes;/?:@&=+,$t\":{\"a\":\"b;/?:@&=+,$\"}}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteEmptyJsonObject() {
        String jsonObject = "";
        String name = "test";
        String value = "1";
        Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_TRUE);
        assertEquals("Empty jsonObject provided!", result.get(RETURN_RESULT));
        assertEquals("Empty jsonObject provided!", result.get(EXCEPTION));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteNullJsonObject() {
        String name = "test";
        String value = "1";
        Map<String, String> result = addJsonPropertyToObject.execute(null, name, value, "");
        assertEquals("Empty jsonObject provided!", result.get(RETURN_RESULT));
        assertEquals("Empty jsonObject provided!", result.get(EXCEPTION));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteEmptyName() {
        String jsonObject = "{}";
        String name = "";
        String value = "1";
        Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_FALSE);
        assertEquals("{\"\":1}", result.get(RETURN_RESULT));
        assertEquals("0", result.get("returnCode"));

    }

    @Test
    public void testExecuteNullName() {
        String jsonObject = "{}";
        String value = "1";

        Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, null, value, VALIDATE_VALUE_FALSE);
        assertEquals("Null newPropertyName provided!", result.get(RETURN_RESULT));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteEmptyValue() {
        String jsonObject = "{}";
        String name = "test";
        String value = "";
        Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_FALSE);
        assertEquals("The value for the property test it is not a valid JSON object!", result.get(RETURN_RESULT));
        assertEquals("The value for the property test it is not a valid JSON object!", result.get(EXCEPTION));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteEmptyValueValidateValue() {
        String jsonObject = "{}";
        String name = "test";
        String value = "";
        Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value, VALIDATE_VALUE_TRUE);
        assertEquals("The value for the property test it is not a valid JSON object!", result.get(RETURN_RESULT));
        assertEquals("The value for the property test it is not a valid JSON object!", result.get(EXCEPTION));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteNullValue() {
        String jsonObject = "{}";
        String name = "test";

        Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, null, VALIDATE_VALUE_FALSE);
        assertEquals("The value for the property test it is not a valid JSON object!", result.get(RETURN_RESULT));
        assertEquals("The value for the property test it is not a valid JSON object!", result.get(EXCEPTION));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteNullValueValidateValue() {
        String jsonObject = "{}";
        String name = "test";

        Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, null, VALIDATE_VALUE_TRUE);
        assertEquals("The value for the property test it is not a valid JSON object!", result.get(RETURN_RESULT));
        assertEquals("The value for the property test it is not a valid JSON object!", result.get(EXCEPTION));
        assertEquals("-1", result.get("returnCode"));
    }
}
