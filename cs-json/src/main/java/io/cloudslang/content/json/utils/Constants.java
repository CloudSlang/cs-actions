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


package io.cloudslang.content.json.utils;

/**
 * Created by ioanvranauhp
 * Date 1/12/2015.
 */
public final class Constants {


    public static final class InputNames extends io.cloudslang.content.constants.InputNames {
        public static final String JSON_OBJECT = "jsonObject";
        public static final String NEW_PROPERTY_NAME = "newPropertyName";
        public static final String NEW_PROPERTY_VALUE = "newPropertyValue";
        public static final String OBJECT = "object";
        public static final String KEY = "key";
        public static final String ARRAY = "array";
        public static final String FROM_INDEX = "fromIndex";
        public static final String TO_INDEX = "toIndex";
        public static final String ARRAY1 = "array1";
        public static final String ARRAY2 = "array2";
        public static final String ACTION = "action";
        public static final String JSON_PATH = "jsonPath";
        public static final String NAME = "name";
        public static final String VALUE = "value";
        public static final String VALIDATE_VALUE = "validateValue";
        public static final String INDEX = "index";
        public static final String ELEMENT = "element";
        public static final String DOUBLE_QUOTES = "\"";
        public static final String BRACKET = "{";
        public static final String SQUARE_BRACKET = "[";
    }

    static final class EditJsonOperations {
        static final String GET_ACTION = "get";
        static final String INSERT_ACTION = "insert";
        static final String UPDATE_ACTION = "update";
        static final String DELETE_ACTION = "delete";
        static final String ADD_ACTION = "add";
    }

    public static final class AddPropertyToObject {

        public static final String NEW_LINE = "\n";
        public static final String ADD_PROPERTY_EXCEPTION = "The value could not be added!";
        public static final String VALUE_FOR_PROPERTY = "The value for the property ";
        public static final String INVALID_JSON_OBJECT_PROVIDED = "Invalid jsonObject provided! ";
        static final String EMPTY_JSON = "Empty JSON string";
        static final String JSON_EXCEPTION = "jsonObject is not a valid JSON Object";

    }

    public static final class GetArraySublistAction {

        public static final String NEGATIVE_FROM_INPUT_VALUE = "Negative fromIndex value.";
        public static final String NEGATIVE_TO_INPUT_VALUE = "Negative toIndex value.";
        public static final String TO_INDEX_HIGHER_THAN_FROM_INDEX = "toIndex value must be higher than the value of the fromIndex input.";
        public static final String INVALID_FROM_INDEX_VALUE = "The value for fromIndex input exceeded the length of the array.";
        public static final String INVALID_TO_INDEX_VALUE = "The value for toIndex input exceeded the length of the array.";


    }
}
