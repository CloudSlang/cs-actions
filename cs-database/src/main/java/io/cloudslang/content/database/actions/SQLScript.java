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
import io.cloudslang.content.database.services.SQLScriptService;
import io.cloudslang.content.database.utils.*;
import io.cloudslang.content.database.utils.other.SQLScriptUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pinteae on 1/11/2017.
 */
public class SQLScript {
    @Action(name = "SQL Command",
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
                                       @Param(value = "sqlCommands", required = true) String sqlCommands,
                                       @Param(value = "Delimiter", required = true) String delimiter,
                                       @Param(value = "scriptFileName", required = true) String scriptFileName,
                                       @Param(value = "trustAllRoots") String trustAllRoots,
                                       @Param(value = "trustStore") String trustStore,
                                       @Param(value = "trustStorePassword") String trustStorePassword,
                                       @Param(value = "databasePoolingProperties") String databasePoolingProperties
                                       // @Param(value = "resultSetType") String resultSetType,
                                       //@Param(value = "resultSetConcurrency") String resultSetConcurrency,
    ) {
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
        Map<String, String> result = new HashMap<>();

        try {
            OOResultSet resultSetType = OOResultSet.TYPE_SCROLL_INSENSITIVE;
            OOResultSet resultSetConcurrency = OOResultSet.CONCUR_READ_ONLY;
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
                result.put("returnCode", "0");
            }
        } catch (Exception e) {
            if (e instanceof SQLException) {
                result.put("exception", SQLUtils.toString((SQLException) e));
            } else {
//         todo       result.put("exception", StringUtils.toString(e));
            }

            result.put(Constants.RETURNRESULT, e.getMessage());
            result.put("returnCode", "-1");
        }
        return result;
    }

    ArrayList<String> readFromFile(String fileName) throws Exception {
        ArrayList<String> lines = new ArrayList<>();

        DataInputStream in = null;
        BufferedReader br = null;
        try {
            File file = new File(fileName);
            FileInputStream fstream = new FileInputStream(file);
            in = new DataInputStream(fstream);
            final InputStreamReader inputStreamReader = new InputStreamReader(in);
            br = new BufferedReader(inputStreamReader);
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
        } finally {
            if (in != null)
                in.close();
            if (br != null)
                br.close();
        }
        return lines;
    }
}
