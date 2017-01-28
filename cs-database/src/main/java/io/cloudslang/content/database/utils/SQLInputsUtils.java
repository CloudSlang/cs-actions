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
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.utils.SQLInputsValidator.isValidDbType;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

/**
 * Created by victor on 18.01.2017.
 */
public class SQLInputsUtils {

    public static int getOrDefaultDBPort(final String dbPort, final String dbType) {
        if(isNoneEmpty(dbPort)) {
            return Integer.valueOf(dbPort);
        }
        if (isValidDbType(dbType)) {
            return getPortForDbType(dbType);
        }
        return -1;
    }

    private static int getPortForDbType(final String dbType) {
        for (final String toCheck : DB_PORTS.keySet()) {
            if (toCheck.equalsIgnoreCase(dbType)) {
                return DB_PORTS.get(toCheck);
            }
        }
        return -1;
    }

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

    public static Properties getOrDefaultDBPoolingProperties(final String dbPoolingProperties, final String defaultVal) {
        final Properties databasePoolingProperties = new Properties();
        try (final Reader reader = new StringReader(defaultIfBlank(dbPoolingProperties, defaultVal))) {
            databasePoolingProperties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return databasePoolingProperties;
    }

    public static int getResultSetConcurrency(final String resultSetConcurrency) {
        if (SQLInputsValidator.isValidResultSetConcurrency(resultSetConcurrency)) {
            return CONCUR_VALUES.get(resultSetConcurrency);
        }
        return -1000000;
    }

    public static int getResultSetType(final String resultSetType) {
        if (SQLInputsValidator.isValidResultSetType(resultSetType)) {
            return TYPE_VALUES.get(resultSetType);
        }
        return -1000000;
    }

    public static boolean notInCollectionIgnoreCase(final String toCheck, final Iterable<String> inList) {
        return !inCollectionIgnoreCase(toCheck, inList);
    }

    public static boolean inCollectionIgnoreCase(final String toCheck, final Iterable<String> inList) {
        boolean isPresent = false;
        for (final String element : inList) {
            if (!element.equalsIgnoreCase(toCheck)) {
                isPresent = true;
                break;
            }
        }
        return isPresent;
    }

    public static String getSqlKey(SQLInputs sqlInputs, String sqlDbType, String sqlDbServer, String sqlCommand, String sqlUsername,
                                   String sqlAuthenticationType, String sqlDbPort, String sqlKey, String sqlTnsEntry, String sqlTnsPath,
                                   String sqlPassword, boolean sqlIgnoreCase) throws SQLException {
        String aKey;
        if (sqlIgnoreCase) {
            //calculate session id for JDBC operations
            if (StringUtils.isNoneEmpty(sqlDbServer)) {
                if (sqlInputs.getInstance() != null) {
                    sqlInputs.setInstance(sqlInputs.getInstance().toLowerCase());
                }
                if (sqlInputs.getDbName() != null) {
                    sqlInputs.setDbName(sqlInputs.getDbName().toLowerCase());
                }
                aKey = SQLUtils.computeSessionId(sqlDbServer.toLowerCase() + sqlDbType.toLowerCase() +
                        sqlUsername + sqlPassword + sqlInputs.getInstance() + sqlDbPort + sqlInputs.getDbName() +
                        sqlAuthenticationType.toLowerCase() + sqlCommand.toLowerCase() + sqlKey);
            } else { //calculate session id for Oracle operations
                if (StringUtils.isEmpty(sqlTnsPath)) {
                    throw new SQLException("Empty TNSPath for Oracle. ");
                }

                if (StringUtils.isEmpty(sqlTnsEntry)) {
                    throw new SQLException("Empty TNSEntry for Oracle. ");
                }
                aKey = SQLUtils.computeSessionId(sqlTnsPath.toLowerCase() +
                        sqlTnsEntry.toLowerCase() + sqlUsername + sqlPassword + sqlCommand.toLowerCase() + sqlKey);
            }
        } else {
            //calculate session id for JDBC operations
            if (!StringUtils.isEmpty(sqlDbServer)) {
                if (sqlInputs.getInstance() != null) {
                    sqlInputs.setInstance(sqlInputs.getInstance());
                }
                if (sqlInputs.getDbName() != null) {
                    sqlInputs.setDbName(sqlInputs.getDbName());
                }
                aKey = SQLUtils.computeSessionId(sqlDbServer + sqlDbType +
                        sqlUsername + sqlPassword + sqlInputs.getInstance() + sqlDbPort + sqlInputs.getDbName() +
                        sqlAuthenticationType + sqlCommand + sqlKey);
            }
            //calculate session id for Oracle operations
            else {
                if (StringUtils.isEmpty(sqlTnsPath)) {
                    throw new SQLException("Empty TNSPath for Oracle. ");
                }

                if (StringUtils.isEmpty(sqlTnsEntry)) {
                    throw new SQLException("Empty TNSEntry for Oracle. ");
                }
                aKey = SQLUtils.computeSessionId(sqlTnsPath +
                        sqlTnsEntry + sqlUsername + sqlPassword + sqlCommand + sqlKey);
            }
        }
        return aKey;
    }
}
