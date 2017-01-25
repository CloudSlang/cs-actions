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
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
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

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBDefaultValues.*;
import static io.cloudslang.content.database.constants.DBInputNames.*;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.constants.DBOutputNames.ROWS_LEFT;
import static io.cloudslang.content.database.constants.DBResponseNames.HAS_MORE;
import static io.cloudslang.content.database.constants.DBResponseNames.NO_MORE;
import static io.cloudslang.content.database.utils.SQLInputsUtils.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;


/**
 * Created by pinteae on 1/11/2017.
 */
public class SQLQueryLOB {

    @Action(name = "SQL Query LOB",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(EXCEPTION),
                    @Output(ROWS_LEFT)
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
                                       @Param(value = DATABASE, required = true) String database,
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

        resultSetType = defaultIfEmpty(resultSetType, TYPE_SCROLL_INSENSITIVE);
        resultSetConcurrency = defaultIfEmpty(resultSetConcurrency, CONCUR_READ_ONLY);

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
        mySqlInputs.setStrDelim(delimiter);
        mySqlInputs.setKey(key);
        mySqlInputs.setTimeout(getOrDefaultTimeout(timeout, DEFAULT_TIMEOUT));
        mySqlInputs.setDatabasePoolingProperties(getOrDefaultDBPoolingProperties(databasePoolingProperties, EMPTY));
        mySqlInputs.setResultSetType(getResultSetType(resultSetType));
        mySqlInputs.setResultSetConcurrency(getResultSetConcurrency(resultSetConcurrency));

        mySqlInputs.setDbUrls(getDbUrls(mySqlInputs.getDbUrl()));

        final List<String> validationIssues = SQLInputsValidator.validateSqlInputs(mySqlInputs);

        Map<String, String> inputParameters = SQLQueryLOBUtil.createInputParametersMap(dbServerName,
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
                databasePoolingProperties);

        inputParameters.put(RESULT_SET_TYPE, resultSetType);
        inputParameters.put(RESULT_SET_CONCURRENCY, resultSetConcurrency);
        Map<String, String> result = new HashMap<>();
        try {
            final SQLInputs sqlInputs = InputsProcessor.handleInputParameters(inputParameters, resultSetType, resultSetConcurrency);
            if (DB2_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())) {
                sqlInputs.setResultSetType(TYPE_VALUES.get(TYPE_FORWARD_ONLY));
            }
            if (StringUtils.isEmpty(sqlInputs.getSqlCommand())) {
                throw new Exception("command input is empty.");
            }
            String aKey = "";
            Map<String, Object> sqlConnectionMap = new HashMap<>();

            //calculate session id for JDBC operations
            if (!StringUtils.isEmpty(sqlInputs.getDbServer())) {
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

                    result.put(Constants.RETURNRESULT, NO_MORE);
                    result.put(ROWS_LEFT, ZERO);
                    result.put(RETURN_CODE, DBReturnCodes.NO_MORE);
                } else if (sqlInputs.getlRowsFiles() == null || sqlInputs.getlRowsFiles().isEmpty() || (sqlInputs.getlRows().size() == sqlInputs.getlRowsFiles().size())) {
                    result.put(Constants.RETURNRESULT, (String) sqlInputs.getlRows().get(0));
                    sqlInputs.getlRows().remove(0);
                    result.put("columnNames", sqlInputs.getStrColumns());
                    result.put(ROWS_LEFT, "" + sqlInputs.getlRows().size());
                    result.put(RETURN_CODE, SUCCESS);
                    sqlConnectionMap.put(aKey, sqlInputs.getlRows());
                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));
                } else {
                    File tmpFile = new File(sqlInputs.getlRowsFiles().get(0).get(0));
                    try {
                        FileInputStream in = new FileInputStream(tmpFile);
                        String resultStr = "";
                        String colName = "CLOB column: " + ((String) ((List) sqlInputs.getlRowsNames().get(0)).get(0));

                        InputStreamReader ir = new InputStreamReader(in, "UTF-8");
                        BufferedReader reader = new BufferedReader(ir);
                        StringBuffer buffer = new StringBuffer();

                        String NL = System.getProperty("line.separator");

                        String line = null;
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
                        if (((List) sqlInputs.getlRowsFiles().get(0)).isEmpty()) {
                            sqlInputs.getlRowsFiles().remove(0);
                            sqlInputs.getlRowsNames().remove(0);
                        }

                        result.put(Constants.RETURNRESULT, resultStr);
                        result.put("columnNames", colName);
                        result.put(ROWS_LEFT, "" + sqlInputs.getlRows().size());
                        result.put(RETURN_CODE, SUCCESS);
                        sqlConnectionMap.put(sqlInputs.getStrKeyFiles(), sqlInputs.getlRowsFiles());
                        sqlConnectionMap.put(sqlInputs.getStrKeyNames(), sqlInputs.getlRowsNames());
                        sqlConnectionMap.put(sqlInputs.getStrKeySkip(), sqlInputs.getSkip());

                        globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));

                    } catch (IOException e) {
//                   todo     result.put(EXCEPTION, StringUtils.toString(e));
                        result.put(ROWS_LEFT, ZERO);
                        result.put(Constants.RETURNRESULT, e.getMessage());
                        result.put(RETURN_CODE, FAILURE);
                    }
                }

            }//globalSessionObject
            else {
                if ("windows".equalsIgnoreCase(sqlInputs.getAuthenticationType()) && !MSSQL_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())) {
                    throw new Exception("Windows authentication can only be used with MSSQL!");
                }
                SQLQueryLobService sqlQueryLobService = new SQLQueryLobService();
                boolean isLOB = sqlQueryLobService.executeSqlQueryLob(sqlInputs);

                if (!sqlInputs.getlRows().isEmpty()) {
                    result.put(Constants.RETURNRESULT, sqlInputs.getlRows().get(0));
                    sqlInputs.getlRows().remove(0);
                    result.put(ROWS_LEFT, "" + sqlInputs.getlRows().size());
                    result.put("columnNames", sqlInputs.getStrColumns());
                    result.put(RETURN_CODE, SUCCESS);
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
                    result.put(Constants.RETURNRESULT, "no rows selected");
                    result.put(ROWS_LEFT, ZERO);
                    result.put("SQLQuery", sqlInputs.getSqlCommand());
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
            result.put(Constants.RETURNRESULT, e.getMessage());
            result.put(RETURN_CODE, FAILURE);
        }

//        result.put("queryCount", String.valueOf(iQueryCount));
        return result;
    }
}
