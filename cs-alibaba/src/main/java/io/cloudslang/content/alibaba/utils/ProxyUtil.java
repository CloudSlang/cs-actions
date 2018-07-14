/*
 * (c) Copyright 2018 Micro Focus
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
package io.cloudslang.content.alibaba.utils;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ProxyUtil {
    public static void setProxies(final String proxyHost, final String proxyPort, final String proxyUsername, final String proxyPassword) {
        System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyPort);
        System.setProperty("https.proxyPort", proxyPort);
        System.setProperty("http.proxyUser", proxyUsername);
        System.setProperty("https.proxyUser", proxyUsername);
        System.setProperty("http.proxyPassword", proxyPassword);
        System.setProperty("https.proxyPassword", proxyPassword);
    }

    public static void clearProxy() {
        System.setProperty("http.proxyHost", EMPTY);
        System.setProperty("https.proxyHost", EMPTY);
        System.setProperty("http.proxyPort", EMPTY);
        System.setProperty("https.proxyPort", EMPTY);
        System.setProperty("http.proxyUser", EMPTY);
        System.setProperty("https.proxyUser", EMPTY);
        System.setProperty("http.proxyPassword", EMPTY);
        System.setProperty("https.proxyPassword", EMPTY);
    }
}
