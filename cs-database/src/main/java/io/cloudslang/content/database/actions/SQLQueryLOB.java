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
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.database.constants.DBReturnCodes;
import io.cloudslang.content.database.services.SQLQueryLobService;
import io.cloudslang.content.database.utils.*;
import io.cloudslang.content.database.utils.other.SQLQueryLOBUtil;
import io.cloudslang.content.utils.BooleanUtilities;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.database.constants.DBDefaultValues.*;
import static io.cloudslang.content.database.constants.DBInputNames.*;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.constants.DBOutputNames.*;
import static io.cloudslang.content.database.constants.DBResponseNames.HAS_MORE;
import static io.cloudslang.content.database.constants.DBResponseNames.NO_MORE;
import static io.cloudslang.content.database.utils.SQLInputsUtils.*;
import static io.cloudslang.content.database.utils.SQLInputsValidator.validateSqlQueryLOBInputs;
import static io.cloudslang.content.utils.NumberUtilities.toInteger;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;


/**
 * Created by pinteae on 1/11/2017.
 */
public class SQLQueryLOB {

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
                    @Response(text = HAS_MORE, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = NO_MORE, field = RETURN_CODE, value = DBReturnCodes.NO_MORE, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, isOnFail = true)
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

        final List<String> preInputsValidation = validateSqlQueryLOBInputs(dbServerName, dbType, username, password, instance, dbPort,
                databaseName, authenticationType, command, trustAllRoots, trustStore, trustStorePassword,
                timeout, resultSetType, resultSetConcurrency);

        if (!preInputsValidation.isEmpty()) {
            return getFailureResultsMap(StringUtils.join(preInputsValidation, NEW_LINE));
        }
        SQLInputs mySqlInputs = new SQLInputs();
        mySqlInputs.setDbServer(dbServerName); //mandatory
        mySqlInputs.setDbType(dbType);
        mySqlInputs.setUsername(username);
        mySqlInputs.setPassword(password);
        mySqlInputs.setInstance(instance);
        mySqlInputs.setDbPort(getOrDefaultDBPort(dbPort, mySqlInputs.getDbType()));
        mySqlInputs.setDbName(getOrDefaultDBName(databaseName, mySqlInputs.getDbType()));
        mySqlInputs.setAuthenticationType(authenticationType);
        mySqlInputs.setDbClass(defaultIfEmpty(dbClass, EMPTY));
        mySqlInputs.setDbUrl(defaultIfEmpty(dbURL, EMPTY));
        mySqlInputs.setSqlCommand(command);
        mySqlInputs.setTrustAllRoots(BooleanUtilities.toBoolean(trustAllRoots));
        mySqlInputs.setTrustStore(trustStore);
        mySqlInputs.setTrustStorePassword(defaultIfEmpty(trustStorePassword, EMPTY));
        mySqlInputs.setStrDelim(delimiter);
        mySqlInputs.setKey(key);
        mySqlInputs.setTimeout(toInteger(timeout));
        mySqlInputs.setDatabasePoolingProperties(getOrDefaultDBPoolingProperties(databasePoolingProperties, EMPTY));
        mySqlInputs.setResultSetType(getResultSetTypeForDbType(resultSetType, mySqlInputs.getDbType()));
        mySqlInputs.setResultSetConcurrency(getResultSetConcurrency(resultSetConcurrency));
        mySqlInputs.setDbUrls(getDbUrls(mySqlInputs.getDbUrl()));

        Map<String, String> inputParameters = SQLQueryLOBUtil.createInputParametersMap(dbServerName,
                dbType,
                username,
                password,
                instance,
                dbPort,
                databaseName,
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
                databasePoolingProperties);

        inputParameters.put(RESULT_SET_TYPE, resultSetType);
        inputParameters.put(RESULT_SET_CONCURRENCY, resultSetConcurrency);
        Map<String, String> result = new HashMap<>();
        try {
            final SQLInputs sqlInputs = InputsProcessor.handleInputParameters(inputParameters, resultSetType, resultSetConcurrency);
//            if (DB2_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())) {
//                sqlInputs.setResultSetType(TYPE_VALUES.get(TYPE_FORWARD_ONLY));
//            }
            String aKey = "";
            Map<String, Object> sqlConnectionMap = new HashMap<>();

            //calculate session id for JDBC operations
            if (StringUtils.isNoneEmpty(sqlInputs.getDbServer())) {
                if (sqlInputs.getInstance() != null) {
                    sqlInputs.setInstance(sqlInputs.getInstance().toLowerCase());
                }
                if (sqlInputs.getDbName() != null) {
                    sqlInputs.setDbName(sqlInputs.getDbName().toLowerCase());
                }
                aKey = SQLUtils.computeSessionId(sqlInputs.getDbServer().toLowerCase() + sqlInputs.getDbType().toLowerCase() +
                        sqlInputs.getUsername() + sqlInputs.getPassword() + sqlInputs.getInstance() + sqlInputs.getDbPort() + sqlInputs.getDbName() +
                        sqlInputs.getAuthenticationType().toLowerCase() + sqlInputs.getSqlCommand().toLowerCase() + sqlInputs.getKey());
            }
            //calculate session id for Oracle operations
            else {
//          todo      aKey = SQLUtils.computeSessionId(sqlInputs.getTnsPath().toLowerCase() +
//                        sqlInputs.getTnsEntry().toLowerCase() + sqlInputs.getUsername() + sqlInputs.getPassword() + sqlInputs.getSqlCommand().toLowerCase() + sqlInputs.getKey());
            }

            sqlInputs.setStrKeyCol(aKey + " - Columns");
            sqlInputs.setStrKeyFiles(aKey + " - Files");
            sqlInputs.setStrKeyNames(aKey + " - CLOBNames");
            sqlInputs.setStrKeySkip(aKey + " - Skip");
            if (globalSessionObject != null && globalSessionObject.get() != null && globalSessionObject.get().get(aKey) != null) {
                sqlInputs.setlRows((ArrayList) globalSessionObject.get().get(aKey));
                sqlInputs.setStrColumns((String) globalSessionObject.get().get(sqlInputs.getStrKeyCol()));
                if (globalSessionObject.get().get(sqlInputs.getStrKeyFiles()) != null) {
                    sqlInputs.setSkip((Long) globalSessionObject.get().get(sqlInputs.getStrKeySkip()));
                    sqlInputs.setlRowsFiles((ArrayList) globalSessionObject.get().get(sqlInputs.getStrKeyFiles()));
                    sqlInputs.setlRowsNames((ArrayList) globalSessionObject.get().get(sqlInputs.getStrKeyNames()));
                }
                if (sqlInputs.getlRows().isEmpty() && (sqlInputs.getlRowsFiles() == null || sqlInputs.getlRowsFiles().isEmpty())) {

                    sqlConnectionMap.put(aKey, null);
                    sqlConnectionMap.put(sqlInputs.getStrKeyCol(), null);
                    sqlConnectionMap.put(sqlInputs.getStrKeyFiles(), null);
                    sqlConnectionMap.put(sqlInputs.getStrKeyNames(), null);
                    sqlConnectionMap.put(sqlInputs.getStrKeySkip(), 0L);

                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));

                    result.put(RETURN_RESULT, NO_MORE);
                    result.put(ROWS_LEFT, ZERO);
                    result.put(RETURN_CODE, DBReturnCodes.NO_MORE);
                } else if (sqlInputs.getlRowsFiles() == null || sqlInputs.getlRowsFiles().isEmpty() || (sqlInputs.getlRows().size() == sqlInputs.getlRowsFiles().size())) {
                    result.put(RETURN_RESULT, sqlInputs.getlRows().get(0));
                    sqlInputs.getlRows().remove(0);
                    result.put(COLUMN_NAMES, sqlInputs.getStrColumns());
                    result.put(ROWS_LEFT, "" + sqlInputs.getlRows().size());
                    result.put(RETURN_CODE, ReturnCodes.SUCCESS);
                    sqlConnectionMap.put(aKey, sqlInputs.getlRows());
                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));
                } else {
                    File tmpFile = new File(sqlInputs.getlRowsFiles().get(0).get(0));
                    try {
                        FileInputStream in = new FileInputStream(tmpFile);
                        String resultStr;
                        String colName = "CLOB column: " + ((List) sqlInputs.getlRowsNames().get(0)).get(0);

                        InputStreamReader ir = new InputStreamReader(in, "UTF-8");
                        BufferedReader reader = new BufferedReader(ir);
                        StringBuilder buffer = new StringBuilder();

                        String NL = System.getProperty("line.separator");

                        String line;
                        boolean isFirstLine = true;
                        while (reader.ready() && (line = reader.readLine()) != null) {
                            if (isFirstLine) {
                                //skip add NL at the beginning
                                isFirstLine = false;
                            } else {
                                //add NL before adding a line
                                //so don't add at the line of the file
                                //adding line because reader.readLine lost the new line char
                                buffer.append(NL);
                            }

                            buffer.append(line);
                        }

                        resultStr = buffer.toString();
                        reader.close();
                        ir.close();
                        in.close();
                        tmpFile.delete();
                        ((List) sqlInputs.getlRowsFiles().get(0)).remove(0);
                        ((List) sqlInputs.getlRowsNames().get(0)).remove(0);
                        if (sqlInputs.getlRowsFiles().get(0).isEmpty()) {
                            sqlInputs.getlRowsFiles().remove(0);
                            sqlInputs.getlRowsNames().remove(0);
                        }

                        result.put(RETURN_RESULT, resultStr);
                        result.put(COLUMN_NAMES, colName);
                        result.put(ROWS_LEFT, "" + sqlInputs.getlRows().size());
                        result.put(RETURN_CODE, ReturnCodes.SUCCESS);
                        sqlConnectionMap.put(sqlInputs.getStrKeyFiles(), sqlInputs.getlRowsFiles());
                        sqlConnectionMap.put(sqlInputs.getStrKeyNames(), sqlInputs.getlRowsNames());
                        sqlConnectionMap.put(sqlInputs.getStrKeySkip(), sqlInputs.getSkip());

                        globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));

                    } catch (IOException e) {
//                   todo     result.put(EXCEPTION, StringUtils.toString(e));
                        result.put(ROWS_LEFT, ZERO);
                        result.put(RETURN_RESULT, e.getMessage());
                        result.put(RETURN_CODE, ReturnCodes.FAILURE);
                    }
                }

            }//globalSessionObject
            else {
                SQLQueryLobService sqlQueryLobService = new SQLQueryLobService();
                boolean isLOB = sqlQueryLobService.executeSqlQueryLob(sqlInputs);

                if (!sqlInputs.getlRows().isEmpty()) {
                    result.put(RETURN_RESULT, sqlInputs.getlRows().get(0));
                    sqlInputs.getlRows().remove(0);
                    result.put(ROWS_LEFT, "" + sqlInputs.getlRows().size());
                    result.put(COLUMN_NAMES, sqlInputs.getStrColumns());
                    result.put(RETURN_CODE, ReturnCodes.SUCCESS);
                    sqlConnectionMap.put(aKey, sqlInputs.getlRows());
                    sqlConnectionMap.put(sqlInputs.getStrKeyCol(), sqlInputs.getStrColumns());

                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));

                    if (isLOB) {
                        sqlConnectionMap.put(sqlInputs.getStrKeyFiles(), sqlInputs.getlRowsFiles());
                        sqlConnectionMap.put(sqlInputs.getStrKeyNames(), sqlInputs.getlRowsNames());
                        sqlConnectionMap.put(sqlInputs.getStrKeySkip(), 0L);

                        globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));
                    }
                } else {
                    result.put(RETURN_RESULT, "no rows selected");
                    result.put(ROWS_LEFT, ZERO);
                    result.put(SQL_QUERY, sqlInputs.getSqlCommand());
                    result.put(RETURN_CODE, DBReturnCodes.NO_MORE);
                }
//                iQueryCount = sqlInputs.getiQuerys();
//                result.put("queryCount", String.valueOf(iQueryCount));
            }
        } catch (Exception e) {
            if (e instanceof SQLException)
                result.put(EXCEPTION, SQLUtils.toString((SQLException) e));
            else
//            todo    result.put(EXCEPTION, StringUtils.toString(e));
                result.put(ROWS_LEFT, ZERO);
            result.put(RETURN_RESULT, e.getMessage());
            result.put(RETURN_CODE, ReturnCodes.FAILURE);
        }

//        result.put("queryCount", String.valueOf(iQueryCount));
        return result;
    }
}
