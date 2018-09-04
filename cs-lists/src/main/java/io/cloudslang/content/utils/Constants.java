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

package io.cloudslang.content.utils;

/**
 * Created by persdana on 10/30/2014.
 */
public class Constants {

    public static final String DEFAULT_DELIMITER = ",";
    public static final String EMPTY_STRING = "";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String INPUT_NOT_BOOLEAN = "Input %s not a boolean value!";


    public static final class OutputNames {
        public static final String RETURN_RESULT = "returnResult";
        public static final String RETURN_CODE = "returnCode";
        public static final String RESPONSE_TEXT = "response";
        public static final String EXCEPTION = "exception";
        public static final String RESPONSE = "response";
        public static final String RESULT_STRING = "resultString";

        public static final String RESULT_TEXT = "result";
    }

    public static final class ResponseNames {
        public static final String SUCCESS = "success";
        public static final String FAILURE = "failure";
    }

    public static final class ReturnCodes {
        public static final String RETURN_CODE_FAILURE = "-1";
        public static final String RETURN_CODE_SUCCESS = "0";
    }

    public static final class Descriptions {
        public static final String LIST_ITERATOR = "This operation is used to iterate a list of values with the help of GlobalSessionObject in order to keep track of the last index. It is not recommended to modify the value of the \"list\" and \"separator\" inputs during the iteration process.";
        public static final String HAS_MORE_DESC = "Another value was found in the list and it has been returned.";
        public static final String NO_MORE_DESC = "The iterator has gone through the entire list. This response is returned once per list iteration.  A subsequent call to the List iterator operation restarts the list iteration process.";
        public static final String FAILURE_DESC = "The operation completed unsuccessfully.";
        public static final String LIST_DESC = "The list to iterate through.";
        public static final String SEPARATOR_DESC = "A delimiter separating the list elements. This may be single character, multi-characters or special characters.";
        public static final String RESULT_STRING_DESC = "The current list element (if the response is \"has more\").\n";
        public static final String HAS_MORE = "has more";
        public static final String NO_MORE = "no more";
        public static final String LIST = "list";
        public static final String SEPARATOR = "separator";
        public static final String RETURN_RESULT_DESC = "The current list element (if the response is \"has more\")";
        public static final String RETURN_CODE_DESC = "\"0\" if has more, \"1\" if no more values, and \"-1\" if failed.";
    }
}
