package io.cloudslang.content.azure.utils;

import io.cloudslang.content.utils.StringUtilities;
import org.jetbrains.annotations.NotNull;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

import static io.cloudslang.content.azure.utils.Constants.PROXY_HTTP_PASSWORD;
import static io.cloudslang.content.azure.utils.Constants.PROXY_HTTP_USER;
import static java.net.Proxy.Type.HTTP;

/**
 * Created by victor on 31.10.2016.
 */
public class HttpUtils {
    @NotNull
    public static Proxy getProxy(@NotNull final String proxyHost, final int proxyPort, @NotNull final String proxyUser, @NotNull final String proxyPassword) {
        if (StringUtilities.isBlank(proxyHost)) {
            return Proxy.NO_PROXY;
        }
        if (StringUtilities.isNotEmpty(proxyUser)) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                }
            });
            System.setProperty(PROXY_HTTP_USER, proxyUser);
            System.setProperty(PROXY_HTTP_PASSWORD, proxyPassword);
        }

        return new Proxy(HTTP, InetSocketAddress.createUnresolved(proxyHost, proxyPort));
    }
}
