/*
 * (c) Copyright 2021 Micro Focus
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
package io.cloudslang.content.active_directory.entities;

import java.util.List;

public interface AddRemoveUsrInput {

    String getHost();

    String getUsername();

    String getPassword();

    String getGroupDistinguishedName();

    String getUserDistinguishedName();

    String getProtocol();

    Boolean getTrustAllRoots();

    String getTrustKeystore();

    String getTrustPassword();

    String getTimeout();

    String getProxyHost();

    int getProxyPort();

    String getProxyUsername();

    String getProxyPassword();

    String getX509HostnameVerifier();

    String getTlsVersion();

    List<String> getAllowedCiphers();
}
