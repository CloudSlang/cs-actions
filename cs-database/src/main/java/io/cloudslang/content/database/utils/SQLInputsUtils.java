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
import io.cloudslang.content.database.services.databases.*;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager.DBType;
import io.cloudslang.content.utils.CollectionUtilities;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.ResultSet;
import java.util.*;

import static io.cloudslang.content.database.constants.DBExceptionValues.INVALID_DB_TYPE;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.utils.SQLInputsValidator.isValidDbType;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Created by victor on 18.01.2017.
 */
public class SQLInputsUtils {

    static final Map<String, Integer> CONCUR_VALUES = createConcurValues();
    static final Map<String, Integer> TYPE_VALUES = createTypeValues();
    static final Map<String, Integer> DB_PORTS = createDBPortValues();
    private static final Map<String, Class<? extends SqlDatabase>> dbTypesClass = getTypesOfDatabase();
    private static final Map<String, DBType> dbTypesToEnum = getTypesEnum();



    @NotNull
    public static GlobalSessionObject<Map<String, Object>> getOrDefaultGlobalSessionObj(final GlobalSessionObject<Map<String, Object>> globalSessionObject) {
        if (globalSessionObject != null && globalSessionObject.get() != null) {
            return globalSessionObject;
        }
        final GlobalSessionObject<Map<String, Object>> newGlobalSessionObject = new GlobalSessionObject<>();
        newGlobalSessionObject.setResource(new SQLSessionResource(new HashMap<String, Object>()));
        return newGlobalSessionObject;
    }

    @NotNull
    public static String getOrLower(@NotNull final String aString, final boolean toLower) {
        if (toLower) {
            return aString.toLowerCase();
        }
        return aString;
    }

    public static int getOrDefaultDBPort(final String dbPort, final String dbType) {
        if (isNoneEmpty(dbPort)) {
            return Integer.valueOf(dbPort);
        }
        if (isValidDbType(dbType)) {
            return DB_PORTS.get(dbType);
        }
        return -1;
    }

    @NotNull
    public static List<String> getSqlCommands(final String sqlCommandsStr, final String scriptFileName, final String commandsDelimiter) {
        if (isNoneEmpty(sqlCommandsStr)) {
            return CollectionUtilities.toList(sqlCommandsStr, commandsDelimiter);
        }
        if (isNoneEmpty(scriptFileName)) {
            return SQLUtils.readFromFile(scriptFileName);
        }
        return Collections.emptyList();
    }

    @NotNull
    public static List<String> getDbUrls(final String dbUrl) {
        final List<String> dbUrls = new ArrayList<>();
        if (isNoneEmpty(dbUrl)) {
            dbUrls.add(dbUrl);
        }
        return dbUrls;
    }

    @NotNull
    public static Properties getOrDefaultDBPoolingProperties(final String dbPoolingProperties, final String defaultVal) {
        final Properties databasePoolingProperties = new Properties();
        try (final Reader reader = new StringReader(defaultIfBlank(dbPoolingProperties, defaultVal))) {
            databasePoolingProperties.load(reader);
        } catch (IllegalArgumentException | IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
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

    public static int getResultSetTypeForDbType(final String resultSetType, final String dbType) {
        if (DB2_DB_TYPE.equalsIgnoreCase(dbType)) {
            return TYPE_VALUES.get(TYPE_FORWARD_ONLY);
        }
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
            if (element.equalsIgnoreCase(toCheck)) {
                isPresent = true;
                break;
            }
        }
        return isPresent;
    }

    @NotNull
    public static String getDbType(@NotNull final String dbType) {
        for (final String element : DB_PORTS.keySet()) {
            if (element.equalsIgnoreCase(dbType.trim())) {
                return element;
            }
        }
        return dbType;
    }

    @NotNull
    public static String getSqlKey(@NotNull final SQLInputs sqlInputs) {
        if (sqlInputs.getIgnoreCase()) {
            return SQLUtils.computeSessionId(sqlInputs.getDbServer().toLowerCase() + sqlInputs.getDbType().toLowerCase() +
                    sqlInputs.getUsername() + sqlInputs.getPassword() + sqlInputs.getInstance() + sqlInputs.getDbPort() + sqlInputs.getDbName() +
                    sqlInputs.getAuthenticationType().toLowerCase() + sqlInputs.getSqlCommand().toLowerCase() + sqlInputs.getKey());
        }
        return SQLUtils.computeSessionId(sqlInputs.getDbServer() + sqlInputs.getDbType() +
                sqlInputs.getUsername() + sqlInputs.getPassword() + sqlInputs.getInstance() + sqlInputs.getDbPort() + sqlInputs.getDbName() +
                sqlInputs.getAuthenticationType() + sqlInputs.getSqlCommand() + sqlInputs.getKey());
    }


    @NotNull
    public static SqlDatabase getDbClassForType(@NotNull final String dbType) {
        try {
            return dbTypesClass.get(dbType).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(INVALID_DB_TYPE, e.getCause());
        }
    }

    @NotNull
    public static DBType getDbEnumForType(@NotNull final String dbType) {
        if (dbTypesToEnum.containsKey(dbType)) {
            return dbTypesToEnum.get(dbType);
        }
        throw new RuntimeException(INVALID_DB_TYPE);
    }

    @NotNull
    private static Map<String, Integer> createDBPortValues() {
        final Map<String, Integer> concurValues = new HashMap<>();
        concurValues.put(ORACLE_DB_TYPE, DEFAULT_PORT_ORACLE);
        concurValues.put(MSSQL_DB_TYPE, DEFAULT_PORT_MSSQL);
        concurValues.put(SYBASE_DB_TYPE, DEFAULT_PORT_SYBASE);
        concurValues.put(NETCOOL_DB_TYPE, DEFAULT_PORT_NETCOOL);
        concurValues.put(DB2_DB_TYPE, DEFAULT_PORT_DB2);
        concurValues.put(MYSQL_DB_TYPE, DEFAULT_PORT_MYSQL);
        concurValues.put(POSTGRES_DB_TYPE, DEFAULT_PORT_PSQL);
        concurValues.put(CUSTOM_DB_TYPE, DEFAULT_PORT_CUSTOM);
        return concurValues;
    }

    @NotNull
    private static Map<String, Integer> createConcurValues() {
        final Map<String, Integer> concurValues = new HashMap<>();
        concurValues.put(CONCUR_READ_ONLY, ResultSet.CONCUR_READ_ONLY);
        concurValues.put(CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
        return concurValues;
    }

    @NotNull
    private static Map<String, Integer> createTypeValues() {
        final Map<String, Integer> typeValues = new HashMap<>();
        typeValues.put(TYPE_FORWARD_ONLY, ResultSet.TYPE_FORWARD_ONLY);
        typeValues.put(TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_SCROLL_INSENSITIVE);
        typeValues.put(TYPE_SCROLL_SENSITIVE, ResultSet.TYPE_SCROLL_SENSITIVE);
        return typeValues;
    }

    @NotNull
    private static Map<String, Class<? extends SqlDatabase>> getTypesOfDatabase() {
        final Map<String, Class<? extends SqlDatabase>> dbForType = new HashMap<>();
        dbForType.put(ORACLE_DB_TYPE, OracleDatabase.class);
        dbForType.put(MYSQL_DB_TYPE, MySqlDatabase.class);
        dbForType.put(MSSQL_DB_TYPE, MSSqlDatabase.class);
        dbForType.put(SYBASE_DB_TYPE, SybaseDatabase.class);
        dbForType.put(NETCOOL_DB_TYPE, NetcoolDatabase.class);
        dbForType.put(POSTGRES_DB_TYPE, PostgreSqlDatabase.class);
        dbForType.put(DB2_DB_TYPE, DB2Database.class);
        dbForType.put(CUSTOM_DB_TYPE, CustomDatabase.class);
        return dbForType;
    }

    @NotNull
    private static Map<String, DBType> getTypesEnum() {
        final Map<String, DBType> dbTypes = new HashMap<>();
        dbTypes.put(ORACLE_DB_TYPE, DBType.ORACLE);
        dbTypes.put(MYSQL_DB_TYPE, DBType.MYSQL);
        dbTypes.put(MSSQL_DB_TYPE, DBType.MSSQL);
        dbTypes.put(SYBASE_DB_TYPE, DBType.SYBASE);
        dbTypes.put(NETCOOL_DB_TYPE, DBType.NETCOOL);
        dbTypes.put(POSTGRES_DB_TYPE, DBType.POSTGRESQL);
        dbTypes.put(DB2_DB_TYPE, DBType.DB2);
        dbTypes.put(CUSTOM_DB_TYPE, DBType.CUSTOM);
        return dbTypes;
    }

}
