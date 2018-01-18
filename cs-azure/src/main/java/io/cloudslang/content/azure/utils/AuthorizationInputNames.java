/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.azure.utils;

import io.cloudslang.content.constants.InputNames;

/**
 * Created by victor on 28.09.2016.
 */
public final class AuthorizationInputNames extends InputNames {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CLIENT_ID = "clientId";
    public static final String LOGIN_AUTHORITY = "loginAuthority";
    public static final String RESOURCE = "resource";
    public static final String IDENTIFIER = "identifier";
    public static final String PRIMARY_OR_SECONDARY_KEY = "primaryOrSecondaryKey";
    public static final String EXPIRY = "expiry";

    public static final String PROXY_HOST = "proxyHost";
    public static final String PROXY_PORT = "proxyPort";
    public static final String PROXY_USERNAME = "proxyUsername";
    public static final String PROXY_PASSWORD = "proxyPassword";
}
