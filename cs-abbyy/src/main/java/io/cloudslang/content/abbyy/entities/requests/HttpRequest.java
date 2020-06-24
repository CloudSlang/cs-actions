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
package io.cloudslang.content.abbyy.entities.requests;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;

import java.net.URISyntaxException;
import java.nio.file.Path;

public interface HttpRequest {
    String getUrl() throws URISyntaxException;


    String getTlsVersion();


    String getAllowedCyphers();


    String getAuthType();


    boolean isPreemptiveAuth();


    String getUsername();


    String getPassword();


    String getKerberosConfigFile();


    String getKerberosLoginConfFile();


    String getKerberosSkipPortForLookup();


    String getProxyHost();


    short getProxyPort();


    String getProxyUsername();


    String getProxyPassword();


    boolean isTrustAllRoots();


    String getX509HostnameVerifier();


    String getTrustKeystore();


    String getTrustPassword();


    String getKeystore();


    String getKeystorePassword();


    int getConnectTimeout();


    int getSocketTimeout();


    boolean isUseCookies();


    boolean isKeepAlive();


    int getConnectionsMaxPerRoute();


    int getConnectionsMaxTotal();


    String getHeaders();


    String getResponseCharacterSet();


    Path getDestinationFile();


    boolean isFollowRedirects();


    String getQueryParams();


    boolean isQueryParamsAreURLEncoded();


    boolean isQueryParamsAreFormEncoded();


    String getFormParams();


    boolean isFormParamsAreURLEncoded();


    Path getSourceFile();


    String getBody();


    String getContentType();


    String getRequestCharacterSet();


    String getMultipartBodies();


    String getMultipartBodiesContentType();


    String getMultipartFiles();


    String getMultipartFilesContentType();


    boolean isMultipartValuesAreURLEncoded();


    boolean isChunkedRequestEntity();


    String getMethod();


    SerializableSessionObject getHttpClientCookieSession();


    GlobalSessionObject getHttpClientPoolingConnectionManager();
}
