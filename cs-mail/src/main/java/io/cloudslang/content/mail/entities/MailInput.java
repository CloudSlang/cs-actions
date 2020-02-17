package io.cloudslang.content.mail.entities;

import java.util.List;

public interface MailInput {
    String getProxyHost();

    String getProxyPort();

    String getProxyUsername();

    String getProxyPassword();

    String getProtocol();

    List<String> getTlsVersions();

    List<String> getAllowedCiphers();
}
