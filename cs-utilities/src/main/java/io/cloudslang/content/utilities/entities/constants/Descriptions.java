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



package io.cloudslang.content.utilities.entities.constants;

/**
 * Created by moldovai on 8/28/2017.
 */
public class Descriptions {

    public static class OperationDescription {
        public static final String OPERATION_DESC = "This operation checks if a string is blank or empty and if it's true a default value will be assigned instead of the initial string.";
        public static final String FIND_TEXT_IN_PDF_OPERATION_DESC = "This operation checks if a text input is found in a PDF file.";
    }

    public static class InputsDescription {
        public static final String INITIAL_VALUE_DESC = "The initial string.";
        public static final String IGNORE_CASE_DESC = "A variable used to check if the comparison should ignore the case of the letters.";
        public static final String DEFAULT_VALUE_DESC = "The default value used to replace the initial string.";
        public static final String TRIM_DESC = "A variable used to check if the initial string is blank or empty.";
        public static final String PASSWORD_DESC = "Password used to decrypt the PDF file.";
    }

    public static class OutputsDescription {
        public static final String RETURN_CODE_DESC = "The returnCode of the operation: 0 for success, -1 for failure.";
        public static final String RETURN_RESULT_DESC = "This will contain the replaced string with the default value.";
        public static final String FIND_TEXT_IN_PDF_RETURN_RESULT_DESC = "The number of occurrences of the text in the PDF file.";
        public static final String EXCEPTION_DESC = "In case of success response, this result is empty. In case of failure response, this result contains the java stack trace of the runtime exception.";
    }

    public static class ResultsDescription {
        public static final String SUCCESS_DESC = "The operation completed successfully.";
        public static final String FAILURE_DESC = "An error occurred during execution.";
    }
}
