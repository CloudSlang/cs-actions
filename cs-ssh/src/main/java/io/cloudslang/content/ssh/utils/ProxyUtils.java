package io.cloudslang.content.ssh.utils;

import com.jcraft.jsch.ProxyHTTP;
import io.cloudslang.content.ssh.entities.ProxyConnectionDetails;

public class ProxyUtils {

    public static ProxyHTTP getHTTPProxy(String proxyHost, String proxyPort, String proxyUsername, String proxyPassword) {
        if (!StringUtils.isEmpty(proxyHost)) {
            int portForProxy = getPortValue(proxyPort, Constants.DEFAULT_PROXY_PORT);
            return createHTTPProxy(proxyHost, portForProxy, proxyUsername, proxyPassword);
        } else {
            return null;
        }
    }

    public static ProxyHTTP createHTTPProxy(String proxyHost, int proxyPort, String proxyUsername, String proxyPassword){
        ProxyConnectionDetails proxyConnectionDetails = new ProxyConnectionDetails(proxyHost, proxyPort, proxyUsername, proxyPassword);
        ProxyHTTP proxyHTTP = new ProxyHTTP(proxyConnectionDetails.getProxyHost(), proxyConnectionDetails.getProxyPort());
        String username = (StringUtils.isEmpty(proxyUsername) ? null : proxyUsername);
        String password = (StringUtils.isEmpty(proxyPassword) ? null : proxyPassword);
        proxyHTTP.setUserPasswd(username, password);
        return proxyHTTP;
    }

    public static int getPortValue(String port, int defaultValue){
        if(StringUtils.isEmpty(port)) {
            return defaultValue;
        }
        else {
            return StringUtils.validatePortNumber(port, Constants.PROXY_PORT);
        }
    }
}
