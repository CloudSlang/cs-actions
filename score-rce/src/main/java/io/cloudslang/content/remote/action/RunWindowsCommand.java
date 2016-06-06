package io.cloudslang.content.remote.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;

public class RunWindowsCommand {

    /**
     * Executes a windows command on a remote host.
     *
     * @param host                 The hostname or ip address of the remote host.
     * @param username             The username used to connect to the remote machine.
     * @param password             The password used to connect to the remote machine.
     * @param command              The windows command.
   */


    @Action(name = "Run Windows Command",
            outputs = {
                    @Output("error_massage"),
                    @Output("command_result"),

            },
            responses = {
                    @Response(text = "success", field = "message", value = "fail", matchType = MatchType.COMPARE_NOT_EQUAL),
                    @Response(text = "failure", field = "message", value = "fail", matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)
            }
    )


    private static int executeCommand(String command){
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(command);
            p.waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }
    private static String getResult(File file) throws FileNotFoundException {
        String temp ="";
        Scanner sc = new Scanner(file);
        while (sc.hasNext()){
             temp+=sc.next()+ " ";
            }
        sc.close();
        file.delete();
        return temp;
    }

    public static String launchCmdWinCommand(String host, String username, String password, String command) throws FileNotFoundException {
        String command_result = "";
        String tempFileForResults = "\\\\" + host + "\\C$\\Temp\\emptyfile.txt";
        String wmiComCreateFile = "wmic /node:\""+ host + "\" /user:"+ username + " /password:" + password+ " process call create \"cmd.exe fsutil file createnew " + tempFileForResults + " 0\"";
        String wmiComExecuteWindCom = "wmic /node:\""+ host + "\" process call create \"cmd.exe /c " + command + " > " + tempFileForResults + " \"";

        // create file for results
        executeCommand(wmiComCreateFile);
        // execute command and put result to file
        executeCommand(wmiComExecuteWindCom);
        // get file for results
        command_result = getResult(new File(tempFileForResults));

        return command_result;
    }


    public Map<String, String> run(@Param(value = "hostname", required = true) String hostname, @Param(value = "command", required = true) String command, @Param(value = "username", required = true) String username, @Param(value = "password", required = true) String password) {
        String command_result = "";
        String error_message = "";
        Map<String, String> results = new HashMap<>();

        try {
            command_result = launchCmdWinCommand(hostname,username,password,command);
            results.put("command_result", command_result);
            results.put("error_message", "");
        } catch (FileNotFoundException e) {
            error_message = "Invalid parameters";
            results.put("error_message", error_message);
        }

        return  results;
    }
}
