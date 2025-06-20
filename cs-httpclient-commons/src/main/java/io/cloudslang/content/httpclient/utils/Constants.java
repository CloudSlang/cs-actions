/*
 * Copyright 2022-2025 Open Text
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



package io.cloudslang.content.httpclient.utils;

public class Constants {

    //Methods
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String PATCH = "PATCH";
    public static final String HEAD = "HEAD";
    public static final String OPTIONS = "OPTIONS";


    public static final String TRACE = "TRACE";
    //DEFAULT VALUES
    public static final String STRICT = "strict";
    public static final String BOOLEAN_FALSE = "false";
    public static final String BOOLEAN_TRUE = "true";
    public static final String DEFAULT_PROXY_PORT = "8080";
    public static final String TLSv12 = "TLSv1.2";
    public static final String TLSv13 = "TLSv1.3";
    public static final String DEFAULT_CONTENT_TYPE = "application/json";
    public static final String DEFAULT_OPTIONS_HEADER = "Content-Type:application/json";

    public static final String DEFAULT_CONNECTIONS_MAX_PER_ROUTE = "2";
    public static final String DEFAULT_CONNECTIONS_MAX_TOTAL = "20";
    public static final String DEFAULT_EXECUTION_TIMEOUT = "300";
    public static final String DEFAULT_CONNECT_TIMEOUT = "300";
    public static final String DEFAULT_RESPONSE_TIMEOUT = "300";


    public static final String HTTP_PORT = "80";
    public static final String HTTPS_PORT = "443";

    public static final String DEFAULT_ALLOWED_CIPHERS_TLSv1_2 = "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384," +
            "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,TLS_DHE_RSA_WITH_AES_256_CBC_SHA256,TLS_DHE_RSA_WITH_AES_128_CBC_SHA256," +
            "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256," +
            "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384,TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384," +
            "TLS_RSA_WITH_AES_256_GCM_SHA384,TLS_RSA_WITH_AES_256_CBC_SHA256,TLS_RSA_WITH_AES_128_CBC_SHA256";

    public static final String DEFAULT_ALLOWED_CIPHERS_TLSv1_3 = "TLS_AES_256_GCM_SHA384,TLS_CHACHA20_POLY1305_SHA256,TLS_AES_128_GCM_SHA256";

    public static final String DEFAULT_ALLOWED_CIPHERS="TLS_AES_256_GCM_SHA384,TLS_CHACHA20_POLY1305_SHA256,TLS_AES_128_GCM_SHA256," +
            "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,TLS_DHE_RSA_WITH_AES_256_CBC_SHA256," +
            "TLS_DHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256," +
            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,TLS_RSA_WITH_AES_256_GCM_SHA384," +
            "TLS_RSA_WITH_AES_256_CBC_SHA256,TLS_RSA_WITH_AES_128_CBC_SHA256";


    public static final String DEFAULT_MULTIPART_BODIES_CONTENT_TYPE = "text/plain";
    public static final String DEFAULT_MULTIPART_FILES_CONTENT_TYPE = "application/octet-stream";

    //AUTH TYPE
    public static final String BASIC = "Basic";
    public static final String DIGEST = "Digest";
    public static final String NTLM = "NTLM";
    public static final String ANONYMOUS = "ANONYMOUS";

    public static final String ZERO = "0";
    public static final String ALLOW_ALL = "allow_all";
    public static final String UTF_8 = "UTF-8";
    public static final String AND = "&";
    public static final String EQUAL = "=";
    public static final String COMMA = ",";
    public static final String COLON = ":";
    public static final String MINUS_1 = "-1";
    public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
    public static final String NEW_LINE = "\n";
    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String TRUSTSTORE_PROPERTY ="javax.net.ssl.trustStore";
    public static final String TRUSTSTORE_PASSWORD_PROPERTY="javax.net.ssl.trustStorePassword";

    //Exceptions
    public static final String EXCEPTION_NULL_EMPTY = "%s can't be null or empty.";
    public static final String EXCEPTION_INVALID_PATH = "%s for %s input is not a valid path.";
    public static final String EXCEPTION_INVALID_BOOLEAN = "%s for %s input is not a valid boolean value.";
    public static final String EXCEPTION_INVALID_NUMBER = "%s for %s input is not a valid number value.";
    public static final String EXCEPTION_INVALID_PORT = "%s for %s input is not a valid port.";
    public static final String EXCEPTION_INVALID_AUTH_TYPE = "%s for %s input is not a valid authType value. The valid values are: 'Basic','Digest','NTLM','ANONYMOUS'.";
    public static final String EXCEPTION_INVALID_HOSTNAME_VERIFIER = "%s for %s input is not a valid x509HostnameVerifier value. The valid values are: 'strict','allow_all'.";
    public static final String EXCEPTION_INVALID_TLS_VERSION = "%s for %s input is not a valid tlsVersion value. The valid values are: 'TLSv1.2','TLSv1.3'.";
    public static final String EXCEPTION_INVALID_ALLOWED_CIPHERS = "%s for %s input is not a valid cipher value. The valid values are "+Constants.DEFAULT_ALLOWED_CIPHERS;
    public static final String EXCEPTION_INVALID_VALUE = "%s is not a valid value for input %s. The value must be an integer number.";
    public static final String EXCEPTION_NOT_POSITIVE_VALUE = "%s is not a valid value for input %s. The value must be a positive number.";
    public static final String EXCEPTION_NEGATIVE_VALUE = "%s is not a valid timeout value for input %s. The value must be bigger or equal to 0.";
    public static final String EXCEPTION_CERTIFICATE_NOT_FOUND ="The specified keystore or truststore file path was not found. Please verify the file path: ";
    public static final String EXCEPTION_TRUSTSTORE_NOT_FOUND = "Trust store path and password not provided, and no " +
            "default system properties were found. Please configure the trust store explicitly or ensure system " +
            "properties 'javax.net.ssl.trustStore' and 'javax.net.ssl.trustStorePassword' are set.";
    public static final String EXCEPTION_KEYSTORE_NOT_FOUND="Keystore path must be provided when 'trustAllRoots' is set to false.";
    public static final String EXCEPTION_INVALID_HEADER_FORMAT="Invalid header format. Headers should be separated by new line, and each header name-value pair must be separated by a colon (:).";
}
