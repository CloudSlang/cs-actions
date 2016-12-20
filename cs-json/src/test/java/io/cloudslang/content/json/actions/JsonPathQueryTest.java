/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.json.actions;

import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ReturnCodes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static io.cloudslang.content.json.utils.JsonExceptionValues.INVALID_JSONOBJECT;
import static io.cloudslang.content.json.utils.JsonExceptionValues.INVALID_JSONPATH;
import static org.junit.Assert.*;

/**
 * Created by victor on 9/12/16.
 */
public class JsonPathQueryTest {
    private JsonPathQuery jsonPathQuery;

    @Before
    public void setUp() throws Exception {
        jsonPathQuery = new JsonPathQuery();
    }

    @After
    public void tearDown() throws Exception {
        jsonPathQuery = null;
    }

    @Test
    public void executeValid() throws Exception {
        final Map<String, String> resultMap = jsonPathQuery.execute("{'a':'b', 'a1':'b1', 'a1':'b1'}", "$.a");
        assertEquals(resultMap.get(OutputNames.RETURN_CODE), ReturnCodes.SUCCESS);
        assertEquals(resultMap.get(OutputNames.RETURN_RESULT), "\"b\"");
    }

    @Test
    public void executeInvalidJsonObjecy() throws Exception {
        final Map<String, String> resultMap = jsonPathQuery.execute(null, "$.a");
        assertEquals(resultMap.get(OutputNames.RETURN_CODE), ReturnCodes.FAILURE);
        assertEquals(resultMap.get(OutputNames.RETURN_RESULT), INVALID_JSONOBJECT);
        assertNotNull(resultMap.get(OutputNames.EXCEPTION));
    }

    @Test
    public void executeInvalidJsonPath() throws Exception {
        final Map<String, String> resultMap = jsonPathQuery.execute("{'a':'b', 'a1':'b1', 'a1':'b1'}", null);
        assertEquals(resultMap.get(OutputNames.RETURN_CODE), ReturnCodes.FAILURE);
        assertEquals(resultMap.get(OutputNames.RETURN_RESULT), INVALID_JSONPATH);
        assertNotNull(resultMap.get(OutputNames.EXCEPTION));
    }

}