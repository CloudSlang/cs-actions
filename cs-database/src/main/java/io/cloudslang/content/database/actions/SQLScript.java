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
import io.cloudslang.content.database.services.SQLScriptService;
import io.cloudslang.content.database.utils.Constants;
import io.cloudslang.content.database.utils.InputsProcessor;
import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.SQLUtils;
import io.cloudslang.content.database.utils.other.SQLScriptUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
public class SQLScript {

    public static final String SQL_COMMANDS = "sqlCommands";

    @Action(name = "SQL Command",
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
                                       @Param(value = SQL_COMMANDS, required = true) String sqlCommands,
                                       @Param(value = DELIMITER, required = true) String delimiter,
                                       @Param(value = SCRIPT_FILE_NAME, required = true) String scriptFileName,
                                       @Param(value = TRUST_ALL_ROOTS) String trustAllRoots,
                                       @Param(value = TRUST_STORE) String trustStore,
                                       @Param(value = TRUST_STORE_PASSWORD) String trustStorePassword,
                                       @Param(value = DATABASE_POOLING_PROPERTIES) String databasePoolingProperties,
                                       @Param(value = RESULT_SET_TYPE) String resultSetType,
                                       @Param(value = RESULT_SET_CONCURRENCY) String resultSetConcurrency) {
        Map<String, String> inputParameters = SQLScriptUtil.createInputParametersMap(dbServerName,
                dbType,
                username,
                password,
                instance,
                dbPort,
                database,
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
        Map<String, String> result = new HashMap<>();

        try {
            final SQLInputs sqlInputs = InputsProcessor.handleInputParameters(inputParameters, resultSetType, resultSetConcurrency);

            String commandsDelimiter = StringUtils.isEmpty(sqlInputs.getStrDelim()) ? "--" : sqlInputs.getStrDelim();
            ArrayList<String> commands = new ArrayList<>(Arrays.asList(sqlCommands.split(commandsDelimiter)));

            //read from SQL script file
            if (commands.isEmpty()) {
                String fileName = inputParameters.get("scriptFileName");
                if (StringUtils.isEmpty(fileName)) {
                    throw new Exception("Both Script file name and Line are empty!");
                } else {
                    commands = readFromFile(fileName);
                }
            }
            if (commands.isEmpty()) {
                throw new Exception("No SQL command to be executed.");
            } else {
                String res = null;
                SQLScriptService sqlScriptService = new SQLScriptService();
                res = sqlScriptService.executeSqlScript(commands, sqlInputs);
                result.put("updateCount", String.valueOf(sqlInputs.getiUpdateCount()));
                result.put(Constants.RETURNRESULT, res);
                result.put(RETURN_CODE, SUCCESS);
            }
        } catch (Exception e) {
            if (e instanceof SQLException) {
                result.put(EXCEPTION, SQLUtils.toString((SQLException) e));
            } else {
//         todo       result.put(EXCEPTION, StringUtils.toString(e));
            }

            result.put(Constants.RETURNRESULT, e.getMessage());
            result.put(RETURN_CODE, FAILURE);
        }
        return result;
    }

    ArrayList<String> readFromFile(String fileName) throws Exception {
        ArrayList<String> lines = new ArrayList<>();


        try (final FileInputStream fstream = new FileInputStream(new File(fileName));
             final DataInputStream in = new DataInputStream(fstream);
             final InputStreamReader inputStreamReader = new InputStreamReader(in);
             final BufferedReader br = new BufferedReader(inputStreamReader)) {

            String strLine = "";
            String aString = "";
            int i = 0;
            boolean youAreInAMultiLineComment = false;
            while ((strLine = br.readLine()) != null) {
                //ignore multi line comments
                if (youAreInAMultiLineComment) {
                    if (strLine.contains("*/")) {
                        int indx = strLine.indexOf("*/");
                        strLine = strLine.substring(indx + 2);
                        youAreInAMultiLineComment = false;
                    } else {
                        continue;
                    }
                }

                if (strLine.contains("/*")) {
                    int indx = strLine.indexOf("/*");
                    String firstPart = strLine.substring(0, indx);
                    String secondPart = strLine.substring(indx + 2);
                    strLine = firstPart;
                    youAreInAMultiLineComment = true;

                    if (secondPart.contains("*/")) {    //the comment starts and ends in the middle of the line
                        indx = secondPart.indexOf("*/");
                        secondPart = secondPart.substring(indx + 2);
                        youAreInAMultiLineComment = false;
                        strLine += secondPart;
                    }
                }

                //ignore one line comments
                if (strLine.contains("--")) {
                    int indx = strLine.indexOf("--");
                    strLine = strLine.substring(0, indx);
                }

                //ingnore empty lines
                if (0 == strLine.length()) {
                    continue;
                }

                //consider if a SQL statement is separated in different lines. eg,
                //create table employee(
                //  first varchar(15));
                //if a sql command finishes in one line, add in commands
                if (strLine.endsWith(";")) {
                    //get rid of ';',otherwise the operation will fail on Oracle database
                    int indx = strLine.indexOf(";", 0);
                    strLine = strLine.substring(0, indx);
                    //if the command has only one line
                    if (i == 0) {
                        lines.add(strLine);
                    }
                    //if the command has multiple lines
                    else {
                        aString += strLine;
                        lines.add(aString);
                        aString = "";
                        i = 0;
                    }
                }
                //if a line doesn't finish with a ';', it means the sql commands is not finished
                else {
                    aString = aString + strLine + " ";
                    i++;
                }
            }
        }
        return lines;
    }
}
