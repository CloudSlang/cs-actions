/*
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.database.constants;

import io.cloudslang.content.constants.ExceptionValues;

/**
 * Created by pinteae on 1/16/2017.
 */
public class DBExceptionValues extends ExceptionValues {
    public static final String INVALID_IGNORE_CASE = "The value \"%s\" is an invalid value for ignoreCase input.";
    public static final String INVALID_AUTH_TYPE = "The authentication type input \"%s\" is not valid authentication method.";
    public static final String INVALID_AUTH_TYPE_WINDOWS = "Windows authentication can only be used with MSSQL!";
    public static final String INVALID_INSTANCE = "The instance input can only be used with MSSQL.";
    public static final String INVALID_NEGATIVE_TIMEOUT = "Timeout must be greater than zero!";
    public static final String INVALID_TIMEOUT = "Timeout has to be a positive integer!";
    public static final String INVALID_TRUST_ALL_ROOTS_REQUIRE = "trustStore or trustStorePassword is mandatory if trustAllRoots is false";
    public static final String INVALID_DB_TYPE = "The dbType input is invalid";
    public static final String INVALID_USERNAME = "username input is empty.";
    public static final String INVALID_PASSWORD = "password input is empty.";
    public static final String INVALID_DATABASE = "database input is empty.";
    public static final String INVALID_COMMAND = "command input is empty.";
    public static final String INVALID_DB_PORT = "dbPort must be an integer.";
    public static final String INVALID_RESULT_SET_TYPE = "the result set is invalid";
    public static final String INVALID_RESULT_SET_CONCURRENCY = "the result set concurrency is invalid";
    public static final String INVALID_TRUST_ALL_ROOTS = "trustAllRoots must be 'true' or 'false'";
    public static final String INVALID_DB_SERVER_NAME = "dbServerName can't be empty";
    public static final String INVALID_COMMANDS_EXCLUSIVITY = "Only one of the sqlCommands and scriptFileName can be specified";
    public static final String INVALID_AUTHENTICATION_TYPE_FOR_MS_SQL = "Invalid authentication type for MS SQL : ";
    public static final String NO_SQL_COMMAND = "No SQL command to be executed.";
}
