/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.ELEMENT_NOT_FOUND;
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.NEED_ELEMENT_TYPE;
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.PARSING_ERROR;
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.REMOVE_FAILURE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.ERROR_MESSAGE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.RESULT_TEXT;
import static io.cloudslang.content.xml.utils.Constants.Outputs.RESULT_XML;
import static io.cloudslang.content.xml.utils.Constants.SuccessMessages.REMOVE_SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by markowis on 23/02/2016.
 */
public class RemoveTest {

    private Remove remove;
    String xml;
    private static final String XML_STRING = "xmlString";
    private static final String XML_PATH = "xmlPath";
    private static final String XML_URL = "xmlUrl";
    private static final String INVALID_XML_DOCUMENT_SOURCE = " is an invalid input value. Valid values are: xmlString, xmlPath and xmlUrl";


    @Before
    public void setUp() throws Exception{
        remove = new Remove();
        URI resource = getClass().getResource("/xml/test.xml").toURI();
        xml = FileUtils.readFileToString(new File(resource));
    }

    @After
    public void tearDown(){
        remove = null;
        xml = null;
    }

    @Test
    public void testRemoveElements() {
        String xPathQuery = "//subelement";

        Map<String, String> result = remove.execute(xml, XML_STRING, xPathQuery, null, FALSE);

        assertEquals(ResponseNames.SUCCESS, result.get(RESULT_TEXT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(REMOVE_SUCCESS, result.get(RETURN_RESULT));
    }

    @Test
    public void testRemoveAttributes() {
        String xPathQuery = "//subelement";
        String name = "attr";

        Map<String, String> result = remove.execute(xml, XML_STRING, xPathQuery, name, FALSE);

        assertEquals(ResponseNames.SUCCESS, result.get(RESULT_TEXT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(REMOVE_SUCCESS, result.get(RETURN_RESULT));
    }

    @Test
    public void testNotFound() {
        String xPathQuery = "/subelement";

        Map<String, String> result = remove.execute(xml, XML_STRING, xPathQuery, null, FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals(PARSING_ERROR + ELEMENT_NOT_FOUND, result.get(ERROR_MESSAGE));
    }

    @Test
    public void testNonElementXPathQuery() {
        String xPathQuery = "//subelement/@attr";

        Map<String, String> result = remove.execute(xml, XML_STRING, xPathQuery, null, FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals(PARSING_ERROR + REMOVE_FAILURE + NEED_ELEMENT_TYPE, result.get(ERROR_MESSAGE));
    }

    @Test
    public void testRemoveElementsWithWrongXmlPath() {
        String xPathQuery = "//subelement";

        Map<String, String> result = remove.execute(xml, "xmlpathd", xPathQuery, null, FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertTrue(result.get(ERROR_MESSAGE).contains(INVALID_XML_DOCUMENT_SOURCE));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertTrue(result.get(RESULT_XML).isEmpty());
    }
    @Test
    public void testRemoveElementWithXmlPath() throws IOException, URISyntaxException {
        String path = getClass().getResource("/xml/test.xml").toURI().getPath();

        Map<String, String> result = remove.execute(path, XML_PATH, "//subelement", "attr", FALSE);

        assertEquals(ResponseNames.SUCCESS, result.get(RESULT_TEXT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(REMOVE_SUCCESS, result.get(RETURN_RESULT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
    }

    @Test
    public void testRemoveElementWithWrongXmlPath() throws IOException, URISyntaxException {

        Map<String, String> result = remove.execute("c:/InvalidtestWrong.xml", XML_PATH, "//subelement", "attr", FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals(FAILURE, result.get("returnCode"));
    }
}
