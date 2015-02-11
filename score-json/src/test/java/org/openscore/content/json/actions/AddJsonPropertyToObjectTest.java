/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package org.openscore.content.json.actions;

import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ioanvranauhp
 * Date 2/5/2015.
 */
public class AddJsonPropertyToObjectTest {

    private static final String RETURN_RESULT = "returnResult";
    public static final String EXCEPTION = "exception";

    private final AddJsonPropertyToObject addJsonPropertyToObject = new AddJsonPropertyToObject();

    @Test
    public void testExecuteSimpleAll() {
        String jsonObject = "{}";
        String name = "test";
        String value = "1";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value);
        assertEquals("{\"test\":1}", result.get(RETURN_RESULT));
        assertEquals("0", result.get("returnCode"));
    }

    @Test
    public void testExecuteJsonObjectBad() throws Exception {
        String jsonObject = "{";
        String name = "test";
        String value = "1";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value);
        assertEquals("Invalid jsonObject provided! java.io.EOFException: End of input at line 1 column 2", result.get(RETURN_RESULT));
        assertEquals("java.io.EOFException: End of input at line 1 column 2", result.get("exception"));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteNameBad() {
        String jsonObject = "{}";
        String name = "test{\"";
        String value = "1";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value);
        assertEquals("{\"test{\\\"\":1}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecutePropertyValueBad() {
        String jsonObject = "{}";
        String name = "test";
        String value = "1\"{";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value);
        assertEquals("The value for the property " + name + " it is not a valid JSON object!", result.get(RETURN_RESULT));
        assertEquals("com.google.gson.stream.MalformedJsonException: Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 4 path $", result.get("exception"));
    }

    @Test
    public void testExecutePropertyValueJson() {
        String jsonObject = "{}";
        String name = "test";
        String value = "{\"a\":\"b\"}";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value);
        assertEquals("{\"test\":{\"a\":\"b\"}}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecutePropertyValueArray() {
        String jsonObject = "{}";
        String name = "test";
        String value = "[1,2,3]";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value);
        assertEquals("{\"test\":[1,2,3]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteJsonObjectComplex() {
        String jsonObject = "{\"one\":{\"a\":\"a\",\"B\":\"B\"}, \"two\":\"two\", \"three\":[1,2,3.4]}";
        String name = "test";
        String value = "{\"a\":\"b\"}";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value);
        assertEquals("{\"one\":{\"a\":\"a\",\"B\":\"B\"},\"two\":\"two\",\"three\":[1,2,3.4],\"test\":{\"a\":\"b\"}}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteJsonObjectComplexPropertyString() {
        String jsonObject = "{\"one\":{\"a\":\"a\",\"B\":\"B\"}, \"two\":\"two\", \"three\":[1,2,3.4]}";
        String name = "test";
        String value = "a";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value);
        assertEquals("{\"one\":{\"a\":\"a\",\"B\":\"B\"},\"two\":\"two\",\"three\":[1,2,3.4],\"test\":\"a\"}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteJsonObjectSpecialChars() {
        String jsonObject = "{\"one\":{\"a\":\"a\",\"B\":\"B\"}, \"two\":\"two\", \"three;/?:@&=+,$\":[1,2,3.4]}";
        String name = "tes;/?:@&=+,$t";
        String value = "{\"a\":\"b;/?:@&=+,$\"}";
        final Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value);
        assertEquals("{\"one\":{\"a\":\"a\",\"B\":\"B\"},\"two\":\"two\",\"three;/?:@&=+,$\":[1,2,3.4],\"" +
                "tes;/?:@&=+,$t\":{\"a\":\"b;/?:@&=+,$\"}}", result.get(RETURN_RESULT));
    }

    @Test
    public void testExecuteEmptyJsonObject() {
        String jsonObject = "";
        String name = "test";
        String value = "1";
        Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value);
        assertEquals("Empty jsonObject provided!", result.get(RETURN_RESULT));
        assertEquals("Empty jsonObject provided!", result.get(EXCEPTION));
        assertEquals("-1", result.get("returnCode"));

        name = "test";
        value = "1";
        result = addJsonPropertyToObject.execute(null, name, value);
        assertEquals("Empty jsonObject provided!", result.get(RETURN_RESULT));
        assertEquals("Empty jsonObject provided!", result.get(EXCEPTION));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteEmptyName() {
        String jsonObject = "{}";
        String name = "";
        String value = "1";
        Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value);
        assertEquals("{\"\":1}", result.get(RETURN_RESULT));
        assertEquals("0", result.get("returnCode"));

        jsonObject = "{}";
        value = "1";
        result = addJsonPropertyToObject.execute(jsonObject, null, value);
        assertEquals("Null newPropertyName provided!", result.get(RETURN_RESULT));
        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testExecuteEmptyValue() {
        String jsonObject = "{}";
        String name = "test";
        String value = "";
        Map<String, String> result = addJsonPropertyToObject.execute(jsonObject, name, value);
        assertEquals("The value for the property test it is not a valid JSON object!", result.get(RETURN_RESULT));
        assertEquals("The value for the property test it is not a valid JSON object!", result.get(EXCEPTION));
        assertEquals("-1", result.get("returnCode"));

        jsonObject = "{}";
        name = "test";
        result = addJsonPropertyToObject.execute(jsonObject, name, null);
        assertEquals("The value for the property test it is not a valid JSON object!", result.get(RETURN_RESULT));
        assertEquals("The value for the property test it is not a valid JSON object!", result.get(EXCEPTION));
        assertEquals("-1", result.get("returnCode"));
    }
}
