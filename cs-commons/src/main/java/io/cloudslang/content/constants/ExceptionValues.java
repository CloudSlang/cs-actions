/*
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.constants;

/**
 * Created by victor on 31.08.2016.
 */
public class ExceptionValues {
    public static final String EXCEPTION_DELIMITER = ": ";
    public static final String INVALID_BOOLEAN_VALUE = "The provided string cannot be converted to a boolean value. It must be 'true' or 'false'";
    public static final String INVALID_INTEGER_VALUE = "The provided string cannot be converted to an integer value.";
    public static final String INVALID_DOUBLE_VALUE = "The provided string cannot be converted to a double value.";
    public static final String INVALID_LONG_VALUE = "The provided string cannot be converted to a long value.";
    public static final String INVALID_BOUNDS = "The lower bound is required to be less than the upper bound";
    public static final String INVALID_KEY_VALUE_PAIR = "The Key, Value pair has to be formatted as: key<keyValueDelimiter>value";
}
