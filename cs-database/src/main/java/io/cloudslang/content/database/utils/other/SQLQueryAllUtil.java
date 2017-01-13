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

import static io.cloudslang.content.database.utils.Constants.*;

/**
 * Created by pinteae on 1/10/2017.
 */
public class SQLQueryAllUtil {
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
                                                               String colDelimiter,
                                                               String rowDelimiter,
                                                               String trustAllRoots,
                                                               String trustStore,
                                                               String trustStorePassword,
                                                               String timeout,
                                                               String databasePoolingProperties) {
        Map<String, String> inputParameters = new HashMap<>();

        inputParameters.put(DBSERVERNAME, dbServerName);
        inputParameters.put(DBTYPE, dbType);
        inputParameters.put(USERNAME, username);
        inputParameters.put(PASSWORD, password);
        inputParameters.put(INSTANCE, instance);
        inputParameters.put(DBPORT, dbPort);
        inputParameters.put(DATABASENAME, database);
        inputParameters.put(AUTH_TYPE, authenticationType);
        inputParameters.put(CUSTOM_DB_CLASS, dbClass);
        inputParameters.put(DBURL, dbURL);
        inputParameters.put(COMMAND, command);
        inputParameters.put("colDelimiter", colDelimiter);
        inputParameters.put("rowDelimiter", rowDelimiter);
        inputParameters.put(TRUST_ALL_ROOTS, trustAllRoots);
        inputParameters.put(TRUST_STORE, trustStore);
        inputParameters.put(TRUST_STORE_PASSWORD, trustStorePassword);
        inputParameters.put(TIMEOUT, timeout);
        inputParameters.put(DATABASE_POOLING_PROPRTIES, databasePoolingProperties);

        return inputParameters;
    }
}
