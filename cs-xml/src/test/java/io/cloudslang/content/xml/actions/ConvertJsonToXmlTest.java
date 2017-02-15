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

import io.cloudslang.content.xml.utils.Constants.Outputs;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.xml.utils.Constants.NEW_LINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Created by ursan on 8/4/2016.
 */
public class ConvertJsonToXmlTest {
    private ConvertJsonToXml converter;

    @Before
    public void setUp() throws Exception {
        converter = new ConvertJsonToXml();
    }

    @Test
    public void testConvertJsonNullToXmlElements() {
        Map<String, String> result = converter.execute(
                null,
                "false",
                "false",
                "",
                "item",
                "",
                "",
                "",
                "",
                ",");
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(result.get(RETURN_CODE), SUCCESS);
        assertEquals(result.get(RETURN_RESULT), "");
    }

    @Test
    public void testConvertJsonEmptyStringToXmlElements() {
        Map<String, String> result = converter.execute(
                "",
                "false",
                "false",
                "",
                "item",
                "",
                "",
                "",
                "",
                ",");
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(result.get(RETURN_CODE), SUCCESS);
        assertEquals(result.get(RETURN_RESULT), "");
    }

    @Test
    public void testConvertJsonArrayWithoutRootTagNameToXmlElements() {
        Map<String, String> result = converter.execute(
                "[{\"name1\":\"value1\"},{\"name2\":\"value2\"}]",
                "false",
                "false",
                "",
                "item",
                "",
                "",
                "",
                "",
                ",");
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(result.get(RETURN_CODE), SUCCESS);
        assertEquals(result.get(RETURN_RESULT), "<item><name1>value1</name1></item>" + NEW_LINE +
                "<item><name2>value2</name2></item>");
    }

    @Test
    public void testConvertJsonObjectWithoutRootTagNameToXmlDocument() {
        Map<String, String> result = converter.execute(
                "{\"property\":{\"name1\":\"value1\"}}",
                "false",
                "true",
                "",
                "item",
                "",
                "",
                "",
                "",
                ",");
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(result.get(RETURN_CODE), SUCCESS);
        assertEquals(result.get(RETURN_RESULT), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NEW_LINE +
                "<property><name1>value1</name1></property>" + NEW_LINE);
    }

    @Test
    public void testConvertComplexJsonObjectWithRootTagNameToXml() {
        Map<String, String> result = converter.execute(
                "{\"@id\":\"Page1\", \"ui:position\": {\"x\":1,\"y\":2}," +
                        "\"f:properties\": [{\"key1\":\"value1\"},{\"key2\":\"value2\"}]," +
                        "\"details\":[{\"type\":\"size\", \"height\":10, \"width\":10},{\"type\":\"color\", \"name\":\"blue\"}\n" +
                        "]}",
                "false",
                "false",
                "root",
                "item",
                "f,ui",
                "http://java.sun.com/jsf/core,urn:x-hp:2012:software:eve:uibinding",
                "f:properties",
                "f:property",
                ",");
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(result.get(RETURN_CODE), SUCCESS);
        assertEquals(result.get(RETURN_RESULT),
                "<root xmlns:f=\"http://java.sun.com/jsf/core\" " +
                        "xmlns:ui=\"urn:x-hp:2012:software:eve:uibinding\" " +
                        "id=\"Page1\">" +
                        "<ui:position><x>1</x><y>2</y></ui:position>" +
                        "<f:properties>" +
                        "<f:property><key1>value1</key1></f:property>" +
                        "<f:property><key2>value2</key2></f:property>" +
                        "</f:properties>" +
                        "<details>" +
                        "<item><type>size</type><height>10</height><width>10</width></item>" +
                        "<item><type>color</type><name>blue</name></item>" +
                        "</details>" +
                        "</root>");
    }

    @Test
    public void testConvertJsonArrayWithRootTagNameToXmlElements() {
        Map<String, String> result = converter.execute(
                "[{\"name1\":\"value1\"},{\"name2\":\"value2\"}]",
                "false",
                "false",
                "items",
                "item",
                "",
                "",
                "",
                "",
                ",");
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(result.get(RETURN_CODE), SUCCESS);
        assertEquals(result.get(RETURN_RESULT), "<items><item><name1>value1</name1></item><item><name2>value2</name2></item></items>");
    }

    @Test
    public void testInvalidBooleanInput() {
        Map<String, String> result = converter.execute(
                " ",
                "invalid",
                "invalid",
                "",
                "",
                "",
                "",
                "",
                "",
                ",");
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(result.get(RETURN_CODE), FAILURE);
        assertEquals(result.get(RETURN_RESULT), "invalid is not a valid value for Boolean");
    }

    @Test
    public void testMissingRootTagName() {
        Map<String, String> result = converter.execute(
                "[{\"name1\":\"value1\"},{\"name2\":\"value2\"}]",
                "false",
                "true",
                "",
                "",
                "",
                "",
                "",
                "",
                ",");
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(result.get(RETURN_CODE), FAILURE);
        assertEquals(result.get(RETURN_RESULT), "The root tag name is missing");
    }

    @Test
    public void testMalformedJson() {
        Map<String, String> result = converter.execute(
                "{items: {\"name1\":\"value1\"},{\"name2\":\"value2\"}}",
                "false",
                "true",
                "",
                "",
                "",
                "",
                "",
                "",
                ",");
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(result.get(RETURN_CODE), FAILURE);
        assertEquals(result.get(RETURN_RESULT), "com.google.gson.stream.MalformedJsonException: Expected name at line 1 column 28 path $.items");
    }
}