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
import io.cloudslang.content.database.services.SQLCommandService;
import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;
import static io.cloudslang.content.database.constants.DBDefaultValues.NEW_LINE;
import static io.cloudslang.content.database.constants.DBInputNames.*;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.constants.DBOutputNames.OUTPUT_TEXT;
import static io.cloudslang.content.database.constants.DBOutputNames.UPDATE_COUNT;
import static io.cloudslang.content.database.utils.SQLInputsUtils.*;
import static io.cloudslang.content.database.utils.SQLInputsValidator.validateSqlCommandInputs;
import static io.cloudslang.content.utils.BooleanUtilities.toBoolean;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

/**
 * Created by pinteae on 1/11/2017.
 */
public class SQLCommand {


    @Action(name = "SQL Command",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(UPDATE_COUNT),
                    @Output(OUTPUT_TEXT),
                    @Output(EXCEPTION),
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
                                       @Param(value = DATABASE_NAME, required = true) String databaseName,
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

        dbType = defaultIfEmpty(dbType, ORACLE_DB_TYPE);
        trustAllRoots = defaultIfEmpty(trustAllRoots, FALSE);
        authenticationType = defaultIfEmpty(authenticationType, AUTH_SQL);
        resultSetType = defaultIfEmpty(resultSetType, TYPE_FORWARD_ONLY);
        resultSetConcurrency = defaultIfEmpty(resultSetConcurrency, CONCUR_READ_ONLY);
        trustStore = defaultIfEmpty(trustStore, EMPTY);
        trustStorePassword = defaultIfEmpty(trustStorePassword, EMPTY);
        instance = defaultIfEmpty(instance, EMPTY);
        final List<String> preInputsValidation = validateSqlCommandInputs(dbServerName, dbType, username, password, instance, dbPort,
                databaseName, authenticationType, command, trustAllRoots, resultSetType, resultSetConcurrency, trustStore,
                trustStorePassword);

        if (!preInputsValidation.isEmpty()) {
            return getFailureResultsMap(StringUtils.join(preInputsValidation, NEW_LINE));
        }

        try {
            SQLInputs sqlInputs = new SQLInputs();
            sqlInputs.setDbServer(dbServerName);
            sqlInputs.setDbType(getDbType(dbType));
            sqlInputs.setUsername(username);
            sqlInputs.setPassword(password);
            sqlInputs.setInstance(instance);
            sqlInputs.setDbPort(getOrDefaultDBPort(dbPort, sqlInputs.getDbType()));
            sqlInputs.setDbName(defaultIfEmpty(databaseName, EMPTY));
            sqlInputs.setAuthenticationType(authenticationType);
            sqlInputs.setDbClass(defaultIfEmpty(dbClass, EMPTY));
            sqlInputs.setDbUrl(defaultIfEmpty(dbURL, EMPTY));
            sqlInputs.setSqlCommand(command);
            sqlInputs.setTrustAllRoots(toBoolean(trustAllRoots));
            sqlInputs.setTrustStore(trustStore);
            sqlInputs.setTrustStorePassword(trustStorePassword);
            sqlInputs.setDatabasePoolingProperties(getOrDefaultDBPoolingProperties(databasePoolingProperties, EMPTY));
            sqlInputs.setResultSetType(getResultSetType(resultSetType));
            sqlInputs.setResultSetConcurrency(getResultSetConcurrency(resultSetConcurrency));
            sqlInputs.setNetcool(checkIsNetcool(sqlInputs.getDbType()));

            String res = SQLCommandService.executeSqlCommand(sqlInputs);

            String outputText = "";

            if (ORACLE_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType()) &&
                    sqlInputs.getSqlCommand().toLowerCase().contains(DBMS_OUTPUT)) {
                if (isNoneEmpty(res)) {
                    outputText = res;
                }
                res = "Command completed successfully";
            } else if (sqlInputs.getlRows().size() == 0 && sqlInputs.getiUpdateCount() != -1) {
                outputText = String.valueOf(sqlInputs.getiUpdateCount()) + " row(s) affected";
            } else if (sqlInputs.getlRows().size() == 0 && sqlInputs.getiUpdateCount() == -1) {
                outputText = "The command has no results!";
                if (sqlInputs.getSqlCommand().toUpperCase().contains(SET_NOCOUNT_ON)) {
                    outputText = res;
                }
            } else {
                outputText = StringUtilities.join(sqlInputs.getlRows(), NEW_LINE);
            }

            final Map<String, String> result = getSuccessResultsMap(res);
            result.put(UPDATE_COUNT, String.valueOf(sqlInputs.getiUpdateCount()));
            result.put(OUTPUT_TEXT, outputText);
            return result;
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }

    }

}
