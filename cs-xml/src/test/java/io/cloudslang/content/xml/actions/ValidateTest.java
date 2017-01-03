/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
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
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.PARSING_ERROR;
import static io.cloudslang.content.xml.utils.Constants.Outputs.ERROR_MESSAGE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.RESULT_TEXT;
import static io.cloudslang.content.xml.utils.Constants.SuccessMessages.PARSING_SUCCESS;
import static io.cloudslang.content.xml.utils.Constants.SuccessMessages.VALIDATION_SUCCESS;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

/**
 * Created by markowis on 18/02/2016.
 */
public class ValidateTest {

    private Validate validate;
    String xml;

    @Before
    public void setUp() throws Exception{
        validate = new Validate();
    }

    @After
    public void tearDown(){
        validate = null;
        xml = null;
    }

    @Test
    public void testWithWellFormedXML() {
        xml = "<root>toot</root>";

        Map<String, String> result = validate.execute(xml, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, null, FALSE);

        assertEquals(ResponseNames.SUCCESS, result.get(RESULT_TEXT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(PARSING_SUCCESS, result.get(RETURN_RESULT));
    }

    @Test
    public void testWithNonWellFormedXML() {
        xml = "<root>toot</roo>";

        Map<String, String> result = validate.execute(xml, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, null, FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals(PARSING_ERROR + "The element type \"root\" must be terminated by the matching end-tag \"</root>\".",
                result.get(ERROR_MESSAGE));
    }

    @Test
    public void testWithValidXML() throws Exception {
        URI resourceXML = getClass().getResource("/xml/valid.xml").toURI();
        xml = FileUtils.readFileToString(new File(resourceXML));

        URI resourceXSD = getClass().getResource("/xml/test.xsd").toURI();
        String xsd = FileUtils.readFileToString(new File(resourceXSD));

        Map<String, String> result = validate.execute(xml, EMPTY, xsd, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, FALSE);

        assertEquals(ResponseNames.SUCCESS, result.get(RESULT_TEXT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(VALIDATION_SUCCESS,
                result.get(RETURN_RESULT));
    }

    @Test
    public void testWithNotValidXML() throws Exception {
        URI resourceXML = getClass().getResource("/xml/notValid.xml").toURI();
        xml = FileUtils.readFileToString(new File(resourceXML));

        URI resourceXSD = getClass().getResource("/xml/test.xsd").toURI();
        String xsd = FileUtils.readFileToString(new File(resourceXSD));

        Map<String, String> result = validate.execute(xml, EMPTY, xsd, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals(PARSING_ERROR +
                "cvc-complex-type.4: Attribute 'someid' must appear on element 'root'.",
                result.get(ERROR_MESSAGE));
    }
}
