
package io.cloudslang.content.actions;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by giloan on 7/8/2016.
 */
public class ListPrependerActionTest {
    private ListPrependerAction listPrepender = new ListPrependerAction();
    private static final String RETURN_RESULT = "returnResult";
    private static final String RETURN_CODE = "returnCode";
    private static final String RETURN_CODE_SUCCESS = "0";
    private static final String LIST = "SpiderMan,IronMan,Hulk,Storm";
    private static final String PREPENDED_LIST = "Thor,SpiderMan,IronMan,Hulk,Storm";

    @Test
    public void testAppendElement() {
        Map<String, String> result = listPrepender.prependElement(LIST, "Thor", ",");
        assertEquals(PREPENDED_LIST, result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
    }
}
