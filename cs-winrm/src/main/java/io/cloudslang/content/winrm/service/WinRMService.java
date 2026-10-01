/*
 * Copyright 2021-2024 Open Text
 * This program and the accompanying materials
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
import org.metricshub.winrm.WinRMClient;
import org.metricshub.winrm.AuthScheme;
import org.metricshub.winrm.CommandResult;

import javax.net.ssl.*;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.UnrecoverableKeyException;
import java.time.Duration;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.STDERR;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static io.cloudslang.content.winrm.utils.Constants.*;
import static io.cloudslang.content.winrm.utils.Outputs.WinRMOutputs.COMMAND_EXIT_CODE;
import static io.cloudslang.content.winrm.utils.Outputs.WinRMOutputs.STDOUT;

public class WinRMService {

    public static Map<String, String> execute(WinRMInputs winRMInputs) throws Exception {
        try {
            // Configure Kerberos if needed
            if (winRMInputs.getAuthType().equalsIgnoreCase(KERBEROS)) {
                configureKerberos(winRMInputs);
            }

            // Build WinRM client
            WinRMClient.Builder builder = WinRMClient.builder(winRMInputs.getHost());

            // Set credentials
            builder.credentials(winRMInputs.getUsername(), winRMInputs.getPassword().toCharArray());

            // Set authentication scheme
            AuthScheme authScheme = getAuthScheme(winRMInputs.getAuthType());
            builder.authentication(authScheme);

            // Set port and protocol
            int port = Integer.parseInt(winRMInputs.getPort());
            boolean useHttps = winRMInputs.getProtocol().equalsIgnoreCase(HTTPS);
            if (useHttps) {
                builder.https().port(port);
            } else {
                builder.port(port);
            }

            // Set timeout
            long timeoutSeconds = winRMInputs.getOperationTimeout();
            builder.timeout(Duration.ofSeconds(timeoutSeconds));

            // Handle SSL/TLS configuration
            if (useHttps) {
                if (Boolean.parseBoolean(winRMInputs.getTrustAllRoots())) {
                    builder.trustAllCertificates();
                } else {
                    configureSSLContext(builder, winRMInputs);
                }
            }

            // Execute command using try-with-resources
            try (WinRMClient client = builder.build()) {
                CommandResult result;

                if (winRMInputs.getCommandType().equalsIgnoreCase(CMD)) {
                    // Execute CMD command
                    if (!winRMInputs.getWorkingDirectory().isEmpty()) {
                        result = client.command(winRMInputs.getCommand())
                                .workingDirectory(winRMInputs.getWorkingDirectory())
                                .execute();
                    } else {
                        result = client.command(winRMInputs.getCommand()).execute();
                    }
                } else {
                    // Execute PowerShell command
                    if (!winRMInputs.getConfigurationName().isEmpty()) {
                        // Encoded command with configuration name
                        String encodedCmd = encodeCommand(winRMInputs.getCommand(), winRMInputs.getConfigurationName());
                        result = client.command(encodedCmd).execute();
                    } else {
                        result = client.powerShell(winRMInputs.getCommand()).execute();
                    }
                }

                // Process and return results
                return processResults(result);
            }

        } catch (Exception e) {
            return getFailureResultsMap("WinRM Error: " + e.getMessage());
        }
    }

    /**
     * Configure Kerberos authentication settings
     */
    private static void configureKerberos(WinRMInputs winRMInputs) throws Exception {
        if (!winRMInputs.getKerberosConfFile().isEmpty()) {
            if (new File(winRMInputs.getKerberosConfFile()).exists()) {
                System.setProperty("java.security.krb5.conf", winRMInputs.getKerberosConfFile());
            } else {
                // Treat as content and create temp file
                BufferedWriter bw = null;
                try {
                    File tempFile = Files.createTempFile(KRB5, CONF).toFile();
                    bw = new BufferedWriter(new FileWriter(tempFile));
                    bw.write(winRMInputs.getKerberosConfFile().replace(SLASH_NEW_LINE, System.getProperty(LINE_SEPARATOR)));
                    tempFile.deleteOnExit();
                    System.setProperty("java.security.krb5.conf", tempFile.getAbsolutePath());
                } finally {
                    if (bw != null) {
                        bw.close();
                    }
                }
            }
            sun.security.krb5.Config.refresh();
        }
    }

    /**
     * Configure SSL context with custom keystore
     */
    private static void configureSSLContext(WinRMClient.Builder builder, WinRMInputs winRMInputs) 
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, 
                   UnrecoverableKeyException, java.io.IOException {
        try {
            KeyStore keyStore = KeyStore.getInstance(JKS);
            keyStore.load(
                Files.newInputStream(Paths.get(winRMInputs.getKeystore())),
                winRMInputs.getKeystorePassword().toCharArray()
            );

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(SunX509);
            keyManagerFactory.init(keyStore, winRMInputs.getKeystorePassword().toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(SunX509);
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance(tlsVersion(winRMInputs.getTlsVersion()));
            sslContext.init(
                keyManagerFactory.getKeyManagers(),
                trustManagerFactory.getTrustManagers(),
                new SecureRandom()
            );

            builder.sslContext(sslContext);
        } catch (Exception e) {
            throw new RuntimeException("SSL context configuration failed: " + e.getMessage(), e);
        }
    }

    /**
     * Map authentication type string to AuthScheme enum
     */
    private static AuthScheme getAuthScheme(String authType) {
        if (authType.equalsIgnoreCase(KERBEROS)) {
            return AuthScheme.KERBEROS;
        } else {
            // Default to NTLM for both "NTLM" and "BASIC"
            return AuthScheme.NTLM;
        }
    }

    /**
     * Encode PowerShell command with configuration name
     */
    private static String encodeCommand(String command, String configurationName) {
        byte[] cmd = command.getBytes(StandardCharsets.UTF_16LE);
        String encoded = DatatypeConverter.printBase64Binary(cmd);
        return "powershell -ConfigurationName " + configurationName + " -encodedcommand " + encoded;
    }

    /**
     * Convert TLS version string to standard Java format
     */
    private static String tlsVersion(String tlsVersion) {
        switch (tlsVersion.toLowerCase()) {
            case "tlsv1":
                return TLSv1;
            case "tlsv1.1":
                return TLSv1_1;
            case "tlsv1.2":
                return TLSv1_2;
            case "tlsv1.3":
                return TLSv1_3;
            default:
                return TLSv1_2;
        }
    }

    /**
     * Process CommandResult into output map
     */
    private static Map<String, String> processResults(CommandResult result) {
        Map<String, String> resultMap = getSuccessResultsMap(result.stdout());
        resultMap.put(STDOUT, result.stdout());
        resultMap.put(STDERR, result.stderr());
        resultMap.put(COMMAND_EXIT_CODE, String.valueOf(result.exitCode()));
        return resultMap;
    }
}