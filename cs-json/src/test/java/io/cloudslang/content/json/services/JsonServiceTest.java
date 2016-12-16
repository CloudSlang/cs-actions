/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.json.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.filter.ValueNode;
import io.cloudslang.content.json.actions.JsonPathQuery;
import io.cloudslang.content.json.exceptions.RemoveEmptyElementException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * Created by nane on 2/9/2016.
 */
public class JsonServiceTest {
    private JsonService jsonServiceUnderTest;
    private String jsonStringInput;
    private String expectedJsonStringOutput;
    private String actualJsonStringOutput;

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Before
    public void setUp() {
        jsonServiceUnderTest = new JsonService();
    }

    @After
    public void tearDown() {
        jsonServiceUnderTest = null;
        jsonStringInput = null;
        expectedJsonStringOutput = null;
        actualJsonStringOutput = null;
    }


    @Test
    public void givenValidJsonStringWithDoubleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{\"removed1\":\"\", \"removed2\":[], \"removed3\":null, \"expected\":\"value\"} ";
        expectedJsonStringOutput = "{\"expected\":\"value\"}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput, actualJsonStringOutput);
    }

    @Test
    public void givenValidJsonStringWithNewLineAndDoubleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{\"removed1\":\"\", \"removed2\":[], \n" +
                " \"removed3\":null, \"expected\":\"value\"} \n";
        expectedJsonStringOutput = "{\"expected\":\"value\"}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput, actualJsonStringOutput);
    }

    @Test
    public void givenValidJsonStringWithEmptySpaceAndDoubleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{   \"removed1\":\"\", \"removed2\":[],  \"removed3\":null, \"expected\":\"value\"} ";
        expectedJsonStringOutput = "{\"expected\":\"value\"}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput, actualJsonStringOutput);
    }

    @Test
    public void givenValidJsonStringWithSingleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{'removed1':'', 'removed2':[], 'removed3':null, 'expected':'value'} ";
        expectedJsonStringOutput = "{'expected':'value'}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput, actualJsonStringOutput);
    }

    @Test
    public void givenValidJsonStringWithNewLineAndSingleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{'removed1':'', 'removed2':[], 'removed3':null, 'expected':'value'} ";
        expectedJsonStringOutput = "{'expected':'value'}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput, actualJsonStringOutput);
    }

    @Test
    public void givenValidJsonStringWithEmptySpaceAndSingleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{    'removed1':'', 'removed2':[],  'removed3':null, 'expected':'value'} ";
        expectedJsonStringOutput = "{'expected':'value'}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput, actualJsonStringOutput);
    }

    @Test
    public void givenValidComplexJsonStringDoubleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{ \n" +
                " \"expected1\": \"value\", \n" +
                " \"expected2\": 1, \n" +
                " \"remove1\": null, \n" +
                " \"remove2\": [], \n" +
                " \"remove3\": \"\", \n" +
                " \"expected\": { \n" +
                "  \"public\": [{ \n" +
                "   \"expected1\": \"value\", \n" +
                "   \"expected2\": 1, \n" +
                "   \"remove1\": null, \n" +
                "   \"remove2\": [], \n" +
                "   \"remove3\": \"\" \n" +
                "  }] \n" +
                " }, \n" +
                " \"links\": [{ \n" +
                "  \"expected1\": \"http://test.com\", \n" +
                "  \"expected2\": 1, \n" +
                "  \"remove1\": null, \n" +
                "  \"remove2\": [], \n" +
                "  \"remove3\": \"\" \n" +
                " }] \n" +
                "} \n";
        expectedJsonStringOutput = "{\"expected1\":\"value\",\"expected2\":1,\"expected\":{\"public\":[{\"expected1\":\"value\",\"expected2\":1}]},\"links\":[{\"expected1\":\"http://test.com\",\"expected2\":1}]}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(JsonPath.parse(expectedJsonStringOutput).json(), JsonPath.parse(actualJsonStringOutput).json());
    }

    @Test
    public void givenValidComplexJsonStringSingleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{ \n" +
                " 'expected1': 'value', \n" +
                " 'expected2': 1, \n" +
                " 'remove1': null, \n" +
                " 'remove2': [], \n" +
                " 'remove3': '', \n" +
                " 'expected': { \n" +
                "  'public': [{ \n" +
                "   'expected1': 'value', \n" +
                "   'expected2': 1, \n" +
                "   'remove1': null, \n" +
                "   'remove2': [], \n" +
                "   'remove3': '' \n" +
                "  }] \n" +
                " }, \n" +
                " 'links': [{ \n" +
                "  'expected1': 'http://test.com', \n" +
                "  'expected2': 1, \n" +
                "  'remove1': null, \n" +
                "  'remove2': [], \n" +
                "  'remove3': '' \n" +
                " }] \n" +
                "} \n";
        expectedJsonStringOutput = "{'expected1':'value','expected2':1,'expected':{'public':[{'expected1':'value','expected2':1}]},'links':[{'expected1':'http://test.com','expected2':1}]}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(JsonPath.parse(expectedJsonStringOutput).json(), JsonPath.parse(actualJsonStringOutput).json());
    }

    @Test
    public void givenURLJsonValueAndDoubleQuoteThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{\"removed1\":\"\", \"removed2\":[], \"removed3\":null, \"expected\":\"http://test.com\"} ";
        expectedJsonStringOutput = "{\"expected\":\"http://test.com\"}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput, actualJsonStringOutput);
    }

    @Test
    public void givenInvalidJsonThenThrowException() throws RemoveEmptyElementException {
        jsonStringInput = "{\"removed1\":\"\", \"removed2\":[], \"removed3\":null \"expected\":\"http://test.com\"}";
        expectedJsonStringOutput = "{\"expected\":\"http://test.com\"}";

        exception.expect(RemoveEmptyElementException.class);
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);
    }

    @Test
    public void givenJsonWithEmptyElementsThenReturnEmptyJsonString() throws RemoveEmptyElementException {
        jsonStringInput = "{'remove1': '','remove2': ''}";
        expectedJsonStringOutput = "{}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput, actualJsonStringOutput);
    }

    @Test
    public void evaluateSimpleJsonPathQuery() throws Exception {
        JsonNode jsonNode = JsonService.evaluateJsonPathQuery("{'key1': 'value1','key2': 'value2', 'key3': { 'key31': 'value31'}}", "$.key3.key31");
        assertEquals(jsonNode.toString(), "\"value31\"");
    }

    @Test
    public void evaluateComplexJsonPathQuery() throws Exception {
        JsonNode jsonNode = JsonService.evaluateJsonPathQuery("{ \"store\": {\n" +
                "    \"book\": [ \n" +
                "      { \"category\": \"reference\",\n" +
                "        \"author\": \"Nigel Rees\",\n" +
                "        \"title\": \"Sayings of the Century\",\n" +
                "        \"price\": 8.95\n" +
                "      },\n" +
                "      { \"category\": \"fiction\",\n" +
                "        \"author\": \"Evelyn Waugh\",\n" +
                "        \"title\": \"Sword of Honour\",\n" +
                "        \"price\": 12.99\n" +
                "      },\n" +
                "      { \"category\": \"fiction\",\n" +
                "        \"author\": \"Herman Melville\",\n" +
                "        \"title\": \"Moby Dick\",\n" +
                "        \"isbn\": \"0-553-21311-3\",\n" +
                "        \"price\": 8.99\n" +
                "      },\n" +
                "      { \"category\": \"fiction\",\n" +
                "        \"author\": \"J. R. R. Tolkien\",\n" +
                "        \"title\": \"The Lord of the Rings\",\n" +
                "        \"isbn\": \"0-395-19395-8\",\n" +
                "        \"price\": 22.99\n" +
                "      }\n" +
                "    ],\n" +
                "    \"bicycle\": {\n" +
                "      \"color\": \"red\",\n" +
                "      \"price\": 19.95\n" +
                "    }\n" +
                "  }\n" +
                "}", "$..book[?(@.price<10)]");
        assertEquals(jsonNode.toString(), "[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99}]");
    }
}
