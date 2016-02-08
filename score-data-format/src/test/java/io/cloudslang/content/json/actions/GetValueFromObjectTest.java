package io.cloudslang.content.json.actions;

import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by ioanvranauhp
 * Date 2/9/2015.
 */
public class GetValueFromObjectTest {

    public static final String EXCEPTION = "exception";
    private static final String RETURN_RESULT = "returnResult";

    private static GetValueFromObject getValueFromObject = new GetValueFromObject();

    @Test
    public void testExecuteSimpleCase() {
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
    public void testExecuteSimpleCaseEmptyValues() {
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
    public void testExecuteSimpleCaseEmptyKeys() {
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
    public void testExecuteJsonArrayIndexExisting() {
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
    public void testExecuteJsonArrayIndexExistingSubKey() {
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
    public void testExecuteJsonArrayIndexExistingSubKey2Levels() {
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
    public void testExecuteJsonArrayNoIndex() {
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
        assertEquals("The location[] key does not exist in JavaScript object!", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteJsonArrayBadIndex() {
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
        assertEquals("The location[a] key does not exist in JavaScript object!", result.get(RETURN_RESULT));

        key = "location[0].a";
        result = getValueFromObject.execute(jsonObject, key);
        assertEquals("The a key does not exist in JavaScript object!", result.get(RETURN_RESULT));

        key = "location[a].b";
        result = getValueFromObject.execute(jsonObject, key);
        assertEquals("The location[a] key does not exist in JavaScript object!", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteSimpleNotExist() {
        String jsonObject = "{}";
        String key = "test";
        final Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("The test key does not exist in JavaScript object!", result.get(RETURN_RESULT));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteJsonObjectBad() {
        String jsonObject = "{";
        String key = "test";
        final Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertTrue(result.get(RETURN_RESULT).toLowerCase().contains("invalid object provided"));
        assertTrue(result.get(EXCEPTION).toLowerCase().contains("unexpected end-of-input"));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteNameBad() {
        String jsonObject = "{}";
        String key = "test{\"";
        final Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("The test{\" key does not exist in JavaScript object!", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteJsonObjectSpecialCharsInKey() {
        String jsonObject = "{\"one\":{\"a\":\"a\",\"B\":\"B\"}, \"two\":\"two\", \"three;/?:@&=+,$\":[1,2,3.4]}";
        String key = "tes;/?:@&=+,$t";
        final Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("The tes;/?:@&=+,$t key does not exist in JavaScript object!", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteEmptyJsonObject() {
        String jsonObject = "";
        String key = "city";
        Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("Empty object provided!", result.get(RETURN_RESULT));
        assertEquals("Empty object provided!", result.get(EXCEPTION));
        assertEquals("-1", result.get("returnCode"));

        key = "city";
        result = getValueFromObject.execute(null, key);
        assertEquals("Empty object provided!", result.get(RETURN_RESULT));
        assertEquals("Empty object provided!", result.get(EXCEPTION));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteEmptyKey() {
        String jsonObject = "{}";
        String key = "";
        Map<String, String> result = getValueFromObject.execute(jsonObject, key);
        assertEquals("The  key does not exist in JavaScript object!", result.get(RETURN_RESULT));
        assertEquals("The  key does not exist in JavaScript object!", result.get(EXCEPTION));
        assertEquals("-1", result.get("returnCode"));

        jsonObject = "{}";
        result = getValueFromObject.execute(jsonObject, null);
        assertEquals("Null key provided!", result.get(RETURN_RESULT));
        assertEquals("Null key provided!", result.get(EXCEPTION));
        assertEquals("-1", result.get("returnCode"));
    }
}
