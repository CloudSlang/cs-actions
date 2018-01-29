/*
 * (c) Copyright 2018 Micro Focus, L.P.
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
package io.cloudslang.content.dca.utils;

public class DefaultValues {
    public static final String DEFAULT_IDM_PORT = "5443";
    public static final String DEFAULT_IDM_PROTOCOL = Constants.HTTPS;
    public static final String DEFAULT_TENANT = "PROVIDER";

    public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
    public static final String DEFAULT_JAVA_KEYSTORE_PASSWORD = "changeit";
    public static final String DEFAULT_DCA_PORT = "443";
    public static final String DEFAULT_TIMEOUT = "1200";
    public static final String DEFAULT_POLLING_INTERVAL = "30";
    public static final String COMMA = ",";
}
