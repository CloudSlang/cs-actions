/*
 * (c) Copyright 2018 Micro Focus, L.P.
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

import static org.junit.Assert.assertEquals;

/**
 * created by Alexandra Boicu
 * 30/07/2018
 */
public class GetKeysTest {
    private static final String RETURN_RESULT = "returnResult";
    private static final String RETURN_CODE = "returnCode";
    private static final GetKeys getKeys = new GetKeys();
    private static String jsonInput;
    private static String jsonPath;

    @Test
    public void testGetKeysSingleQuotes() {
        jsonInput = "{ \'city\' : \'Palo Alto\', \n" +
                "     \'state\' : \'CA\' }";
        jsonPath = "$";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("[city, state]", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetKeysSingle() {
        jsonInput = "{\"outer_key1\": \"outer_value1\", \"outer_key2\": {\"inner_key1\": \"inner_value1\"}}";
        jsonPath = "outer_key2";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("[inner_key1]", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetKeysDup() {

        jsonInput = " { \"abc\" : \"test1\", \n" +
                "     \"abc\" : \"test2\", " +
                "     \"value\" : \"test3\" " +
                "}";
        jsonPath = "$";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("-1", result.get(RETURN_CODE));
    }

    @Test
    public void testGetKeysInner() {
        jsonInput = "{ \"location\": [\n" +
                "      {\"city1\": \"Roseville\",\"country1\": \"United States\"},\n" +
                "      {\"city2\": \"Cluj\",\"country2\": \"Romania\"},\n" +
                "      {\"city3\": \"Yehud\",\"country3\": \"Israel\"}]    \n" +
                "}";

        jsonPath = "$.location[1]";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("[city2, country2]", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetKeysEmptyPath() {
        jsonInput = "{ \"city\" : \"Palo Alto\", \n" +
                "     \"state\" : \"CA\" }";
        jsonPath = "";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("Invalid jsonPath provided!", result.get(RETURN_RESULT));

    }

    @Test
    public void testGetKeysEmptyInput() {
        jsonInput = "";
        jsonPath = "$";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("Invalid jsonObject provided!", result.get(RETURN_RESULT));
        assertEquals("-1", result.get(RETURN_CODE));
    }

    @Test
    public void testGetKeysInvalidInput() {
        jsonInput = "{asdkjgafkga/sdgasf[opajfs";
        jsonPath = "$";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("-1", result.get(RETURN_CODE));

    }

    @Test
    public void testGetKeysInvalidPath() {
        jsonInput = "{ \"city\" : \"Palo Alto\", \n" +
                "     \"state\" : \"CA\" }";
        jsonPath = "$.city[0].city";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("Filter: [0]['city'] can only be applied to arrays. Current context is: Palo Alto", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetKeysEmptyName() {
        jsonInput = "{ \"\" : \"Palo Alto\", \n" +
                "     \"state\" : \"CA\" }";
        jsonPath = "$";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("[, state]", result.get(RETURN_RESULT));

    }

    @Test
    public void testGetKeysSingleEmptyName() {
        jsonInput = "{ \"\" : \"Palo Alto\"}";
        jsonPath = "$";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("[]", result.get(RETURN_RESULT));

    }

    @Test
    public void testGetKeysNone() {
        jsonInput = "{ \"\" : \"Palo Alto\", \n" +
                "     \"state\" : \"CA\" }";
        jsonPath = "$.state";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("There are no keys", result.get(RETURN_RESULT));

    }

    @Test
    public void testGetKeysJSONArray() {
        jsonInput = "{\"[arraykey1, arraykey2]\":\"arrayvalue\"}";
        jsonPath = "$";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("[[arraykey1, arraykey2]]", result.get(RETURN_RESULT));

    }

    @Test
    public void testGetKeysNone2() {
        jsonInput = "{}";
        jsonPath = "";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("Invalid jsonPath provided!", result.get(RETURN_RESULT));

    }

    @Test
    public void testGetKeysNullArray() {
        jsonInput = "{\"[]\": \"value\"}";
        jsonPath = "$";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("[[]]", result.get(RETURN_RESULT));

    }

    @Test
    public void testGetKeysSpecialCharacter() {
        jsonInput = "{\"k1\": \"v1\",\"k1!#%@&*`|>-\": \"v2!#%@&*`?|>{[-\"}";
        jsonPath = "$";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("[k1, k1!#%@&*`|>-]", result.get(RETURN_RESULT));


    }

    @Test
    public void testGetKeysEmptyJson() {
        jsonInput = "{}";
        jsonPath = "$";
        Map<String, String> result = getKeys.execute(jsonInput, jsonPath);
        assertEquals("There are no keys", result.get(RETURN_RESULT));
    }
}
