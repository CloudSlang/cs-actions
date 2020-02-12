package io.cloudslang.content.mail.constants;

public final class SmtpPropNames {
    public static final String SMTP = "smtp";
    private static final String MAIL_SMTP = "mail.smtp.";
    public static final String TIMEOUT = MAIL_SMTP + "timeout";
    public static final String START_TLS_ENABLE = MAIL_SMTP + "starttls.enable";
    public static final String AUTH = MAIL_SMTP + "auth";
    public static final String PASSWORD = MAIL_SMTP + "password";
    public static final String USER = MAIL_SMTP + "user";
    public static final String PORT = MAIL_SMTP + "port";
    public static final String HOST = MAIL_SMTP + "host";
}
