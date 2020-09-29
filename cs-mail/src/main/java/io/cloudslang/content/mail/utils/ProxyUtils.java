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

import io.cloudslang.content.mail.constants.*;
import io.cloudslang.content.mail.entities.MailInput;

import java.util.Properties;

public final class ProxyUtils {
    public static void setPropertiesProxy(Properties props, MailInput input) {
        props.setProperty(String.format(PropNames.MAIL_PROXY_HOST, input.getProtocol()), input.getProxyHost());
        props.setProperty(String.format(PropNames.MAIL_PROXY_PORT, input.getProtocol()), String.valueOf(input.getProxyPort()));
        props.setProperty(String.format(PropNames.MAIL_PROXY_USER, input.getProtocol()), input.getProxyUsername());
        props.setProperty(String.format(PropNames.MAIL_PROXY_PASSWORD, input.getProtocol()), input.getProxyPassword());

        props.setProperty(String.format(PropNames.MAIL_PROXY_HOST, input.getProtocol() + SecurityConstants.SECURE_SUFFIX), input.getProxyHost());
        props.setProperty(String.format(PropNames.MAIL_PROXY_PORT, input.getProtocol() + SecurityConstants.SECURE_SUFFIX), String.valueOf(input.getProxyPort()));
        props.setProperty(String.format(PropNames.MAIL_PROXY_USER, input.getProtocol() + SecurityConstants.SECURE_SUFFIX), input.getProxyUsername());
        props.setProperty(String.format(PropNames.MAIL_PROXY_PASSWORD, input.getProtocol() + SecurityConstants.SECURE_SUFFIX), input.getProxyPassword());
    }
}
