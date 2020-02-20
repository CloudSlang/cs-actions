/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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



package io.cloudslang.content.httpclient.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.Constants.CHANGEIT;
import static io.cloudslang.content.httpclient.entities.Constants.DEFAULT_JAVA_KEYSTORE;
import static io.cloudslang.content.httpclient.entities.Constants.TLSv12;
import static io.cloudslang.content.httpclient.services.HttpClientService.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/15/14
 */
public class HttpClientAction {


    /**
     * This operation does an http request and a parsing of the response.
     * It provides features like: http authentication, http secure, connection pool, cookies, proxy.
     * To accomplish this it uses the third parties from Apache: HttpClient 4.3, HttpCore 4.3.
     * It also uses the JCIFS library from the Samba for the 'NTLM' authentication.
     *
     * @param url                                The web address to make the request to. This must be a standard URL as specified in RFC 3986. This is a required input.
     *                                           <br>Format: scheme://domain:port/path?query_string#fragment_id.
     *                                           <br>Examples: https://[fe80::1260:4bff:fe49:42fc]:8080/my/path?key1=val1&key2=val2#my_fragment
     * @param authType                           The type of authentication used by this operation when trying to execute the request on the target server.
     *                                           The authentication is not preemptive: a plain request not including authentication info
     *                                           will be made and only when the server responds with a 'WWW-Authenticate' header the client will
     *                                           send required headers. If the server needs no authentication but you specify one in this input
     *                                           the request will work nevertheless. Then client cannot choose the authentication method and there
     *                                           is no fallback so you have to know which one you need. If the web application and proxy use
     *                                           different authentication types, these must be specified like in the Example model.
     *                                           <br>Default value: basic. Valid values: basic, digest, ntlm, kerberos, any, anonymous, "" or a list of valid values separated by comma.
     * @param preemptiveAuth                     If this field is 'true' authentication info will be sent in the first request.
     *                                           If this is 'false' a request with no authentication info will be made and if server responds
     *                                           with 401 and a header like WWW-Authenticate: Basic realm="myRealm" only then the authentication
     *                                           info will be sent. Default value: true. Valid values: true, false
     * @param username                           The user name used for authentication. For NTLM authentication, the required format is 'domain\\user'
     *                                           and if you only specify the 'user' it will append a dot like '.\\user' so that a local user on the
     *                                           target machine can be used. In order for all authentication schemes to work (except Kerberos) username is required.
     * @param password                           The password used for authentication.
     * @param kerberosConfFile                   A krb5.conf file with content similar to the one in the examples
     *                                           (where you replace CONTOSO.COM with your domain and 'ad.contoso.com' with your kdc FQDN).
     *                                           This configures the Kerberos mechanism required by the Java GSS-API methods.
     *                                           <br>Format: http://web.mit.edu/kerberos/krb5-1.4/krb5-1.4.4/doc/krb5-admin/krb5.conf.html
     * @param kerberosLoginConfFile              A login.conf file needed by the JAAS framework with the content similar to the one in examples
     *                                           Format: http://docs.oracle.com/javase/7/docs/jre/api/security/jaas/spec/com/sun/security/auth/module/Krb5LoginModule.html
     * @param kerberosSkipPortForLookup          Do not include port in the key distribution center database lookup. Default value: true. Valid values: true, false
     * @param proxyHost                          The proxy server used to access the web site.
     * @param proxyPort                          The proxy server port. Default value: 8080. Valid values: -1 and integer values greater than 0.
     *                                           The value '-1' indicates that the proxy port is not set and the scheme default port will be used.
     *                                           If the scheme is 'http://' and the 'proxyPort' is set to '-1' then port '80' will be used.
     * @param proxyUsername                      The user name used when connecting to the proxy. The 'authType' input will be used to choose authentication type.
     *                                           The 'Basic' and 'Digest' proxy authentication types are supported.
     * @param proxyPassword                      The proxy server password associated with the proxyUsername input value.
     * @param trustAllRoots                      Specifies whether to enable weak security over SSL/TSL. A certificate is trusted even if no trusted
     *                                           certification authority issued it. Default value: false. Valid values: true, false
     * @param x509HostnameVerifier               Specifies the way the server hostname must match a domain name in the subject's Common Name (CN)
     *                                           or subjectAltName field of the X.509 certificate. Set this to "allow_all" to skip any checking.
     *                                           For the value "browser_compatible" the hostname verifier works the same way as Curl and Firefox.
     *                                           The hostname must match either the first CN, or any of the subject-alts.
     *                                           A wildcard can occur in the CN, and in any of the subject-alts. The only difference
     *                                           between "browser_compatible" and "strict" is that a wildcard (such as "*.foo.com")
     *                                           with "browser_compatible" matches all subdomains, including "a.b.foo.com".
     *                                           Default value: strict. Valid values: strict,browser_compatible,allow_all
     * @param trustKeystore                      The pathname of the Java TrustStore file. This contains certificates from other parties
     *                                           that you expect to communicate with, or from Certificate Authorities that you trust to
     *                                           identify other parties.  If the protocol (specified by the 'url') is not 'https' or if
     *                                           trustAllRoots is 'true' this input is ignored. Default value: <OO_Home>/java/lib/security/cacerts. Format: Java KeyStore (JKS)
     * @param trustPassword                      The password associated with the TrustStore file. If trustAllRoots is false and trustKeystore is empty,
     *                                           trustPassword default will be supplied. Default value: changeit
     * @param keystore                           The pathname of the Java KeyStore file. You only need this if the server requires client authentication.
     *                                           If the protocol (specified by the 'url') is not 'https' or if trustAllRoots is 'true' this input is ignored.
     *                                           <br>Default value: <OO_Home>/java/lib/security/cacerts. Format: Java KeyStore (JKS)
     * @param keystorePassword                   The password associated with the KeyStore file. If trustAllRoots is false and keystore
     *                                           is empty, keystorePassword default will be supplied. Default value: changeit
     * @param connectTimeout                     The time to wait for a connection to be established, in seconds.
     *                                           A timeout value of '0' represents an infinite timeout. Default value: 0
     * @param socketTimeout                      The timeout for waiting for data (a maximum period inactivity between two consecutive data packets),
     *                                           in seconds. A socketTimeout value of '0' represents an infinite timeout. Default value: 0.
     * @param useCookies                         Specifies whether to enable cookie tracking or not. Cookies are stored between consecutive calls
     *                                           in a serializable session object therefore they will be available on a branch level.
     *                                           If you specify a non-boolean value, the default value is used. Default value: true. Valid values: true, false
     * @param keepAlive                          Specifies whether to create a shared connection that will be used in subsequent calls.
     *                                           If keepAlive is false, the already open connection will be used and after execution it will close it.
     *                                           The operation will use a connection pool stored in a GlobalSessionObject that will be available throughout
     *                                           the execution (the flow and subflows, between parallel split lanes). Default value: true. Valid values: true, false.
     * @param connectionsMaxPerRoot              The maximum limit of connections on a per route basis.
     *                                           The default will create no more than 2 concurrent connections per given route. Default value: 2
     * @param connectionsMaxTotal                The maximum limit of connections in total.
     *                                           The default will create no more than 2 concurrent connections in total. Default value: 20
     * @param headers                            The list containing the headers to use for the request separated by new line (CRLF).
     *                                           The header name - value pair will be separated by ":". Format: According to HTTP standard for headers (RFC 2616).
     *                                           Examples: Accept:text/plain
     * @param responseCharacterSet               The character encoding to be used for the HTTP response.
     *                                           If responseCharacterSet is empty, the charset from the 'Content-Type' HTTP response header will be used.
     *                                           If responseCharacterSet is empty and the charset from the HTTP response Content-Type header is empty,
     *                                           the default value will be used. You should not use this for method=HEAD or OPTIONS. Default value: ISO-8859-1
     * @param destinationFile                    The absolute path of a file on disk where to save the entity returned by the response.
     *                                           'returnResult' will no longer be populated with the entity if this is specified.
     *                                           You should not use this for method=HEAD or OPTIONS. Example: C:\temp\destinationFile.txt
     * @param followRedirects                    Specifies whether the HTTP client automatically follows redirects.
     *                                           Redirects explicitly prohibited by the HTTP specification as requiring user intervention
     *                                           will not be followed (redirects on POST and PUT requests that are converted to GET requests).
     *                                           If you specify a non-boolean value, the default value is used. Default value: true. Valid values: true, false
     * @param queryParams                        The list containing query parameters to append to the URL. The names and the values must not
     *                                           be URL encoded unless you specify "queryParamsAreURLEncoded"=true because if they are encoded
     *                                           and "queryParamsAreURLEncoded"=false they will get double encoded.
     *                                           The separator between name-value pairs is "&". The query name will be separated from query
     *                                           value by "=". Note that you need to URL encode at least "&" to "%26" and "=" to "%3D" and
     *                                           set "queryParamsAreURLEncoded"="true" if you leave the other special URL characters un-encoded
     *                                           they will be encoded by the HTTP Client. Examples: parameterName1=parameterValue1&parameterName2=parameterValue2;
     * @param queryParamsAreURLEncoded           Specifies whether to encode  (according to the url encoding standard) the queryParams.
     *                                           If you set "queryParamsAreURLEncoded"=true and you have invalid characters in 'queryParams'
     *                                           they will get encoded anyway. If "queryParamsAreURLEncoded"=false all characters will be encoded.
     *                                           But the ' ' (space) character will be encoded as + if queryParamsAreURLEncoded is either true or false.
     *                                           Also %20 will be encoded as + if "queryParamsAreURLEncoded"=true.
     *                                           If you specify a non-boolean value, the default value is used.
     *                                           Default value: false. Valid values: true, false
     * @param queryParamsAreFormEncoded          Specifies whether to encode the queryParams in the form request format or not.
     *                                           This format is the default format used by the apache http client library.
     *                                           If queryParamsAreFormEncoded=true then all characters will be encoded based on the queryParamsAreURLEncoded
     *                                           input. If queryParamsAreFormEncoded=false all reserved characters are not encoded no matter of
     *                                           queryParamsAreURLEncoded input. The only exceptions are for ' ' (space) character which is encoded as %20 in both
     *                                           cases of queryParamsAreURLEncoded input and + (plus) which is encoded as %20 if queryParamsAreURLEncoded=true
     *                                           and not encoded if queryParamsAreURLEncoded=false. If the special characters are already encoded
     *                                           and queryParamsAreURLEncoded=true then they will be transformed into their original format.
     *                                           For example: %40 will be @, %2B will be +. But %20 (space) will not be transformed.
     *                                           The list of reserved chars is: ;/?:@&=+,$
     *                                           Default value: true. Valid values: true, false
     *                                           Example: query=test te%20@st will be encoded in query=test%20te%20@st
     * @param formParams                         This input needs to be given in form encoded format and will set the entity to be sent in the request.
     *                                           It will also set the content-type to application/x-www-form-urlencoded.
     *                                           This should only be used with method=POST. Note that you need to URL encode at
     *                                           least "&" to "%26" and "=" to "%3D" and set "queryParamsAreURLEncoded"="true" if you leave the
     *                                           other special URL characters un-encoded they will be encoded by the HTTP Client.
     *                                           <br>Examples: input1=value1&input2=value2. (The client will send: input1=value1&in+put+2=val+u%0A+e2)
     * @param formParamsAreURLEncoded            formParams will be encoding  (according to the url encoding standard) if this is 'true'.
     *                                           If you set "formParamsAreURLEncoded"=true and you have invalid characters in 'formParams'
     *                                           they will get encoded anyway. This should only be used with method=POST.
     *                                           Default value: false. Valid values: true, false
     * @param sourceFile                         The absolute path of a file on disk from where to read the entity for the http request.
     *                                           This will be read using 'requestCharacterSet' or 'contentType' input (see below).
     *                                           This should not be provided for method=GET, HEAD, TRACE. Examples: C:\temp\sourceFile.txt
     * @param body                               The string to include in body for HTTP POST operation. If both sourceFile and body will be provided,
     *                                           the body input has priority over sourceFile. This should not be provided for method=GET, HEAD, TRACE
     * @param contentType                        The content type that should be set in the request header, representing the MIME-type of the
     *                                           data in the message body. Default value: text/plain. Examples: "text/html", "application/x-www-form-urlencoded"
     * @param requestCharacterSet                The character encoding to be used for the HTTP request body.
     *                                           If contentType is empty, the requestCharacterSet will use the default value.
     *                                           If contentType will include charset (ex.: "application/json; charset=UTF-8"),
     *                                           the requestCharacterSet value will overwrite the charset value from contentType input.
     *                                           This should not be provided for method=GET, HEAD, TRACE. Default value: ISO-8859-1
     * @param multipartBodies                    This is a name=textValue list of pairs separated by "&". This will also take into account
     *                                           the "contentType" and "charset" inputs. The request entity will be like:
     *                                           <br>Content-Disposition: form-data; name="name1"
     *                                           <br>Content-Type: text/plain; charset=UTF-8
     *                                           <br>Content-Transfer-Encoding: 8bit
     *                                           <br>
     *                                           <br>textvalue1
     *                                           <br>Examples: name1=textvalue1&name2=textvalue2
     * @param multipartBodiesContentType         Each entity from the multipart entity has a content-type header.
     *                                           You can only specify it once for all the parts and it is the only way to change
     *                                           the characterSet of the encoding. Default value: text/plain; charset=ISO-8859-1
     *                                           Examples: text/plain; charset=UTF-8
     * @param multipartFiles                     This is a list of name=filePath pairs. This will also take into account the "contentType"
     *                                           and "charset" inputs. The request entity will be like:
     *                                           <br>Content-Disposition: form-data; name="name3"; filename="readme.txt"
     *                                           <br>Content-Type: application/octet-stream; charset=UTF-8
     *                                           <br>Content-Transfer-Encoding: binary the text in readme.txt
     *                                           <br>Examples: name3=c:\temp\readme.txt&name4=c:\temp\log4j.properties
     * @param multipartFilesContentType          Each entity from the multipart entity has a content-type header. You can only specify it once for all parts.
     *                                           Default value: application/octet-stream. Examples: image/png,text/plain
     * @param multipartValuesAreURLEncoded       You need to set this to 'true' if the bodies may contain the "&" and "="
     *                                           separators  and you also need to URL encode them so that "&" becomes %26 and "=" becomes %3D
     *                                           (using the URL Encoder operation on each value or by a simple replace). Default value: false
     * @param chunkedRequestEntity               Data is sent in a series of "chunks". It uses the Transfer-Encoding HTTP header in place
     *                                           of the Content-Length header.Generally it is recommended to let HttpClient choose the
     *                                           most appropriate transfer encoding based on the properties of the HTTP message being transferred.
     *                                           It is possible, however, to inform HttpClient that chunk coding is preferred by setting this input to "true".
     *                                           Please note that HttpClient will use this flag as a hint only.
     *                                           This value will be ignored when using HTTP protocol versions that do not support chunk coding, such as HTTP/1.0.
     *                                           This setting is ignored for multipart post entities.
     * @param method                             The HTTP method used. This is a required input.
     * @param httpClientCookieSession            the session object that holds the cookies if the useCookies input is true.
     * @param httpClientPoolingConnectionManager the GlobalSessionObject that holds the http client pooling connection manager.
     * @return a map containing the output of the operation. Keys present in the map are:
     * <br><br><b>returnResult</b> - This will contain the response entity (unless 'destinationFile' is specified).
     * In case of an error this output will contain the error message.
     * <br><b>exception</b> - In case of success response, this result is empty. In case of failure response,
     * this result contains the java stack trace of the runtime exception.
     * <br><b>statusCode</b> - The HTTP status code.
     * <br><i>Format</i>: <br>1xx (Informational - Request received, continuing process),
     * <br>2xx (Success - The action was successfully received, understood, and accepted),
     * <br>3xx (Redirection - Further action must be taken in order to complete the request),
     * <br>4xx (Client Error - The request contains bad syntax or cannot be fulfilled),
     * <br>5xx Server Error - The server failed to fulfil an apparently valid request)
     * <br>Examples: 200, 404
     * <br><br><b>finalLocation</b> - The final location after redirects. Format: URL
     * <br><b>responseHeaders</b> - The list containing the headers of the response message, separated by newline.
     * Format: This is conforming to HTTP standard for headers (RFC 2616).
     * <br><b>protocolVersion</b> - The HTTP protocol version. Examples: HTTP/1.1
     * <br><b>reasonPhrase</b> - The reason phrase from the origin HTTP response. This depends on the status code and are according to RFC 1945 and RFC 2048
     * <br>Examples: (HTTP 1.0): OK, Created, Accepted, No Content, Moved Permanently, Moved Temporarily, Not Modified, Bad Request,
     * Unauthorized, Forbidden, Not Found, Internal Server Error, Not Implemented, Bad Gateway,
     * Service Unavailable	Values (HTTP 1.1): Continue, Temporary Redirect, Method Not Allowed,
     * Conflict, Precondition Failed, Request Too Long, Request-URI Too Long, Unsupported Media Type,
     * Multiple Choices, See Other, Use Proxy, Payment Required, Not Acceptable, Proxy Authentication Required,
     * Request Timeout, Switching Protocols, Non Authoritative Information, Reset Content, Partial Content,
     * Gateway Timeout, Http Version Not Supported, Gone, Length Required, Requested Range Not Satisfiable, Expectation Failed
     * <p/>
     * <br><br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     */
    @Action(name = "Http Client",
            outputs = {
                    @Output(EXCEPTION),
                    @Output(STATUS_CODE),
                    @Output(FINAL_LOCATION),
                    @Output(RESPONSE_HEADERS),
                    @Output(PROTOCOL_VERSION),
                    @Output(REASON_PHRASE),
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(
            @Param(value = HttpClientInputs.URL, required = true) String url,
            @Param(HttpClientInputs.TLS_VERSION) String tlsVersion,
            @Param(HttpClientInputs.ALLOWED_CYPHERS) String allowedCyphers,
            @Param(HttpClientInputs.AUTH_TYPE) String authType,
            @Param(HttpClientInputs.PREEMPTIVE_AUTH) String preemptiveAuth,
            @Param(HttpClientInputs.USERNAME) String username,
            @Param(HttpClientInputs.PASSWORD) String password,
            @Param(HttpClientInputs.KERBEROS_CONFIG_FILE) String kerberosConfFile,
            @Param(HttpClientInputs.KERBEROS_LOGIN_CONFIG_FILE) String kerberosLoginConfFile,
            @Param(HttpClientInputs.KERBEROS_SKIP_PORT_CHECK) String kerberosSkipPortForLookup,
            @Param(HttpClientInputs.PROXY_HOST) String proxyHost,
            @Param(HttpClientInputs.PROXY_PORT) String proxyPort,
            @Param(HttpClientInputs.PROXY_USERNAME) String proxyUsername,
            @Param(HttpClientInputs.PROXY_PASSWORD) String proxyPassword,
            @Param(HttpClientInputs.TRUST_ALL_ROOTS) String trustAllRoots,
            @Param(HttpClientInputs.X509_HOSTNAME_VERIFIER) String x509HostnameVerifier,
            @Param(HttpClientInputs.TRUST_KEYSTORE) String trustKeystore,
            @Param(HttpClientInputs.TRUST_PASSWORD) String trustPassword,
            @Param(HttpClientInputs.KEYSTORE) String keystore,
            @Param(HttpClientInputs.KEYSTORE_PASSWORD) String keystorePassword,
            @Param(HttpClientInputs.CONNECT_TIMEOUT) String connectTimeout,
            @Param(HttpClientInputs.SOCKET_TIMEOUT) String socketTimeout,
            @Param(HttpClientInputs.USE_COOKIES) String useCookies,
            @Param(HttpClientInputs.KEEP_ALIVE) String keepAlive,
            @Param(HttpClientInputs.CONNECTIONS_MAX_PER_ROUTE) String connectionsMaxPerRoot,
            @Param(HttpClientInputs.CONNECTIONS_MAX_TOTAL) String connectionsMaxTotal,
            @Param(HttpClientInputs.HEADERS) String headers,
            @Param(HttpClientInputs.RESPONSE_CHARACTER_SET) String responseCharacterSet,
            @Param(HttpClientInputs.DESTINATION_FILE) String destinationFile,
            @Param(HttpClientInputs.FOLLOW_REDIRECTS) String followRedirects,
            @Param(HttpClientInputs.QUERY_PARAMS) String queryParams,
            @Param(HttpClientInputs.QUERY_PARAMS_ARE_URLENCODED) String queryParamsAreURLEncoded,
            @Param(HttpClientInputs.QUERY_PARAMS_ARE_FORM_ENCODED) String queryParamsAreFormEncoded,
            @Param(HttpClientInputs.FORM_PARAMS) String formParams,
            @Param(HttpClientInputs.FORM_PARAMS_ARE_URLENCODED) String formParamsAreURLEncoded,
            @Param(HttpClientInputs.SOURCE_FILE) String sourceFile,
            @Param(HttpClientInputs.BODY) String body,
            @Param(HttpClientInputs.CONTENT_TYPE) String contentType,
            @Param(HttpClientInputs.REQUEST_CHARACTER_SET) String requestCharacterSet,
            @Param(HttpClientInputs.MULTIPART_BODIES) String multipartBodies,
            @Param(HttpClientInputs.MULTIPART_BODIES_CONTENT_TYPE) String multipartBodiesContentType,
            @Param(HttpClientInputs.MULTIPART_FILES) String multipartFiles,
            @Param(HttpClientInputs.MULTIPART_FILES_CONTENT_TYPE) String multipartFilesContentType,
            @Param(HttpClientInputs.MULTIPART_VALUES_ARE_URLENCODED) String multipartValuesAreURLEncoded,
            @Param(HttpClientInputs.CHUNKED_REQUEST_ENTITY) String chunkedRequestEntity,
            @Param(value = HttpClientInputs.METHOD, required = true) String method,
            @Param(HttpClientInputs.SESSION_COOKIES) SerializableSessionObject httpClientCookieSession,
            @Param(HttpClientInputs.SESSION_CONNECTION_POOL) GlobalSessionObject httpClientPoolingConnectionManager) {

        HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(url);
        httpClientInputs.setAuthType(authType);
        httpClientInputs.setPreemptiveAuth(preemptiveAuth);
        httpClientInputs.setUsername(username);
        httpClientInputs.setPassword(password);
        httpClientInputs.setKerberosConfFile(kerberosConfFile);
        httpClientInputs.setKerberosLoginConfFile(kerberosLoginConfFile);
        httpClientInputs.setKerberosSkipPortCheck(kerberosSkipPortForLookup);
        httpClientInputs.setProxyHost(proxyHost);
        httpClientInputs.setProxyPort(proxyPort);
        httpClientInputs.setProxyUsername(proxyUsername);
        httpClientInputs.setProxyPassword(proxyPassword);
        httpClientInputs.setTrustAllRoots(trustAllRoots);
        httpClientInputs.setX509HostnameVerifier(x509HostnameVerifier);
        httpClientInputs.setTrustKeystore(defaultIfEmpty(trustKeystore, DEFAULT_JAVA_KEYSTORE));
        httpClientInputs.setTrustPassword(defaultIfEmpty(trustPassword, CHANGEIT));
        httpClientInputs.setKeystore(defaultIfEmpty(keystore, DEFAULT_JAVA_KEYSTORE));
        httpClientInputs.setKeystorePassword(defaultIfEmpty(keystorePassword, CHANGEIT));
        httpClientInputs.setConnectTimeout(connectTimeout);
        httpClientInputs.setSocketTimeout(socketTimeout);
        httpClientInputs.setUseCookies(useCookies);
        httpClientInputs.setKeepAlive(keepAlive);
        httpClientInputs.setConnectionsMaxPerRoute(connectionsMaxPerRoot);
        httpClientInputs.setConnectionsMaxTotal(connectionsMaxTotal);
        httpClientInputs.setHeaders(headers);
        httpClientInputs.setResponseCharacterSet(responseCharacterSet);
        httpClientInputs.setDestinationFile(destinationFile);
        httpClientInputs.setFollowRedirects(followRedirects);
        httpClientInputs.setQueryParams(queryParams);
        httpClientInputs.setQueryParamsAreURLEncoded(queryParamsAreURLEncoded);
        httpClientInputs.setQueryParamsAreFormEncoded(queryParamsAreFormEncoded);
        httpClientInputs.setFormParams(formParams);
        httpClientInputs.setFormParamsAreURLEncoded(formParamsAreURLEncoded);
        httpClientInputs.setSourceFile(sourceFile);
        httpClientInputs.setBody(body);
        httpClientInputs.setContentType(contentType);
        httpClientInputs.setRequestCharacterSet(requestCharacterSet);
        httpClientInputs.setMultipartBodies(multipartBodies);
        httpClientInputs.setMultipartFiles(multipartFiles);
        httpClientInputs.setMultipartBodiesContentType(multipartBodiesContentType);
        httpClientInputs.setMultipartFilesContentType(multipartFilesContentType);
        httpClientInputs.setMultipartValuesAreURLEncoded(multipartValuesAreURLEncoded);
        httpClientInputs.setChunkedRequestEntity(chunkedRequestEntity);
        httpClientInputs.setMethod(method);
        httpClientInputs.setTlsVersion(tlsVersion);
        httpClientInputs.setAllowedCyphers(allowedCyphers);
        httpClientInputs.setCookieStoreSessionObject(httpClientCookieSession);
        httpClientInputs.setConnectionPoolSessionObject(httpClientPoolingConnectionManager);

        if (!isEmpty(tlsVersion)) {
            if (tlsVersion.toUpperCase().contains(TLSv12.toUpperCase()) && tlsVersion.split(",").length > 1) {
                try {
                    httpClientInputs.setTlsVersion(TLSv12);
                    return new HttpClientService().execute(httpClientInputs);
                } catch (Exception e) {
                    Set<String> otherTls = new HashSet<>(Arrays.asList(tlsVersion.split(",")));
                    String tls12 = "";
                    for (String protocol : otherTls) {
                        if (protocol.toUpperCase().equals(TLSv12.toUpperCase()))
                            tls12 = protocol;
                    }
                    otherTls.remove(tls12);
                    httpClientInputs.setTlsVersion(otherTls.toString().replace("[", "").replace("]", ""));
                    httpClientInputs.setCookieStoreSessionObject(new SerializableSessionObject());
                    httpClientInputs.setConnectionPoolSessionObject(new GlobalSessionObject());
                    try {
                        return new HttpClientService().execute(httpClientInputs);
                    } catch (Exception ex) {
                        return exceptionResult(ex.getMessage(), ex);
                    }
                }
            } else {
                try {
                    return new HttpClientService().execute(httpClientInputs);
                } catch (Exception e) {
                    return exceptionResult(e.getMessage(), e);
                }
            }
        }else {
            try {
                return new HttpClientService().execute(httpClientInputs);
            } catch (Exception e) {
                return exceptionResult(e.getMessage(), e);
            }
        }
    }

    private Map<String, String> exceptionResult(String message, Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String eStr = writer.toString().replace("" + (char) 0x00, "");

        Map<String, String> returnResult = new HashMap<>();
        returnResult.put("returnResult", message);
        returnResult.put("returnCode", "-1");
        returnResult.put("exception", eStr);
        return returnResult;
    }
}
