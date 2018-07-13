package io.cloudslang.content.alibaba.utils;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ProxyUtil {
    public static void setProxies(final String proxyHost, final String proxyPort, final String proxyUsername, final String proxyPassword) {
        System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyPort);
        System.setProperty("https.proxyPort", proxyPort);
        System.setProperty("http.proxyUser", proxyUsername);
        System.setProperty("https.proxyUser", proxyUsername);
        System.setProperty("http.proxyPassword", proxyPassword);
        System.setProperty("https.proxyPassword", proxyPassword);
    }

    public static void clearProxy() {
        System.setProperty("http.proxyHost", EMPTY);
        System.setProperty("https.proxyHost", EMPTY);
        System.setProperty("http.proxyPort", EMPTY);
        System.setProperty("https.proxyPort", EMPTY);
        System.setProperty("http.proxyUser", EMPTY);
        System.setProperty("https.proxyUser", EMPTY);
        System.setProperty("http.proxyPassword", EMPTY);
        System.setProperty("https.proxyPassword", EMPTY);
    }
}
