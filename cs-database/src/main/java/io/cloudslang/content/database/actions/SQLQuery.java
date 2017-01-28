/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.actions;


import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.database.constants.DBReturnCodes;
import io.cloudslang.content.database.services.SQLQueryService;
import io.cloudslang.content.database.utils.*;
import io.cloudslang.content.database.utils.other.SQLQueryUtil;
import io.cloudslang.content.utils.BooleanUtilities;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;
import static io.cloudslang.content.database.constants.DBDefaultValues.DEFAULT_TIMEOUT;
import static io.cloudslang.content.database.constants.DBDefaultValues.NEW_LINE;
import static io.cloudslang.content.database.constants.DBInputNames.*;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.constants.DBOutputNames.*;
import static io.cloudslang.content.database.constants.DBResponseNames.HAS_MORE;
import static io.cloudslang.content.database.constants.DBResponseNames.NO_MORE;
import static io.cloudslang.content.database.utils.SQLInputsUtils.*;
import static io.cloudslang.content.database.utils.SQLInputsValidator.validateSqlQueryInputs;
import static io.cloudslang.content.utils.NumberUtilities.toInteger;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by pinteae on 1/5/2017.
 */
public class SQLQuery {

    @Action(name = "SQL Query",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION),
                    @Output(ROWS_LEFT),
                    @Output(COLUMN_NAMES),
                    @Output(SQL_QUERY)
            },
            responses = {
                    @Response(text = HAS_MORE, field = RETURN_CODE, value = SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = NO_MORE, field = RETURN_CODE, value = DBReturnCodes.NO_MORE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = DB_SERVER_NAME, required = true) String dbServerName,
                                       @Param(value = DB_TYPE) String dbType,
                                       @Param(value = USERNAME, required = true) String username,
                                       @Param(value = PASSWORD, required = true, encrypted = true) String password,
                                       @Param(value = INSTANCE) String instance,
                                       @Param(value = DB_PORT) String dbPort,
                                       @Param(value = DATABASE_NAME, required = true) String database,
                                       @Param(value = AUTHENTICATION_TYPE) String authenticationType,
                                       @Param(value = DB_CLASS) String dbClass,
                                       @Param(value = DB_URL) String dbURL,
                                       @Param(value = COMMAND, required = true) String command,
                                       @Param(value = TRUST_ALL_ROOTS) String trustAllRoots,
                                       @Param(value = TRUST_STORE) String trustStore,
                                       @Param(value = TRUST_STORE_PASSWORD) String trustStorePassword,
                                       @Param(value = DELIMITER, required = true) String delimiter,
                                       @Param(value = KEY, required = true) String key,
                                       @Param(value = TIMEOUT) String timeout,
                                       @Param(value = DATABASE_POOLING_PROPERTIES) String databasePoolingProperties,
                                       @Param(value = RESULT_SET_TYPE) String resultSetType,
                                       @Param(value = RESULT_SET_CONCURRENCY) String resultSetConcurrency,
                                       @Param(value = IGNORE_CASE) String ignoreCase,
                                       @Param(value = GLOBAL_SESSION_OBJECT) GlobalSessionObject<Map<String, Object>> globalSessionObject) {


        dbType = defaultIfEmpty(dbType, ORACLE_DB_TYPE);
        instance = defaultIfEmpty(instance, EMPTY);
        authenticationType = defaultIfEmpty(authenticationType, AUTH_SQL);
        trustAllRoots = defaultIfEmpty(trustAllRoots, FALSE);
        trustStore = defaultIfEmpty(trustStore, EMPTY);
        trustStorePassword = defaultIfEmpty(trustStorePassword, EMPTY);
        timeout = defaultIfEmpty(timeout, DEFAULT_TIMEOUT);
        resultSetType = defaultIfEmpty(resultSetType, TYPE_SCROLL_INSENSITIVE);
        resultSetConcurrency = defaultIfEmpty(resultSetConcurrency, CONCUR_READ_ONLY);
        ignoreCase = defaultIfEmpty(ignoreCase, TRUE);

        final List<String> preInputsValidation = validateSqlQueryInputs(dbServerName, dbType, username, password, instance, dbPort,
                database, authenticationType, command, trustAllRoots, trustStore, trustStorePassword,
                timeout, resultSetType, resultSetConcurrency, ignoreCase);
        if (preInputsValidation.isEmpty()) {
            return getFailureResultsMap(StringUtils.join(preInputsValidation, NEW_LINE));
        }
        SQLInputs mySqlInputs = new SQLInputs();
        mySqlInputs.setDbServer(dbServerName); //mandatory
        mySqlInputs.setDbType(dbType);
        mySqlInputs.setUsername(username);
        mySqlInputs.setPassword(password);
        mySqlInputs.setInstance(instance);
        mySqlInputs.setDbPort(getOrDefaultDBPort(dbPort, mySqlInputs.getDbType()));
        mySqlInputs.setDbName(database);
        mySqlInputs.setAuthenticationType(authenticationType);
        mySqlInputs.setDbClass(defaultIfEmpty(dbClass, EMPTY));
        mySqlInputs.setDbUrl(defaultIfEmpty(dbURL, EMPTY));
        mySqlInputs.setSqlCommand(command);
        mySqlInputs.setTrustAllRoots(BooleanUtilities.toBoolean(trustAllRoots));
        mySqlInputs.setTrustStore(trustStore);
        mySqlInputs.setTrustStorePassword(trustStorePassword);
        mySqlInputs.setStrDelim(delimiter);
        mySqlInputs.setKey(key);
        mySqlInputs.setTimeout(toInteger(timeout));
        mySqlInputs.setDatabasePoolingProperties(getOrDefaultDBPoolingProperties(databasePoolingProperties, EMPTY));
        mySqlInputs.setResultSetType(getResultSetType(resultSetType));
        mySqlInputs.setResultSetConcurrency(getResultSetConcurrency(resultSetConcurrency));
        mySqlInputs.setIgnoreCase(ignoreCase);
        mySqlInputs.setDbUrls(getDbUrls(mySqlInputs.getDbUrl()));

        Map<String, String> inputParameters = SQLQueryUtil.createInputParametersMap(dbServerName,
                dbType,
                username,
                password,
                instance,
                dbPort,
                database,
                authenticationType,
                dbClass,
                dbURL,
                command,
                delimiter,
                key,
                trustAllRoots,
                trustStore,
                trustStorePassword,
                timeout,
                databasePoolingProperties,
                ignoreCase);

        inputParameters.put(RESULT_SET_TYPE, resultSetType);
        inputParameters.put(RESULT_SET_CONCURRENCY, resultSetConcurrency);
        Map<String, String> result = new HashMap<>();

        try {
            final SQLInputs sqlInputs = InputsProcessor.handleInputParameters(inputParameters, resultSetType, resultSetConcurrency);
            final String sqlDbType = sqlInputs.getDbType();
            final String sqlDbServer = sqlInputs.getDbServer();
            final String sqlCommand = sqlInputs.getSqlCommand();
            final String sqlUsername = sqlInputs.getUsername();
            final String sqlAuthenticationType = sqlInputs.getAuthenticationType();
            final String sqlDbPort = Integer.toString(sqlInputs.getDbPort());
            final String sqlKey = sqlInputs.getKey();
            final String sqlTnsEntry = ""; //todo
            final String sqlTnsPath = ""; //todo
            final String sqlPassword = sqlInputs.getPassword();
            final boolean sqlIgnoreCase = Boolean.parseBoolean(sqlInputs.getIgnoreCase());

//            if (DB2_DB_TYPE.equalsIgnoreCase(sqlDbType)) { //todo this should be documented
//                sqlInputs.setResultSetType(TYPE_VALUES.get(TYPE_FORWARD_ONLY));
//            }

            String aKey = getSqlKey(sqlInputs, sqlDbType, sqlDbServer, sqlCommand, sqlUsername, sqlAuthenticationType, sqlDbPort, sqlKey, sqlTnsEntry, sqlTnsPath, sqlPassword, sqlIgnoreCase);

            Map<String, Object> sqlConnectionMap = new HashMap<>();
            if (globalSessionObject.getResource() != null) {
                sqlInputs.setlRows((List<String>) globalSessionObject.getResource().get().get(aKey));
                sqlInputs.setStrColumns((String) globalSessionObject.getResource().get().get(sqlInputs.getStrKeyCol()));

                if (!sqlInputs.getlRows().isEmpty()) {
                    result.put(RETURN_RESULT, sqlInputs.getlRows().get(0));
                    sqlInputs.getlRows().remove(0);
                    result.put(COLUMN_NAMES, sqlInputs.getStrColumns());
                    result.put(ROWS_LEFT, "" + sqlInputs.getlRows().size());
                    result.put(RETURN_CODE, SUCCESS);

                    sqlConnectionMap.put(aKey, sqlInputs.getlRows());
                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));

                } else {
                    sqlConnectionMap.put(aKey, null);
                    sqlConnectionMap.put(sqlInputs.getStrKeyCol(), null);
                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));
                    result.put(RETURN_RESULT, NO_MORE);
                    result.put(ROWS_LEFT, SUCCESS);
                    result.put(RETURN_CODE, DBReturnCodes.NO_MORE);
                }
            } else {

                SQLQueryService sqlQueryService = new SQLQueryService();
                sqlQueryService.executeSqlQuery(sqlInputs);

                if (!sqlInputs.getlRows().isEmpty()) {
                    result.put(RETURN_RESULT, sqlInputs.getlRows().get(0));
                    sqlInputs.getlRows().remove(0);
                    result.put(COLUMN_NAMES, sqlInputs.getStrColumns());
                    result.put(ROWS_LEFT, "" + sqlInputs.getlRows().size());
                    result.put(RETURN_CODE, SUCCESS);
                    sqlConnectionMap.put(aKey, sqlInputs.getlRows());
                    sqlConnectionMap.put(sqlInputs.getStrKeyCol(), sqlInputs.getStrColumns());
                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));
                } else {
                    result.put(RETURN_RESULT, "no rows selected");
                    result.put(ROWS_LEFT, ZERO);
                    result.put(SQL_QUERY, sqlCommand);
                    result.put(RETURN_CODE, DBReturnCodes.NO_MORE);
                }
//                result.put("queryCount", String.valueOf(sqlInputs.getiQuerys()));
            }

        } catch (Exception e) {
            if (e instanceof SQLException)
                result.put(EXCEPTION, SQLUtils.toString((SQLException) e));
            else
//            todo    result.put(EXCEPTION, StringUtils.toString(e));
                result.put(ROWS_LEFT, SUCCESS);
            result.put(Constants.RETURNRESULT, e.getMessage());
            result.put(RETURN_CODE, FAILURE);
        }

        return result;
    }
}
