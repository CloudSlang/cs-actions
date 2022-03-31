package io.cloudslang.content.httpclient.utils;

public class Descriptions {

    public static class HTTPClient {
        public static final String HTTP_CLIENT_GET_ACTION = "HTTP Client Get";

        //Inputs
        public static final String HTTP_CLIENT_ACTION_DESC = "This operation does an http request and a parsing of " +
                "the response. It provides features like: http authentication, http secure, connection pool, cookies, " +
                "proxy.";

        //FROM WIKI
        //==============================================================================================================
        public static final String HOST_DESC = "The hostname or IP address of the host.";
        public static final String PROTOCOL_DESC = "Specifies what protocol is used to execute commands on the remote host. " +
                "Valid values: http, https\n " +
                "Default value: https";
        public static final String PROXY_HOST_DESC = "The proxy server used to access the host.";
        public static final String PROXY_PORT_DESC = "The proxy server port.\n Default value:8080";
        public static final String PROXY_USERNAME_DESC = "The username used when connecting to the proxy.";
        public static final String PROXY_PASSWORD_DESC = "The proxy server password associated with the proxyUsername input value.";
        public static final String TLS_VERSION_DESC = "The version of TLS to use. The value of this input will be ignored if 'protocol'" +
                "is set to 'HTTP'. This capability is provided “as is”, please see product documentation for further information." +
                "Valid values: TLSv1, TLSv1.1, TLSv1.2. \n" +
                "Default value: TLSv1.2.  \n";
        public static final String ALLOWED_CIPHERS_DESC = "A list of ciphers to use. The value of this input will be ignored " +
                "if 'tlsVersion' does\n" +
                "not contain 'TLSv1.2'. This capability is provided “as is”, please see product documentation for further security considerations." +
                "In order to connect successfully to the target host, it should accept at least one of the following ciphers. If this is not the case, it is\n" +
                "the user's responsibility to configure the host accordingly or to update the list of allowed ciphers. \n" +
                "Default value: TLS_DHE_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,\n" +
                "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256, TLS_DHE_RSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384, " +
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,\n" +
                "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384, TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, TLS_RSA_WITH_AES_256_GCM_SHA384, TLS_RSA_WITH_AES_256_CBC_SHA256,\n" +
                "TLS_RSA_WITH_AES_128_CBC_SHA256.";
        public static final String TRUST_ALL_ROOTS_DESC = "Specifies whether to enable weak security over SSL/TSL. " +
                "A certificate is trusted even if no trusted certification authority issued it.";
        public static final String X509_HOSTNAME_VERIFIER_DESC = "Specifies the way the server hostname must match a domain name in " +
                "the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to " +
                "\"allow_all\" to skip any checking. For the value \"browser_compatible\" the hostname verifier " +
                "works the same way as Curl and Firefox. The hostname must match either the first CN, or any of " +
                "the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only " +
                "difference between \"browser_compatible\" and \"strict\" is that a wildcard (such as \"*.foo.com\") " +
                "with \"browser_compatible\" matches all subdomains, including \"a.b.foo.com\".";
        public static final String TRUST_KEYSTORE_DESC = "The pathname of the Java TrustStore file. This contains " +
                "certificates from other parties that you expect to communicate with, or from Certificate Authorities" +
                " that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. \n " +
                "Format: Java KeyStore (JKS)";
        public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustStore file. If " +
                "trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.";
        public static final String KEYSTORE_DESC = "The pathname of the Java KeyStore file. You only need this if " +
                "the server requires client authentication. If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS)";
        public static final String KEYSTORE_PASSWORD_DESC = "The password associated with the KeyStore file. If " +
                "trustAllRoots is false and keystore is empty, keystorePassword default will be supplied.";
        public static final String CONNECT_TIMEOUT_DESC = "The time to wait for a connection to be established, " +
                "in seconds. A timeout value of '0' represents an infinite timeout.";
        public static final String EXECUTION_TIMEOUT_DESC = "The amount of time (in seconds) to allow the client to complete the execution " +
                "of an API call. A value of '0' disables this feature. \n" +
                "Default: 60  \n";
        public static final String KEEP_ALIVE_DESC = "Specifies whether to create a shared connection that will be " +
                "used in subsequent calls. If keepAlive is false, the already open connection will be used and after" +
                "execution it will close it.";
        public static final String CONNECTIONS_MAX_PER_ROUTE_DESC = "The maximum limit of connections on a per route basis.";
        public static final String CONNECTIONS_MAX_DESC = "The maximum limit of connections in total.";

        public static final String SUCCESS_DESC = "Request went successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to do the request.";
        //==============================================================================================================

        public static final String URL_DESC = "The web address to make the request to. \n " +
                "Format: scheme://domain:port/path?query_string#fragment_id. \n" +
                "Examples: https://[fe80::1260:4bff:fe49:42fc]:8080/my/path?key1=val1&key2=val2#my_fragment";
        public static final String METHOD_DESC = "The HTTP method used.";
        public static final String FOLLOW_REDIRECTS_DESC = "Specifies whether the HTTP client automatically follows redirects. " +
                "Redirects explicitly prohibited by the HTTP specification as requiring user intervention will not be " +
                "followed(redirects on POST and PUT requests that are converted to GET requests). If you specify a non-boolean " +
                "value, the default value is used. \n" +
                "Default value: true \n" +
                "Valid values: true, false \n";
        public static final String QUERY_PARAMS_DESC = "The list containing query parameters to append to the URL. " +
                "The names and the values must not be URL encoded unless you specify query_params_are_URL_encoded=true " +
                "because if they are encoded and query_params_are_URL_encoded=false they will get double encoded. " +
                "The separator between name-value pairs is \"&\". The query name will be separated from query value by \"=\". \n" +
                "Note that you need to URL encode at least \"&\" to \"%26\" and \"=\" to \"%3D\" and set query_params_are_URL_encoded=true " +
                "if you leave the other special URL characters un-encoded, they will be encoded by the HTTP Client. \n" +
                "Examples: parameterName1=parameterValue1&parameterName2=parameterValue2; \n";
        public static final String QUERY_PARAMS_ARE_URLENCODED_DESC = "Specifies whether to encode (according to the URL " +
                "encoding standard) the query_params. If you set query_params_are_URL_encoded=true and you have invalid " +
                "characters in query_params they will get encoded anyway. If query_params_are_URL_encoded=false, all " +
                "characters will be encoded. But the ' '(space) character will be encoded as + if query_params_are_URL_encoded " +
                "is either true or false. Also %20 will be encoded as + if query_params_are_URL_encoded=true. If you specify " +
                "a non-boolean value, the default value is used. \n" +
                "Default value: false \n" +
                "Valid values: true, false \n";
        public static final String QUERY_PARAMS_ARE_FORM_ENCODED_DESC = "Specifies whether to encode the query_params in " +
                "the form request format or not. This format is the default format used by the apache http client library. " +
                "If query_params_are_URL_encoded=true then all characters will be encoded based on the " +
                "query_params_are_URL_encoded input. If query_params_are_URL_encoded=false all reserved characters are not " +
                "encoded no matter of query_params_are_URL_encoded input. The only exceptions are for ' '(space) character " +
                "which is encoded as %20 in both cases of query_params_are_URL_encoded input and + (plus) which is encoded " +
                "as %20 if queryParamsAreURLEncoded=true and not encoded if query_params_are_URL_encoded=false. If the " +
                "special characters are already encoded and query_params_are_URL_encoded=true then they will be transformed " +
                "into their original format. For example: %40 will be @, %2B will be +. But %20(space) will not be transformed. " +
                "The list of reserved chars is: ;/?:@&=+,$ \n" +
                "Default value: true \n" +
                "Valid values: true, false \n" +
                "Example: query=test te%20@st will be encoded in query=test%20te%20@st \n";
        public static final String FORM_PARAMS_DESC = "This input needs to be given in form encoded format and will set " +
                "the entity to be sent in the request. It will also set the content-type to application/x-www-form-urlencoded. " +
                "This should only be used with method=POST. \n " +
                "Note that you need to URL encode at least \"&\" to \"%26\" " +
                "and \"=\" to \"%3D\" and set query_params_are_URL_encoded=true if you leave the other special URL characters " +
                "un-encoded, they will be encoded by the HTTP Client. \n" +
                "<br>Examples: input1=value1&input2=value2. (The client will send: input1=value1&in+put+2=val+u%0A+e2) \n";
        public static final String FORM_PARAMS_ARE_URLENCODED_DESC = "form_params will be encoding (according to the URL " +
                "encoding standard) if this is 'true'. If you set form_params_are_URL_encoded=true and you have invalid " +
                "characters in form_params they will get encoded anyway. This should only be used with method=POST. \n" +
                "Default value: false \n" +
                "Valid values: true, false \n";
        public static final String SOURCE_FILE_DESC = "The absolute path of a file on disk from where to read the entity " +
                "for the http request. This will be read using 'request_character_set' or 'content_type' input (see below). " +
                "This should not be provided for method=GET, HEAD, TRACE. \n" +
                "Examples: C:\\temp\\sourceFile.txt \n";
        public static final String REQUEST_CHARACTER_SET_DESC = "The character encoding to be used for the HTTP request body. " +
                "If content_type is empty, the request_character_set will use the default value. If content_type will include " +
                "charset (ex.: \"application/json; charset=UTF-8\"), the request_character_set value will overwrite the " +
                "charset value from content_type input. This should not be provided for method=GET, HEAD, TRACE. \n" +
                "Default value: UTF-8 \n";
        public static final String BODY_DESC = "The string to include in body for HTTP POST operation. If both source_file " +
                "and body will be provided, the body input has priority over source_file. " +
                "This should not be provided for method=GET, HEAD, TRACE.";
        public static final String CONTENT_TYPE_DESC = "The content type that should be set in the request header, " +
                "representing the MIME-type of the in the message body. \n" +
                "Default value: text/plain \n" +
                "Examples: \"text/html\", \"application/x-www-form-urlencoded\" \n";
        public static final String AUTH_TYPE_DESC = "The type of authentication used by this operation when trying to " +
                "execute the request on the target server. The authentication is not preemptive: a plain request not " +
                "including authentication info will be made and only when the server responds with a 'WWW-Authenticate' " +
                "header the client will send required headers. If the server needs no authentication but you specify one in " +
                "this input the request will work nevertheless. Then client cannot choose the authentication method and " +
                "there is no  fallback so you have to know which one you need. If the web application and proxy use different " +
                "authentication types, these must be specified like in the Example model.\n" +
                "<br>Default value: basic. \n" +
                "Valid values: basic, digest, ntlm, kerberos, any, anonymous, \"\" or a list of valid values separated by comma.\n";
        public static final String PREEMPTIVE_AUTH_DESC = "If this field is 'true' authentication info will be sent in " +
                "the first request. If this is 'false' a request with no authentication info will be made and if server " +
                "responds only then the authentication info will be sent. \n" +
                "Default value: true \n" +
                "Valid values: true, false \n";
        public static final String USERNAME_DESC = "The username used for authentication. For NTLM authentication, the " +
                "required format is 'domain\\\\user' and if you only specify the 'user' it will append a dot like '.\\\\user' " +
                "so that a local user on the target machine can be used. In order for all authentication schemes to work (except Kerberos) " +
                "username is required.";
        public static final String PASSWORD_DESC = "The password used for authentication.";
        public static final String SOCKET_TIMEOUT_DESC = "The timeout for waiting for data (a maximum period inactivity " +
                "between two consecutive data packets), in seconds. A socket_timeout value of '0' represents an infinite " +
                "timeout. \n" +
                "Default value: 0 \n";
        public static final String USE_COOKIES_DESC = "Specifies whether to enable cookie tracking or not. Cookies are" +
                "stored between consecutive calls in a serializable session object therefore they will be available on a" +
                "branch level. If you specify a non-boolean value, the default value is used. \n" +
                "Default value: true \n" +
                "Valid values: true, false \n";
        public static final String HEADERS_DESC = "The list containing the headers to use for the request separated by " +
                "new line(CRLF). The header name - value pair will be separated by \":\". " +
                "Format: According to HTTP standard for headers (RFC 2616). \n" +
                "Examples: Accept:text/plain \n";
        public static final String RESPONSE_CHARACTER_SET_DESC = "The character encoding to be used for the HTTP response. " +
                "If response_character_set is empty, the charset from the 'Content-Type' HTTP response header will be used. " +
                "If response_character_set is empty and the charset from the HTTP response Content-Type header is empty, " +
                "the default value will be used. You should not use this for method=HEAD or OPTIONS. \n" +
                "Default value: UTF-8 \n";
        public static final String DESTINATION_FILE_DESC = "The absolute path of a file on disk where to save the entity " +
                "returned by the response. return_result will no longer be populated with the entity if this is specified. " +
                "You should not use this for method=HEAD or OPTIONS. \n" +
                "Example: C:\\temp\\destinationFile.txt \n";
        public static final String MULTIPART_BODIES_DESC = "This is a name=textValue list of pairs separated by \"&\". " +
                "his will also consider the \"contentType\" and \"charset\" inputs. The request entity will be like:\n" +
                "<br>Content-Disposition: form-data; name=\"name1\" \n" +
                "<br>Content-Type: text/plain; charset=UTF-8 \n" +
                "<br>Content-Transfer-Encoding: 8bit \n" +
                "<br> \n" +
                "<br>textvalue1 \n" +
                "<br>Examples: name1=textvalue1&name2=textvalue2 \n";
        public static final String MULTIPART_BODIES_CONTENT_TYPE_DESC = "Each entity from the multipart entity has a " +
                "content-type header. You can only specify it once for all the parts and it is the only way to change the " +
                "characterSet of the encoding. \n" +
                "Default value: text/plain; charset=UTF-8. \n " +
                "Examples: text/plain; charset=UTF-8 \n";
        public static final String MULTIPART_FILES_DESC = "This is a list of name=filePath pairs. This will also consider " +
                "the \"contentType\" and \"charset\" inputs. \n The request entity will be like:\n" +
                "<br>Content-Disposition: form-data; name=\"name3\"; filename=\"readme.txt\" \n" +
                "<br>Content-Type: application/octet-stream; charset=UTF-8 \n" +
                "<br>Content-Transfer-Encoding: binary the text in readme.txt \n" +
                "<br>Examples: name3=c:\\temp\\readme.txt";
        public static final String MULTIPART_FILES_CONTENT_TYPE_DESC = "Each entity from the multipart entity has a " +
                "content-type header. You can only specify it once for all parts. \n" +
                "Default value: application/octet-stream. \n" +
                "Examples: image/png,text/plain";
        public static final String MULTIPART_VALUES_ARE_URL_ENCODED_DESC = "You need to set this to 'true' if the bodies " +
                "may contain the \"&\" and \"=\" separators and you also need to URL encode them so that \"&\" becomes " +
                "%26 and \"=\" becomes %3D (using the URL Encoder operation on each value or by a simple replace). \n" +
                "Default value: false";
        public static final String CHUNKED_REQUEST_ENTITY_DESC = "Data is sent in a series of \"chunks\". It uses the " +
                "Transfer-Encoding HTTP header in place of the Content-Length header. Generally, it is recommended to let " +
                "HttpClient choose the most appropriate transfer encoding based on the properties of the HTTP message being " +
                "transferred. It is possible, however, to inform HttpClient that chunk coding is preferred by setting this " +
                "input to \"true\". Please note that HttpClient will use this flag as a hint only. This value will be ignored " +
                "when using HTTP protocol versions that do not support chunk coding, such as HTTP/1.0. This setting is " +
                "ignored for multipart post entities.";

        public static final String SESSION_CONNECTION_POOL_DESC = "The GlobalSessionObject that holds the http client pooling " +
                "connection manager.";
        public static final String SESSION_COOKIES_DESC = "The session object that holds the cookies if the useCookies input is true.";

        //Outputs
        public static final String RETURN_RESULT_DESC = "If successful, returns the complete API response. In case of " +
                "an error this output will contain the error message.";
        public static final String STATUS_CODE_DESC = "The status_code returned by the server.";
        public static final String RETURN_CODE_DESC = "The return_code of the operation: 0 for success, -1 for failure.";
        public static final String EXCEPTION_DESC = "In case of success response, this result is empty. In case of " +
                "failure response, this result contains the java stack trace of the runtime exception.";

        //Descriptions on messages for users
        public static final String INVALID_STRING_ARRAY_INPUT_DESC = "The value provided is an invalid string array.";
    }
}
