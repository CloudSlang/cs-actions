package com.hp.score.content.ssh.actions;

import com.hp.score.content.ssh.entities.SSHConnection;
import com.hp.score.content.ssh.services.SSHService;
import com.hp.score.content.ssh.utils.Constants;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;


import java.util.HashMap;
import java.util.Map;

/**
 * @author ioanvranauhp
 * Date: 11/03/14
 */
public class SSHShellLogoff extends SSHShellAbstract {

    @Action(name = "SSH Shell Logoff",
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
    public Map<String, String> sshLogoff(
            @Param(Constants.SSH_SESSIONS_DEFAULT_ID) GlobalSessionObject<Map<String, SSHConnection>> globalSessionObject,
            @Param(Constants.SSH_SESSIONS_DEFAULT_ID) GlobalSessionObject<Map<String, SSHConnection>> sessionObject, // TODO Session object
            @Param(value = Constants.SESSION_ID, required = true) String sessionId) {

        Map<String, String> returnResult = new HashMap<>();
        try {
            // get the cached SSH session
            SSHService service = getFromCache(globalSessionObject, sessionObject, sessionId);
            if (service == null) {
                throw new RuntimeException("Could not find sessionId in the session context.");
            }

            // close the SSH session
            service.close();
            service.removeFromCache(globalSessionObject, sessionId);
            service.removeFromCache(sessionObject, sessionId);

            returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
        } catch (Exception e) {
            populateResult(returnResult, e);
        }
        return returnResult;
    }
}
