package com.hp.score.content.ssh.services.actions;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SessionResource;
import com.hp.score.content.ssh.entities.KeyFile;
import com.hp.score.content.ssh.entities.SSHConnection;
import com.hp.score.content.ssh.services.SSHService;
import com.hp.score.content.ssh.services.impl.SSHServiceImpl;
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

    protected KeyFile getKeyFile(String privateKeyFile, String privateKeyPassphrase) {
        KeyFile keyFile = null;
        if (privateKeyFile != null && !privateKeyFile.isEmpty()) {
            if (privateKeyPassphrase != null && !privateKeyPassphrase.isEmpty()) {
                keyFile = new KeyFile(privateKeyFile, privateKeyPassphrase);
            } else {
                keyFile = new KeyFile(privateKeyFile);
            }
        }
        return keyFile;
    }

    protected SSHService getFromCache(GlobalSessionObject<?> globalSessionObject, GlobalSessionObject<?> sessionObject, String sessionId) { //TODO SessionObject?
        SSHService service = SSHServiceImpl.getFromCache(sessionObject, sessionId);
        synchronized (sessionId) {
            if (service == null) {
                service = SSHServiceImpl.getFromCache(globalSessionObject, sessionId);
            }
            return service;
        }
    }

    protected SSHService getFromCache(GlobalSessionObject<?> globalSessionObject, GlobalSessionObject<?> sessionObject, boolean useGlobalContextBoolean, String sessionId) {//TODO SessionObject?
        if (useGlobalContextBoolean) {
            return SSHServiceImpl.getFromCache(globalSessionObject, sessionId);
        }
        incrementSessionsCounter(globalSessionObject, sessionId);
        return SSHServiceImpl.getFromCache(sessionObject, sessionId);
    }

    private void incrementSessionsCounter(GlobalSessionObject<?> globalSessionObject, String sessionId) {
        SessionResource resource = globalSessionObject.getResource();
        if (resource != null) {
            Map<String, SSHConnection> tempMap = (Map<String, SSHConnection>) resource.get();
            if (tempMap != null) {
                SSHConnection connection = tempMap.get(sessionId);
                synchronized (connection) {
                    int oldValue = connection.getSessionsCounter();
                    connection.setSessionsCounter(++oldValue);
                }
            }
        }
    }

    protected void saveToCache(GlobalSessionObject<?> globalSessionObject, GlobalSessionObject<?> sessionObject, boolean useGlobalContextBoolean, SSHService service, String sessionId) {//TODO SessionObject?
        if (useGlobalContextBoolean) {
            if (globalSessionObject.getName() == null) {
                globalSessionObject.setName(Constants.SSH_SESSIONS_DEFAULT_ID);
            }
            service.saveToCache(globalSessionObject, sessionId);
        } else {
            if (sessionObject.getName() == null) {
                sessionObject.setName(Constants.SSH_SESSIONS_DEFAULT_ID);
            }
            service.saveToCache(sessionObject, sessionId);
        }
    }

    protected void populateResult(Map<String, String> returnResult, Throwable e) {
        returnResult.put(Constants.OutputNames.RETURN_RESULT, e.getMessage());
        returnResult.put(Constants.OutputNames.EXCEPTION, StringUtils.getStackTraceAsString(e));
        returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
    }
}
