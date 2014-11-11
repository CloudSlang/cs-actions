package com.hp.score.content.mail.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.score.content.mail.entities.SendMailInputs;
import com.hp.score.content.mail.services.SendMail;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by giloan on 10/30/2014.
 */
public class SendMailAction {

    @Action(name = "Send Mail",
            outputs = {
                    @Output(SendMail.RETURN_RESULT),
                    @Output(SendMail.RETURN_CODE),
                    @Output(SendMail.EXCEPTION)
            },
            responses = {
                    @Response(text = SendMail.SUCCESS, field = SendMail.RETURN_CODE, value = SendMail.SUCCESS_RETURN_CODE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = SendMail.FAILURE, field = SendMail.RETURN_CODE, value = SendMail.FAILURE_RETURN_CODE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
	public Map<String, String> execute(
            @Param(SendMailInputs.HOSTNAME) String hostname,
            @Param(SendMailInputs.PORT) String port,
            @Param(SendMailInputs.HTML_EMAIL) String htmlEmail,
            @Param(SendMailInputs.FROM) String from,
            @Param(SendMailInputs.TO) String to,
            @Param(SendMailInputs.CC) String cc,
            @Param(SendMailInputs.BCC) String bcc,
            @Param(SendMailInputs.SUBJECT) String subject,
            @Param(SendMailInputs.BODY) String body,
            @Param(SendMailInputs.READ_RECEIPT) String readReceipt,
            @Param(SendMailInputs.ATTACHMENTS) String attachments,
            @Param(SendMailInputs.USER) String user,
            @Param(SendMailInputs.PASSWORD) String password,
            @Param(SendMailInputs.DELIMITER) String delimiter,
            @Param(SendMailInputs.CHARACTERSET) String characterSet,
            @Param(SendMailInputs.CONTENT_TRANSFER_ENCODING) String contentTransferEncoding
    ) throws Exception {

        SendMailInputs sendMailInputs = new SendMailInputs();
        sendMailInputs.setSmtpHostname(hostname);
        sendMailInputs.setPort(port);
        sendMailInputs.setHtmlEmail(htmlEmail);
        sendMailInputs.setFrom(from);
        sendMailInputs.setTo(to);
        sendMailInputs.setCc(cc);
        sendMailInputs.setBcc(bcc);
        sendMailInputs.setSubject(subject);
        sendMailInputs.setBody(body);
        sendMailInputs.setReadReceipt(readReceipt);
        sendMailInputs.setAttachments(attachments);
        sendMailInputs.setUser(user);
        sendMailInputs.setPassword(password);
        sendMailInputs.setDelimiter(delimiter);
        sendMailInputs.setCharacterset(characterSet);
        sendMailInputs.setContentTransferEncoding(contentTransferEncoding);

        try {
            return new SendMail().execute(sendMailInputs);
        } catch (Exception e) {
            return exceptionResult(e.getMessage(), e);
        }
	}

    private Map<String, String> exceptionResult(String message, Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String eStr = writer.toString().replace("" + (char) 0x00, "");

        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(SendMail.RETURN_RESULT, message);
        returnResult.put(SendMail.RETURN_CODE, SendMail.FAILURE_RETURN_CODE);
        returnResult.put(SendMail.EXCEPTION, eStr);
        return returnResult;
    }
}
