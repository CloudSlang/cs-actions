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
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.database.services.SQLQueryAllRowsService;
import io.cloudslang.content.database.utils.*;
import io.cloudslang.content.database.utils.other.SQLQueryAllUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.database.utils.Constants.RESULT_SET_CONCURRENCY;
import static io.cloudslang.content.database.utils.Constants.RESULT_SET_TYPE;

/**
 * Created by pinteae on 1/10/2017.
 */
public class SQLQueryAllRows {
    @Action(name = "SQL Query All Rows",
            outputs = {
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS,
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
                                       @Param(value = "colDelimiter", required = true) String colDelimiter,
                                       @Param(value = "rowDelimiter", required = true) String rowDelimiter,
                                       @Param(value = "timeout") String timeout,
                                       @Param(value = "databasePoolingProperties") String databasePoolingProperties,
                                       @Param(value = "resultSetType") String resultSetType,
                                       @Param(value = "resultSetConcurrency") String resultSetConcurrency) {
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
            //default values
//            OOResultSet resultSetType = OOResultSet.TYPE_SCROLL_INSENSITIVE;
//            OOResultSet resultSetConcurrency = OOResultSet.CONCUR_READ_ONLY;
            final SQLInputs sqlInputs = InputsProcessor.handleInputParameters(inputParameters, resultSetType, resultSetConcurrency);
            if (Constants.DB2_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())) {
                sqlInputs.setResultSetType(OOResultSet.TYPE_FORWARD_ONLY.toString());
            }
            if (StringUtils.isEmpty(sqlInputs.getSqlCommand())) {
                throw new Exception("command input is empty.");
            }
            // String colDelimiter = StringUtils.resolveString(actionRequest, COL_DELIMITER);
            // String rowDelimiter = StringUtils.resolveString(actionRequest, ROW_DELIMITER);
            if (colDelimiter == null || colDelimiter.length() <= 0) {
                colDelimiter = ",";
            }
            if (rowDelimiter == null || rowDelimiter.length() <= 0) {
                rowDelimiter = "\n";
            }

            SQLQueryAllRowsService sqlQueryAllRowsService = new SQLQueryAllRowsService();
            String queryResult = sqlQueryAllRowsService.execQueryAllRows(sqlInputs, colDelimiter, rowDelimiter);
            result.put(Constants.RETURNRESULT, queryResult);
            result.put("returnCode", "0");
        } catch (Exception e) {
            if (e instanceof SQLException)
                result.put("exception", SQLUtils.toString((SQLException) e));
            else
//         todo       result.put("exception", StringUtils.toString(e));
                result.put(Constants.RETURNRESULT, e.getMessage());
            result.put("returnCode", "-1");
        }
        return result;
    }
}
