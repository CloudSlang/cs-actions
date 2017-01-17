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
import io.cloudslang.content.database.constants.DBInputNames;
import io.cloudslang.content.database.services.SQLCommandService;
import io.cloudslang.content.database.utils.Constants;
import io.cloudslang.content.database.utils.InputsProcessor;
import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.SQLUtils;
import io.cloudslang.content.database.utils.other.SQLCommandUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBInputNames.*;
import static io.cloudslang.content.database.constants.DBOtherValues.CONCUR_VALUES;

/**
 * Created by pinteae on 1/11/2017.
 */
public class SQLCommand {

    private final static String SET_NOCOUNT_ON = "SET NOCOUNT ON";

    @Action(name = "SQL Command",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
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
                                       @Param(value = DATABASE_POOLING_PROPERTIES) String databasePoolingProperties,
                                       @Param(value = RESULT_SET_TYPE) String resultSetType,
                                       @Param(value = RESULT_SET_CONCURRENCY) String resultSetConcurrency) {
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
        inputParameters.put(RESULT_SET_TYPE, resultSetType);
        inputParameters.put(RESULT_SET_CONCURRENCY, resultSetConcurrency);

        Map<String, String> result = new HashMap<>();
        try {
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
                final StringBuilder stringBuilder = new StringBuilder(String.valueOf(sqlInputs.getiUpdateCount()));
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
            result.put(RETURN_CODE, SUCCESS);
        } catch (Exception e) {
            if (e instanceof SQLException)
                result.put(EXCEPTION, SQLUtils.toString((SQLException) e));
            else
//           todo     result.put(EXCEPTION, StringUtils.toString(e));
                result.put(Constants.RETURNRESULT, e.getMessage());
            result.put(RETURN_CODE, FAILURE);
        }

        return result;
    }
}
