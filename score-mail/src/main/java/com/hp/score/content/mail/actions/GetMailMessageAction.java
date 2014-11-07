package com.hp.score.content.mail.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.score.content.mail.actionsinp.GetMailMessageInputs;
import com.hp.score.content.mail.entities.GetMailMessage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by giloan on 11/3/2014.
 */
public class GetMailMessageAction {

    @Action(name = "Get Mail Message",
            outputs = {
                    @Output(GetMailMessage.RETURN_RESULT),
                    @Output(GetMailMessage.RETURN_CODE),
                    @Output(GetMailMessage.EXCEPTION)
            },
            responses = {
                    @Response(text = GetMailMessage.SUCCESS, field = GetMailMessage.RETURN_CODE, value = GetMailMessage.SUCCESS_RETURN_CODE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = GetMailMessage.FAILURE, field = GetMailMessage.RETURN_CODE, value = GetMailMessage.FAILURE_RETURN_CODE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(
            @Param(GetMailMessageInputs.HOSTNAME) String hostname,
            @Param(GetMailMessageInputs.PORT) String port,
            @Param(GetMailMessageInputs.PROTOCOL) String protocol,
            @Param(GetMailMessageInputs.USERNAME) String username,
            @Param(GetMailMessageInputs.PASSWORD) String password,
            @Param(GetMailMessageInputs.FOLDER) String folder,
            @Param(GetMailMessageInputs.TRUSTALLROOTS) String trustAllRoots,
            @Param(GetMailMessageInputs.MESSAGE_NUMBER) String messageNumber,
            @Param(GetMailMessageInputs.SUBJECT_ONLY) String subjectOnly,
            @Param(GetMailMessageInputs.ENABLESSL) String enableSSL,
            @Param(GetMailMessageInputs.KEYSTORE) String keystore,
            @Param(GetMailMessageInputs.KEYSTORE_PASSWORD) String keystorePassword,
            @Param(GetMailMessageInputs.TRUST_KEYSTORE) String trustKeystore,
            @Param(GetMailMessageInputs.TRUST_PASSWORD) String trustPassword,
            @Param(GetMailMessageInputs.CHARACTER_SET) String characterSet,
            @Param(GetMailMessageInputs.DELETE_UPON_RETRIVAL) String deleteUponRetrieval
    ) {
        GetMailMessageInputs getMailMessageInputs = new GetMailMessageInputs();
        getMailMessageInputs.setHostname(hostname);
        getMailMessageInputs.setPort(port);
        getMailMessageInputs.setProtocol(protocol);
        getMailMessageInputs.setUsername(username);
        getMailMessageInputs.setPassword(password);
        getMailMessageInputs.setFolder(folder);
        getMailMessageInputs.setTrustAllRoots(trustAllRoots);
        getMailMessageInputs.setMessageNumber(messageNumber);
        getMailMessageInputs.setSubjectOnly(subjectOnly);
        getMailMessageInputs.setEnableSSL(enableSSL);
        getMailMessageInputs.setKeystore(keystore);
        getMailMessageInputs.setKeystorePassword(keystorePassword);
        getMailMessageInputs.setTrustKeystore(trustKeystore);
        getMailMessageInputs.setTrustPassword(trustPassword);
        getMailMessageInputs.setCharacterSet(characterSet);
        getMailMessageInputs.setDeleteUponRetrieval(deleteUponRetrieval);

        try {
            return new GetMailMessage().execute(getMailMessageInputs);
        } catch (Exception e) {
            return exceptionResult(e.getMessage(), e);
        }
    }

    private Map<String, String> exceptionResult(String message, Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String eStr = writer.toString().replace("" + (char) 0x00, "");

        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(GetMailMessage.RETURN_RESULT, message);
        returnResult.put(GetMailMessage.RETURN_CODE, GetMailMessage.FAILURE_RETURN_CODE);
        returnResult.put(GetMailMessage.EXCEPTION, eStr);
        return returnResult;
    }
}
