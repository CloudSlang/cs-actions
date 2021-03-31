package io.cloudslang.content.winrm.service;

import io.cloudslang.content.winrm.entities.WinRMInputs;
import io.cloudslang.content.winrm.utils.MySSLSocketFactory;
import io.cloudsoft.winrm4j.client.WinRmClientContext;
import io.cloudsoft.winrm4j.winrm.WinRmTool;
import io.cloudsoft.winrm4j.winrm.WinRmToolResponse;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;

import javax.net.ssl.HostnameVerifier;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.STDERR;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static io.cloudslang.content.winrm.utils.Outputs.WinRMOutputs.SCRIPT_EXIT_CODE;
import static io.cloudslang.content.winrm.utils.Outputs.WinRMOutputs.STDOUT;

public class WinRMService {

    public static Map<String, String> execute(WinRMInputs winRMInputs) {

        WinRmClientContext context = WinRmClientContext.newInstance();
        boolean useHttps = useHttps(winRMInputs);

        WinRmTool.Builder builder = WinRmTool.Builder.builder(winRMInputs.getHost(), winRMInputs.getUsername(), winRMInputs.getPassword())
                .context(context)
                .authenticationScheme(authScheme(winRMInputs.getAuthType()))
                .port(Integer.parseInt(winRMInputs.getPort()))
                .useHttps(useHttps)
                .disableCertificateChecks(Boolean.parseBoolean(winRMInputs.getTrustAllRoots()));

        if(!winRMInputs.getWorkingDirectory().isEmpty())
            builder.workingDirectory(winRMInputs.getWorkingDirectory());
        if (winRMInputs.getAuthType().equalsIgnoreCase("kerberos"))
            builder.requestNewKerberosTicket(Boolean.parseBoolean(winRMInputs.getRequestNewKerberosToken()));

        if (!Boolean.parseBoolean(winRMInputs.getTrustAllRoots())) {
            try {
                MySSLSocketFactory.setTrustAllRoots(Boolean.parseBoolean(winRMInputs.getTrustAllRoots()));
                MySSLSocketFactory.setKeystore(winRMInputs.getKeystore());
                MySSLSocketFactory.setKeystorePassword(winRMInputs.getKeystorePassword());
                MySSLSocketFactory.setTrustKeystore(winRMInputs.getTrustKeystore());
                MySSLSocketFactory.setTrustPassword(winRMInputs.getTrustPassword());

                builder.sslSocketFactory(new MySSLSocketFactory());
                builder.hostnameVerifier(x509HostnameVerifier(winRMInputs.getX509HostnameVerifier()));
            } catch (Exception exception) {
                return getFailureResultsMap(exception);
            }
        }

        WinRmTool tool = builder.build();

        long userTimeout = winRMInputs.getOperationTimeout();
        StopWatch watch = new StopWatch();
        watch.start();
        WinRmToolResponse res = tool.executePs(winRMInputs.getScript());
        watch.stop();

        long result = watch.getTime();
        if (result > userTimeout) {
            context.shutdown();
            Thread.currentThread().interrupt();
            return getFailureResultsMap("Operation timed out.");
        } else {
            context.shutdown();
        }

        Map<String, String> results;
        if (res.getStatusCode() == 0) {
            results = getSuccessResultsMap(res.getStdOut());
            results.put(STDOUT, res.getStdOut());
        } else {
            results = getFailureResultsMap(res.getStdErr());
            results.put(STDERR, res.getStdErr());
        }
        results.put(SCRIPT_EXIT_CODE, String.valueOf(res.getStatusCode()));
        return results;
    }

    private static boolean useHttps(WinRMInputs winRMInputs) {
        boolean useHttps = false;
        if (winRMInputs.getProtocol().equalsIgnoreCase("https")) {
            useHttps = true;
            if (!winRMInputs.getProxyHost().isEmpty()) {
                System.setProperty("https.proxyHost", winRMInputs.getProxyHost());
                System.setProperty("https.proxyPort", "8080");
            }
            if (!winRMInputs.getProxyUsername().isEmpty()) {
                System.setProperty("https.proxyUser", winRMInputs.getProxyUsername());
                System.setProperty("https.proxyPassword", winRMInputs.getProxyPassword());
            }
        } else if (winRMInputs.getProtocol().equalsIgnoreCase("http")) {
            if (!winRMInputs.getProxyHost().isEmpty()) {
                System.setProperty("http.proxyHost", winRMInputs.getProxyHost());
                System.setProperty("http.proxyPort", "8080");
            }
            if (!winRMInputs.getProxyUsername().isEmpty()) {
                System.setProperty("http.proxyUser", winRMInputs.getProxyUsername());
                System.setProperty("http.proxyPassword", winRMInputs.getProxyPassword());
            }
        }
        return useHttps;
    }

    private static String authScheme(String authScheme) {
        String authType = authScheme;
        switch (authType.toLowerCase()) {
            case "basic":
                authType = AuthSchemes.BASIC;
                break;
            case "ntlm":
                authType = AuthSchemes.NTLM;
                break;
            case "kerberos":
                authType = AuthSchemes.KERBEROS;
                break;
        }
        return authType;
    }

    private static HostnameVerifier x509HostnameVerifier(String hostnameVerifier) {
        String x509HostnameVerifierStr = hostnameVerifier.toLowerCase();

        X509HostnameVerifier x509HostnameVerifier = SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER;
        ;
        switch (x509HostnameVerifierStr) {
            case "strict":
                x509HostnameVerifier = SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER;
                break;
            case "browser_compatible":
                x509HostnameVerifier = SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
                break;
            case "allow_all":
                x509HostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                break;
        }
        return x509HostnameVerifier;
    }
}