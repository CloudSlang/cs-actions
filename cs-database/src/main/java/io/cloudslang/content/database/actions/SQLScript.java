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
import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.utils.BooleanUtilities;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;
import static io.cloudslang.content.database.constants.DBDefaultValues.NEW_LINE;
import static io.cloudslang.content.database.constants.DBExceptionValues.NO_SQL_COMMAND;
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

    /**
     * @param dbServerName
     * @param dbType
     * @param username
     * @param password
     * @param instance
     * @param dbPort
     * @param databaseName
     * @param authenticationType
     * @param dbClass
     * @param dbURL
     * @param delimiter
     * @param sqlCommands
     * @param scriptFileName
     * @param databasePoolingProperties
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     */
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
//                                       @Param(value = TRUST_ALL_ROOTS) String trustAllRoots,
//                                       @Param(value = TRUST_STORE) String trustStore,
//                                       @Param(value = TRUST_STORE_PASSWORD) String trustStorePassword,
                                       @Param(value = DATABASE_POOLING_PROPERTIES) String databasePoolingProperties,
                                       @Param(value = RESULT_SET_TYPE) String resultSetType,
                                       @Param(value = RESULT_SET_CONCURRENCY) String resultSetConcurrency) {

        dbType = defaultIfEmpty(dbType, ORACLE_DB_TYPE);
        instance = defaultIfEmpty(instance, EMPTY);
        authenticationType = defaultIfEmpty(authenticationType, AUTH_SQL);
//        trustAllRoots = defaultIfEmpty(trustAllRoots, FALSE);
//        trustStore = defaultIfEmpty(trustStore, EMPTY);
//        trustStorePassword = defaultIfEmpty(trustStorePassword, EMPTY);

        resultSetType = defaultIfEmpty(resultSetType, TYPE_SCROLL_INSENSITIVE);
        resultSetConcurrency = defaultIfEmpty(resultSetConcurrency, CONCUR_READ_ONLY);

        final List<String> preInputsValidation = validateSqlScriptInputs(dbServerName, dbType, username, password, instance, dbPort,
                databaseName, authenticationType, sqlCommands, scriptFileName, /*trustAllRoots, trustStore, trustStorePassword,*/
                resultSetType, resultSetConcurrency);
        if (!preInputsValidation.isEmpty()) {
            return getFailureResultsMap(StringUtils.join(preInputsValidation, NEW_LINE));
        }

        dbType = getDbType(dbType);
        delimiter = defaultIfEmpty(delimiter, SEMI_COLON);
        final SQLInputs sqlInputs = SQLInputs.builder()
                .dbServer(dbServerName)
                .dbType(dbType)
                .username(username)
                .password(password)
                .instance(instance)
                .dbPort(getOrDefaultDBPort(dbPort, dbType))
                .dbName(defaultIfEmpty(databaseName, EMPTY))
                .authenticationType(authenticationType)
                .dbClass(getOrDefaultDBClass(dbClass, dbType))
                .dbUrl(defaultIfEmpty(dbURL, EMPTY))
                .strDelim(delimiter)
                .sqlCommands(getSqlCommands(sqlCommands, scriptFileName, delimiter))
//                .trustAllRoots(BooleanUtilities.toBoolean(trustAllRoots))
//                .trustStore(trustStore)
//                .trustStorePassword(trustStorePassword)
                .databasePoolingProperties(getOrDefaultDBPoolingProperties(databasePoolingProperties, EMPTY))
                .resultSetType(getResultSetType(resultSetType))
                .resultSetConcurrency(getResultSetConcurrency(resultSetConcurrency))
                .isNetcool(checkIsNetcool(dbType))
                .build();

        try {
            final List<String> commands = sqlInputs.getSqlCommands();
            if (!commands.isEmpty()) {
                final String res = SQLScriptService.executeSqlScript(commands, sqlInputs);
                final Map<String, String> result = getSuccessResultsMap(res);
                result.put(UPDATE_COUNT, String.valueOf(sqlInputs.getIUpdateCount()));
                return result;
            }
            return getFailureResultsMap(NO_SQL_COMMAND);
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
