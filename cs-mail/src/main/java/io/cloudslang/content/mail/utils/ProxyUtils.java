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
package io.cloudslang.content.mail.utils;

import io.cloudslang.content.mail.constants.ImapPropNames;
import io.cloudslang.content.mail.constants.PopPropNames;
import io.cloudslang.content.mail.constants.SmtpPropNames;
import io.cloudslang.content.mail.entities.GetMailInput;
import io.cloudslang.content.mail.entities.ProxyMailInput;

import java.util.Properties;

public final class ProxyUtils {
    public static void setPropertiesProxy(Properties prop, ProxyMailInput input) {
        if (input.getProtocol().contains(ImapPropNames.IMAP)) {
            prop.setProperty(ImapPropNames.PROXY_HOST, input.getProxyHost());
            prop.setProperty(ImapPropNames.PROXY_PORT, input.getProxyPort());
            prop.setProperty(ImapPropNames.PROXY_USER, input.getProxyUsername());
            prop.setProperty(ImapPropNames.PROXY_PASSWORD, input.getProxyPassword());

        }
        if (input.getProtocol().contains(PopPropNames.POP)) {
            prop.setProperty(PopPropNames.PROXY_HOST, input.getProxyHost());
            prop.setProperty(PopPropNames.PROXY_PORT, input.getProxyPort());
            prop.setProperty(PopPropNames.PROXY_USER, input.getProxyUsername());
            prop.setProperty(PopPropNames.PROXY_PASSWORD, input.getProxyPassword());
        }
        if (input.getProtocol().contains(PopPropNames.SMTP)) {
            prop.setProperty(SmtpPropNames.PROXY_HOST, input.getProxyHost());
            prop.setProperty(SmtpPropNames.PROXY_PORT, input.getProxyPort());
            prop.setProperty(SmtpPropNames.PROXY_USER, input.getProxyUsername());
            prop.setProperty(SmtpPropNames.PROXY_PASSWORD, input.getProxyPassword());
        }
    }
}
