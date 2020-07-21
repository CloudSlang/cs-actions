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
package main.java.io.cloudslang.content.ldap.constants;

public final class PropNames {
    public static final String MAIL = "mail.";
    public static final String SSL_ENABLE = ".ssl.enable";
    public static final String START_TLS_ENABLE = ".starttls.enable";
    public static final String START_TLS_REQUIRED = ".starttls.required";
    public static final String SOCKET_FACTORY = ".socketFactory";
    public static final String SOCKET_FACTORY_CLASS = ".socketFactory.class";
    public static final String SOCKET_FACTORY_FALLBACK = ".socketFactory.fallback";
    public static final String SOCKET_FACTORY_PORT = ".socketFactory.port";
    public static final String HOST = ".host";
    public static final String PORT = ".port";
    public static final String TIMEOUT = ".timeout";
    public static final String JAVA_HOME = "java.home";
    public static final String FILE_SEPARATOR = "file.separator";

    public static final String MAIL_SSL_ENABLE = "mail.%s.ssl.enable";
    public static final String MAIL_STARTTLS_ENABLE = "mail.%s.starttls.enable";
    public static final String MAIL_STARTTLS_REQUIRED = "mail.%s.starttls.required";
    public static final String MAIL_SOCKET_FACTORY = "mail.%s.socketFactory";
    public static final String MAIL_SOCKET_FACTORY_FALLBACK = "mail.%s.socketFactory.fallback";
    public static final String MAIL_PROXY_HOST = "mail.%s.proxy.host";
    public static final String MAIL_PROXY_PORT = "mail.%s.proxy.port";
    public static final String MAIL_PROXY_USER = "mail.%s.proxy.user";
    public static final String MAIL_PROXY_PASSWORD = "mail.%s.proxy.password";
}
