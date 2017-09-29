/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.ssh.services.actions;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.ssh.entities.KeyFile;
import io.cloudslang.content.ssh.entities.SSHConnection;
import io.cloudslang.content.ssh.entities.SSHShellInputs;
import io.cloudslang.content.ssh.services.SSHService;
import io.cloudslang.content.ssh.utils.CacheUtils;
import io.cloudslang.content.ssh.utils.Constants;
import io.cloudslang.content.ssh.utils.StringUtils;
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
        if (sessionId != null) {
            synchronized (sessionId) {
                return CacheUtils.getFromCache(sshShellInputs.getSshGlobalSessionObject().getResource(), sessionId);
            }
        } else {
            return null;
        }
    }

    protected boolean saveToCache(GlobalSessionObject<Map<String, SSHConnection>> sessionParam, SSHService service, String sessionId) {
        if (sessionParam.getName() == null) {
            sessionParam.setName(Constants.SSH_SESSIONS_DEFAULT_ID);
        }
        return service.saveToCache(sessionParam, sessionId);
    }

    protected void populateResult(Map<String, String> returnResult, Throwable e) {
        returnResult.put(OutputNames.RETURN_RESULT, e.getMessage());
        returnResult.put(OutputNames.EXCEPTION, StringUtils.getStackTraceAsString(e));
        returnResult.put(OutputNames.RETURN_CODE, ReturnCodes.FAILURE);
    }
}
