package io.cloudslang.content.mail.entities;

public interface GetMailInput extends MailInput {

    int getTimeout();

    String getPassword();

    String getUsername();

    boolean isEnableTLS();

    boolean isEnableSSL();

    boolean isTrustAllRoots();

    String getKeystore();

    String getKeystorePassword();

    Short getPort();

    String getHostname();

}
