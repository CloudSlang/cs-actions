/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.winrm.service;

import io.cloudslang.content.winrm.entities.WinRMInputs;
import io.cloudsoft.winrm4j.client.WinRmClientContext;
import io.cloudsoft.winrm4j.winrm.WinRmTool;
import io.cloudsoft.winrm4j.winrm.WinRmToolResponse;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import sun.security.krb5.KrbException;

import javax.net.ssl.*;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.STDERR;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static io.cloudslang.content.winrm.utils.Constants.*;
import static io.cloudslang.content.winrm.utils.Outputs.WinRMOutputs.COMMAND_EXIT_CODE;
import static io.cloudslang.content.winrm.utils.Outputs.WinRMOutputs.STDOUT;

public class WinRMService {

    public static Map<String, String> execute(WinRMInputs winRMInputs) throws KrbException, IOException, InvalidPathException {

        boolean useHttps = useHttps(winRMInputs);
        if (winRMInputs.getAuthType().equalsIgnoreCase(KERBEROS)) {
            if (!winRMInputs.getUseSubjectCredsOnly().isEmpty())
                System.setProperty("javax.security.auth.useSubjectCredsOnly", winRMInputs.getUseSubjectCredsOnly());
            else
                System.setProperty("javax.security.auth.useSubjectCredsOnly", "true");

            if (!winRMInputs.getKerberosConfFile().isEmpty()) {
                if (new File(winRMInputs.getKerberosConfFile()).exists()) {
                    System.setProperty("java.security.krb5.conf", winRMInputs.getKerberosConfFile());
                } else {
                    BufferedWriter bw = null;
                    try {
                        File tempFile = Files.createTempFile("krb5", ".conf").toFile();
                        bw = new BufferedWriter(new FileWriter(tempFile));
                        bw.write(winRMInputs.getKerberosConfFile().replace("\\n", System.getProperty("line.separator")));
                        tempFile.deleteOnExit();
                        System.setProperty("java.security.krb5.conf", tempFile.getAbsolutePath());
                    } finally {
                        if (bw != null)
                            bw.close();
                    }
                }
                sun.security.krb5.Config.refresh();
            }
            if (!winRMInputs.getKerberosLoginConfFile().isEmpty()) {
                if (new File(winRMInputs.getKerberosLoginConfFile()).exists()) {
                    System.setProperty("java.security.auth.login.config", winRMInputs.getKerberosLoginConfFile());
                } else {
                    BufferedWriter bw = null;
                    try {
                        File tempFile = Files.createTempFile("login", ".conf").toFile();
                        bw = new BufferedWriter(new FileWriter(tempFile));
                        bw.write(winRMInputs.getKerberosLoginConfFile().replace("\\n", System.getProperty("line.separator")));
                        tempFile.deleteOnExit();
                        System.setProperty("java.security.auth.login.config", tempFile.getAbsolutePath());
                    } finally {
                        if (bw != null)
                            bw.close();
                    }
                }
            } else
                System.setProperty("java.security.auth.login.config", "");
        }
        WinRmTool.Builder builder;

        if (winRMInputs.getDomain().isEmpty())
            builder = WinRmTool.Builder.builder(winRMInputs.getHost(), winRMInputs.getUsername(), winRMInputs.getPassword());
        else
            builder = WinRmTool.Builder.builder(winRMInputs.getHost(), winRMInputs.getDomain(), winRMInputs.getUsername(), winRMInputs.getPassword());
        builder.authenticationScheme(authScheme(winRMInputs.getAuthType()))
                .port(Integer.parseInt(winRMInputs.getPort()))
                .useHttps(useHttps)
                .disableCertificateChecks(Boolean.parseBoolean(winRMInputs.getTrustAllRoots()));

        if (!winRMInputs.getWorkingDirectory().isEmpty())
            builder.workingDirectory(winRMInputs.getWorkingDirectory());

        if (winRMInputs.getAuthType().equalsIgnoreCase(KERBEROS))
            builder.requestNewKerberosTicket(Boolean.parseBoolean(winRMInputs.getRequestNewKerberosTicket()));

        if (!Boolean.parseBoolean(winRMInputs.getTrustAllRoots())) {
            try {
                System.setProperty("javax.net.ssl.keyStore", winRMInputs.getKeystore());
                System.setProperty("javax.net.ssl.keyStorePassword", winRMInputs.getKeystorePassword());
                System.setProperty("javax.net.ssl.trustStore", winRMInputs.getTrustKeystore());
                System.setProperty("javax.net.ssl.trustStorePassword", winRMInputs.getTrustPassword());

                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(new FileInputStream(winRMInputs.getKeystore()), winRMInputs.getKeystorePassword().toCharArray());
                // Create key manager
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
                keyManagerFactory.init(keyStore, winRMInputs.getKeystorePassword().toCharArray());
                KeyManager[] km = keyManagerFactory.getKeyManagers();
                // Create trust manager
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
                trustManagerFactory.init(keyStore);
                TrustManager[] tm = trustManagerFactory.getTrustManagers();
                // Initialize SSLContext
                SSLContext sslContext = SSLContext.getInstance(tlsVersion(winRMInputs.getTlsVersion()));
                sslContext.init(km, tm, new SecureRandom());
                sslContext.createSSLEngine();

                builder.sslContext(sslContext);
                builder.hostnameVerifier(x509HostnameVerifier(winRMInputs.getX509HostnameVerifier()));
            } catch (Exception exception) {
                return getFailureResultsMap(exception);
            }
        } else {
            try {
                SSLContext sslContext = SSLContext.getInstance(tlsVersion(winRMInputs.getTlsVersion()));
                sslContext.init(null, new TrustManager[]{getTrustAllRoots()}, new SecureRandom());
                sslContext.createSSLEngine();

                builder.sslContext(sslContext);

            } catch (Exception exception) {
                return getFailureResultsMap(exception);
            }
        }

        long userTimeout = winRMInputs.getOperationTimeout() * 1000L;
        StopWatch watch = new StopWatch();
        WinRmToolResponse res;
        WinRmClientContext context = null;
        try {
            context = WinRmClientContext.newInstance();
            WinRmTool tool = builder.context(context).build();
            watch.start();
            if (winRMInputs.getCommandType().equalsIgnoreCase(CMD))
                res = tool.executeCommand(winRMInputs.getCommand());
            else {
                if (winRMInputs.getConfigurationName().isEmpty())
                    res = tool.executePs(winRMInputs.getCommand());
                else
                    res = tool.executeCommand(compilePs(winRMInputs.getCommand(), winRMInputs.getConfigurationName()), new StringWriter(), new StringWriter());
            }
            watch.stop();
            long result = watch.getTime();
            if (result > userTimeout) {
                Thread.currentThread().interrupt();
                return getFailureResultsMap(EXCEPTION_TIMED_OUT);
            }
        } finally {
            if (context != null)
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
        results.put(COMMAND_EXIT_CODE, String.valueOf(res.getStatusCode()));
        return results;
    }

    private static boolean useHttps(WinRMInputs winRMInputs) {
        boolean useHttps = false;
        if (winRMInputs.getProtocol().equalsIgnoreCase(HTTPS)) {
            useHttps = true;
            if (!winRMInputs.getProxyHost().isEmpty()) {
                System.setProperty("https.proxyHost", winRMInputs.getProxyHost());
                System.setProperty("https.proxyPort", winRMInputs.getProxyPort());
            } else {
                System.setProperty("https.proxyHost", "");
                System.setProperty("https.proxyPort", "");
            }
            if (!winRMInputs.getProxyUsername().isEmpty()) {
                System.setProperty("https.proxyUser", winRMInputs.getProxyUsername());
                System.setProperty("https.proxyPassword", winRMInputs.getProxyPassword());
            } else {
                System.setProperty("https.proxyUser", "");
                System.setProperty("https.proxyPassword", "");
            }
        } else if (winRMInputs.getProtocol().equalsIgnoreCase(HTTP)) {
            if (!winRMInputs.getProxyHost().isEmpty()) {
                System.setProperty("http.proxyHost", winRMInputs.getProxyHost());
                System.setProperty("http.proxyPort", winRMInputs.getProxyPort());
            } else {
                System.setProperty("http.proxyHost", "");
                System.setProperty("http.proxyPort", "");
            }
            if (!winRMInputs.getProxyUsername().isEmpty()) {
                System.setProperty("http.proxyUser", winRMInputs.getProxyUsername());
                System.setProperty("http.proxyPassword", winRMInputs.getProxyPassword());
            } else {
                System.setProperty("http.proxyUser", "");
                System.setProperty("http.proxyPassword", "");
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

    private static String tlsVersion(String tlsVersion) {
        String tls_version = tlsVersion;
        switch (tlsVersion.toLowerCase()) {
            case "tlsv1":
                tls_version = "TLSv1";
                break;
            case "tlsv1.1":
                tls_version = "TLSv1.1";
                break;
            case "tlsv1.2":
                tls_version = "TLSv1.2";
                break;
            case "tlsv1.3":
                tls_version = "TLSv1.3";
                break;
        }
        return tls_version;
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

    private static TrustManager getTrustAllRoots() {
        return new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
    }

    private static String compilePs(String psScript, String configurationName) {
        byte[] cmd = psScript.getBytes(StandardCharsets.UTF_16LE);
        String arg = DatatypeConverter.printBase64Binary(cmd);
        return "powershell -ConfigurationName " + configurationName + " -encodedcommand " + arg;
    }

}