/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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

public class Descriptions {
    public static class AddPropertyToObject {
        public static final String ADD_PROPERTY_TO_OBJECT = "Add Property to Object";
        public static final String SUCCESS_DESC = "The property was added successfully to the object.";
        public static final String FAILURE_DESC = "There was an error while trying to add property to object.";

        public static final String EXCEPTION_DESC = "In case of success response, this result is empty. In case of failure response " +
                "this result contains the java stack trace of the runtime exception.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String RETURN_RESULT_DESC = "This will contain the JSON with the new property/value added or error message in case of failure.";

        public static final String JSON_OBJECT_DESC = "String representation of a JSON object. Objects in JSON are a collection of name value pairs " +
                "separated by a colon and surrounded with curly brackets {}. The name must be a string value and the value can be " +
                "a single string or any valid JSON object or array. Examples: {\"one\":1, \"two\":2}, {\"one\":{\"a\":\"a\",\"B\":\"B\"}, \"two\":\"two\", \"three\":[1,2,3.4]}";
        public static final String NEW_PROPERTY_NAME_DESC = "The name of the new property to add to the JSON object. There is no rule as to which character to use. Examples: property1, some_property, another property";
        public static final String NEW_PROPERTY_VALUE_DESC = "The value for the new property. This is interpreted as a string, no matter what the contents of the input. Examples: value, 1, [1,2,3]";
    }
}
