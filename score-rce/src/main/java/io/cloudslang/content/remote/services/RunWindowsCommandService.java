package io.cloudslang.content.remote.services;

import io.cloudslang.content.remote.utils.RunWindowsCommandInputs;
import io.cloudslang.content.remote.utils.CommandUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pasternd on 07/06/2016.
 */
public class RunWindowsCommandService {

    RunWindowsCommandHelper runWindowsCommandHelper = new RunWindowsCommandHelper();

    public String launchCmdWinCommand(RunWindowsCommandInputs runWindowsCommandInputs) throws IOException {

        String tempFileForResults = CommandUtils.getTempFileForResults(runWindowsCommandInputs.getHostname());
        String wmiComCreateFile = CommandUtils.getWmiComCreateFile(runWindowsCommandInputs.getHostname(),runWindowsCommandInputs.getUsername(),runWindowsCommandInputs.getPassword(),tempFileForResults);
        String wmiComExecuteWindCom = CommandUtils.getWmiComExecuteWindCom(runWindowsCommandInputs.getHostname(),runWindowsCommandInputs.getCommand(),tempFileForResults);

        // create file for results
        runWindowsCommandHelper.executeCommand(wmiComCreateFile);
        // execute command and put result to file
        runWindowsCommandHelper.executeCommand(wmiComExecuteWindCom);
        // get file for results
        String command_result = runWindowsCommandHelper.getCommandResult(new File(tempFileForResults));

        return command_result;
    }

    public Map<String, String> execute(RunWindowsCommandInputs runWindowsCommandInputs) {
        String command_result;
        String error_message;

        Map<String, String> results = new HashMap<>();

        try {
            command_result = launchCmdWinCommand(runWindowsCommandInputs);
            results.put("command_result", command_result);
            results.put("error_message", "");
        } catch (Exception e) {
            error_message = "Invalid parameters";
            results.put("error_message", error_message);
        }

        return  results;
    }


}
