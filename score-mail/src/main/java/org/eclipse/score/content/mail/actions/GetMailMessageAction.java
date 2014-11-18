package org.eclipse.score.content.mail.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import org.eclipse.score.content.mail.entities.GetMailMessageInputs;
import org.eclipse.score.content.mail.services.GetMailMessage;

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
                    @Output(GetMailMessage.RETURN_CODE),
                    @Output(GetMailMessage.RETURN_RESULT),
                    @Output(GetMailMessage.SUBJECT),
                    @Output(GetMailMessage.BODY_RESULT),
                    @Output(GetMailMessage.ATTACHED_FILE_NAMES_RESULT),
                    @Output(GetMailMessage.EXCEPTION)
            },
            responses = {
                    @Response(text = GetMailMessage.SUCCESS, field = GetMailMessage.RETURN_CODE, value = GetMailMessage.SUCCESS_RETURN_CODE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = GetMailMessage.FAILURE, field = GetMailMessage.RETURN_CODE, value = GetMailMessage.FAILURE_RETURN_CODE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(
            @Param(value = GetMailMessageInputs.HOSTNAME, required = true) String hostname,
            @Param(value = GetMailMessageInputs.PORT) String port,
            @Param(value = GetMailMessageInputs.PROTOCOL) String protocol,
            @Param(value = GetMailMessageInputs.USERNAME, required = true) String username,
            @Param(value = GetMailMessageInputs.PASSWORD, required = true) String password,
            @Param(value = GetMailMessageInputs.FOLDER, required = true) String folder,
            @Param(value = GetMailMessageInputs.TRUSTALLROOTS) String trustAllRoots,
            @Param(value = GetMailMessageInputs.MESSAGE_NUMBER, required = true) String messageNumber,
            @Param(value = GetMailMessageInputs.SUBJECT_ONLY) String subjectOnly,
            @Param(value = GetMailMessageInputs.ENABLESSL) String enableSSL,
            @Param(value = GetMailMessageInputs.KEYSTORE) String keystore,
            @Param(value = GetMailMessageInputs.KEYSTORE_PASSWORD) String keystorePassword,
            @Param(value = GetMailMessageInputs.TRUST_KEYSTORE) String trustKeystore,
            @Param(value = GetMailMessageInputs.TRUST_PASSWORD) String trustPassword,
            @Param(value = GetMailMessageInputs.CHARACTER_SET) String characterSet,
            @Param(value = GetMailMessageInputs.DELETE_UPON_RETRIVAL) String deleteUponRetrieval
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
