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
import io.cloudslang.content.constants.BooleanValues;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.database.constants.DBReturnCodes;
import io.cloudslang.content.database.services.SQLQueryService;
import io.cloudslang.content.database.utils.*;
import io.cloudslang.content.database.utils.other.SQLQueryUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;
import static io.cloudslang.content.database.constants.DBDefaultValues.ORACLE_DB_TYPE;
import static io.cloudslang.content.database.constants.DBInputNames.*;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.constants.DBOutputNames.ROWS_LEFT;
import static io.cloudslang.content.database.constants.DBResponseNames.HAS_MORE;
import static io.cloudslang.content.database.constants.DBResponseNames.NO_MORE;
import static io.cloudslang.content.database.utils.SQLInputsValidator.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by pinteae on 1/5/2017.
 */
public class SQLQuery {

    @Action(name = "SQL Query",
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
                                       @Param(value = IGNORE_CASE) String ignoreCase,
                                       @Param(value = GLOBAL_SESSION_OBJECT) GlobalSessionObject<Map<String, Object>> globalSessionObject) {

        SQLInputs mySqlInputs = new SQLInputs();
        mySqlInputs.setDbServer(dbServerName); //mandatory
        mySqlInputs.setDbType(defaultIfEmpty(dbType, ORACLE_DB_TYPE));
        mySqlInputs.setUsername(username);
        mySqlInputs.setPassword(password);
        mySqlInputs.setInstance(defaultIfEmpty(instance, ""));
        mySqlInputs.setDbPort(defaultIfEmpty(dbPort, ""));
        mySqlInputs.setDbName(database);
        mySqlInputs.setAuthenticationType(defaultIfEmpty(authenticationType, AUTH_SQL));
        mySqlInputs.setDbClass(defaultIfEmpty(dbClass, ""));
        mySqlInputs.setDbUrl(defaultIfEmpty(dbURL, ""));
        mySqlInputs.setSqlCommand(command);
        mySqlInputs.setTrustAllRoots(defaultIfEmpty(trustAllRoots, BooleanValues.FALSE));
        mySqlInputs.setTrustStore(defaultIfEmpty(trustStore, ""));
        mySqlInputs.setTrustStorePassword(defaultIfEmpty(trustStorePassword, ""));
        mySqlInputs.setStrDelim(delimiter);
        mySqlInputs.setKey(key);
        mySqlInputs.setTimeout(getOrDefaultTimeout(timeout, "1200"));
        mySqlInputs.setDatabasePoolingProperties(getOrDefaultDBPoolingProperties(databasePoolingProperties, ""));
        mySqlInputs.setResultSetType(getOrDefaultResultSetType(resultSetType, TYPE_SCROLL_INSENSITIVE));
        mySqlInputs.setResultSetConcurrency(getOrDefaultResultSetConcurrency(resultSetConcurrency, CONCUR_READ_ONLY));
        mySqlInputs.setIgnoreCase(defaultIfEmpty(ignoreCase, BooleanValues.TRUE));
//        mySqlInputs.setGlobalSessionObject();

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
            final String sqlDbPort = sqlInputs.getDbPort();
            final String sqlKey = sqlInputs.getKey();
            final String sqlTnsEntry = sqlInputs.getTnsEntry();
            final String sqlTnsPath = sqlInputs.getTnsPath();
            final String sqlPassword = sqlInputs.getPassword();
            final boolean sqlIgnoreCase = Boolean.parseBoolean(sqlInputs.getIgnoreCase());

            if (Constants.DB2_DB_TYPE.equalsIgnoreCase(sqlDbType)) {
                sqlInputs.setResultSetType(TYPE_VALUES.get(TYPE_FORWARD_ONLY));
            }
            if (StringUtils.isEmpty(sqlCommand)) {
                throw new Exception("Command input is empty.");
            }
            String aKey = "";
            Map<String, Object> sqlConnectionMap = new HashMap<>();

            if (sqlIgnoreCase) {
                //calculate session id for JDBC operations
                if (!StringUtils.isEmpty(sqlDbServer)) {
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

            if (globalSessionObject.getResource() != null) {
                sqlInputs.setlRows((List<String>) globalSessionObject.getResource().get().get(aKey));
                sqlInputs.setStrColumns((String) globalSessionObject.getResource().get().get(sqlInputs.getStrKeyCol()));

                if (!sqlInputs.getlRows().isEmpty()) {
                    result.put(Constants.RETURNRESULT, sqlInputs.getlRows().get(0));
                    sqlInputs.getlRows().remove(0);
                    result.put("columnNames", sqlInputs.getStrColumns());
                    result.put(ROWS_LEFT, "" + sqlInputs.getlRows().size());
                    result.put(RETURN_CODE, SUCCESS);

                    sqlConnectionMap.put(aKey, sqlInputs.getlRows());
                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));

                } else {
                    sqlConnectionMap.put(aKey, null);
                    sqlConnectionMap.put(sqlInputs.getStrKeyCol(), null);
                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));
                    result.put(Constants.RETURNRESULT, NO_MORE);
                    result.put(ROWS_LEFT, SUCCESS);
                    result.put(RETURN_CODE, DBReturnCodes.NO_MORE);
                }
            } else {

                SQLQueryService sqlQueryService = new SQLQueryService();
                sqlQueryService.executeSqlQuery(sqlInputs);

                if (!sqlInputs.getlRows().isEmpty()) {
                    result.put(Constants.RETURNRESULT, sqlInputs.getlRows().get(0));
                    sqlInputs.getlRows().remove(0);
                    result.put("columnNames", sqlInputs.getStrColumns());
                    result.put(ROWS_LEFT, "" + sqlInputs.getlRows().size());
                    result.put(RETURN_CODE, SUCCESS);
                    sqlConnectionMap.put(aKey, sqlInputs.getlRows());
                    sqlConnectionMap.put(sqlInputs.getStrKeyCol(), sqlInputs.getStrColumns());
                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));
                } else {
                    result.put(Constants.RETURNRESULT, "no rows selected");
                    result.put(ROWS_LEFT, ZERO);
                    result.put("sqlQuery", sqlCommand);
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
