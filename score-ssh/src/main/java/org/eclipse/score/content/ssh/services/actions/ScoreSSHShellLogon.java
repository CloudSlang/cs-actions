package org.eclipse.score.content.ssh.services.actions;

import org.eclipse.score.content.ssh.entities.ConnectionDetails;
import org.eclipse.score.content.ssh.entities.KeyFile;
import org.eclipse.score.content.ssh.entities.SSHShellInputs;
import org.eclipse.score.content.ssh.services.SSHService;
import org.eclipse.score.content.ssh.services.impl.SSHServiceImpl;
import org.eclipse.score.content.ssh.utils.Constants;
import org.eclipse.score.content.ssh.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ioanvranauhp on 11/5/2014.
 */
public class ScoreSSHShellLogon extends SSHShellAbstract {

    public Map<String, String> execute(SSHShellInputs sshShellInputs) {
        Map<String, String> returnResult = new HashMap<>();
        boolean providerAdded = addSecurityProvider();

        try {
            // default values
            int portNumber = StringUtils.toInt(sshShellInputs.getPort(), Constants.DEFAULT_PORT);

            // configure ssh parameters
            ConnectionDetails connection = new ConnectionDetails(sshShellInputs.getHost(), portNumber, sshShellInputs.getUsername(), sshShellInputs.getPassword());
            KeyFile keyFile = getKeyFile(sshShellInputs.getPrivateKeyFile(), sshShellInputs.getPassword());

            // get the cached SSH session
            String sessionId = "sshSession:" + sshShellInputs.getHost() + "-" + portNumber + "-" + sshShellInputs.getUsername();
            synchronized (sessionId) {
                SSHService service = getFromCache(sshShellInputs, sessionId);
                boolean saveSSHSession = false;
                if (service == null || !service.isConnected() || !service.isExpectChannelConnected()) {
                    saveSSHSession = true;
                    service = new SSHServiceImpl(connection, keyFile, Constants.DEFAULT_CONNECT_TIMEOUT, true);
                }
                // save SSH session in the cache
                if (saveSSHSession) {
                    boolean saved = saveToCache(sshShellInputs.getSshGlobalSessionObject(), service, sessionId);
                    if (!saved) {
                        throw new RuntimeException("The SSH session could not be saved in the given sessionParam.");
                    }
                }
            }

            // populate the results
            populateResult(returnResult, sessionId);
        } catch (Exception e) {
            populateResult(returnResult, e);
        } finally {
            if (providerAdded) {
                removeSecurityProvider();
            }
        }
        return returnResult;
    }

    private void populateResult(Map<String, String> returnResult, String sessionId) {
        returnResult.put(Constants.SESSION_ID, sessionId);
        returnResult.put(Constants.OutputNames.RETURN_RESULT, sessionId);
        returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
    }
}
