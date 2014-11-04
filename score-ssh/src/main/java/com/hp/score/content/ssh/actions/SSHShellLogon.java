package com.hp.score.content.ssh.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.score.content.ssh.entities.ConnectionDetails;
import com.hp.score.content.ssh.entities.KeyFile;
import com.hp.score.content.ssh.entities.SSHConnection;
import com.hp.score.content.ssh.services.SSHService;
import com.hp.score.content.ssh.services.impl.SSHServiceImpl;
import com.hp.score.content.ssh.utils.Constants;
import com.hp.score.content.ssh.utils.StringUtils;

import java.util.HashMap;
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

        Map<String, String> returnResult = new HashMap<>();
        boolean providerAdded = addSecurityProvider();

        try {
            // default values
            int portNumber = StringUtils.toInt(port, Constants.DEFAULT_PORT);
            boolean useGlobalContextBoolean = StringUtils.toBoolean(useGlobalContext, Constants.DEFAULT_USE_GLOBAL_CONTEXT);

            // configure ssh parameters
            ConnectionDetails connection = new ConnectionDetails(host, portNumber, username, password);
            KeyFile keyFile = getKeyFile(privateKeyFile, password);

            // get the cached SSH session
            String sessionId = "sshSession:" + host + "-" + portNumber + "-" + username;
            synchronized (sessionId) {
                SSHService service = getFromCache(globalSessionObject, sessionObject, useGlobalContextBoolean, sessionId);
                boolean saveSSHSession = false;
                if (service == null || !service.isConnected() || !service.isExpectChannelConnected()) {
                    saveSSHSession = true;
                    service = new SSHServiceImpl(connection, keyFile, Constants.DEFAULT_CONNECT_TIMEOUT, true);
                }
                // save SSH session in the cache
                if (saveSSHSession) {
                    saveToCache(globalSessionObject, sessionObject, useGlobalContextBoolean, service, sessionId);
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