/*
 * (c) Copyright 2022 Micro Focus
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
/*
 * (c) Copyright 2022 Micro Focus
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
package io.cloudslang.content.redhat.utils;

public final class Constants {

    public static final class CommonConstants {

        //Inputs
        public static final String HOST = "host";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String NAME = "name";
        public static final String NAMESPACE = "namespace";
        public static final String DEPLOYMENT = "deployment";
        public static final String DEPLOYMENT_NAME= "deploymentName";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String TLS_VERSION = "tlsVersion";
        public static final String ALLOWED_CIPHERS = "allowedCiphers";
        public static final String TRUST_ALL_ROOTS = "trustAllRoots";
        public static final String X509_HOSTNAME_VERIFIER = "x509HostnameVerifier";
        public static final String TRUST_KEYSTORE = "trustKeystore";
        public static final String TRUST_PASSWORD = "trustPassword";
        public static final String KEYSTORE = "keystore";
        public static final String KEYSTORE_PASSWORD = "keystorePassword";
        public static final String CONNECT_TIMEOUT = "connectTimeout";
        public static final String EXECUTION_TIMEOUT = "executionTimeout";
        public static final String CONNECTIONS_MAX_PER_ROUTE = "connectionsMaxPerRoute";
        public static final String CONNECTIONS_MAX_TOTAL = "connectionsMaxTotal";
        public static final String SESSION_COOKIES = "httpClientCookieSession";
        public static final String SESSION_CONNECTION_POOL = "httpClientPoolingConnectionManager";

        //
        public static final String BASIC = "BASIC";
        public static final String APPLICATION_JSON = "application/json";
        public static final String CONTENT_TYPE = "Content-Type:";
        public static final String TRUE = "true";
        public static final String FALSE = "FALSE";
        public static final String EMPTY_STRING = "";
        public static final String AUTHORIZATION_BEARER = "Authorization: Bearer ";
        public static final String AUTHORIZATION_BASIC = "Authorization: Basic ";
        public static final String ANONYMOUS = "Anonymous";

        //Other
        public static final String FORWARD_SLASH = "/";
        public static final String COMMA = ",";


        // Create Deployment
        public static final String AUTH_TOKEN = "authToken";
        public static final String DEFINITION = "definition";

        //Routes
        public static final String ROUTE_NAME = "routeName";


        // endpoints
        public static final String CREATE_DEPLOYMENT_ENDPOINT(String namespace) {
            return "/apis/apps/v1/namespaces/" + namespace + "/deployments";
        }

        public static final String APPS_V1_NAMESPACES = "/apis/apps/v1/namespaces/";
        public static final String DEPLOYMENTS = "/deployments";

        //query for authorization
        public static final String QUERY_PARAM = "response_type=code&client_id=openshift-browser-client";
        public static final String AUTHORIZE_TOKEN_URL = "/oauth/authorize";
        public static final String FORM_INPUT = "form > input";
        public static final String VALUE = "value";
        public static final String CODE = "code";
        public static final String CSRF = "&csrf";
        public static final String EQUAL = "=";
        public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
        public static final String DISPLAY_TOKEN_ENDPOINT = "/oauth/token/display";
        public static final String COLON_PUNCTUATION = ":";

        public static final String GET_DEPLOYMENT_STATUS_ENDPOINT_1 = "/apis/apps/v1/namespaces/";
        public static final String GET_DEPLOYMENT_STATUS_ENDPOINT_2 = "/deployments/";
        public static final String GET_DEPLOYMENT_STATUS_ENDPOINT_3 = "/status";
        public static final String GET_DEPLOYMENT_STATUS_ENDPOINT_4 = "/deployments";


        public static final String POD_ENDPOINT_1 = "/api/v1/namespaces/";
        public static final String POD_ENDPOINT_2 = "/pods";
        public static final String POD_ENDPOINT_3 = "/podtemplates";
        public static final String NEGATIVE_RETURN_CODE = "-1";
        public static final String CONNECTION_MAX_PER_ROUTE = "20";
        public static final String CONNECTIONS_MAX_TOTAL_VALUE = "200";

        public static final String PROPERTY_NAME = "name";
        public static final String PROPERTY_UID = "uid";
        public static final String PROPERTY_ITEMS = "items";
        public static final String PROPERTY_METADATA = "metadata";
        public static final String SPECIFICATION = "specification";
        public static final String POD_NAME = "podName";
        public static final String BUILD_CONFIG = "buildConfig";

        public static final String ROUTE_ENDPOINT_1 = "/apis/route.openshift.io/v1/namespaces/";
        public static final String ROUTE_ENDPOINT_2 = "/routes";

        public static final String TEMPLATE_ENDPOINT_1 = "/apis/template.openshift.io/v1/namespaces/";
        public static final String TEMPLATE_ENDPOINT_2 = "/templates";

        public static final String CREATE_BUILD_ENDPOINT_1 = "/apis/build.openshift.io/v1/namespaces/";
        public static final String CREATE_BUILD_ENDPOINT_2 = "/buildconfigs/";
        public static final String CREATE_BUILD_ENDPOINT_3 = "/instantiate";
    }

}

