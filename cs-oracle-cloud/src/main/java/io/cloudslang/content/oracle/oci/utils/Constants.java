/*
 * (c) Copyright 2020 Micro Focus, L.P.
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
package io.cloudslang.content.oracle.oci.utils;

import org.omg.CORBA.PUBLIC_MEMBER;

public class Constants {
    public static class Common {
        public static final String API_VERSION = "/20160918";
        public static final String NEW_LINE = "\n";
        public static final String DEFAULT_API_VERSION = "20160918";
        public static final String DEFAULT_PROXY_PORT = "8080";
        public static final String BOOLEAN_FALSE = "false";
        public static final String BOOLEAN_TRUE = "true";
        public static final String STRICT = "strict";
        public static final String EMPTY = "";
        public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
        public static final String EXCEPTION_INVALID_PROXY = "The %s is not a valid proxy details.";
        public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
        public static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not a valid number value.";
        public static final String ANONYMOUS = "anonymous";
        public static final String GET = "get";
        public static final String HEAD = "head";
        public static final String DELETE = "delete";
        public static final String POST = "post";
        public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
        public static final String CHANGEIT = "changeit";
        public static final String ZERO = "0";
        public static final String CONNECT_TIMEOUT_CONST = "10000";
        public static final String UTF8 = "UTF-8";
        public static final String CONNECTIONS_MAX_PER_ROUTE_CONST = "2";
        public static final String CONNECTIONS_MAX_TOTAL_CONST = "20";
        public static final String AUTHORIZATION = "Authorization:";
        public static final String IAAS = "iaas";
        public static final String OCI_HOST = "oraclecloud.com";
        public static final String LIST_INSTANCES = "/instances";
        public static final String COMPARTMENT_ID = "compartmentId=";
        public static final String QUERY = "?";
        public static final String HTTPS = "https";
        public static final String STATUS_CODE = "statusCode";
        public static final String DELIMITER = ",";
        public static final String DATE = "date";
        public static final String REQUEST_TARGET = "(request-target)";
        public static final String HOST = "host";
        public static final String CONTENT_LENGTH = "content-length";
        public static final String COLON = ":";
        public static final String PUT = "put";
        public static final String CONTENT_TYPE = "content-type";
        public static final String X_CONTENT_SHA256 = "x-content-sha256";
        public static final String APPLICATION_JSON = "application/json";
        public static final String ALLOWED_CYPHERS = "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256," +
                "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256,TLS_DHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384," +
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256," +
                "TLS_RSA_WITH_AES_256_GCM_SHA384,TLS_RSA_WITH_AES_256_CBC_SHA256,TLS_RSA_WITH_AES_128_CBC_SHA256";

    }


    public static class ListInstancesConstants {
        public static final String LIST_INSTANCES_OPERATION_NAME = "List Instances";
        public static final String INSTANCES_LIST_JSON_PATH = "$.[*].displayName";
    }

}
