/*
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
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
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.APPEND_CHILD_FAILURE;
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.ELEMENT_NOT_FOUND;
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.NEED_ELEMENT_TYPE;
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.PARSING_ERROR;
import static io.cloudslang.content.xml.utils.Constants.Outputs.ERROR_MESSAGE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.RESULT_TEXT;
import static io.cloudslang.content.xml.utils.Constants.SuccessMessages.APPEND_CHILD_SUCCESS;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

/**
 * Created by markowis on 25/02/2016.
 */
public class AppendChildTest {

    private AppendChild appendChild;
    String xml;

    @Before
    public void setUp() throws Exception{
        appendChild = new AppendChild();
        URI resource = getClass().getResource("/xml/test.xml").toURI();
        xml = FileUtils.readFileToString(new File(resource));
    }

    @After
    public void tearDown(){
        appendChild = null;
        xml = null;
    }

    @Test
    public void testAppendChild() {
        String xPathQuery = "//element1";
        String xmlChild = "<sub1 attr=\"ibute\">Sub1</sub1>";

        Map<String, String> result = appendChild.execute(xml, EMPTY, xPathQuery, xmlChild, FALSE);

        assertEquals(ResponseNames.SUCCESS, result.get(RESULT_TEXT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(APPEND_CHILD_SUCCESS, result.get(RETURN_RESULT));
    }

    @Test
    public void testAppendChildMultiple() {
        String xPathQuery = "//subelement";
        String xmlChild = "<subsub attr=\"ibute\">Deeply nested</subsub>";

        Map<String, String> result = appendChild.execute(xml, EMPTY, xPathQuery, xmlChild, FALSE);

        assertEquals(ResponseNames.SUCCESS, result.get(RESULT_TEXT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(APPEND_CHILD_SUCCESS,
                result.get(RETURN_RESULT));
    }

    @Test
    public void testNotFound() {
        String xPathQuery = "/subelement";
        String xmlChild = "<toAdd>Text</toAdd>";

        Map<String, String> result = appendChild.execute(xml, EMPTY, xPathQuery, xmlChild, FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals(PARSING_ERROR + ELEMENT_NOT_FOUND, result.get(ERROR_MESSAGE));
    }

    @Test
    public void testInvalidChild() {
        String xPathQuery = "//subelement";
        String xmlChild = "<open>Text</close>";

        Map<String, String> result = appendChild.execute(xml, EMPTY, xPathQuery, xmlChild, FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals(PARSING_ERROR +
                "The element type \"open\" must be terminated by the matching end-tag \"</open>\".",
                result.get(ERROR_MESSAGE));
    }

    @Test
    public void testNonElementXPathQuery() {
        String xPathQuery = "//element1/@attr";
        String xmlChild = "<toAdd>Text</toAdd>";

        Map<String, String> result = appendChild.execute(xml, EMPTY, xPathQuery, xmlChild, FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals(PARSING_ERROR + APPEND_CHILD_FAILURE + NEED_ELEMENT_TYPE, result.get(ERROR_MESSAGE));
    }
}
