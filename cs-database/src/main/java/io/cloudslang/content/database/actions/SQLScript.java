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
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.database.services.SQLScriptService;
import io.cloudslang.content.database.utils.SQLInputs;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;
import static io.cloudslang.content.database.constants.DBDefaultValues.NEW_LINE;
import static io.cloudslang.content.database.constants.DBExceptionValues.NO_SQL_COMMAND;
import static io.cloudslang.content.database.constants.DBInputNames.*;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.constants.DBOutputNames.UPDATE_COUNT;
import static io.cloudslang.content.database.utils.SQLInputsUtils.*;
import static io.cloudslang.content.database.utils.SQLInputsValidator.validateSqlScriptInputs;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by pinteae on 1/11/2017.
 */
public class SQLScript {

    /**
     * @param dbServerName              The hostname or ip address of the database server.
     * @param dbType                    The type of database to connect to.
     *                                  Valid values: Oracle, MSSQL, Sybase, Netcool, DB2, PostgreSQL and Custom.
     * @param username                  The username to use when connecting to the database.
     * @param password                  The password to use when connecting to the database.
     * @param instance                  The name instance (for MSSQL Server). Leave it blank for default instance.
     * @param dbPort                    The port to connect to.
     *                                  Default values: Oracle: 1521, MSSQL: 1433, Sybase: 5000, Netcool: 4100, DB2: 50000, PostgreSQL: 5432.
     * @param databaseName              The name of the database.
     * @param authenticationType        The type of authentication used to access the database (applicable only to MSSQL type).
     *                                  Default: sql
     *                                  Values: sql
     *                                  Note: currently, the only valid value is sql, more are planed
     * @param dbClass                   The classname of the JDBC driver to use.
     * @param dbURL                     The url required to load up the driver and make your connection.
     * @param delimiter                 The delimiter to use <sqlCommands>
     * @param sqlCommands               All the SQL commands that you want to run using the <delimiter>
     * @param scriptFileName            SQL script file name. The command in the file need to have ';' to indicate the end of the command
     *                                  Note: this is mutual exclusive with <sqlCommands>
     * @param databasePoolingProperties Properties for database pooling configuration. Pooling is disabled by default.
     *                                  Default: db.pooling.enable=false
     *                                  Example: db.pooling.enable=true
     * @param resultSetType             The result set type. See JDBC folder description for more details.
     *                                  Valid values: TYPE_FORWARD_ONLY, TYPE_SCROLL_INSENSITIVE,TYPE_SCROLL_SENSITIVE.
     *                                  Default value: TYPE_FORWARD_ONLY
     * @param resultSetConcurrency      The result set concurrency. See JDBC folder description for more details.
     *                                  Valid values: CONCUR_READ_ONLY, CONCUR_UPDATABLE
     *                                  Default value: CONCUR_READ_ONLY
     * @return Returns a success or failure message.
     */
    @Action(name = "SQL Script",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION),
                    @Output(UPDATE_COUNT),
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = DB_SERVER_NAME, required = true) String dbServerName,
                                       @Param(value = DB_TYPE) String dbType,
                                       @Param(value = USERNAME, required = true) String username,
                                       @Param(value = PASSWORD, required = true, encrypted = true) String password,
                                       @Param(value = INSTANCE) String instance,
                                       @Param(value = DB_PORT) String dbPort,
                                       @Param(value = DATABASE_NAME, required = true) String databaseName,
                                       @Param(value = AUTHENTICATION_TYPE) String authenticationType,
                                       @Param(value = DB_CLASS) String dbClass,
                                       @Param(value = DB_URL) String dbURL,
                                       @Param(value = DELIMITER) String delimiter,
                                       @Param(value = SQL_COMMANDS) String sqlCommands,
                                       @Param(value = SCRIPT_FILE_NAME) String scriptFileName,
//                                       @Param(value = TRUST_ALL_ROOTS) String trustAllRoots,
//                                       @Param(value = TRUST_STORE) String trustStore,
//                                       @Param(value = TRUST_STORE_PASSWORD) String trustStorePassword,
                                       @Param(value = DATABASE_POOLING_PROPERTIES) String databasePoolingProperties,
                                       @Param(value = RESULT_SET_TYPE) String resultSetType,
                                       @Param(value = RESULT_SET_CONCURRENCY) String resultSetConcurrency) {

        dbType = defaultIfEmpty(dbType, ORACLE_DB_TYPE);
        instance = defaultIfEmpty(instance, EMPTY);
        authenticationType = defaultIfEmpty(authenticationType, AUTH_SQL);
//        trustAllRoots = defaultIfEmpty(trustAllRoots, FALSE);
//        trustStore = defaultIfEmpty(trustStore, EMPTY);
//        trustStorePassword = defaultIfEmpty(trustStorePassword, EMPTY);

        resultSetType = defaultIfEmpty(resultSetType, TYPE_SCROLL_INSENSITIVE);
        resultSetConcurrency = defaultIfEmpty(resultSetConcurrency, CONCUR_READ_ONLY);

        final List<String> preInputsValidation = validateSqlScriptInputs(dbServerName, dbType, username, password, instance, dbPort,
                databaseName, authenticationType, sqlCommands, scriptFileName, /*trustAllRoots, trustStore, trustStorePassword,*/
                resultSetType, resultSetConcurrency);
        if (!preInputsValidation.isEmpty()) {
            return getFailureResultsMap(StringUtils.join(preInputsValidation, NEW_LINE));
        }

        dbType = getDbType(dbType);
        delimiter = defaultIfEmpty(delimiter, SEMI_COLON);
        final SQLInputs sqlInputs = SQLInputs.builder()
                .dbServer(dbServerName)
                .dbType(dbType)
                .username(username)
                .password(password)
                .instance(instance)
                .dbPort(getOrDefaultDBPort(dbPort, dbType))
                .dbName(defaultIfEmpty(databaseName, EMPTY))
                .authenticationType(authenticationType)
                .dbClass(getOrDefaultDBClass(dbClass, dbType))
                .dbUrl(defaultIfEmpty(dbURL, EMPTY))
                .strDelim(delimiter)
                .sqlCommands(getSqlCommands(sqlCommands, scriptFileName, delimiter))
//                .trustAllRoots(BooleanUtilities.toBoolean(trustAllRoots))
//                .trustStore(trustStore)
//                .trustStorePassword(trustStorePassword)
                .databasePoolingProperties(getOrDefaultDBPoolingProperties(databasePoolingProperties, EMPTY))
                .resultSetType(getResultSetType(resultSetType))
                .resultSetConcurrency(getResultSetConcurrency(resultSetConcurrency))
                .isNetcool(checkIsNetcool(dbType))
                .build();

        try {
            final List<String> commands = sqlInputs.getSqlCommands();
            if (!commands.isEmpty()) {
                final String res = SQLScriptService.executeSqlScript(commands, sqlInputs);
                final Map<String, String> result = getSuccessResultsMap(res);
                result.put(UPDATE_COUNT, String.valueOf(sqlInputs.getIUpdateCount()));
                return result;
            }
            return getFailureResultsMap(NO_SQL_COMMAND);
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
