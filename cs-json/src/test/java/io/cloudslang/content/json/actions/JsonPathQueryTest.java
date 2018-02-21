
package io.cloudslang.content.json.actions;

import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ReturnCodes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static io.cloudslang.content.json.utils.JsonExceptionValues.INVALID_JSONOBJECT;
import static io.cloudslang.content.json.utils.JsonExceptionValues.INVALID_JSONPATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by victor on 9/12/16.
 */
public class JsonPathQueryTest {
    private JsonPathQuery jsonPathQuery;
    private static final String BOOKSTORE_JSON = "{\"store\":{\"book\":[{\"title\":\"Sayings of the Century\",\"price\":8.95}," +
            "{\"title\":\"Sword of Honour\",\"price\":12.99},{\"title\":\"Moby Dick\",\"price\":8.99}," +
            "{\"title\":\"The Lord of the Rings\",\"price\":22.99}]}," +
            "\"expensive\":10}";

    @Before
    public void setUp() {
        jsonPathQuery = new JsonPathQuery();
    }

    @After
    public void tearDown() {
        jsonPathQuery = null;
    }

    @Test
    public void executeValid() {
        final Map<String, String> resultMap = jsonPathQuery.execute("{'a':'b', 'a1':'b1', 'a1':'b1'}", "$.a");
        assertEquals(resultMap.get(OutputNames.RETURN_CODE), ReturnCodes.SUCCESS);
        assertEquals(resultMap.get(OutputNames.RETURN_RESULT), "\"b\"");
    }

    @Test
    public void executeInvalidJsonObject() {
        final Map<String, String> resultMap = jsonPathQuery.execute(null, "$.a");
        assertEquals(resultMap.get(OutputNames.RETURN_CODE), ReturnCodes.FAILURE);
        assertEquals(resultMap.get(OutputNames.RETURN_RESULT), INVALID_JSONOBJECT);
        assertNotNull(resultMap.get(OutputNames.EXCEPTION));
    }

    @Test
    public void executeInvalidJsonPath() {
        final Map<String, String> resultMap = jsonPathQuery.execute("{'a':'b', 'a1':'b1', 'a1':'b1'}", null);
        assertEquals(resultMap.get(OutputNames.RETURN_CODE), ReturnCodes.FAILURE);
        assertEquals(resultMap.get(OutputNames.RETURN_RESULT), INVALID_JSONPATH);
        assertNotNull(resultMap.get(OutputNames.EXCEPTION));
    }

    @Test
    public void executeComplexJsonPath() {
        final Map<String, String> resultMap = jsonPathQuery.execute(BOOKSTORE_JSON, "$..book[?(@.price <= $['expensive'])]");
        assertEquals(resultMap.get(OutputNames.RETURN_CODE), ReturnCodes.SUCCESS);
        assertEquals(resultMap.get(OutputNames.RETURN_RESULT), "[{\"title\":\"Sayings of the Century\",\"price\":8.95},{\"title\":\"Moby Dick\",\"price\":8.99}]");
    }
}