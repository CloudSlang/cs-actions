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
import io.cloudslang.content.database.services.SQLScriptService;
import io.cloudslang.content.database.utils.InputsProcessor;
import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.other.SQLScriptUtil;
import io.cloudslang.content.utils.BooleanUtilities;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;
import static io.cloudslang.content.database.constants.DBDefaultValues.NEW_LINE;
import static io.cloudslang.content.database.constants.DBInputNames.*;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.constants.DBOutputNames.UPDATE_COUNT;
import static io.cloudslang.content.database.utils.SQLInputsUtils.*;
import static io.cloudslang.content.database.utils.SQLInputsValidator.validateSqlScriptInputs;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by pinteae on 1/11/2017.
 */
public class SQLScript {

    @Action(name = "SQL Command",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION),
                    @Output(UPDATE_COUNT),
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
                                       @Param(value = DATABASE_NAME, required = true) String databaseName,
                                       @Param(value = AUTHENTICATION_TYPE) String authenticationType,
                                       @Param(value = DB_CLASS) String dbClass,
                                       @Param(value = DB_URL) String dbURL,
                                       @Param(value = DELIMITER) String delimiter,
                                       @Param(value = SQL_COMMANDS) String sqlCommands,
                                       @Param(value = SCRIPT_FILE_NAME) String scriptFileName,
                                       @Param(value = TRUST_ALL_ROOTS) String trustAllRoots,
                                       @Param(value = TRUST_STORE) String trustStore,
                                       @Param(value = TRUST_STORE_PASSWORD) String trustStorePassword,
                                       @Param(value = DATABASE_POOLING_PROPERTIES) String databasePoolingProperties,
                                       @Param(value = RESULT_SET_TYPE) String resultSetType,
                                       @Param(value = RESULT_SET_CONCURRENCY) String resultSetConcurrency) {

        dbType = defaultIfEmpty(dbType, ORACLE_DB_TYPE);
        instance = defaultIfEmpty(instance, EMPTY);
        authenticationType = defaultIfEmpty(authenticationType, AUTH_SQL);
        trustAllRoots = defaultIfEmpty(trustAllRoots, FALSE);
        trustStore = defaultIfEmpty(trustStore, EMPTY);
        trustStorePassword = defaultIfEmpty(trustStorePassword, EMPTY);

        resultSetType = defaultIfEmpty(resultSetType, TYPE_SCROLL_INSENSITIVE);
        resultSetConcurrency = defaultIfEmpty(resultSetConcurrency, CONCUR_READ_ONLY);

        final List<String> preInputsValidation = validateSqlScriptInputs(dbServerName, dbType, username, password, instance, dbPort,
                databaseName, authenticationType, sqlCommands, scriptFileName, trustAllRoots, trustStore, trustStorePassword,
                resultSetType, resultSetConcurrency);
        if (!preInputsValidation.isEmpty()) {
            return getFailureResultsMap(StringUtils.join(preInputsValidation, NEW_LINE));
        }

        SQLInputs mySqlInputs = new SQLInputs();
        mySqlInputs.setDbServer(dbServerName);
        mySqlInputs.setDbType(getDbType(dbType));
        mySqlInputs.setUsername(username);
        mySqlInputs.setPassword(password);
        mySqlInputs.setInstance(instance);
        mySqlInputs.setDbPort(getOrDefaultDBPort(dbPort, mySqlInputs.getDbType()));
        mySqlInputs.setDbName(getOrDefaultDBName(databaseName, mySqlInputs.getDbType()));
        mySqlInputs.setAuthenticationType(authenticationType);
        mySqlInputs.setDbClass(defaultIfEmpty(dbClass, EMPTY));
        mySqlInputs.setDbUrl(defaultIfEmpty(dbURL, EMPTY));
        mySqlInputs.setStrDelim(defaultIfEmpty(delimiter, "--")); //todo not ok
        mySqlInputs.setSqlCommands(getSqlCommands(sqlCommands, scriptFileName, mySqlInputs.getStrDelim()));
        mySqlInputs.setTrustAllRoots(BooleanUtilities.toBoolean(trustAllRoots));
        mySqlInputs.setTrustStore(trustStore);
        mySqlInputs.setTrustStorePassword(trustStorePassword);
        mySqlInputs.setDatabasePoolingProperties(getOrDefaultDBPoolingProperties(databasePoolingProperties, EMPTY));
        mySqlInputs.setResultSetType(getResultSetType(resultSetType));
        mySqlInputs.setResultSetConcurrency(getResultSetConcurrency(resultSetConcurrency));
//        mySqlInputs.setDbUrls(getDbUrls(mySqlInputs.getDbUrl()));

        Map<String, String> inputParameters = SQLScriptUtil.createInputParametersMap(dbServerName,
                dbType,
                username,
                password,
                instance,
                dbPort,
                databaseName,
                authenticationType,
                dbClass,
                dbURL,
                sqlCommands,
                delimiter,
                scriptFileName,
                trustAllRoots,
                trustStore,
                trustStorePassword,
                databasePoolingProperties);
        inputParameters.put(RESULT_SET_TYPE, resultSetType);
        inputParameters.put(RESULT_SET_CONCURRENCY, resultSetConcurrency);

        try {
            final SQLInputs sqlInputs = InputsProcessor.handleInputParameters(inputParameters, resultSetType, resultSetConcurrency);

            String commandsDelimiter = StringUtils.isEmpty(sqlInputs.getStrDelim()) ? "--" : sqlInputs.getStrDelim(); //todo this is not ok
            List<String> commands = new ArrayList<>(Arrays.asList(sqlCommands.split(commandsDelimiter)));

            //read from SQL script file
//            if (commands.isEmpty()) {
//                String fileName = inputParameters.get("scriptFileName");
//                if (StringUtils.isEmpty(fileName)) {
//                    throw new Exception("Both Script file name and Line are empty!");
//                } else {
//                    commands = readFromFile(fileName);
//                }
//            }
            if (!commands.isEmpty()) {
                final String res = SQLScriptService.executeSqlScript(commands, sqlInputs);
                final Map<String, String> result = getSuccessResultsMap(res);
                result.put(UPDATE_COUNT, String.valueOf(sqlInputs.getiUpdateCount()));
                return result;
            } else {
                return getFailureResultsMap("No SQL command to be executed.");
            }
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
