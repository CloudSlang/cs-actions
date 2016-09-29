package io.cloudslang.content.actions;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by giloan on 7/8/2016.
 */
public class ListRemoverActionTest {
    private ListRemoverAction listRemover = new ListRemoverAction();
    private static final String RETURN_RESULT = "returnResult";
    private static final String RETURN_CODE = "returnCode";
    private static final String RETURN_CODE_SUCCESS = "0";
    private static final String LIST = "SpiderMan,IronMan,Hulk,Storm";
    private static final String SHORTER_LIST = "IronMan,Hulk,Storm";

    @Test
    public void testAppendElement() {
        Map<String, String> result = listRemover.removeElement(LIST, "0", ",");
        assertEquals(SHORTER_LIST, result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
    }
}
