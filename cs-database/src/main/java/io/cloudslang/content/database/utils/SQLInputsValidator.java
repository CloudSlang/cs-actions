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

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

import static io.cloudslang.content.database.constants.DBOtherValues.CONCUR_VALUES;
import static io.cloudslang.content.database.constants.DBOtherValues.TYPE_VALUES;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by victor on 18.01.2017.
 */
public class SQLInputsValidator {

    public static int getOrDefaultTimeout(final String timeout, final String defaultVal) {
        return -1;
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
        if (isValidResultSetConcurrency(resultSetConcurrency)) {
            return CONCUR_VALUES.get(resultSetConcurrency);
        } else if (isEmpty(resultSetConcurrency) && isValidResultSetConcurrency(defaultVal)) {
            return CONCUR_VALUES.get(defaultVal);
        }
        throw new RuntimeException("Invalid resultSetConcurrency input"); // todo custom exception
    }

    public static int getOrDefaultResultSetType(final String resultSetType, final String defaultVal) {
        if (isValidResultSetType(resultSetType)) {
            return TYPE_VALUES.get(resultSetType);
        } else if (isEmpty(resultSetType) && isValidResultSetType(defaultVal)) {
            return TYPE_VALUES.get(defaultVal);
        }
        throw new RuntimeException("Invalid resultSetType input"); // todo custom exception
    }

    public static boolean isValidResultSetConcurrency(final String resultSetConcurrency) {
        return CONCUR_VALUES.containsKey(resultSetConcurrency);
    }

    public static boolean isValidResultSetType(final String resultSetType) {
        return TYPE_VALUES.containsKey(resultSetType);
    }


}
