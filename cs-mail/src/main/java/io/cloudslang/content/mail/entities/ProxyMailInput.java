package io.cloudslang.content.mail.entities;

public interface ProxyMailInput {
    String getProxyHost();

    String getProxyPort();

    String getProxyUsername();

    String getProxyPassword();

    String getProtocol();
}
