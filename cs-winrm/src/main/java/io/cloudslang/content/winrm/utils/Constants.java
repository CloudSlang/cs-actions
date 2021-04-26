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
package io.cloudslang.content.winrm.utils;

public final class Constants {
    public static final String KERBEROS = "kerberos";
    public static final String CMD = "cmd";
    public static final String POWERSHELL = "powershell";
    public static final String DEFAULT_PORT = "5986";
    public static final String TLSv12 ="TLSv1.2";
    public static final String DEFAULT_TIMEOUT ="60";
    public static final String DEFAULT_COMMAND_TYPE = "cmd";
    public static final String BASIC = "Basic";
    public static final String NTLM = "NTLM";
    public static final String HTTPS = "https";
    public static final String HTTP = "http";
    public static final String DEFAULT_PROXY_PORT = "8080";
    public static final String BOOLEAN_FALSE = "false";
    public static final String BOOLEAN_TRUE = "true";
    public static final String STRICT = "strict";
    public static final String NEW_LINE = "\n";
    public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
    public static final String CHANGE_IT = "changeit";
    public static final String EXCEPTION_NULL_EMPTY = "%s can't be null or empty.";
    public static final String EXCEPTION_INVALID_PATH = "%s for %s input is not a valid path.";
    public static final String EXCEPTION_INVALID_BOOLEAN = "%s for %s input is not a valid boolean value.";
    public static final String EXCEPTION_INVALID_NUMBER = "%s for %s input is not a valid number value.";
    public static final String EXCEPTION_INVALID_PORT = "%s for %s input is not a valid port.";
    public static final String EXCEPTION_INVALID_AUTH_TYPE = "%s for %s input is not a valid authType value. The valid values are: 'Basic','NTLM','Kerberos'.";
    public static final String EXCEPTION_INVALID_HOSTNAME_VERIFIER = "%s for %s input is not a valid x509HostnameVerifier value. The valid values are: 'strict','browser_compatible','allow_all'.";
    public static final String EXCEPTION_INVALID_TLS_VERSION = "%s for %s input is not a valid tlsVersion value. The valid values are: 'SSLv3','TLSv1','TLSv1.1','TLSv1.2','TLSv1.3'.";
    public static final String EXCEPTION_INVALID_PROXY = "%s for input %s is not a valid proxy port.";
    public static final String EXCEPTION_INVALID_PROTOCOL = "%s for input %s is not a valid protocol. The valid values are 'http' or 'https'.";
    public static final String EXCEPTION_INVALID_COMMAND_TYPE = "%s for input %s is not a valid command type. The valid values are 'cmd' or 'powershell'.";
    public static final String EXCEPTION_TIMED_OUT = "Operation timed out.";

}
