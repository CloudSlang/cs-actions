package io.cloudslang.content.remote.action;

import java.util.Map;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.remote.constants.Inputs;
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

    public Map<String, String> run(@Param(value = Inputs.HOSTNAME, required = true) String hostname, @Param(value = Inputs.COMMAND, required = true) String command, @Param(value = Inputs.USERNAME, required = true) String username, @Param(value = Inputs.PASSWORD, required = true) String password) {

        RunWindowsCommandInputs runWindowsCommandInputs = null;
        try {

            runWindowsCommandInputs = new RunWindowsCommandInputs.RunWindowsCommandInputsBuilder()
                    .withHostname(hostname)
                    .withUsername(username)
                    .withPassword(password)
                    .withCommand(command)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new RunWindowsCommandService().execute(runWindowsCommandInputs);
    }
}
