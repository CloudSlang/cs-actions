package io.cloudslang.content.httpclient.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.NTCredentials;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;

import java.net.URI;
import java.util.Locale;

import static org.apache.hc.client5.http.auth.StandardAuthScheme.NTLM;

public class CustomCredentialsProvider {

    public static BasicCredentialsProvider getCredentialsProvider(HttpClientInputs httpInputs, URI uri) {
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        if (!StringUtils.isEmpty(httpInputs.getUsername())) {
            Credentials credentials;
            if (httpInputs.getAuthType().equalsIgnoreCase(NTLM)) {
                String[] domainAndUsername = getDomainUsername(httpInputs.getUsername());
                credentials = new NTCredentials(domainAndUsername[1], httpInputs.getPassword().toCharArray(), httpInputs.getHost(), domainAndUsername[0]);
            } else {
                credentials = new UsernamePasswordCredentials(httpInputs.getUsername(), httpInputs.getPassword().toCharArray());
            }

            credentialsProvider.setCredentials(new AuthScope(httpInputs.getHost(), uri.getPort() == -1 ? 443 : uri.getPort()), credentials);
        }

        //Auth Provider for Proxy
        if (!StringUtils.isEmpty(httpInputs.getProxyUsername())) {
            credentialsProvider.setCredentials(new AuthScope(httpInputs.getProxyHost(), Integer.parseInt(httpInputs.getProxyPort())),
                    new UsernamePasswordCredentials(httpInputs.getProxyUsername(), httpInputs.getProxyPassword().toCharArray()));
        }
        return credentialsProvider;
    }


    private static String[] getDomainUsername(String username) {
        int atBackSlash = username.indexOf('\\');
        String usernameWithoutDomain = username.substring(atBackSlash + 1);
        String domain = ".";
        if (atBackSlash >= 0) {
            domain = username.substring(0, atBackSlash).toUpperCase(Locale.ENGLISH);
        }

        return new String[]{domain, usernameWithoutDomain};
    }
}
