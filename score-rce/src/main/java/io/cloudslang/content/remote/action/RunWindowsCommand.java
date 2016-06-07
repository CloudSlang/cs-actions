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
import io.cloudslang.content.remote.services.RunWindowsCommandService;
import io.cloudslang.content.remote.utils.RunWindowsCommandInputs;

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

    public Map<String, String> run(@Param(value = "hostname", required = true) String hostname, @Param(value = "command", required = true) String command, @Param(value = "username", required = true) String username, @Param(value = "password", required = true) String password) {

        RunWindowsCommandInputs runWindowsCommandInputs = new RunWindowsCommandInputs(hostname,  username, password, command);
        runWindowsCommandInputs.setHostname(hostname);
        runWindowsCommandInputs.setUsername(username);
        runWindowsCommandInputs.setPassword(password);
        runWindowsCommandInputs.setCommand(command);


        return new RunWindowsCommandService().execute(runWindowsCommandInputs);
    }
}
