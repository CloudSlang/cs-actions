package com.hp.score.content.mail.entities;

import javax.mail.Session;
import javax.mail.Transport;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by giloan on 10/30/2014.
 */
public class SendMail {

    public static final String RETURN_RESULT = "returnResult";
    public static final String RETURN_CODE = "returnCode";
    public static final String EXCEPTION = "exception";

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    public static final String SUCCESS_RETURN_CODE = "0";
    public static final String FAILURE_RETURN_CODE = "-1";

    //Operation inputs
    String attachments = "";
    String smtpHost;
    String from;
    String to;
    String cc;
    String bcc;
    String subject;
    String body;
    String delimiter;
    String user;
    String password;
    String charset;
    String transferEncoding;
    int smtpPort;
    boolean html = false;
    boolean readReceipt = false;

    //TODO
    public Map<String, String> execute(SendMailInputs sendMailInputs) throws Exception {
        Map<String, String> result = new HashMap<>();
        Session session = null;
        Transport transport = null;

        try {
            processInputs(sendMailInputs);
        }  catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("IOException")) {
                throw new Exception("Cannot attach '" + attachments + "' Check if the location exists and is accessible.");
            } else {
                throw e;
            }
        } finally {
            if (null != transport) transport.close();
        }
        return result;
    }

    private void processInputs(SendMailInputs sendMailInputs) {
        try {
            html = Boolean.parseBoolean(sendMailInputs.getHtmlEmail());
        } catch (Exception e) {
            html = false;
        }
        try {
            readReceipt = Boolean.parseBoolean(sendMailInputs.getRead_receipt());
        } catch (Exception e) {
            readReceipt = false;
        }
        smtpHost = sendMailInputs.getSMTPHostname();
        smtpPort = Integer.parseInt(sendMailInputs.getPort());
        from = sendMailInputs.getFrom();
        to = sendMailInputs.getTo();
        cc = sendMailInputs.getCc();
        bcc = sendMailInputs.getBcc();
        subject = sendMailInputs.getSubject();
        body = sendMailInputs.getBody();
        attachments = sendMailInputs.getAttachments();
        String delim = sendMailInputs.getDelimiter();
        delimiter = (delim == null || delim.equals("")) ? "," : delim;
        user = sendMailInputs.getUser();
        String pass = sendMailInputs.getPassword();
        password = (null == pass) ? "" : pass;
        charset = sendMailInputs.getCharacterset();
        transferEncoding = sendMailInputs.getContentTransferEncoding();
    }
}
