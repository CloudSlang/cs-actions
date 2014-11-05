package com.hp.score.content.ssh.services.actions;

import com.hp.score.content.ssh.entities.SSHShellInputs;
import com.hp.score.content.ssh.services.SSHService;
import com.hp.score.content.ssh.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vranau on 11/5/2014.
 */
public class ScoreSSHShellLogOff extends SSHShellAbstract {
    public Map<String, String> execute(SSHShellInputs sshShellInputs) {
        Map<String, String> returnResult = new HashMap<>();
        try {
            // get the cached SSH session
            SSHService service = getFromCache(sshShellInputs.getSshGlobalSessionObject(), sshShellInputs.getSshSessionObject(), sshShellInputs.getSessionId());
            if (service == null) {
                throw new RuntimeException("Could not find sessionId in the session context.");
            }

            // close the SSH session
            service.close();
            service.removeFromCache(sshShellInputs.getSshGlobalSessionObject(), sshShellInputs.getSessionId());
            service.removeFromCache(sshShellInputs.getSshSessionObject(), sshShellInputs.getSessionId());

            returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
        } catch (Exception e) {
            populateResult(returnResult, e);
        }
        return returnResult;
    }
}
