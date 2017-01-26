/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.utils.other;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.InputNames.DELIMITER;
import static io.cloudslang.content.database.constants.DBInputNames.*;


/**
 * Created by pinteae on 1/11/2017.
 */
public class SQLQueryLOBUtil {
    public static Map<String, String> createInputParametersMap(String dbServerName,
                                                               String dbType,
                                                               String username,
                                                               String password,
                                                               String instance,
                                                               String dbPort,
                                                               String database,
                                                               String authenticationType,
                                                               String dbClass,
                                                               String dbURL,
                                                               String command,
                                                               String delimiter,
                                                               String key,
                                                               String trustAllRoots,
                                                               String trustStore,
                                                               String trustStorePassword,
                                                               String timeout,
                                                               String databasePoolingProperties) {
        Map<String, String> inputParameters = new HashMap<>();

        inputParameters.put(DB_SERVER_NAME, dbServerName);
        inputParameters.put(DB_TYPE, dbType);
        inputParameters.put(USERNAME, username);
        inputParameters.put(PASSWORD, password);
        inputParameters.put(INSTANCE, instance);
        inputParameters.put(DB_PORT, dbPort);
        inputParameters.put(DATABASE_NAME, database);
        inputParameters.put(AUTHENTICATION_TYPE, authenticationType);
        inputParameters.put(DB_CLASS, dbClass);
        inputParameters.put(DB_URL, dbURL);
        inputParameters.put(COMMAND, command);
        inputParameters.put(DELIMITER, delimiter);
        inputParameters.put(KEY, key);
        inputParameters.put(TRUST_ALL_ROOTS, trustAllRoots);
        inputParameters.put(TRUST_STORE, trustStore);
        inputParameters.put(TRUST_STORE_PASSWORD, trustStorePassword);
        inputParameters.put(TIMEOUT, timeout);
        inputParameters.put(DATABASE_POOLING_PROPERTIES, databasePoolingProperties);

        return inputParameters;
    }
}
