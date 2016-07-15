/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.json.utils;

/**
 * Created by ioanvranauhp
 * Date 1/12/2015.
 */
public class Constants {

    public static final String EMPTY_STRING = "";

    public static final class OutputNames {

        public static final String RETURN_RESULT = "returnResult";
        public static final String EXCEPTION = "exception";
        public static final String RETURN_CODE = "returnCode";
        public static final String ERROR_MESSAGE = "errorMessage";
    }

    public static final class ReturnCodes {

        public static final String RETURN_CODE_FAILURE = "-1";
        public static final String RETURN_CODE_SUCCESS = "0";
    }

    public static final class ResponseNames {

        public static final String SUCCESS = "success";
        public static final String FAILURE = "failure";
    }

    public static final class InputNames {

        public static final String JSON_OBJECT = "jsonObject";
        public static final String NEW_PROPERTY_NAME = "newPropertyName";
        public static final String NEW_PROPERTY_VALUE = "newPropertyValue";
        public static final String OBJECT = "object";
        public static final String KEY = "key";
        public static final String ARRAY = "array";
        public static final String ACTION = "action";
        public static final String JSON_PATH = "jsonPath";
        public static final String NAME = "name";
        public static final String VALUE = "value";
        public static final String VALIDATE_VALUE = "validateValue";
    }

    public static final class EditJsonOperations {

        public static final String GET_ACTION = "get";
        public static final String INSERT_ACTION = "insert";
        public static final String UPDATE_ACTION = "update";
        public static final String DELETE_ACTION = "delete";
        public static final String ADD_ACTION = "add";
    }
}
