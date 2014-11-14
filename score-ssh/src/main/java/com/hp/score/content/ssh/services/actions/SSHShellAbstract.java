package com.hp.score.content.ssh.services.actions;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SessionParam;
import com.hp.score.content.ssh.entities.KeyFile;
import com.hp.score.content.ssh.entities.SSHConnection;
import com.hp.score.content.ssh.entities.SSHShellInputs;
import com.hp.score.content.ssh.services.SSHService;
import com.hp.score.content.ssh.utils.CacheUtils;
import com.hp.score.content.ssh.utils.Constants;
import com.hp.score.content.ssh.utils.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;
import java.util.Map;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public abstract class SSHShellAbstract {

    public static final String COMMAND_IS_NOT_SPECIFIED_MESSAGE = "Command is not specified.";

    protected boolean addSecurityProvider() {
        boolean providerAdded = false;
        Provider provider = Security.getProvider("BC");
        if (provider == null) {
            providerAdded = true;
            Security.insertProviderAt(new BouncyCastleProvider(), 2);
        }
        return providerAdded;
    }

    protected void removeSecurityProvider() {
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
    }

    protected KeyFile getKeyFile(String privateKeyFile, String privateKeyPassPhrase) {
        KeyFile keyFile = null;
        if (privateKeyFile != null && !privateKeyFile.isEmpty()) {
            if (privateKeyPassPhrase != null && !privateKeyPassPhrase.isEmpty()) {
                keyFile = new KeyFile(privateKeyFile, privateKeyPassPhrase);
            } else {
                keyFile = new KeyFile(privateKeyFile);
            }
        }
        return keyFile;
    }

    protected SSHService getFromCache(SSHShellInputs sshShellInputs, String sessionId) {

        synchronized (sessionId) {
            return CacheUtils.getFromCache(sshShellInputs.getSshGlobalSessionObject().getResource(), sessionId);
        }
    }
    protected boolean saveToCache(GlobalSessionObject<Map<String, SSHConnection>> sessionParam, SSHService service, String sessionId) {
        if (sessionParam.getName() == null) {
            sessionParam.setName(Constants.SSH_SESSIONS_DEFAULT_ID);
        }
        return service.saveToCache(sessionParam, sessionId);
    }

    protected void populateResult(Map<String, String> returnResult, Throwable e) {
        returnResult.put(Constants.OutputNames.RETURN_RESULT, e.getMessage());
        returnResult.put(Constants.OutputNames.EXCEPTION, StringUtils.getStackTraceAsString(e));
        returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
    }
}
