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

public final class PopPropNames {
    public static final String POP = "pop";
    public static final String POP3 = "pop3";
    public static final String POP3_PORT = "110";
    public static final String SMTP = "smtp";
    private static final String MAIL_POP3 = "mail.pop3.";
    public static final String PROXY_PASSWORD = MAIL_POP3 + "proxy.password";
    public static final String PROXY_USER = MAIL_POP3 + "proxy.user";
    public static final String PROXY_PORT = MAIL_POP3 + "proxy.port";
    public static final String PROXY_HOST = MAIL_POP3 + "proxy.host";
}
