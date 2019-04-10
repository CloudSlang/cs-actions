

package io.cloudslang.content.xml.actions;

import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.xml.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.ELEMENT_NOT_FOUND;
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.GENERAL_ERROR;
import static io.cloudslang.content.xml.utils.Constants.Outputs.ERROR_MESSAGE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.RESULT_TEXT;
import static org.junit.Assert.assertEquals;

/**
 * Created by markowis on 25/02/2016.
 */
public class AddAttributeTest {

    public static final String EMPTY_STRING = "";
    private AddAttribute addAttribute;
    String xml;

    @Before
    public void setUp() throws Exception{
        addAttribute = new AddAttribute();
        URI resource = getClass().getResource("/xml/test.xml").toURI();
        xml = FileUtils.readFileToString(new File(resource));
    }

    @After
    public void tearDown(){
        addAttribute = null;
        xml = null;
    }

    @Test
    public void testAddAttribute() {
        String xPathQuery = "/root/element1";
        String attributeName = "newAttr";
        String value = "New Value";

        Map<String, String> result = addAttribute.execute(xml, EMPTY_STRING, xPathQuery, attributeName, value, FALSE);

        assertEquals(ResponseNames.SUCCESS, result.get(RESULT_TEXT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(Constants.SuccessMessages.ADD_ATTRIBUTE_SUCCESS, result.get(RETURN_RESULT));
    }

    @Test
    public void testNotFound() {
        String xPathQuery = "/element1";
        String attributeName = "newAttr";
        String value = "New Value";

        Map<String, String> result = addAttribute.execute(xml, EMPTY_STRING, xPathQuery, attributeName, value, FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals(GENERAL_ERROR + ELEMENT_NOT_FOUND, result.get(ERROR_MESSAGE));
    }

    @Test
    public void testNonElementXPathQuery() {
        String xPathQuery = "//element1/@attr";
        String attributeName = "newAttr";
        String value = "New Value";

        Map<String, String> result = addAttribute.execute(xml, EMPTY_STRING, xPathQuery, attributeName, value, FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals(GENERAL_ERROR + Constants.ErrorMessages.ADD_ATTRIBUTE_FAILURE +
                Constants.ErrorMessages.NEED_ELEMENT_TYPE, result.get(ERROR_MESSAGE));
    }
}
