/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.database.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.database.constants.DBReturnCodes;
import io.cloudslang.content.database.services.SQLQueryLobService;
import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.SQLInputsUtils;
import io.cloudslang.content.database.utils.SQLSessionResource;
import io.cloudslang.content.utils.BooleanUtilities;
import io.cloudslang.content.utils.OutputUtilities;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBDefaultValues.*;
import static io.cloudslang.content.database.constants.DBInputNames.*;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.constants.DBOutputNames.*;
import static io.cloudslang.content.database.constants.DBResponseNames.HAS_MORE;
import static io.cloudslang.content.database.constants.DBResponseNames.NO_MORE;
import static io.cloudslang.content.database.utils.SQLInputsUtils.*;
import static io.cloudslang.content.database.utils.SQLInputsValidator.validateSqlQueryLOBInputs;
import static io.cloudslang.content.database.utils.SQLUtils.getRowsFromGlobalSessionMap;
import static io.cloudslang.content.database.utils.SQLUtils.getStrColumns;
import static io.cloudslang.content.utils.NumberUtilities.toInteger;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;


/**
 * Created by pinteae on 1/11/2017.
 */
public class SQLQueryLOB {

    /**
     * @param dbServerName              The hostname or ip address of the database server.
     * @param dbType                    The type of database to connect to
     *                                  Valid values: Oracle, MSSQL, Sybase, Netcool, DB2, PostgreSQL and Custom.
     *                                  Default value: Oracle
     * @param username                  The username to use when connecting to the server.
     * @param password                  The password to use when connecting to the server.
     * @param instance                  The name instance of MSSQL Server. Leave it blank for default instance.
     *                                  Example: MSSQLSERVER
     * @param dbPort                    The port to connect to.
     *                                  Valid values: Oracle: 1521, MSSQL: 1433, Sybase: 5000, Netcool: 4100, DB2: 50000, PostgreSQL: 5432.
     * @param databaseName              The name of the database to connect to.
     * @param authenticationType        The type of authentication used to access the database (applicable only to MSSQL type).
     *                                  Default: sql
     *                                  Values: sql, windows
     * @param dbClass                   The class name of the JDBC driver to use.
     * @param dbURL                     The URL required to load up the driver and make your connection.
     * @param command                   The SQL query to execute.
     * @param trustAllRoots             Specifies whether to enable weak security over SSL/TSL. A certificate is trusted even if no trusted certification authority issued it.
     *                                  Default value: false
     *                                  Valid values: true, false
     *                                  Note: If trustAllRoots is set to 'false', a trustStore and a trustStorePassword must be provided.
     * @param trustStore                The pathname of the Java TrustStore file. This contains certificates from other parties that you expect to communicate with,
     *                                  or from Certificate Authorities that you trust to identify other parties.
     *                                  If the trustAllRoots input is set to 'true' this input is ignored.
     * @param trustStorePassword        The password associated with the trustStore file.
     * @param authLibraryPath           The path to the folder where sqljdbc_auth.dll is located. This path must be provided when using windows authentication.
     *                                  Note: The sqljdbc_auth.dll can be found inside the sqljdbc driver. The driver can be downloaded from https://www.microsoft.com/en-us/download/details.aspx?id=11774.
     *                                  The downloaded jar should be extracted and the library can be found in the 'auth' folder.
     *                                  The path provided should be the path to the folder where the sqljdbc_auth.dll library is located, not the path to the file itself.
     * @param delimiter                 The delimiter to use between resulted values in "returnResult" and column names in "columnNames".
     * @param key                       The key to help keep multiple query results distinct.
     * @param timeout                   Seconds to wait before timing out the SQL command execution. When the default value is used, there
     *                                  is no limit on the amount of time allowed for a running command to complete.
     *                                  Default values: 0
     * @param databasePoolingProperties Properties for database pooling configuration. Pooling is disabled by default.
     *                                  Default: db.pooling.enable=false
     *                                  Example: db.pooling.enable=true
     * @param resultSetType             the result set type. See JDBC folder description for more details.
     *                                  Valid values: TYPE_FORWARD_ONLY, TYPE_SCROLL_INSENSITIVE,TYPE_SCROLL_SENSITIVE.
     *                                  Default value: TYPE_SCROLL_INSENSITIVE except DB2 which is overridden to TYPE_FORWARD_ONLY
     * @param resultSetConcurrency      the result set concurrency. See JDBC folder description for more details.
     *                                  Valid values: CONCUR_READ_ONLY, CONCUR_UPDATABLE
     *                                  Default value: CONCUR_READ_ONLY
     * @return Returns the data of a row returned from query. It is delimited by <delimiter>.
     */
    @Action(name = "SQL Query LOB",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION),
                    @Output(ROWS_LEFT),
                    @Output(SQL_QUERY),
                    @Output(COLUMN_NAMES)
            },
            responses = {
                    @Response(text = HAS_MORE, field = RETURN_CODE, value = SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = NO_MORE, field = RETURN_CODE, value = DBReturnCodes.NO_MORE, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = DB_SERVER_NAME, required = true) String dbServerName,
                                       @Param(value = DB_TYPE) String dbType,
                                       @Param(value = USERNAME) String username,
                                       @Param(value = PASSWORD, encrypted = true) String password,
                                       @Param(value = INSTANCE) String instance,
                                       @Param(value = DB_PORT) String dbPort,
                                       @Param(value = DATABASE_NAME, required = true) String databaseName,
                                       @Param(value = AUTHENTICATION_TYPE) String authenticationType,
                                       @Param(value = DB_CLASS) String dbClass,
                                       @Param(value = DB_URL) String dbURL,
                                       @Param(value = COMMAND, required = true) String command,
                                       @Param(value = TRUST_ALL_ROOTS) String trustAllRoots,
                                       @Param(value = TRUST_STORE) String trustStore,
                                       @Param(value = TRUST_STORE_PASSWORD) String trustStorePassword,
                                       @Param(value = AUTH_LIBRARY_PATH) String authLibraryPath,
                                       @Param(value = DELIMITER, required = true) String delimiter,
                                       @Param(value = KEY, required = true) String key,
                                       @Param(value = TIMEOUT) String timeout,
                                       @Param(value = DATABASE_POOLING_PROPERTIES) String databasePoolingProperties,
                                       @Param(value = RESULT_SET_TYPE) String resultSetType,
                                       @Param(value = RESULT_SET_CONCURRENCY) String resultSetConcurrency,
                                       @Param(value = GLOBAL_SESSION_OBJECT) GlobalSessionObject<Map<String, Object>> globalSessionObject) {
        dbType = defaultIfEmpty(dbType, ORACLE_DB_TYPE);
        username = defaultIfEmpty(username, EMPTY);
        password = defaultIfEmpty(password, EMPTY);
        instance = defaultIfEmpty(instance, EMPTY);
        authenticationType = defaultIfEmpty(authenticationType, AUTH_SQL);
        trustAllRoots = defaultIfEmpty(trustAllRoots, FALSE);
        trustStore = defaultIfEmpty(trustStore, EMPTY);
        trustStorePassword = defaultIfEmpty(trustStorePassword, EMPTY);
        timeout = defaultIfEmpty(timeout, DEFAULT_TIMEOUT);

        resultSetType = defaultIfEmpty(resultSetType, TYPE_SCROLL_INSENSITIVE);
        resultSetConcurrency = defaultIfEmpty(resultSetConcurrency, CONCUR_READ_ONLY);

        final List<String> preInputsValidation = validateSqlQueryLOBInputs(dbServerName, dbType, username, password, instance, dbPort,
                databaseName, authenticationType, command, trustAllRoots, trustStore, trustStorePassword,
                timeout, resultSetType, resultSetConcurrency, authLibraryPath);

        if (!preInputsValidation.isEmpty()) {
            return getFailureResultsMap(StringUtils.join(preInputsValidation, NEW_LINE));
        }
        dbType = getDbType(dbType);


        final SQLInputs sqlInputs = SQLInputs.builder()
                .dbServer(dbServerName)
                .dbType(dbType)
                .username(username)
                .password(password)
                .instance(getOrLower(instance, true))
                .dbPort(getOrDefaultDBPort(dbPort, dbType))
                .dbName(getOrLower(defaultIfEmpty(databaseName, EMPTY), true))
                .authenticationType(authenticationType)
                .dbClass(getOrDefaultDBClass(dbClass, dbType))
                .dbUrl(defaultIfEmpty(dbURL, EMPTY))
                .sqlCommand(command)
                .trustAllRoots(BooleanUtilities.toBoolean(trustAllRoots))
                .trustStore(trustStore)
                .trustStorePassword(defaultIfEmpty(trustStorePassword, EMPTY))
                .authLibraryPath(authLibraryPath)
                .strDelim(delimiter)
                .key(key)
                .timeout(toInteger(timeout))
                .databasePoolingProperties(getOrDefaultDBPoolingProperties(databasePoolingProperties, EMPTY))
                .resultSetType(getResultSetTypeForDbType(resultSetType, dbType))
                .resultSetConcurrency(getResultSetConcurrency(resultSetConcurrency))
                .ignoreCase(true)
                .isNetcool(checkIsNetcool(dbType))
                .build();

        try {

            final String aKey = SQLInputsUtils.getSqlKey(sqlInputs);

            final String strKeyCol = format(KEY_COLUMNS, aKey);
            final String strKeyFiles = format(KEY_FILES, aKey);
            final String strKeyNames = format(KEY_CLOB_NAMES, aKey);
            final String strKeySkip = format(KEY_SKIP, aKey);

            globalSessionObject = getOrDefaultGlobalSessionObj(globalSessionObject);

            final Map<String, Object> sqlConnectionMap = globalSessionObject.get();

            Map<String, String> result = new HashMap<>();

            if (sqlConnectionMap.containsKey(aKey)) {

                sqlInputs.setLRows(getRowsFromGlobalSessionMap(globalSessionObject, aKey));
                sqlInputs.setStrColumns(getStrColumns(globalSessionObject, strKeyCol));

                if (sqlConnectionMap.get(strKeyFiles) != null) {
                    sqlInputs.setSkip((Long) sqlConnectionMap.get(strKeySkip));
                    sqlInputs.setLRowsFiles((List) sqlConnectionMap.get(strKeyFiles));
                    sqlInputs.setLRowsNames((List) sqlConnectionMap.get(strKeyNames));
                }

                if (sqlInputs.getLRows().isEmpty() && (sqlInputs.getLRowsFiles() == null || sqlInputs.getLRowsFiles().isEmpty())) {

                    sqlConnectionMap.put(aKey, null);
                    sqlConnectionMap.put(strKeyCol, null);
                    sqlConnectionMap.put(strKeyFiles, null);
                    sqlConnectionMap.put(strKeyNames, null);
                    sqlConnectionMap.put(strKeySkip, 0L);

                    result.put(RETURN_RESULT, NO_MORE);
                    result.put(ROWS_LEFT, ZERO);
                    result.put(RETURN_CODE, DBReturnCodes.NO_MORE);
                } else if (sqlInputs.getLRowsFiles() == null || sqlInputs.getLRowsFiles().isEmpty() || (sqlInputs.getLRows().size() == sqlInputs.getLRowsFiles().size())) {
                    final String getFirstRow = sqlInputs.getLRows().remove(0);

                    result.put(RETURN_RESULT, getFirstRow);
                    result.put(COLUMN_NAMES, sqlInputs.getStrColumns());
                    result.put(ROWS_LEFT, String.valueOf(sqlInputs.getLRows().size()));
                    result.put(RETURN_CODE, SUCCESS);
                    sqlConnectionMap.put(aKey, sqlInputs.getLRows());
                } else {
                    final String colName = "CLOB column: " + ((List) sqlInputs.getLRowsNames().get(0)).get(0);
                    final File tmpFile = new File(sqlInputs.getLRowsFiles().get(0).get(0));
                    final String fileContent = FileUtils.readFileToString(tmpFile, StandardCharsets.UTF_8);
                    FileUtils.deleteQuietly(tmpFile);

                    sqlInputs.getLRowsFiles().get(0).remove(0);
                    sqlInputs.getLRowsNames().get(0).remove(0);
                    if (sqlInputs.getLRowsFiles().get(0).isEmpty()) {
                        sqlInputs.getLRowsFiles().remove(0);
                        sqlInputs.getLRowsNames().remove(0);
                    }


                    result.put(RETURN_RESULT, fileContent);
                    result.put(COLUMN_NAMES, colName);
                    result.put(ROWS_LEFT, String.valueOf(sqlInputs.getLRows().size()));
                    result.put(RETURN_CODE, SUCCESS);

                    sqlConnectionMap.put(strKeyFiles, sqlInputs.getLRowsFiles());
                    sqlConnectionMap.put(strKeyNames, sqlInputs.getLRowsNames());
                    sqlConnectionMap.put(strKeySkip, sqlInputs.getSkip());

                }
                globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));

            } else { //globalSessionObject
                boolean isLOB = SQLQueryLobService.executeSqlQueryLob(sqlInputs);

                if (!sqlInputs.getLRows().isEmpty()) {
                    final String getFirstRow = sqlInputs.getLRows().remove(0);

                    result.put(RETURN_RESULT, getFirstRow);
                    result.put(ROWS_LEFT, String.valueOf(sqlInputs.getLRows().size()));
                    result.put(COLUMN_NAMES, sqlInputs.getStrColumns());
                    result.put(RETURN_CODE, SUCCESS);

                    sqlConnectionMap.put(aKey, sqlInputs.getLRows());
                    sqlConnectionMap.put(strKeyCol, sqlInputs.getStrColumns());
                    if (isLOB) {
                        sqlConnectionMap.put(strKeyFiles, sqlInputs.getLRowsFiles());
                        sqlConnectionMap.put(strKeyNames, sqlInputs.getLRowsNames());
                        sqlConnectionMap.put(strKeySkip, 0L);
                    }
                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));
                } else {
                    result.put(SQL_QUERY, sqlInputs.getSqlCommand());
                    result.put(RETURN_RESULT, NO_MORE);
                    result.put(ROWS_LEFT, ZERO);
                    result.put(RETURN_CODE, DBReturnCodes.NO_MORE);
                }
            }
            globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));
            return result;
        } catch (Exception e) {
            final Map<String, String> failureMap = OutputUtilities.getFailureResultsMap(e);
            failureMap.put(ROWS_LEFT, ZERO);
            return failureMap;
        }
    }

}
