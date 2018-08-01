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


package io.cloudslang.content.json.utils;

/**
 * Created by ioanvranauhp
 * Date 1/12/2015.
 */
public final class Constants {

    public static final class InputNames extends io.cloudslang.content.constants.InputNames {
        public static final String JSON_OBJECT = "jsonObject";
        public static final String JSON_INPUT = "jsonInput";
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
        public static final String JSON_INPUT_DESC = "The string representation of a JSON object";
        public static final String JSON_PATH_DESC = "The JSON Path query to run";
    }

    static final class EditJsonOperations {
        static final String GET_ACTION = "get";
        static final String INSERT_ACTION = "insert";
        static final String UPDATE_ACTION = "update";
        static final String DELETE_ACTION = "delete";
        static final String ADD_ACTION = "add";
    }
}
