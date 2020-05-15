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

package io.cloudslang.content.oracle.utils;

import io.cloudslang.content.constants.InputNames;

public class Inputs extends InputNames {

    public static class CommonInputs {
        public static final String TENANCY_OCID = "tenancyOcid";
        public static final String USER_OCID = "userOcid";
        public static final String FINGER_PRINT = "fingerPrint";
        public static final String PRIVATE_KEY_FILE = "privateKeyFile";
        public static final String AUTH_TOKEN = "authToken";
        public static final String ORGANIZATION_NAME = "organizationName";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String REQUEST_BODY = "requestBody";
        public static final String OCI_VERSION = "ociVersion";
        public static final String PAGE_NUMBER = "pageNumber";
        public static final String PAGE_SIZE = "pageSize";
    }

    public static class ListInstancesInputs {
        public static final String COMPARTMENT_OCID = "compartmentOcid";
    }



}
