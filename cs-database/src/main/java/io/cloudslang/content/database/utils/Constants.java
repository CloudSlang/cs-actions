/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.database.utils;


import static io.cloudslang.content.database.constants.DBInputNames.TRUST_STORE;
import static io.cloudslang.content.database.constants.DBInputNames.TRUST_STORE_PASSWORD;
import static io.cloudslang.content.database.constants.DBOtherValues.SEMI_COLON;

/**
 * Created by victor on 13.01.2017.
 */
public class Constants {

    //default values
    public static final int DEFAULTTIMEOUT = 0;
    public static final String AUTH_WINDOWS = "Windows";
    public static final String TRIM_ROWSTAT = "trimRowstat";
    public static final String MSSQL_URL = "jdbc:sqlserver://";
    public static final String TRUST_SERVER_CERTIFICATE = "trustServerCertificate";
    public static final String JTDS_JDBC_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
    public static final String ENCRYPT = "encrypt";

    public static final String EQUALS = "=";
    public static final String STRING_PARAMETER = "%s";
    public static final String TRUSTORE_PARAMS = SEMI_COLON + TRUST_STORE + EQUALS + STRING_PARAMETER + SEMI_COLON + TRUST_STORE_PASSWORD + EQUALS + STRING_PARAMETER;
    public static final String COLON = ":";
    public static final String INTEGRATED_SECURITY = "integratedSecurity";

    public static final String TRUE = Boolean.TRUE.toString();
    public static final String EMPTY_DRIVER_PATH_EXCEPTION = "The authDriverPath is empty.";
    public static final String DRIVER_PATH_NOT_ABSOLUTE_EXCEPTION = "The authDriverPath provided is not absolute.";
    public static final String NOT_THE_SHORTEST_PATH_EXCEPTION = "The path provided for the authDriverPath is not a valid one. The path should be the shortest possible, i.e. normalized.";
    public static final String JAVA_LIBRARY_PATH = "java.library.path";
    public static final String CURRENT_DIRECTORY_NOTATION = ".";
    public static final String SYS_PATHS = "sys_paths";
    public static final String NEW_LINE = System.lineSeparator();
    public static final String SYMBOLIC_PATH_EXCEPTION = "The library path provided should be an absolute path not a symbolic link.";
    public static final String INVALID_DIRECTORY_PATH_EXCEPTION = "The provided path does not point to a valid directory.";
    public static final String INVALID_PATH = "The provided path is invalid.";
    public static final String PATH_SEPARATOR = "path.separator";
    public static final String INACCESSIBLE_OR_INEXISTENT_SYS_PATHS_FIELD_EXCEPTION = "Field named 'sys_paths' could not be found or it is not accessible.";
}
