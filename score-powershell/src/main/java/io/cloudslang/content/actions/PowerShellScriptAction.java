package io.cloudslang.content.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.entities.PowerShellInputs;
import io.cloudslang.content.services.PowerShellScriptService;
import io.cloudslang.content.services.PowerShellScriptServiceImpl;
import io.cloudslang.content.utils.Constants;

import java.util.Map;

/**
 * User: bancl
 * Date: 10/6/2015
 */
public class PowerShellScriptAction {
    /**
     * Executes a PowerShell command(s) on the remote machine.
     *
     * @param host The hostname or ip address of the source remote machine.
     * @param userName The username used to connect to the the remote machine.
     * @param password The password used to connect to the remote machine.
     * @param script The PowerShell script that will run on the remote machine.
     * @param enableHTTPS True if HTTPS connections is chosen.
     */
    @Action(name = "PowerShell Script Action",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_CODE),
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            }
    )
    public Map<String, String> execute(
            @Param(value = Constants.InputNames.INPUT_HOST) String host,
            @Param(value = Constants.InputNames.INPUT_USERNAME) String userName,
            @Param(value = Constants.InputNames.INPUT_PASSWORD) String password,
            @Param(value = Constants.InputNames.INPUT_SCRIPT) String script,
            @Param(value = Constants.InputNames.ENABLE_HTTPS) String enableHTTPS) {

        PowerShellInputs inputs = new PowerShellInputs();
        inputs.setHost(host);
        inputs.setUsername(userName);
        inputs.setPassword(password);
        inputs.setScript(script);
        inputs.setEnableHTTPS(enableHTTPS);
        PowerShellScriptService service = new PowerShellScriptServiceImpl();
        return service.execute(inputs);
    }
}
