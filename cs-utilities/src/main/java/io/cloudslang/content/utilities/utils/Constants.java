/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.utilities.utils;

/**
 * Created by persdana on 10/30/2014.
 */
public class Constants {
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String EMPTY_STRING = "";

    public static final class OutputNames {
        public static final String RETURN_RESULT = "returnResult";
        public static final String RETURN_CODE = "returnCode";
        public static final String RESPONSE_TEXT = "response";
        public static final String EXCEPTION = "exception";

        public static final String RESULT_STRING = "ResultString";
        public static final String RESULT_TEXT = "result";
    }

    public static final class ResponseNames {
        public static final String SUCCESS = "success";
        public static final String FAILURE = "failure";
    }

    public static final class ReturnCodes {

        public static final String RETURN_CODE_FAILURE = "-1";
        public static final String RETURN_CODE_SUCCESS = "0";
    }
}
