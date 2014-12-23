package org.openscore.content.utilities.actions;

import org.openscore.content.utilities.actions.IfAction;
import org.openscore.content.utilities.utils.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by persdana on 10/31/2014.
 */
public class IfActionTest {

    @Test
    public void testIfAction() {
        IfAction toTest = new IfAction();
        String condition = "1==1";

        Map<String, String> result = toTest.ifAction(condition);
        String returnResult = result.get(Constants.OutputNames.RETURN_RESULT);

        Assert.assertEquals("true", returnResult);
    }

    @Test
    public void test2IfAction() {
        IfAction toTest = new IfAction();
        String condition = "1==2";

        Map<String, String> result = toTest.ifAction(condition);
        String returnResult = result.get(Constants.OutputNames.RETURN_RESULT);

        Assert.assertEquals("false", returnResult);
    }

    @Test
    public void testIfActionNotEqual() {
        IfAction toTest = new IfAction();
        String condition = "1!=2";

        Map<String, String> result = toTest.ifAction(condition);
        String returnResult = result.get(Constants.OutputNames.RETURN_RESULT);

        Assert.assertEquals("true", returnResult);
    }

    @Test
    public void testIfActionWithMathExpr() {
        IfAction toTest = new IfAction();
        String condition = "(4+6)/2==4+1";

        Map<String, String> result = toTest.ifAction(condition);
        String returnResult = result.get(Constants.OutputNames.RETURN_RESULT);

        Assert.assertEquals("true", returnResult);
    }

    @Test
    public void testIfActionWithNegativeMathExpr() {
        IfAction toTest = new IfAction();
        String condition = "(4+6)/2==4+2";

        Map<String, String> result = toTest.ifAction(condition);
        String returnResult = result.get(Constants.OutputNames.RETURN_RESULT);

        Assert.assertEquals("false", returnResult);
    }

    @Test
    public void testIfActionWithBooleanExpr() {
        IfAction toTest = new IfAction();
        String condition = "true == true";

        Map<String, String> result = toTest.ifAction(condition);
        String returnResult = result.get(Constants.OutputNames.RETURN_RESULT);

        Assert.assertEquals("true", returnResult);
    }

    @Test
    public void test2IfActionWithBooleanExpr() {
        IfAction toTest = new IfAction();
        String condition = "(true & true) == (true | false)";

        Map<String, String> result = toTest.ifAction(condition);
        String returnResult = result.get(Constants.OutputNames.RETURN_RESULT);

        Assert.assertEquals("true", returnResult);
    }

    @Test
    public void testIfActionWithComposedExpr() {
        IfAction toTest = new IfAction();
        String condition = "((true & true) == (true | false)) && 2==2";

        Map<String, String> result = toTest.ifAction(condition);
        String returnResult = result.get(Constants.OutputNames.RETURN_RESULT);

        Assert.assertEquals("true", returnResult);
    }

    @Test
    public void test2IfActionWithComposedExpr() {
        IfAction toTest = new IfAction();
        String condition = "((true & true) == (true | false)) && 2==3";

        Map<String, String> result = toTest.ifAction(condition);
        String returnResult = result.get(Constants.OutputNames.RETURN_RESULT);

        Assert.assertEquals("false", returnResult);
    }
}
