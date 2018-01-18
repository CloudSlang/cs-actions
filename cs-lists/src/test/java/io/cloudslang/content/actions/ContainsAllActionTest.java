
package io.cloudslang.content.actions;

import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.assertEquals;

/**
 * Created by moldovas on 7/12/2016.
 */
public class ContainsAllActionTest {
    private ListContainsAction listContains = new ListContainsAction();
    private static final String RETURN_RESULT = "returnResult";
    private static final String RETURN_CODE = "returnCode";
    private static final String RETURN_CODE_SUCCESS = "0";
    private static final String RETURN_CODE_FAILURE = "-1";
    private static final String RESPONSE = "response";
    private static final String RESPONSE_SUCCESS = "true";
    private static final String RESPONSE_FAILURE = "false";
    private static final String RETURN_RESULT_FAILURE = "CaptainAmerica,BlackWidow";
    private static final String CONTAINER = "SpiderMan,IronMan,Hulk,Storm,Deadpool";

    @Test
    public void testContainsElement() {
        Map<String, String> result = listContains.containsElement("SpiderMan,Hulk,IronMan,IronMan", CONTAINER, ",", "false");
        assertEquals(RESPONSE_SUCCESS, result.get(RESPONSE));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
    }
    @Test
    public void testNotContainingElement() {
        Map<String, String> result = listContains.containsElement("CaptainAmerica,BlackWidow", CONTAINER, ",", "false");
        assertEquals(RESPONSE_FAILURE, result.get(RESPONSE));
        assertEquals(RETURN_RESULT_FAILURE, result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_FAILURE, result.get(RETURN_CODE));
    }
}