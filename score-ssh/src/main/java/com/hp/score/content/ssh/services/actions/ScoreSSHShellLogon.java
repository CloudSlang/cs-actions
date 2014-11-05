package com.hp.score.content.ssh.services.actions;

import com.hp.score.content.ssh.entities.ConnectionDetails;
import com.hp.score.content.ssh.entities.KeyFile;
import com.hp.score.content.ssh.entities.SSHShellInputs;
import com.hp.score.content.ssh.services.SSHService;
import com.hp.score.content.ssh.services.impl.SSHServiceImpl;
import com.hp.score.content.ssh.utils.Constants;
import com.hp.score.content.ssh.utils.StringUtils;

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
            boolean useGlobalContextBoolean = StringUtils.toBoolean(sshShellInputs.getUseGlobalContext(), Constants.DEFAULT_USE_GLOBAL_CONTEXT);

            // configure ssh parameters
            ConnectionDetails connection = new ConnectionDetails(sshShellInputs.getHost(), portNumber, sshShellInputs.getUsername(), sshShellInputs.getPassword());
            KeyFile keyFile = getKeyFile(sshShellInputs.getPrivateKeyFile(), sshShellInputs.getPassword());

            // get the cached SSH session
            String sessionId = "sshSession:" + sshShellInputs.getHost() + "-" + portNumber + "-" + sshShellInputs.getUsername();
            synchronized (sessionId) {
                SSHService service = getFromCache(sshShellInputs.getSshGlobalSessionObject(), sshShellInputs.getSshSessionObject(), useGlobalContextBoolean, sessionId);
                boolean saveSSHSession = false;
                if (service == null || !service.isConnected() || !service.isExpectChannelConnected()) {
                    saveSSHSession = true;
                    service = new SSHServiceImpl(connection, keyFile, Constants.DEFAULT_CONNECT_TIMEOUT, true);
                }
                // save SSH session in the cache
                if (saveSSHSession) {
                    saveToCache(sshShellInputs.getSshGlobalSessionObject(), sshShellInputs.getSshSessionObject(), useGlobalContextBoolean, service, sessionId);
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
