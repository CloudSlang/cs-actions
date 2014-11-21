package org.eclipse.score.content.ssh.services.actions;

import org.eclipse.score.content.ssh.entities.SSHShellInputs;
import org.eclipse.score.content.ssh.services.SSHService;
import org.eclipse.score.content.ssh.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vranau on 11/5/2014.
 */
public class ScoreSSHShellLogOff extends SSHShellAbstract {

    public static final String SESSION_ID_NOT_IN_SESSION_CONTEXT_MESSAGE = "Could not find sessionId in the session context.";

    public Map<String, String> execute(SSHShellInputs sshShellInputs) {
        Map<String, String> returnResult = new HashMap<>();
        try {
            // get the cached SSH session
            SSHService service = getFromCache(sshShellInputs, sshShellInputs.getSessionId()); // TODO SESSION OBJECT
            if (service == null) {
                throw new RuntimeException(SESSION_ID_NOT_IN_SESSION_CONTEXT_MESSAGE);
            }
            cleanupService(sshShellInputs, service);


            returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
        } catch (Exception e) {
            populateResult(returnResult, e);
        }
        return returnResult;
    }

    protected void cleanupService(SSHShellInputs sshShellInputs, SSHService service) {
        // close the SSH session
        service.close();
        service.removeFromCache(sshShellInputs.getSshGlobalSessionObject(), sshShellInputs.getSessionId());
//        service.removeFromCache(sshShellInputs.getSshSessionObject(), sshShellInputs.getSessionId()); TODO
    }
}
