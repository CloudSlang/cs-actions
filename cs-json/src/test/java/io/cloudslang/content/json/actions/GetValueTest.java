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
public class GetValueTest {

    private static final String RETURN_RESULT = "returnResult";
    private static final String RETURN_CODE = "returnCode";
    private static GetValue getValue = new GetValue();
    private static String jsonInput;
    private static String jsonPath;

    @Test
    public void testGetValueSimpleCase() {
        jsonInput = " { \"city\" : \"Paris\", \n" +
                "     \"state\" : \"CA\" }";
        jsonPath = "city";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("Paris", result.get(RETURN_RESULT));

        jsonPath = "state";
        result = getValue.execute(jsonInput, jsonPath);
        assertEquals("CA", result.get(RETURN_RESULT));
    }


    @Test
    public void testGetValueSimpleCaseEmptyValues() {
        jsonInput = " { \"city\" : \"\", \n" +
                "     \"country\" : \"\" }";
        jsonPath = "city";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("", result.get(RETURN_RESULT));

        jsonPath = "state";
        result = getValue.execute(jsonInput, jsonPath);
        assertEquals("No results for path: $['state']", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetValueSimpleCaseEmptyKeys() {
        jsonInput = " { \"abc\" : \"test1\", \n" +
                "     \"\" : \"test2\", " +
                "     \"value\" : \"test3\" " +
                "}";
        jsonPath = "$.['']";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("test2", result.get(RETURN_RESULT));

        jsonPath = "value";
        result = getValue.execute(jsonInput, jsonPath);
        assertEquals("test3", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetValueJsonArrayIndexExisting() {
        jsonInput = "{ \"location\": [\n" +
                "      {\"city\": \"Roseville\", \"country\": \"United States\"},\n" +
                "      {\"city\": \"Cluj\", \"country\": \"Romania\"},\n" +
                "      {\"city\": \"Yehud\", \"country\": \"Israel\"}]    \n" +
                "}";
        jsonPath = "location[0]";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("{\"city\":\"Roseville\",\"country\":\"United States\"}", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetValueJsonArrayIndexExistingSubKey() {
        jsonInput = "{ \"location\": [\n" +
                "      {\"city\": \"Roseville\", \"country\": \"United States\"},\n" +
                "      {\"city\": \"Husi\", \"country\": \"Romania\"},\n" +
                "      {\"city\": \"Paris\", \"country\": \"France\"}]    \n" +
                "}";
        jsonPath = "location[0].city";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("Roseville", result.get(RETURN_RESULT));

        jsonPath = "location[0].country";
        result = getValue.execute(jsonInput, jsonPath);
        assertEquals("United States", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetValueJsonArrayIndexExistingSubKey2Levels() {
        jsonInput = "{ \"location\": [\n" +
                "      {\"city\": [\n" +
                "      {\"city\": \"Roseville\", \"country\": \"United States\"},\n" +
                "      {\"city\": \"Cluj\", \"country\": \"Romania\"},\n" +
                "      {\"city\": \"Paris\", \"country\": \"France\"}]    \n" +
                "},\n" +
                "      {\"city\": \"Cluj\", \"country\": \"Romania\"},\n" +
                "      {\"city\": \"Yehud\", \"country\": \"Israel\"}]    \n" +
                "}";
        jsonPath = "location[0].city[0].city";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("Roseville", result.get(RETURN_RESULT));

        jsonPath = "location[0].city[0].country";
        result = getValue.execute(jsonInput, jsonPath);
        assertEquals("United States", result.get(RETURN_RESULT));

        jsonPath = "location[0].city[0]";
        result = getValue.execute(jsonInput, jsonPath);
        assertEquals("{\"city\":\"Roseville\",\"country\":\"United States\"}", result.get(RETURN_RESULT));

        jsonPath = "location[0].city";
        result = getValue.execute(jsonInput, jsonPath);
        assertEquals("[{\"city\":\"Roseville\",\"country\":\"United States\"}," +
                "{\"city\":\"Cluj\",\"country\":\"Romania\"}," +
                "{\"city\":\"Paris\",\"country\":\"France\"}]", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetValueJsonArrayNoIndex() {
        jsonInput = "{ \"location\": [\n" +
                "      {\"city\": \"Roseville\", \"country\": \"United States\"},\n" +
                "      {\"city\": \"Cluj\", \"country\": \"Romania\"},\n" +
                "      {\"city\": \"Yehud\", \"country\": \"Israel\"}]    \n" +
                "}";
        jsonPath = "location";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("[{\"city\":\"Roseville\",\"country\":\"United States\"},{\"city\":\"Cluj\",\"country\":\"Romania\"}," +
                "{\"city\":\"Yehud\",\"country\":\"Israel\"}]", result.get(RETURN_RESULT));

        jsonPath = "location[]";
        result = getValue.execute(jsonInput, jsonPath);
        assertEquals("Could not parse token starting at position 10. Expected ?, ', 0-9, * ", result.get(RETURN_RESULT));
        assertEquals("-1", result.get(RETURN_CODE));
    }

    @Test
    public void testGetValueJsonArrayBadIndex() {
        jsonInput = "{ \"location\": [\n" +
                "      {\"city\": \"Roseville\", \"country\": \"United States\"},\n" +
                "      {\"city\": \"Cluj\", \"country\": \"Romania\"},\n" +
                "      {\"city\": \"Yehud\", \"country\": \"Israel\"}]    \n" +
                "}";
        jsonPath = "location[987].city";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("Expected to find an object with property ['city'] in path $['location'][987] but found 'null'. This is not a json object according to the JsonProvider: 'com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider'.", result.get(RETURN_RESULT));

        jsonPath = "location[a]";
        result = getValue.execute(jsonInput, jsonPath);
        assertEquals("Could not parse token starting at position 10. Expected ?, ', 0-9, * ", result.get(RETURN_RESULT));
        assertEquals("-1", result.get(RETURN_CODE));

        jsonPath = "location[0].a";
        result = getValue.execute(jsonInput, jsonPath);
        assertEquals("No results for path: $['location'][0]['a']", result.get(RETURN_RESULT));
        assertEquals("-1", result.get(RETURN_CODE));

        jsonPath = "location[a].b";
        result = getValue.execute(jsonInput, jsonPath);
        assertEquals("Could not parse token starting at position 10. Expected ?, ', 0-9, * ", result.get(RETURN_RESULT));
        assertEquals("-1", result.get(RETURN_CODE));
    }

    @Test
    public void testGetValueSimpleNotExist() {
        jsonInput = "{}";
        jsonPath = "test";
        final Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("No results for path: $['test']", result.get(RETURN_RESULT));
        assertEquals("-1", result.get(RETURN_CODE));
    }

    @Test
    public void testGetValueJsonObjectBad() {
        jsonInput = "{";
        jsonPath = "test";
        final Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("-1", result.get(RETURN_CODE));
    }

    @Test
    public void testGetValueNameBad() {
        jsonInput = "{}";
        jsonPath = "test{\"";
        final Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("No results for path: $['test{\"']", result.get(RETURN_RESULT));
        assertEquals("-1", result.get(RETURN_CODE));
    }

    @Test
    public void testGetValueJsonObjectSpecialCharsInKey() {
        jsonInput = "{\"one\":{\"a\":\"a\",\"B\":\"B\"}, \"two\":\"two\", \"three;/?:@&=+,$\":[1,2,3.4]}";
        jsonPath = "tes;/?:@&=+,$t";
        final Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("No results for path: $['tes;/?:@&=+,$t']", result.get(RETURN_RESULT));
        assertEquals("-1", result.get(RETURN_CODE));
    }

    @Test
    public void tesGEtValueEmptyInput() {
        jsonInput = "";
        jsonPath = "city";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("Invalid jsonObject provided!", result.get(RETURN_RESULT));
        assertEquals("-1", result.get(RETURN_CODE));

        jsonPath = "city";
        result = getValue.execute(null, jsonPath);
        assertEquals("Invalid jsonObject provided!", result.get(RETURN_RESULT));
        assertEquals("-1", result.get(RETURN_CODE));
    }

    @Test
    public void testGetValueEmptyKey() {
        jsonInput = "{}";
        jsonPath = "";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("Invalid jsonPath provided!", result.get(RETURN_RESULT));
        assertEquals("-1", result.get(RETURN_CODE));

        jsonInput = "{}";
        result = getValue.execute(jsonInput, null);
        assertEquals("Invalid jsonPath provided!", result.get(RETURN_RESULT));
        assertEquals("-1", result.get(RETURN_CODE));
    }

    @Test
    public void testGetValueDupKeys() {
        jsonInput = "{ \"location\": [\n" +
                "      {\"city\": \"Roseville\", \"country\": \"United States\"},\n" +
                "      {\"city  \": \"Cluj\", \"country\": \"Romania\"},\n" +
                "      {\"city\": \"Yehud\", \"country\": \"Israel\"}]    \n" +
                "}";

        jsonPath = "$.location[0].city";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("0", result.get(RETURN_CODE));
        jsonInput = " { \"abc\" : \"test1\", \n" +
                "     \"abc\" : \"test2\", " +
                "     \"value\" : \"test3\" " +
                "}";
        jsonPath = "city";
        result = getValue.execute(jsonInput, jsonPath);
        assertEquals("-1", result.get(RETURN_CODE));

    }

    @Test
    public void testGetValueSingleQuotes() {
        jsonInput = "{ \'city\' : \'Palo Alto\', \n" +
                "     \'state\' : \'CA\' }";
        jsonPath = "city";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("0", result.get(RETURN_CODE));
    }

    @Test
    public void testGetValueEverything() {
        jsonInput = "{ \'city\' : \'Palo Alto\', \n" +
                "     \'state\' : \'CA\' }";
        jsonPath = "$";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("{\"city\":\"Palo Alto\",\"state\":\"CA\"}", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetValuePathIndexExceeded() {
        jsonInput = "{ \"location\": [\n" +
                "      {\"city\": \"Roseville\", \"country\": \"United States\"},\n" +
                "      {\"city  \": \"Cluj\", \"country\": \"Romania\"},\n" +
                "      {\"city\": \"Yehud\", \"country\": \"Israel\"}]    \n" +
                "}";
        jsonPath = "$.location[20]";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("null", result.get(RETURN_RESULT));
        assertEquals("0", result.get(RETURN_CODE));
    }

    @Test
    public void testGetValueArray() {
        jsonInput = "{\"outer_key1\": \"outer_value1\", \"outer_key2\": {\"inner_key1\": \"inner_value1\", \"inner_key2\": [\"list_item1\", \"list_item2\"]}}";
        jsonPath = "$.outer_key2.inner_key2";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("[\"list_item1\",\"list_item2\"]", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetValueScientificFloat() {
        jsonInput = "{\"number1\": 1.9e1, \"outer_key2\": {\"number2\": 2.9e+1, \"inner_key2\": [\"list_item1\", \"list_item2\"]}}";
        jsonPath = "$.outer_key2.number2";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("29.0", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetValueSpecialCharacter() {
        jsonInput = "{\"k1\": \"v1\",\"k1!#%@&*`|>-\": \"v2!#%@&*`?|>{[-\"}";
        jsonPath = "k1!#%@&*`|>-";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("v2!#%@&*`?|>{[-", result.get(RETURN_RESULT));


    }

    @Test
    public void testGetValueEmptyInput2() {
        jsonInput = "{}";
        jsonPath = "$";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("{}", result.get(RETURN_RESULT));

    }

    @Test
    public void testGetValueNull() {
        jsonInput = "{\"v\": null, \"v2\":\"hello\"}";
        jsonPath = "$.v";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("null", result.get(RETURN_RESULT));

    }

    @Test
    public void testGetValueBoolean() {
        jsonInput = "{\"v\":true,  \"v2\":\"hello\"}";
        jsonPath = "$.v";
        Map<String, String> result = getValue.execute(jsonInput, jsonPath);
        assertEquals("true", result.get(RETURN_RESULT));

    }

}
