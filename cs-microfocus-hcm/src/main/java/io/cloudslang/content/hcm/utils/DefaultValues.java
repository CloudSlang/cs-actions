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
package io.cloudslang.content.hcm.utils;

public class DefaultValues {

    public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
    public static final String DEFAULT_TRUST_ALL_ROOTS = "false";
    public static final String DEFAULT_X_509_HOSTNAME_VERIFIER = "strict";
    public static final String DEFAULT_PROXY_PORT = "8080";
    public static final String DEFAULT_JAVA_KEYSTORE_PASSWORD = "changeit";

}
