/*
 * Copyright 2026-2027 Open Text
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

package io.cloudslang.content.winrm.client;

import io.cloudslang.content.winrm.client.kerberos.KerberosAuthenticator;
import io.cloudslang.content.winrm.client.ntlm.NtlmEngine;
import io.cloudslang.content.winrm.client.ntlm.NtlmSessionSecurity;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.DefaultHostnameVerifier;
import org.apache.hc.client5.http.ssl.HostnameVerificationPolicy;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.Timeout;
import org.w3c.dom.Document;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * HTTP transport for WS-Management messages, built on Apache HttpClient 5.
 * <p>
 * Authentication is handled here rather than by HttpClient, because WinRM uses the NTLM session keys to sign and
 * seal (encrypt) the SOAP payloads ("HTTP-SPNEGO-session-encrypted"), which requires access to the NTLM handshake.
 */
final class WinRmTransport implements Closeable {

    static final String SOAP_CONTENT_TYPE = "application/soap+xml;charset=UTF-8";

    private static final String NEGOTIATE = "Negotiate";
    private static final String NTLM = "NTLM";
    private static final String BASIC = "Basic";
    private static final int UNAUTHORIZED = 401;

    private final URI endpoint;
    private final AuthenticationScheme authenticationScheme;
    private final String username;
    private final String password;
    private final String domain;
    private final KerberosAuthenticator kerberosAuthenticator;
    private final CloseableHttpClient httpClient;
    private final HttpClientContext httpContext = HttpClientContext.create();

    private boolean authenticated;
    private NtlmSessionSecurity sessionSecurity;

    WinRmTransport(final Settings settings) {
        this.endpoint = URI.create(settings.endpoint);
        this.authenticationScheme = settings.authenticationScheme;
        this.username = settings.username;
        this.password = settings.password;
        this.domain = settings.domain;
        this.kerberosAuthenticator = authenticationScheme == AuthenticationScheme.KERBEROS
                ? new KerberosAuthenticator(endpoint.getHost(), username, password, settings.requestNewKerberosTicket)
                : null;
        this.httpClient = createHttpClient(settings);
    }

    /**
     * Sends a SOAP message and returns the parsed SOAP response.
     *
     * @throws IOException          if the server cannot be reached (connection failures may be retried)
     * @throws WinRmFaultException  if the server answered with a SOAP fault
     * @throws WinRmException       for any other unexpected server response
     */
    synchronized Document send(final String soapMessage) throws IOException {
        final byte[] soap = soapMessage.getBytes(StandardCharsets.UTF_8);
        final HttpResult result;
        switch (authenticationScheme) {
            case BASIC:
                result = post(soap, SOAP_CONTENT_TYPE, BASIC + " " + base64((username + ":" + password).getBytes(StandardCharsets.UTF_8)));
                break;
            case NTLM:
                result = sendNtlm(soap);
                break;
            case KERBEROS:
                result = sendKerberos(soap);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported authentication scheme " + authenticationScheme);
        }
        return handleResponse(result);
    }

    private HttpResult sendNtlm(final byte[] soap) throws IOException {
        if (authenticated) {
            final HttpResult result = sessionSecurity != null
                    ? post(sessionSecurity.encrypt(soap), NtlmSessionSecurity.ENCRYPTED_CONTENT_TYPE, null)
                    : post(soap, SOAP_CONTENT_TYPE, null);
            if (result.status != UNAUTHORIZED) {
                return result;
            }
            // the authenticated connection was closed by the server; authenticate a new one
            authenticated = false;
            sessionSecurity = null;
        }
        return ntlmHandshake(soap);
    }

    private HttpResult ntlmHandshake(final byte[] soap) throws IOException {
        String scheme = NEGOTIATE;
        int flags = NtlmEngine.DEFAULT_FLAGS | NtlmEngine.ENCRYPTION_FLAGS;
        HttpResult result = post(new byte[0], null, scheme + " " + base64(NtlmEngine.createNegotiateMessage(flags)));
        String challengeToken = result.challengeToken(scheme);
        if (result.status == UNAUTHORIZED && challengeToken == null && result.offersScheme(NTLM)) {
            // the server does not accept NTLM through Negotiate; payload encryption is only available with Negotiate
            scheme = NTLM;
            flags = NtlmEngine.DEFAULT_FLAGS;
            result = post(new byte[0], null, scheme + " " + base64(NtlmEngine.createNegotiateMessage(flags)));
            challengeToken = result.challengeToken(scheme);
        }
        if (result.status != UNAUTHORIZED || challengeToken == null) {
            if (result.status == UNAUTHORIZED) {
                throw new WinRmException("The server did not return an NTLM challenge; the NTLM authentication scheme " +
                        "may not be enabled on " + endpoint + " (offered schemes: " + result.authenticateHeaders + ")");
            }
            throw unexpectedResponse(result);
        }

        final NtlmEngine.ChallengeMessage challenge = NtlmEngine.parseChallengeMessage(Base64.getDecoder().decode(challengeToken));
        final NtlmEngine.AuthenticateMessage authenticate = new NtlmEngine().createAuthenticateMessage(challenge, username, password, domain);
        final String authorization = scheme + " " + base64(authenticate.getMessage());

        final boolean encrypt = NEGOTIATE.equals(scheme)
                && authenticate.getExportedSessionKey() != null
                && (authenticate.getFlags() & NtlmEngine.NEGOTIATE_SEAL) != 0;
        final NtlmSessionSecurity security = encrypt ? new NtlmSessionSecurity(authenticate.getExportedSessionKey(), authenticate.getFlags()) : null;
        sessionSecurity = security;
        result = security != null
                ? post(security.encrypt(soap), NtlmSessionSecurity.ENCRYPTED_CONTENT_TYPE, authorization)
                : post(soap, SOAP_CONTENT_TYPE, authorization);
        if (result.status == UNAUTHORIZED) {
            sessionSecurity = null;
            throw invalidCredentials();
        }
        authenticated = true;
        return result;
    }

    private HttpResult sendKerberos(final byte[] soap) throws IOException {
        if (authenticated) {
            final HttpResult result = post(soap, SOAP_CONTENT_TYPE, null);
            if (result.status != UNAUTHORIZED) {
                return result;
            }
            authenticated = false;
        }
        final HttpResult result = post(soap, SOAP_CONTENT_TYPE, NEGOTIATE + " " + base64(kerberosAuthenticator.createToken()));
        if (result.status == UNAUTHORIZED) {
            throw invalidCredentials();
        }
        authenticated = true;
        return result;
    }

    private Document handleResponse(final HttpResult result) throws IOException {
        if (result.status == UNAUTHORIZED) {
            throw invalidCredentials();
        }
        byte[] body = result.body;
        if (result.contentType != null && result.contentType.toLowerCase(Locale.ROOT).startsWith("multipart/encrypted")) {
            if (sessionSecurity == null) {
                throw new WinRmException("Received an encrypted response from " + endpoint + " but no encryption session is established");
            }
            body = sessionSecurity.decrypt(body);
        }

        Document document = null;
        if (body.length > 0) {
            try {
                document = WsManMessages.parse(body);
            } catch (WinRmException e) {
                if (result.status == 200) {
                    throw e;
                }
            }
        }
        if (document != null) {
            final WinRmFaultException fault = WsManMessages.getFault(document);
            if (fault != null) {
                throw fault;
            }
        }
        if (result.status != 200 || document == null) {
            throw unexpectedResponse(result);
        }
        return document;
    }

    private HttpResult post(final byte[] body, final String contentType, final String authorization) throws IOException {
        final HttpPost post = new HttpPost(endpoint);
        if (authorization != null) {
            post.setHeader(HttpHeaders.AUTHORIZATION, authorization);
        }
        if (contentType != null) {
            post.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        }
        post.setEntity(new ByteArrayEntity(body, null));
        return httpClient.execute(post, httpContext, response -> {
            final List<String> authenticateHeaders = new ArrayList<>();
            for (Header header : response.getHeaders(HttpHeaders.WWW_AUTHENTICATE)) {
                authenticateHeaders.add(header.getValue());
            }
            final Header responseContentType = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
            final HttpEntity entity = response.getEntity();
            return new HttpResult(response.getCode(), response.getReasonPhrase(), authenticateHeaders,
                    responseContentType == null ? null : responseContentType.getValue(),
                    entity == null ? new byte[0] : EntityUtils.toByteArray(entity));
        });
    }

    private WinRmException invalidCredentials() {
        authenticated = false;
        sessionSecurity = null;
        return new WinRmException("HTTP response '401: Unauthorized' when communicating with " + endpoint +
                ". Invalid credentials or incompatible authentication schemes.");
    }

    private IOException unexpectedResponse(final HttpResult result) {
        // an IOException (as with CXF) so that the failed operation is retried like other transport failures
        return new IOException("HTTP response '" + result.status + ": " + result.reasonPhrase +
                "' when communicating with " + endpoint);
    }

    private static CloseableHttpClient createHttpClient(final Settings settings) {
        // an explicit SSL context / hostname verifier takes precedence over disableCertificateChecks (as in winrm4j)
        final SSLContext sslContext;
        HostnameVerifier hostnameVerifier = settings.hostnameVerifier;
        if (settings.sslContext != null) {
            sslContext = settings.sslContext;
        } else if (settings.disableCertificateChecks) {
            sslContext = trustAllSslContext();
            if (hostnameVerifier == null) {
                hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            }
        } else {
            sslContext = SSLContexts.createSystemDefault();
        }
        if (hostnameVerifier == null) {
            hostnameVerifier = new DefaultHostnameVerifier();
        }

        // keep the protocols and cipher suites enabled by the SSL context (HttpClient would otherwise drop TLSv1 / TLSv1.1)
        final SSLParameters sslParameters = sslContext.getDefaultSSLParameters();
        final PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setTlsSocketStrategy(new DefaultClientTlsStrategy(sslContext, sslParameters.getProtocols(),
                        sslParameters.getCipherSuites(), null, HostnameVerificationPolicy.CLIENT, hostnameVerifier))
                .setDefaultConnectionConfig(ConnectionConfig.custom()
                        .setConnectTimeout(Timeout.ofMilliseconds(settings.connectionTimeout))
                        .setSocketTimeout(Timeout.ofMilliseconds(settings.receiveTimeout))
                        .build())
                .setMaxConnTotal(1)
                .setMaxConnPerRoute(1)
                .build();

        final HttpClientBuilder builder = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(Timeout.ofMilliseconds(settings.connectionRequestTimeout))
                        .setResponseTimeout(Timeout.ofMilliseconds(settings.receiveTimeout))
                        .build())
                // target authentication is performed by this class (see class documentation)
                .setTargetAuthenticationStrategy((challengeType, challenges, context) -> Collections.emptyList())
                .disableRedirectHandling()
                .disableAutomaticRetries()
                .disableContentCompression()
                .disableCookieManagement();

        if (settings.proxyHost != null && !settings.proxyHost.isEmpty()) {
            final int proxyPort = Integer.parseInt(settings.proxyPort);
            builder.setProxy(new HttpHost(settings.proxyHost, proxyPort));
            if (settings.proxyUsername != null && !settings.proxyUsername.isEmpty()
                    && settings.proxyPassword != null && !settings.proxyPassword.isEmpty()) {
                final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(new AuthScope(settings.proxyHost, proxyPort),
                        new UsernamePasswordCredentials(settings.proxyUsername, settings.proxyPassword.toCharArray()));
                builder.setDefaultCredentialsProvider(credentialsProvider);
            }
        }
        return builder.build();
    }

    private static SSLContext trustAllSslContext() {
        try {
            return SSLContexts.custom().loadTrustMaterial(TrustAllStrategy.INSTANCE).build();
        } catch (GeneralSecurityException e) {
            throw new WinRmException("Unable to create the SSL context: " + e.getMessage(), e);
        }
    }

    private static String base64(final byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

    static final class Settings {
        String endpoint;
        AuthenticationScheme authenticationScheme;
        String username;
        String password;
        String domain;
        boolean requestNewKerberosTicket;
        SSLContext sslContext;
        HostnameVerifier hostnameVerifier;
        boolean disableCertificateChecks;
        String proxyHost;
        String proxyPort;
        String proxyUsername;
        String proxyPassword;
        long connectionTimeout;
        long connectionRequestTimeout;
        long receiveTimeout;
    }

    private static final class HttpResult {
        private final int status;
        private final String reasonPhrase;
        private final List<String> authenticateHeaders;
        private final String contentType;
        private final byte[] body;

        private HttpResult(final int status, final String reasonPhrase, final List<String> authenticateHeaders,
                           final String contentType, final byte[] body) {
            this.status = status;
            this.reasonPhrase = reasonPhrase;
            this.authenticateHeaders = authenticateHeaders;
            this.contentType = contentType;
            this.body = body;
        }

        private boolean offersScheme(final String scheme) {
            for (String header : authenticateHeaders) {
                final String value = header.trim();
                if (value.equalsIgnoreCase(scheme) || value.regionMatches(true, 0, scheme + " ", 0, scheme.length() + 1)) {
                    return true;
                }
            }
            return false;
        }

        private String challengeToken(final String scheme) {
            for (String header : authenticateHeaders) {
                final String value = header.trim();
                if (value.length() > scheme.length() + 1 && value.regionMatches(true, 0, scheme + " ", 0, scheme.length() + 1)) {
                    final String token = value.substring(scheme.length() + 1).trim();
                    if (!token.isEmpty()) {
                        return token;
                    }
                }
            }
            return null;
        }
    }
}
