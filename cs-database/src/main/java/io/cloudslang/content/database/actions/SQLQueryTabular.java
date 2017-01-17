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
import io.cloudslang.content.database.services.SQLQueryTabularService;
import io.cloudslang.content.database.utils.*;
import io.cloudslang.content.database.utils.other.SQLQueryTabularUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBInputNames.*;

/**
 * Created by pinteae on 1/11/2017.
 */
public class SQLQueryTabular {
    @Action(name = "SQL Query Tabular",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
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
                                       @Param(value = DATABASE, required = true) String database,
                                       @Param(value = AUTHENTICATION_TYPE) String authenticationType,
                                       @Param(value = DB_CLASS) String dbClass,
                                       @Param(value = DB_URL) String dbURL,
                                       @Param(value = COMMAND, required = true) String command,
                                       @Param(value = TRUST_ALL_ROOTS) String trustAllRoots,
                                       @Param(value = TRUST_STORE) String trustStore,
                                       @Param(value = TRUST_STORE_PASSWORD) String trustStorePassword,
                                       @Param(value = TIMEOUT) String timeout,
                                       @Param(value = DATABASE_POOLING_PROPERTIES) String databasePoolingProperties,
                                       @Param(value = RESULT_SET_TYPE) String resultSetType,
                                       @Param(value = RESULT_SET_CONCURRENCY) String resultSetConcurrency) {
        Map<String, String> inputParameters = SQLQueryTabularUtil.createInputParametersMap(dbServerName,
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
                timeout,
                databasePoolingProperties);

        inputParameters.put(RESULT_SET_TYPE, resultSetType);
        inputParameters.put(RESULT_SET_CONCURRENCY, resultSetConcurrency);
        Map<String, String> result = new HashMap<>();
        try {
//            OOResultSet resultSetType = OOResultSet.TYPE_SCROLL_INSENSITIVE;
//            OOResultSet resultSetConcurrency = OOResultSet.CONCUR_READ_ONLY;
            final SQLInputs sqlInputs = InputsProcessor.handleInputParameters(inputParameters, resultSetType, resultSetConcurrency);
            if (Constants.DB2_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())) {
                sqlInputs.setResultSetType(OOResultSet.TYPE_FORWARD_ONLY.toString());
            }
            if (StringUtils.isEmpty(sqlInputs.getSqlCommand())) {
                throw new Exception("command input is empty.");
            }
            SQLQueryTabularService sqlQueryTabularService = new SQLQueryTabularService();
            String queryResult = sqlQueryTabularService.execSqlQueryTabular(sqlInputs);
            result.put(Constants.RETURNRESULT, queryResult);
            result.put(RETURN_CODE, SUCCESS);
        } catch (Exception e) {
            if (e instanceof SQLException)
                result.put(EXCEPTION, SQLUtils.toString((SQLException) e));
            else
//          todo      result.put(EXCEPTION, StringUtils.toString(e));
                result.put(Constants.RETURNRESULT, e.getMessage());
            result.put(RETURN_CODE, FAILURE);
        }
        return result;
    }
}
