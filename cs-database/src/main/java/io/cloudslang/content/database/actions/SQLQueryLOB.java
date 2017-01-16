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
import io.cloudslang.content.database.services.SQLQueryLobService;
import io.cloudslang.content.database.utils.*;
import io.cloudslang.content.database.utils.other.SQLQueryLOBUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pinteae on 1/11/2017.
 */
public class SQLQueryLOB {

//    private int iQueryCount = 0;

    @Action(name = "SQL Query LOB",
            outputs = {
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.EXCEPTION),
                    @Output("rowsLeft")
            },
            responses = {
                    @Response(text = "has more", field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = "no more", field = OutputNames.RETURN_CODE, value = "1",
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
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
                                       // @Param(value = "resultSetType") String resultSetType,
                                       //@Param(value = "resultSetConcurrency") String resultSetConcurrency,
                                       GlobalSessionObject<Map<String, Object>> globalSessionObject) {
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
        Map<String, String> result = new HashMap<>();
        try {
            OOResultSet resultSetType = OOResultSet.TYPE_SCROLL_INSENSITIVE;
            OOResultSet resultSetConcurrency = OOResultSet.CONCUR_READ_ONLY;
            final SQLInputs sqlInputs = InputsProcessor.handleInputParameters(inputParameters, resultSetType, resultSetConcurrency);
            if (Constants.DB2_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())) {
                sqlInputs.setResultSetType(OOResultSet.TYPE_FORWARD_ONLY);
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
                aKey = SQLUtils.computeSessionId(sqlInputs.getTnsPath().toLowerCase() +
                        sqlInputs.getTnsEntry().toLowerCase() + sqlInputs.getUsername() + sqlInputs.getPassword() + sqlInputs.getSqlCommand().toLowerCase() + sqlInputs.getKey());
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

                    result.put(Constants.RETURNRESULT, "no more");
                    result.put("rowsLeft", "0");
                    result.put("returnCode", "1");
                } else if (sqlInputs.getlRowsFiles() == null || sqlInputs.getlRowsFiles().isEmpty() || (sqlInputs.getlRows().size() == sqlInputs.getlRowsFiles().size())) {
                    result.put(Constants.RETURNRESULT, (String) sqlInputs.getlRows().get(0));
                    sqlInputs.getlRows().remove(0);
                    result.put("columnNames", sqlInputs.getStrColumns());
                    result.put("rowsLeft", "" + sqlInputs.getlRows().size());
                    result.put("returnCode", "0");
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
                        result.put("rowsLeft", "" + sqlInputs.getlRows().size());
                        result.put("returnCode", "0");
                        sqlConnectionMap.put(sqlInputs.getStrKeyFiles(), sqlInputs.getlRowsFiles());
                        sqlConnectionMap.put(sqlInputs.getStrKeyNames(), sqlInputs.getlRowsNames());
                        sqlConnectionMap.put(sqlInputs.getStrKeySkip(), sqlInputs.getSkip());

                        globalSessionObject.setResource(new SQLSessionResource(sqlConnectionMap));

                    } catch (IOException e) {
//                   todo     result.put("exception", StringUtils.toString(e));
                        result.put("rowsLeft", "0");
                        result.put(Constants.RETURNRESULT, e.getMessage());
                        result.put("returnCode", "-1");
                    }
                }

            }//globalSessionObject
            else {
                if ("windows".equalsIgnoreCase(sqlInputs.getAuthenticationType()) && !Constants.MSSQL_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())) {
                    throw new Exception("Windows authentication can only be used with MSSQL!");
                }
                SQLQueryLobService sqlQueryLobService = new SQLQueryLobService();
                boolean isLOB = sqlQueryLobService.executeSqlQueryLob(sqlInputs);

                if (!sqlInputs.getlRows().isEmpty()) {
                    result.put(Constants.RETURNRESULT, sqlInputs.getlRows().get(0));
                    sqlInputs.getlRows().remove(0);
                    result.put("rowsLeft", "" + sqlInputs.getlRows().size());
                    result.put("columnNames", sqlInputs.getStrColumns());
                    result.put("returnCode", "0");
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
                    result.put("rowsLeft", "0");
                    result.put("SQLQuery", sqlInputs.getSqlCommand());
                    result.put("returnCode", "1");
                }
//                iQueryCount = sqlInputs.getiQuerys();
//                result.put("queryCount", String.valueOf(iQueryCount));
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

//        result.put("queryCount", String.valueOf(iQueryCount));
        return result;
    }
}
