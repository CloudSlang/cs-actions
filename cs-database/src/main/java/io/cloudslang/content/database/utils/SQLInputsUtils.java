/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.utils;

import io.cloudslang.content.utils.CollectionUtilities;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static io.cloudslang.content.database.constants.DBOtherValues.CONCUR_VALUES;
import static io.cloudslang.content.database.constants.DBOtherValues.TYPE_VALUES;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Created by victor on 18.01.2017.
 */
public class SQLInputsUtils {

    public static List<String> getSqlCommands(final String sqlCommandsStr, final String scriptFileName, final String commandsDelimiter) {
        if (isNoneEmpty(sqlCommandsStr)) {
            return CollectionUtilities.toList(sqlCommandsStr, commandsDelimiter);
        }
        if (isNoneEmpty(scriptFileName)) {
            return SQLUtils.readFromFile(scriptFileName);
        }
        return Collections.emptyList();
    }

    public static List<String> getDbUrls(final String dbUrl) {
        final List<String> dbUrls = new ArrayList<>();
        if (isNoneEmpty(dbUrl)) {
            dbUrls.add(dbUrl);
        }
        return dbUrls;
    }

    public static int getOrDefaultTimeout(final String timeout, final String defaultVal) {
        try {
            return Integer.parseInt(defaultIfEmpty(timeout, defaultVal));
        } catch (Exception e) {
            e.printStackTrace(); //todo
            return -1;
        }
    }

    public static Properties getOrDefaultDBPoolingProperties(final String dbPoolingProperties, final String defaultVal) {
        final Properties databasePoolingProperties = new Properties();
        try (final Reader reader = new StringReader(defaultIfBlank(dbPoolingProperties, defaultVal))) {
            databasePoolingProperties.load(reader);
        } catch (IOException e) {
            e.printStackTrace(); // todo throw runtime
        }
        return databasePoolingProperties;
    }

    public static int getOrDefaultResultSetConcurrency(final String resultSetConcurrency, final String defaultVal) {
        if (SQLInputsValidator.isValidResultSetConcurrency(resultSetConcurrency)) {
            return CONCUR_VALUES.get(resultSetConcurrency);
        } else if (isEmpty(resultSetConcurrency) && SQLInputsValidator.isValidResultSetConcurrency(defaultVal)) {
            return CONCUR_VALUES.get(defaultVal);
        }
        throw new RuntimeException("Invalid resultSetConcurrency input"); // todo custom exception
    }

    public static int getOrDefaultResultSetType(final String resultSetType, final String defaultVal) {
        if (SQLInputsValidator.isValidResultSetType(resultSetType)) {
            return TYPE_VALUES.get(resultSetType);
        } else if (isEmpty(resultSetType) && SQLInputsValidator.isValidResultSetType(defaultVal)) {
            return TYPE_VALUES.get(defaultVal);
        }
        throw new RuntimeException("Invalid resultSetType input"); // todo custom exception
    }

}
