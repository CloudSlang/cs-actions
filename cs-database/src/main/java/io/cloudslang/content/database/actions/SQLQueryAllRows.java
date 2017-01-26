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
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.database.services.SQLQueryAllRowsService;
import io.cloudslang.content.database.utils.InputsProcessor;
import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.SQLUtils;
import io.cloudslang.content.database.utils.other.SQLQueryAllUtil;
import io.cloudslang.content.utils.BooleanUtilities;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OtherValues.COMMA_DELIMITER;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBDefaultValues.*;
import static io.cloudslang.content.database.constants.DBInputNames.*;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.utils.SQLInputsUtils.*;
import static io.cloudslang.content.database.utils.SQLInputsValidator.validateSqlQueryAllRowsInputs;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Created by pinteae on 1/10/2017.
 */
public class SQLQueryAllRows {

    @Action(name = "SQL Query All Rows",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION)
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
                                       @Param(value = DATABASE_NAME, required = true) String database,
                                       @Param(value = AUTHENTICATION_TYPE) String authenticationType,
                                       @Param(value = DB_CLASS) String dbClass,
                                       @Param(value = DB_URL) String dbURL,
                                       @Param(value = COMMAND, required = true) String command,
                                       @Param(value = TRUST_ALL_ROOTS) String trustAllRoots,
                                       @Param(value = TRUST_STORE) String trustStore,
                                       @Param(value = TRUST_STORE_PASSWORD) String trustStorePassword,
                                       @Param(value = COL_DELIMITER) String colDelimiter,
                                       @Param(value = ROW_DELIMITER) String rowDelimiter,
                                       @Param(value = TIMEOUT) String timeout,
                                       @Param(value = DATABASE_POOLING_PROPERTIES) String databasePoolingProperties,
                                       @Param(value = RESULT_SET_TYPE) String resultSetType,
                                       @Param(value = RESULT_SET_CONCURRENCY) String resultSetConcurrency) {
        dbType = defaultIfEmpty(dbType, ORACLE_DB_TYPE);
        instance = defaultIfEmpty(instance, EMPTY);
        authenticationType = defaultIfEmpty(authenticationType, AUTH_SQL);
        trustAllRoots = defaultIfEmpty(trustAllRoots, FALSE);
        trustStore = defaultIfEmpty(trustStore, EMPTY);
        trustStorePassword = defaultIfEmpty(trustStorePassword, EMPTY);
        timeout = defaultIfEmpty(timeout, DEFAULT_TIMEOUT);

        resultSetType = defaultIfEmpty(resultSetType, TYPE_SCROLL_INSENSITIVE);
        resultSetConcurrency = defaultIfEmpty(resultSetConcurrency, CONCUR_READ_ONLY);

        final List<String> preInputsValidation = validateSqlQueryAllRowsInputs(dbServerName, dbType, username, password, instance,
                dbPort, database, authenticationType, dbClass, dbURL, command, trustAllRoots, trustStore, trustStorePassword, colDelimiter,
                rowDelimiter, timeout, databasePoolingProperties, resultSetType, resultSetConcurrency);

        SQLInputs mySqlInputs = new SQLInputs();
        mySqlInputs.setDbServer(dbServerName); //mandatory
        mySqlInputs.setDbType(defaultIfEmpty(dbType, ORACLE_DB_TYPE));
        mySqlInputs.setUsername(username);
        mySqlInputs.setPassword(password);
        mySqlInputs.setInstance(defaultIfEmpty(instance, EMPTY));
        mySqlInputs.setDbPort(getOrDefaultDBPort(dbPort, mySqlInputs.getDbType()));
        mySqlInputs.setDbName(database);
        mySqlInputs.setAuthenticationType(defaultIfEmpty(authenticationType, AUTH_SQL));
        mySqlInputs.setDbClass(defaultIfEmpty(dbClass, EMPTY));
        mySqlInputs.setDbUrl(defaultIfEmpty(dbURL, EMPTY));
        mySqlInputs.setSqlCommand(command);
        mySqlInputs.setTrustAllRoots(BooleanUtilities.toBoolean(trustAllRoots, DEFAULT_TRUST_ALL_ROOTS));
        mySqlInputs.setTrustStore(defaultIfEmpty(trustStore, EMPTY));
        mySqlInputs.setTrustStorePassword(defaultIfEmpty(trustStorePassword, EMPTY));
        mySqlInputs.setColDelimiter(defaultIfEmpty(colDelimiter, COMMA_DELIMITER));
        mySqlInputs.setRowDelimiter(defaultIfEmpty(rowDelimiter, NEW_LINE));
        mySqlInputs.setTimeout(getOrDefaultTimeout(timeout, DEFAULT_TIMEOUT));
        mySqlInputs.setDatabasePoolingProperties(getOrDefaultDBPoolingProperties(databasePoolingProperties, EMPTY));
        mySqlInputs.setResultSetType(getResultSetType(resultSetType));
        mySqlInputs.setResultSetConcurrency(getResultSetConcurrency(resultSetConcurrency));
        mySqlInputs.setDbUrls(getDbUrls(mySqlInputs.getDbUrl()));

        Map<String, String> inputParameters = SQLQueryAllUtil.createInputParametersMap(dbServerName,
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
                colDelimiter,
                rowDelimiter,
                trustAllRoots,
                trustStore,
                trustStorePassword,
                timeout,
                databasePoolingProperties);

        inputParameters.put(RESULT_SET_TYPE, resultSetType);
        inputParameters.put(RESULT_SET_CONCURRENCY, resultSetConcurrency);
        Map<String, String> result = new HashMap<>();
        try {
            final SQLInputs sqlInputs = InputsProcessor.handleInputParameters(inputParameters, resultSetType, resultSetConcurrency);
            if (DB2_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())) {
                sqlInputs.setResultSetType(TYPE_VALUES.get(TYPE_FORWARD_ONLY));
            }
            if (isEmpty(sqlInputs.getSqlCommand())) {
                throw new Exception("command input is empty.");
            }
            // String colDelimiter = StringUtils.resolveString(actionRequest, COL_DELIMITER);
            // String rowDelimiter = StringUtils.resolveString(actionRequest, ROW_DELIMITER);

            SQLQueryAllRowsService sqlQueryAllRowsService = new SQLQueryAllRowsService();
            String queryResult = sqlQueryAllRowsService.execQueryAllRows(sqlInputs, mySqlInputs.getColDelimiter(), mySqlInputs.getRowDelimiter());
            result.put(RETURN_RESULT, queryResult);
            result.put(RETURN_CODE, SUCCESS);
        } catch (Exception e) {
            if (e instanceof SQLException)
                result.put(EXCEPTION, SQLUtils.toString((SQLException) e));
//            else
//         todo       result.put(EXCEPTION, StringUtils.toString(e));
                result.put(RETURN_RESULT, e.getMessage());
            result.put(RETURN_CODE, FAILURE);
        }
        return result;
    }
}
