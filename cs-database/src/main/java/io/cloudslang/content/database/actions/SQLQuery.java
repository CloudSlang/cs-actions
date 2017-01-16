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
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.database.services.SQLQueryService;
import io.cloudslang.content.database.utils.*;
import io.cloudslang.content.database.utils.other.SQLQueryUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.database.utils.Constants.RESULT_SET_CONCURRENCY;
import static io.cloudslang.content.database.utils.Constants.RESULT_SET_TYPE;

/**
 * Created by pinteae on 1/5/2017.
 */
public class SQLQuery {

    @Action(name = "SQL Query",
            outputs = {
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.EXCEPTION),
                    @Output("rowsLeft")
            },
            responses = {
                    @Response(text = "has more", field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = "no more", field = OutputNames.RETURN_CODE, value = "1", matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = "dbServerName", required = true) String dbServerName,
                                       @Param(value = "dbType") String dbType,
                                       @Param(value = "username", required = true) String username,
                                       @Param(value = "password", required = true, encrypted = true) String password,
                                       @Param(value = "instance") String instance,
                                       @Param(value = "DBPort") String dbPort,
                                       @Param(value = "database", required = true) String database,
                                       @Param(value = "authenticationType") String authenticationType,
                                       @Param(value = "dbClass") String dbClass,
                                       @Param(value = "dbURL") String dbURL,
                                       @Param(value = "command", required = true) String command,
                                       @Param(value = "trustAllRoots") String trustAllRoots,
                                       @Param(value = "trustStore") String trustStore,
                                       @Param(value = "trustStorePassword") String trustStorePassword,
                                       @Param(value = "delimiter", required = true) String delimiter,
                                       @Param(value = "key", required = true) String key,
                                       @Param(value = "timeout") String timeout,
                                       @Param(value = "databasePoolingProperties") String databasePoolingProperties,
                                       @Param(value = "resultSetType") String resultSetType,
                                       @Param(value = "resultSetConcurrency") String resultSetConcurrency,
                                       @Param(value = "ignoreCase") String ignoreCase,
                                       GlobalSessionObject<Map<String, Object>> globalSessionObject) {

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
//            OOResultSet resultSetType = OOResultSet.TYPE_SCROLL_INSENSITIVE;
//            OOResultSet resultSetConcurrency = OOResultSet.CONCUR_READ_ONLY;
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
                sqlInputs.setResultSetType(OOResultSet.TYPE_FORWARD_ONLY.toString());
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
                }
                //calculate session id for Oracle operations
                else {
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
                sqlInputs.setlRows((ArrayList<String>) globalSessionObject.getResource().get().get(aKey));
                sqlInputs.setStrColumns((String) globalSessionObject.getResource().get().get(sqlInputs.getStrKeyCol()));

                if (!sqlInputs.getlRows().isEmpty()) {
                    result.put(Constants.RETURNRESULT, sqlInputs.getlRows().get(0));
                    sqlInputs.getlRows().remove(0);
                    result.put("columnNames", sqlInputs.getStrColumns());
                    result.put("rowsLeft", "" + sqlInputs.getlRows().size());
                    result.put("returnCode", "0");

                    sqlConnectionMap.put(aKey, sqlInputs.getlRows());
                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));

                } else {
                    sqlConnectionMap.put(aKey, null);
                    sqlConnectionMap.put(sqlInputs.getStrKeyCol(), null);
                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));
                    result.put(Constants.RETURNRESULT, "no more");
                    result.put("rowsLeft", "0");
                    result.put("returnCode", "1");
                }
            } else {

                SQLQueryService sqlQueryService = new SQLQueryService();
                sqlQueryService.executeSqlQuery(sqlInputs);

                if (!sqlInputs.getlRows().isEmpty()) {
                    result.put(Constants.RETURNRESULT, sqlInputs.getlRows().get(0));
                    sqlInputs.getlRows().remove(0);
                    result.put("columnNames", sqlInputs.getStrColumns());
                    result.put("rowsLeft", "" + sqlInputs.getlRows().size());
                    result.put("returnCode", "0");
                    sqlConnectionMap.put(aKey, sqlInputs.getlRows());
                    sqlConnectionMap.put(sqlInputs.getStrKeyCol(), sqlInputs.getStrColumns());
                    globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));
                } else {
                    result.put(Constants.RETURNRESULT, "no rows selected");
                    result.put("rowsLeft", "0");
                    result.put("sqlQuery", sqlCommand);
                    result.put("returnCode", "1");
                }
//                result.put("queryCount", String.valueOf(sqlInputs.getiQuerys()));
            }
        } catch (Exception e) {
            if (e instanceof SQLException)
                result.put("exception", SQLUtils.toString((SQLException) e));
            else
//            todo    result.put("exception", StringUtils.toString(e));
                result.put("rowsLeft", "0");
            result.put(Constants.RETURNRESULT, e.getMessage());
            result.put("returnCode", "-1");
        }

        return result;
    }
}
