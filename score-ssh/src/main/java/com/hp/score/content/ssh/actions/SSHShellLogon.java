package com.hp.score.content.ssh.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.score.content.ssh.services.actions.SSHShellAbstract;
import com.hp.score.content.ssh.services.actions.ScoreSSHShellLogon;
import com.hp.score.content.ssh.entities.SSHConnection;
import com.hp.score.content.ssh.entities.SSHShellInputs;
import com.hp.score.content.ssh.utils.Constants;

import java.util.Map;

/**
 * @author ioanvranauhp
 *         Date: 11/03/14
 */
public class SSHShellLogon extends SSHShellAbstract {

    @Action(name = "SSH Shell Logon",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_CODE),
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.SESSION_ID),
                    @Output(Constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            }
    )
    public Map<String, String> sshLogon(
            @Param(value = Constants.InputNames.HOST, required = true) String host,
            @Param(Constants.InputNames.PORT) String port,
            @Param(value = Constants.InputNames.USERNAME, required = true) String username,
            @Param(value = Constants.InputNames.PASSWORD, required = true, encrypted = true) String password,
            @Param(Constants.PRIVATE_KEY_FILE) String privateKeyFile,
            @Param(Constants.SSH_SESSIONS_DEFAULT_ID) GlobalSessionObject<Map<String, SSHConnection>> globalSessionObject,
            @Param(Constants.SSH_SESSIONS_DEFAULT_ID) GlobalSessionObject<Map<String, SSHConnection>> sessionObject, //TODO Session global
            @Param(Constants.USE_GLOBAL_CONTEXT) String useGlobalContext) {

        SSHShellInputs sshShellInputs = new SSHShellInputs();
        sshShellInputs.setHost(host);
        sshShellInputs.setPort(port);
        sshShellInputs.setUsername(username);
        sshShellInputs.setPassword(password);
        sshShellInputs.setPrivateKeyFile(privateKeyFile);
        sshShellInputs.setSshGlobalSessionObject(globalSessionObject);
//        sshShellInputs.setSshSessionObject(sessionObject); TODO
//        sshShellInputs.setUseGlobalContext(useGlobalContext);
        return new ScoreSSHShellLogon().execute(sshShellInputs);
    }


}