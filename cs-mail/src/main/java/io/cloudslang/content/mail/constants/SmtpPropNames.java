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
package io.cloudslang.content.mail.constants;

public final class SmtpPropNames {
    public static final String SMTP = "smtp";
    private static final String MAIL_SMTP = "mail.smtp.";
    public static final String TIMEOUT = MAIL_SMTP + "timeout";
    public static final String START_TLS_ENABLE = MAIL_SMTP + "starttls.enable";
    public static final String AUTH = MAIL_SMTP + "auth";
    public static final String PASSWORD = MAIL_SMTP + "password";
    public static final String USER = MAIL_SMTP + "user";
    public static final String PORT = MAIL_SMTP + "port";
    public static final String HOST = MAIL_SMTP + "host";
    public static final String PROXY_HOST = MAIL_SMTP + ".proxy.host";
    public static final String PROXY_PORT = MAIL_SMTP + ".proxy.port";
    public static final String PROXY_USER = MAIL_SMTP + ".proxy.user";
    public static final String PROXY_PASSWORD = MAIL_SMTP + ".proxy.password";
}
