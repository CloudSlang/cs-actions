package io.cloudslang.content.remote.services;

import io.cloudslang.content.remote.utils.RunWindowsCommandInputs;
import io.cloudslang.content.remote.utils.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by pasternd on 07/06/2016.
 */
public class RunWindowsCommandService {

    private int executeCommand(String command){
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(command);
            p.waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    private String getResult(File file) throws IOException {
        String result ="";
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            result = sb.toString();

        } finally {
            br.close();
            file.delete();
        }
        return result;
    }

    public String launchCmdWinCommand(RunWindowsCommandInputs runWindowsCommandInputs) throws IOException {

        String tempFileForResults = StringUtils.getTempFileForResults(runWindowsCommandInputs.getHostname());
        String wmiComCreateFile = StringUtils.getWmiComCreateFile(runWindowsCommandInputs.getHostname(),runWindowsCommandInputs.getUsername(),runWindowsCommandInputs.getPassword(),tempFileForResults);
        String wmiComExecuteWindCom = StringUtils.getWmiComExecuteWindCom(runWindowsCommandInputs.getHostname(),runWindowsCommandInputs.getCommand(),tempFileForResults);

        // create file for results
        this.executeCommand(wmiComCreateFile);
        // execute command and put result to file
        executeCommand(wmiComExecuteWindCom);
        // get file for results
        String command_result = getResult(new File(tempFileForResults));

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
