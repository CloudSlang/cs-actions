/*
 * (c) Copyright 2021 Micro Focus, L.P.
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

package io.cloudslang.content.microsoftAD.utils;

import io.cloudslang.content.constants.OutputNames;

public final class Outputs extends OutputNames {

    public static class OutputNames {
        public static final String USER_ID = "userId";
        public static final String STATUS_CODE = "statusCode";
        public static final String EXCEPTION = "exception";
        public static final String AUTH_TOKEN = "authToken";
        public static final String ACCOUNT_ENABLED = "accountEnabled";
        public static final String AVAILABLE_SKUIDS_LIST = "availableSkuIdsList";

    }
}

