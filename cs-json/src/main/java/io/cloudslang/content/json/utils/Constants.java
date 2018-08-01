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

package io.cloudslang.content.json.utils;

/**
 * Created by ioanvranauhp
 * Date 1/12/2015.
 */
public final class Constants {

    public static final class InputNames extends io.cloudslang.content.constants.InputNames {
        public static final String JSON_OBJECT = "jsonObject";
        public static final String JSON_SCHEMA = "jsonSchema";
        public static final String NEW_PROPERTY_NAME = "newPropertyName";
        public static final String NEW_PROPERTY_VALUE = "newPropertyValue";
        public static final String OBJECT = "object";
        public static final String KEY = "key";
        public static final String ARRAY = "array";
        public static final String ACTION = "action";
        public static final String JSON_PATH = "jsonPath";
        public static final String NAME = "name";
        public static final String VALUE = "value";
        public static final String VALIDATE_VALUE = "validateValue";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String HTTP_GET = "get";
    }

    public static final class Description {
        public static final String JSON_OBJECT_DESC = "The JSON to validate.";
        public static final String JSON_SCHEMA_DESC = "The JSON schema to validate against. Can also be an URL or a file path.";
        public static final String PROXY_HOST_DESC = "The proxy host for the Get request.";
        public static final String PROXY_PORT_DESC = "The proxy port for the Get request.";
        public static final String PROXY_USERNAME_DESC = "The username for connecting via proxy.";
        public static final String PROXY_PASSWORD_DESC = "The password for connecting via proxy.";
    }

    public static final class ValidationMessages {
        public static final String VALID_JSON = "Valid JSON";
        public static final String EMPTY_JSON_PROVIDED = "Empty JSON string provided";
        public static final String EMPTY_SCHEMA_URL = "Empty schema returned by request";
        public static final String VALID_JSON_AGAINST_SCHEMA = "The JSON validates against the schema";
        public static final String INVALID_JSON_AGAINST_SCHEMA = "The JSON does not validate against the schema";
    }

    static final class EditJsonOperations {
        static final String GET_ACTION = "get";
        static final String INSERT_ACTION = "insert";
        static final String UPDATE_ACTION = "update";
        static final String DELETE_ACTION = "delete";
        static final String ADD_ACTION = "add";
    }

}
