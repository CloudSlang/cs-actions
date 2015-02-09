package org.openscore.content.json.actions;

import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ioanvranauhp
 * Date 2/9/2015.
 */
public class GetValueFromObjectTest {

    private static GetValueFromObject getValueFromObject = new GetValueFromObject();
    private static final String RETURN_RESULT = "returnResult";

    @Test
    public void testExecuteSimpleCase() throws Exception {
        String jsonObject = " { \"city\" : \"Palo Alto\", \n" +
                "     \"state\" : \"CA\" }";
        String key = "city";
        Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("Palo Alto", result.get(RETURN_RESULT));

        key = "state";
        result = getValueFromObject.execute(jsonObject, key);
        assertEquals("CA", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteSimpleCaseEmptyValues() throws Exception {
        String jsonObject = " { \"city\" : \"\", \n" +
                "     \"state\" : \"\" }";
        String key = "city";
        Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("", result.get(RETURN_RESULT));

        key = "state";
        result = getValueFromObject.execute(jsonObject, key);
        assertEquals("", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteSimpleCaseEmptyKeys() throws Exception {
        String jsonObject = " { \"\" : \"test1\", \n" +
                "     \"\" : \"test2\", " +
                "     \"value\" : \"test3\" " +
            "}";
        String key = "";
        Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("test2", result.get(RETURN_RESULT));

        key = "value";
        result = getValueFromObject.execute(jsonObject, key);
        assertEquals("test3", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteJsonArrayIndexExisting() throws Exception {
        String jsonObject = "{ \"location\": [\n" +
                "      {\"city\": \"Roseville\", \"country\": \"United States\"},\n" +
                "      {\"city\": \"Cluj\", \"country\": \"Romania\"},\n" +
                "      {\"city\": \"Yehud\", \"country\": \"Israel\"}]    \n" +
                "}";
        String key = "location[0]";
        Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("{\"city\":\"Roseville\",\"country\":\"United States\"}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteJsonArrayIndexExistingSubKey() throws Exception {
        String jsonObject = "{ \"location\": [\n" +
                "      {\"city\": \"Roseville\", \"country\": \"United States\"},\n" +
                "      {\"city\": \"Cluj\", \"country\": \"Romania\"},\n" +
                "      {\"city\": \"Yehud\", \"country\": \"Israel\"}]    \n" +
                "}";
        String key = "location[0].city";
        Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("Roseville", result.get(RETURN_RESULT));

        key = "location[0].country";
        result = getValueFromObject.execute(jsonObject, key);
        assertEquals("United States", result.get(RETURN_RESULT));
    }
    @Test
    public void testExecuteJsonArrayIndexExistingSubKey2Levels() throws Exception {
        String jsonObject = "{ \"location\": [\n" +
                "      {\"city\": [\n" +
                    "      {\"city\": \"Roseville\", \"country\": \"United States\"},\n" +
                    "      {\"city\": \"Cluj\", \"country\": \"Romania\"},\n" +
                    "      {\"city\": \"Yehud\", \"country\": \"Israel\"}]    \n" +
                "},\n" +
                "      {\"city\": \"Cluj\", \"country\": \"Romania\"},\n" +
                "      {\"city\": \"Yehud\", \"country\": \"Israel\"}]    \n" +
                "}";
        String key = "location[0].city[0].city";
        Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("Roseville", result.get(RETURN_RESULT));

        key = "location[0].city[0].country";
        result = getValueFromObject.execute(jsonObject, key);
        assertEquals("United States", result.get(RETURN_RESULT));

        key = "location[0].city[0]";
        result = getValueFromObject.execute(jsonObject, key);
        assertEquals("{\"city\":\"Roseville\",\"country\":\"United States\"}", result.get(RETURN_RESULT));

        key = "location[0].city";
        result = getValueFromObject.execute(jsonObject, key);
        assertEquals("[{\"city\":\"Roseville\",\"country\":\"United States\"}," +
                "{\"city\":\"Cluj\",\"country\":\"Romania\"}," +
                "{\"city\":\"Yehud\",\"country\":\"Israel\"}]", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteJsonArrayNoIndex() throws Exception {
        String jsonObject = "{ \"location\": [\n" +
                "      {\"city\": \"Roseville\", \"country\": \"United States\"},\n" +
                "      {\"city\": \"Cluj\", \"country\": \"Romania\"},\n" +
                "      {\"city\": \"Yehud\", \"country\": \"Israel\"}]    \n" +
                "}";
        String key = "location";
        Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("[{\"city\":\"Roseville\",\"country\":\"United States\"},{\"city\":\"Cluj\",\"country\":\"Romania\"}," +
                "{\"city\":\"Yehud\",\"country\":\"Israel\"}]", result.get(RETURN_RESULT));

        key = "location[]";
        result = getValueFromObject.execute(jsonObject, key);
        assertEquals("The location[] key does not exist in JavaScript object.", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteJsonArrayBadIndex() throws Exception {
        String jsonObject = "{ \"location\": [\n" +
                "      {\"city\": \"Roseville\", \"country\": \"United States\"},\n" +
                "      {\"city\": \"Cluj\", \"country\": \"Romania\"},\n" +
                "      {\"city\": \"Yehud\", \"country\": \"Israel\"}]    \n" +
                "}";
        String key = "location[987].city";
        Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("The provided 987 index is out of range! Provide a valid index value in the provided JSON!", result.get(RETURN_RESULT));

        key = "location[a]";
        result = getValueFromObject.execute(jsonObject, key);
        assertEquals("The location[a] key does not exist in JavaScript object.", result.get(RETURN_RESULT));

        key = "location[0].a";
        result = getValueFromObject.execute(jsonObject, key);
        assertEquals("The a key does not exist in JavaScript object.", result.get(RETURN_RESULT));

        key = "location[a].b";
        result = getValueFromObject.execute(jsonObject, key);
        assertEquals("The location[a] key does not exist in JavaScript object.", result.get(RETURN_RESULT));
    }
    @Test
    public void testExecuteSimpleNotExist() throws Exception {
        String jsonObject = "{}";
        String key = "test";
        final Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("The test key does not exist in JavaScript object.", result.get(RETURN_RESULT));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteJsonObjectBad() throws Exception {
        String jsonObject = "{";
        String key = "test";
        final Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("Invalid jsonObject provided! java.io.EOFException: End of input at line 1 column 2", result.get(RETURN_RESULT));
        assertEquals("java.io.EOFException: End of input at line 1 column 2", result.get("exception"));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteNameBad() throws Exception {
        String jsonObject = "{}";
        String key = "test{\"";
        final Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("The test{\" key does not exist in JavaScript object.", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteJsonObjectSpecialCharsInKey() throws Exception {
        String jsonObject = "{\"one\":{\"a\":\"a\",\"B\":\"B\"}, \"two\":\"two\", \"three;/?:@&=+,$\":[1,2,3.4]}";
        String key = "tes;/?:@&=+,$t";
        final Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("The tes;/?:@&=+,$t key does not exist in JavaScript object.", result.get(RETURN_RESULT));
    }
}
