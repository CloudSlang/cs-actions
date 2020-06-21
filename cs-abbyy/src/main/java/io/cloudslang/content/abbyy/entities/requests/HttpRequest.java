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

import java.io.File;
import java.net.URISyntaxException;

public interface HttpRequest {
    String getUrl() throws URISyntaxException;


    String getTlsVersion();


    String getAllowedCyphers();


    String getAuthType();


    Boolean isPreemptiveAuth();


    String getUsername();


    String getPassword();


    String getKerberosConfigFile();


    String getKerberosLoginConfFile();


    String getKerberosSkipPortForLookup();


    String getProxyHost();


    Short getProxyPort();


    String getProxyUsername();


    String getProxyPassword();


    Boolean isTrustAllRoots();


    String getX509HostnameVerifier();


    String getTrustKeystore();


    String getTrustPassword();


    String getKeystore();


    String getKeystorePassword();


    Integer getConnectTimeout();


    Integer getSocketTimeout();


    Boolean isUseCookies();


    Boolean isKeepAlive();


    Integer getConnectionsMaxPerRoute();


    Integer getConnectionsMaxTotal();


    String getHeaders();


    String getResponseCharacterSet();


    File getDestinationFile();


    Boolean isFollowRedirects();


    String getQueryParams();


    Boolean isQueryParamsAreURLEncoded();


    Boolean isQueryParamsAreFormEncoded();


    String getFormParams();


    Boolean isFormParamsAreURLEncoded();


    File getSourceFile();


    String getBody();


    String getContentType();


    String getRequestCharacterSet();


    String getMultipartBodies();


    String getMultipartBodiesContentType();


    String getMultipartFiles();


    String getMultipartFilesContentType();


    Boolean isMultipartValuesAreURLEncoded();


    Boolean isChunkedRequestEntity();


    String getMethod();


    SerializableSessionObject getHttpClientCookieSession();


    GlobalSessionObject getHttpClientPoolingConnectionManager();
}
