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
import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.SQLSessionResource;
import io.cloudslang.content.utils.BooleanUtilities;
import io.cloudslang.content.utils.OutputUtilities;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBDefaultValues.*;
import static io.cloudslang.content.database.constants.DBInputNames.*;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.constants.DBOutputNames.*;
import static io.cloudslang.content.database.constants.DBResponseNames.HAS_MORE;
import static io.cloudslang.content.database.constants.DBResponseNames.NO_MORE;
import static io.cloudslang.content.database.utils.SQLInputsUtils.*;
import static io.cloudslang.content.database.utils.SQLInputsValidator.validateSqlQueryInputs;
import static io.cloudslang.content.utils.NumberUtilities.toInteger;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
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
                                       @Param(value = DATABASE_NAME, required = true) String databaseName,
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
                databaseName, authenticationType, command, trustAllRoots, trustStore, trustStorePassword,
                timeout, resultSetType, resultSetConcurrency, ignoreCase);

        if (!preInputsValidation.isEmpty()) {
            return getFailureResultsMap(StringUtils.join(preInputsValidation, NEW_LINE));
        }

        final boolean ignoreCaseBool = BooleanUtilities.toBoolean(ignoreCase);

        SQLInputs sqlInputs = new SQLInputs();
        sqlInputs.setDbServer(dbServerName); //mandatory
        sqlInputs.setDbType(getDbType(dbType));
        sqlInputs.setUsername(username);
        sqlInputs.setPassword(password);
        sqlInputs.setInstance(getOrLower(instance, ignoreCaseBool));
        sqlInputs.setDbPort(getOrDefaultDBPort(dbPort, sqlInputs.getDbType()));
        sqlInputs.setDbName(getOrLower(defaultIfEmpty(databaseName, EMPTY), ignoreCaseBool));
        sqlInputs.setAuthenticationType(authenticationType);
        sqlInputs.setDbClass(defaultIfEmpty(dbClass, EMPTY));
        sqlInputs.setDbUrl(defaultIfEmpty(dbURL, EMPTY));
        sqlInputs.setSqlCommand(command);
        sqlInputs.setTrustAllRoots(BooleanUtilities.toBoolean(trustAllRoots));
        sqlInputs.setTrustStore(trustStore);
        sqlInputs.setTrustStorePassword(trustStorePassword);
        sqlInputs.setStrDelim(delimiter);
        sqlInputs.setKey(key);
        sqlInputs.setTimeout(toInteger(timeout));
        sqlInputs.setDatabasePoolingProperties(getOrDefaultDBPoolingProperties(databasePoolingProperties, EMPTY));
        sqlInputs.setResultSetType(getResultSetTypeForDbType(resultSetType, sqlInputs.getDbType()));
        sqlInputs.setResultSetConcurrency(getResultSetConcurrency(resultSetConcurrency));
        sqlInputs.setIgnoreCase(ignoreCaseBool);

        Map<String, String> result = new HashMap<>();

        try {

            final String aKey = getSqlKey(sqlInputs);

            final Map<String, Object> sqlConnectionMap = new HashMap<>();

            if (globalSessionObject.getResource() != null) {
                sqlInputs.setlRows((List<String>) globalSessionObject.getResource().get().get(aKey));
                sqlInputs.setStrColumns((String) globalSessionObject.getResource().get().get(sqlInputs.getStrKeyCol()));

                if (!sqlInputs.getlRows().isEmpty()) {
                    final String getFirstRow = sqlInputs.getlRows().remove(0);
                    sqlConnectionMap.put(aKey, sqlInputs.getlRows());

                    result = getSuccessResultsMap(getFirstRow);
                    result.put(COLUMN_NAMES, sqlInputs.getStrColumns());
                    result.put(ROWS_LEFT, String.valueOf(sqlInputs.getlRows().size()));

                } else {
                    sqlConnectionMap.put(aKey, null);
                    sqlConnectionMap.put(sqlInputs.getStrKeyCol(), null);
                    result.put(RETURN_RESULT, NO_MORE);
                    result.put(ROWS_LEFT, SUCCESS);
                    result.put(RETURN_CODE, DBReturnCodes.NO_MORE);
                }
                globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));
            } else {

                SQLQueryService.executeSqlQuery(sqlInputs);

                if (!sqlInputs.getlRows().isEmpty()) {
                    final String getFirstRow = sqlInputs.getlRows().remove(0);
                    result.put(RETURN_RESULT, getFirstRow);
                    result.put(COLUMN_NAMES, sqlInputs.getStrColumns());
                    result.put(ROWS_LEFT, "" + sqlInputs.getlRows().size());
                    result.put(RETURN_CODE, SUCCESS);
                    sqlConnectionMap.put(aKey, sqlInputs.getlRows()); //todo check if sqlInputs.getStrKeyCol was used in the old code
                    sqlConnectionMap.put(sqlInputs.getStrKeyCol(), sqlInputs.getStrColumns());
                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));
                } else {
                    result.put(RETURN_RESULT, "no rows selected");
                    result.put(ROWS_LEFT, ZERO);
                    result.put(SQL_QUERY, sqlInputs.getSqlCommand());
                    result.put(RETURN_CODE, DBReturnCodes.NO_MORE);
                }
//                result.put("queryCount", String.valueOf(sqlInputs.getiQuerys()));
            }

        } catch (Exception e) {
            final Map<String, String> failureMap = getFailureResultsMap(e);
            failureMap.put(ROWS_LEFT, ZERO);
            return failureMap;
        }

        return result;
    }
}
