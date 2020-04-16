/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.abbyy.entities;

import java.io.File;

public interface AbbyyRequest {
    LocationId getLocationId();

    String getApplicationId();

    String getPassword();

    String getProxyHost();

    short getProxyPort();

    String getProxyUsername();

    String getProxyPassword();

    boolean isTrustAllRoots();

    String getX509HostnameVerifier();

    String getTrustKeystore();

    String getTrustPassword();

    int getConnectTimeout();

    int getSocketTimeout();

    boolean isKeepAlive();

    int getConnectionsMaxPerRoute();

    int getConnectionsMaxTotal();

    String getResponseCharacterSet();

    File getDestinationFile();

    File getSourceFile();
}
