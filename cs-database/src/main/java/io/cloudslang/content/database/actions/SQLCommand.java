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
import io.cloudslang.content.database.services.SQLCommandService;
import io.cloudslang.content.database.utils.*;
import io.cloudslang.content.database.utils.other.SQLCommandUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pinteae on 1/11/2017.
 */
public class SQLCommand {

    private final static String SET_NOCOUNT_ON = "SET NOCOUNT ON";

    @Action(name = "SQL Command",
            outputs = {
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
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
                                       @Param(value = "databasePoolingProperties") String databasePoolingProperties
                                       // @Param(value = "resultSetType") String resultSetType,
                                       //@Param(value = "resultSetConcurrency") String resultSetConcurrency,
    ) {
        Map<String, String> inputParameters = SQLCommandUtil.createInputParametersMap(dbServerName,
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
                trustAllRoots,
                trustStore,
                trustStorePassword,
                databasePoolingProperties);
        Map<String, String> result = new HashMap<>();
        try {
            OOResultSet resultSetType = OOResultSet.TYPE_FORWARD_ONLY;
            OOResultSet resultSetConcurrency = OOResultSet.CONCUR_READ_ONLY;
            final SQLInputs sqlInputs = InputsProcessor.handleInputParameters(inputParameters, resultSetType, resultSetConcurrency);
            if (StringUtils.isEmpty(sqlInputs.getSqlCommand())) {
                throw new Exception("command input is empty.");
            }
            SQLCommandService sqlCommandService = new SQLCommandService();
            String res = sqlCommandService.executeSqlCommand(sqlInputs);

            String outputText = "";

            if (Constants.ORACLE_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType()) &&
                    sqlInputs.getSqlCommand().toLowerCase().contains("dbms_output")) {
                if (!"".equalsIgnoreCase(res)) {
                    outputText = res;
                }
                res = "Command completed successfully";
            } else if (sqlInputs.getlRows().size() == 0 && sqlInputs.getiUpdateCount() != -1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(String.valueOf(sqlInputs.getiUpdateCount()));
                stringBuilder.append(" row(s) affected");
                outputText += stringBuilder.toString();
            } else if (sqlInputs.getlRows().size() == 0 && sqlInputs.getiUpdateCount() == -1) {
                outputText = "The command has no results!";
                if (sqlInputs.getSqlCommand().toUpperCase().contains(SET_NOCOUNT_ON)) {
                    outputText = res;
                }
            } else {
                for (String row : sqlInputs.getlRows()) {
                    outputText += row + "\n";
                }
            }

            result.put("updateCount", String.valueOf(sqlInputs.getiUpdateCount()));
            result.put(Constants.RETURNRESULT, res);
            result.put(Constants.OUTPUTTEXT, outputText);
            result.put("returnCode", "0");
        } catch (Exception e) {
            if (e instanceof SQLException)
                result.put("exception", SQLUtils.toString((SQLException) e));
            else
//           todo     result.put("exception", StringUtils.toString(e));
                result.put(Constants.RETURNRESULT, e.getMessage());
            result.put("returnCode", "-1");
        }

        return result;
    }
}
