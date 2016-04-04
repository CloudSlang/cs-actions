package io.cloudslang.content.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.services.WSManService;
import io.cloudslang.content.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by giloan on 3/26/2016.
 */
public class PowerShellScriptAction {

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
            @Param(value = Constants.InputNames.INPUT_HOST, required = true) String host,
            @Param(value = Constants.InputNames.INPUT_PORT) String port,
            @Param(value = Constants.InputNames.PROTOCOL) String protocol,
            @Param(value = Constants.InputNames.INPUT_USERNAME, required = true) String username,
            @Param(value = Constants.InputNames.INPUT_PASSWORD, required = true, encrypted = true) String password,
            @Param(value = Constants.InputNames.INPUT_SCRIPT, required = true) String script
            ) {
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            WSManService wsManService = new WSManService();
            resultMap.put(Constants.OutputNames.RETURN_RESULT, wsManService.runCommand(host, port, protocol, username, password, script));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public static void main(String[] args) {
        PowerShellScriptAction powerShellScriptAction = new PowerShellScriptAction();
        Map<String, String> map = powerShellScriptAction.execute(
                "16.77.9.170",
                "5985",
                "http",
                "Administrator",
                "B33f34t3r",
//                "Get-WMIObject -class win32_physicalmemory | Format-Table devicelocator, capacity -a"
                "get-host"
        );
        System.out.println(map.toString());
    }
}
