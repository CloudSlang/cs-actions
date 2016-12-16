/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.azure.actions.utils;

import io.cloudslang.content.azure.actions.utils.GetSharedAccessKeyToken;
import io.cloudslang.content.constants.ReturnCodes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by victor on 29.09.2016.
 */
public class GetSharedAccessKeyTokenTest {
    private GetSharedAccessKeyToken authorizationToken;

    @Before
    public void setUp() throws Exception {
        authorizationToken = new GetSharedAccessKeyToken();
    }

    @After
    public void tearDown() throws Exception {
        authorizationToken = null;
    }

    @Test
    public void executeTestSuccess() throws Exception {
        final Map<String, String> resultMap = authorizationToken.execute("53dd860e1b72ff0467030003", "pXeTVcmdbU9XxH6fPcPlq8Y9D9G3Cdo5Eh2nMSgKj/DWqeSFFXDdmpz5Trv+L2hQNM+nGa704Rf8Z22W9O1jdQ==", "08/04/2014 10:03 PM");
        assertEquals(resultMap.get(RETURN_RESULT), "SharedAccessSignature uid=53dd860e1b72ff0467030003&ex=2014-08-04T22:03:00.0000000Z&sn=FWBrF2PBYVEZgpoyaPWNKIBIGLQxbJAw6A3Lu4EB78aooaZldHXZTZBkqyMWlLdrpK5/q3BFDgj9vqX1vrR0ww==");
        assertEquals(resultMap.get(RETURN_CODE), SUCCESS);
    }

    @Test
    public void executeTestFailureEmptyInputs() throws Exception {
        final Map<String, String> resultMap = authorizationToken.execute("", "", "");
        assertEquals(resultMap.get(RETURN_RESULT), "The identifier can't be null or empty.\nThe primaryOrSecondaryKey can't be null or empty.\nThe expiry can't be null or empty.");
        assertEquals(resultMap.get(EXCEPTION), "The identifier can't be null or empty.\nThe primaryOrSecondaryKey can't be null or empty.\nThe expiry can't be null or empty.");
        assertEquals(resultMap.get(RETURN_CODE), FAILURE);
    }

    @Test
    public void executeTestFailureInvalidDateFormat() throws Exception {
        final Map<String, String> resultMap = authorizationToken.execute("53dd860e1b72ff0467030003", "pXeTVcmdbU9XxH6fPcPlq8Y9D9G3Cdo5Eh2nMSgKj/DWqeSFFXDdmpz5Trv+L2hQNM+nGa704Rf8Z22W9O1jdQ==", "11111111");
        assertEquals(resultMap.get(RETURN_RESULT), "Unable to parse the date: 11111111");
        assertTrue(resultMap.get(EXCEPTION).startsWith("java.text.ParseException: Unable to parse the date: 11111111"));
        assertEquals(resultMap.get(RETURN_CODE), FAILURE);
    }
}