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
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.xml.utils.Constants.Outputs.RESULT_TEXT;
import static io.cloudslang.content.xml.utils.Constants.Outputs.SELECTED_VALUE;
import static io.cloudslang.content.xml.utils.Constants.QueryTypes.NODE;
import static io.cloudslang.content.xml.utils.Constants.QueryTypes.NODE_LIST;
import static io.cloudslang.content.xml.utils.Constants.QueryTypes.VALUE;
import static io.cloudslang.content.xml.utils.Constants.SuccessMessages.SELECT_SUCCESS;
import static org.apache.commons.io.IOUtils.readLines;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.join;
import static org.junit.Assert.assertEquals;

/**
 * Created by markowis on 22/02/2016.
 */
public class XpathQueryTest {

    private static final String XML_PATH = "xmlPath";
    private String xml;
    private XpathQuery select;

    @Before
    public void setUp() throws Exception {
        select = new XpathQuery();
        xml = join(readLines(ClassLoader.getSystemResourceAsStream("xml/test.xml"), Charset.forName("UTF-8")), IOUtils.LINE_SEPARATOR);
    }

    @After
    public void tearDown() {
        select = null;
        xml = null;
    }

    @Test
    public void testSelectValue() {
        String xPathQuery = "/root/element3/subelement";
        String expectedResult = "Sub3";

        Map<String, String> result = select.execute(xml, EMPTY, xPathQuery, VALUE, null, FALSE);

        assertEquals(expectedResult, result.get(SELECTED_VALUE));
        assertEquals(SELECT_SUCCESS, result.get(RETURN_RESULT));
    }

    @Test
    public void testSelectNode() {
        String xPathQuery = "/root/element3/subelement";
        String expectedResult = "<subelement attr=\"toDelete\">Sub3</subelement>";

        Map<String, String> result = select.execute(xml, EMPTY, xPathQuery, NODE, null, FALSE);

        assertEquals(expectedResult, result.get(SELECTED_VALUE));
        assertEquals(SELECT_SUCCESS, result.get(RETURN_RESULT));
    }

    @Test
    public void testSelectElementList() {
        String xPathQuery = "//subelement";
        String delimiter = ",";
        String expectedResult = "<subelement attr=\"toDelete\">Sub2</subelement>,<subelement attr=\"toDelete\">Sub3</subelement>";

        Map<String, String> result = select.execute(xml, EMPTY, xPathQuery, NODE_LIST, delimiter, FALSE);

        assertEquals(expectedResult, result.get(SELECTED_VALUE));
        assertEquals(SELECT_SUCCESS, result.get(RETURN_RESULT));
    }

    @Test
    public void testSelectAttributeList() {
        String xPathQuery = "//root/@*";
        String delimiter = ",";
        String expectedResult = "someid=\"5\"";

        Map<String, String> result = select.execute(xml, EMPTY, xPathQuery, NODE_LIST, delimiter, FALSE);

        assertEquals(expectedResult, result.get(SELECTED_VALUE));
        assertEquals(SELECT_SUCCESS, result.get(RETURN_RESULT));
    }

    @Test
    public void testNotFoundValue() {
        String xPathQuery = "/root/element1/@id";
        String expectedResult = "No match found";

        Map<String, String> result = select.execute(xml, EMPTY, xPathQuery, VALUE, null, FALSE);

        assertEquals(expectedResult, result.get(SELECTED_VALUE));
        assertEquals(SELECT_SUCCESS, result.get(RETURN_RESULT));
    }

    @Test
    public void testNotFoundNode() {
        String xPathQuery = "/root/element1/subelement";
        String expectedResult = "No match found";

        Map<String, String> result = select.execute(xml, EMPTY, xPathQuery, NODE, null, FALSE);

        assertEquals(expectedResult, result.get(SELECTED_VALUE));
        assertEquals(SELECT_SUCCESS, result.get(RETURN_RESULT));
    }

    @Test
    public void testFindWithNameSpace() throws Exception {

        URI resource = getClass().getResource("/xml/namespaceTest.xml").toURI();
        String namespaceXml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//foo:element1";
        String expectedResult = "<foo:element1 xmlns:foo=\"http://www.foo.org/\">First element</foo:element1>";

        Map<String, String> result = select.execute(namespaceXml, EMPTY, xPathQuery, NODE, null, FALSE);

        assertEquals(expectedResult, result.get(SELECTED_VALUE));
        assertEquals(SELECT_SUCCESS, result.get(RETURN_RESULT));
    }

    @Test
    public void testSelectElementWithXmlPath() throws IOException, URISyntaxException {
        String path = getClass().getResource("/xml/test.xml").toURI().getPath();
        String xPathQuery = "/root/element3/subelement";
        String expectedResult = "Sub3";

        Map<String, String> result = select.execute(path, XML_PATH, xPathQuery, VALUE, null, FALSE);

        assertEquals(expectedResult, result.get(SELECTED_VALUE));
        assertEquals(SELECT_SUCCESS, result.get(RETURN_RESULT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
    }

    @Test
    public void testSelectElementWithWrongXmlPath() throws IOException, URISyntaxException {
        String path = "/xml/Wrongtest.xml";
        String xPathQuery = "/root/element3/subelement";

        Map<String, String> result = select.execute(path, XML_PATH, xPathQuery, VALUE, null, FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
    }
}