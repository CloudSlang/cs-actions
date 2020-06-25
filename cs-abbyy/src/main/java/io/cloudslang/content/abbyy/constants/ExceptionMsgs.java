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
package io.cloudslang.content.abbyy.constants;

public final class ExceptionMsgs {

    public static final String INVALID_VALUE_FOR_INPUT = "Invalid or unsupported value '%s' provided for input '%s'.";
    public static final String INVALID_VALUE_DETECTED = "Invalid or unsupported value '%s' detected in list of input '%s'.";
    public static final String INVALID_CREDENTIALS = "Invalid credentials provided for given location id.";
    public static final String INVALID_REGION = "Given coordinates do not represent a valid region.";
    public static final String UNEXPECTED_TASK_STATUS = "Unexpected task status '%s'.";
    public static final String INVALID_CREDITS = "Invalid number of credits received.";
    public static final String INVALID_ABBYY_RESPONSE = "Invalid ABBYY response received.";
    public static final String INVALID_HTTP_RESPONSE = "Invalid HTTP response received.";
    public static final String EMPTY_ID = "Value of attribute 'id' was empty.";
    public static final String NOT_ENOUGH_CREDITS = "Not enough credits to complete task. Required nr of credits: %d";
    public static final String TASK_PROCESSING_FAILED = "Task processing failed. Reason: %s";
    public static final String TASK_DELETED = "Task was deleted prematurely.";
    public static final String PROCESSING_RESULT_COULD_NOT_BE_RETRIEVED = "Processing result could not be retrieved for export format '%s'.";
    public static final String CONTENT_LENGTH_COULD_NOT_BE_RETRIEVED = "The Content-Length header could not be retrieved for export format '%s'.";
    public static final String MAX_SIZE_OF_TXT_RESULT_EXCEEDED = String.format("The maximum size of %dB was exceeded for export format '%s'.", Limits.MAX_SIZE_OF_TXT_FILE, "%s");
    public static final String MAX_SIZE_OF_XML_RESULT_EXCEEDED = String.format("The maximum size of %dB was exceeded for export format '%s'.", Limits.MAX_SIZE_OF_XML_FILE, "%s");
    public static final String MAX_SIZE_OF_PDF_RESULT_EXCEEDED = String.format("The maximum size of %dB was exceeded for export format '%s'.", Limits.MAX_SIZE_OF_PDF_FILE, "%s");
    public static final String MAX_SIZE_OF_DESCR_EXCEEDED = String.format("The maximum size of %d characters was exceeded for description.", Limits.MAX_SIZE_OF_DESCR);
    public static final String INVALID_ESTIMATED_PROCESSING_TIME = "Invalid number of milliseconds for estimated processing time was received.";
    public static final String OPERATION_TIMEOUT = "Processing of the document was not finished in the expected time.";
    public static final String TOO_MANY_EXPORT_FORMATS = "Maximum number of export formats exceeded.";
    public static final String DUPLICATED_EXPORT_FORMATS = "Some export formats appear more than once.";
    public static final String MISSING_EXPORT_FORMATS = "Missing export formats.";
    public static final String MISSING_RESULT_URLS = "Missing result URLs.";
    public static final String UNEXPECTED_STATUS = "Unexpected status.";
    public static final String DESTINATION_FOLDER_IS_NOT_FOLDER = "Given destination folder is not a directory.";
    public static final String DESTINATION_FOLDER_DOES_NOT_EXIST = "Given destination folder does not exist.";
    public static final String DESTINATION_FILE_ALREADY_EXISTS = "Given destination file already exists.";
    public static final String SOURCE_FILE_IS_NOT_REGULAR_FILE = "Given source file is not a regular file.";
    public static final String SOURCE_FILE_DOES_NOT_EXIST = "Given source file does not exist.";
    public static final String INVALID_NR_OF_COORDS = "Given region has an invalid number of coordinates.";
    public static final String RESPONSE_VALIDATION_ERROR = "An error occurred during validation of server response.";
    public static final String INVALID_TARGET_PDF = "The PDF result file seems to be invalid.";
    public static final String NEGATIVE_NUMBER = "Value of argument '%s' cannot be negative";
    public static final String INVALID_INTERVAL = "Given values do not specify a valid interval";
    public static final String ILLEGAL_RESULT_URL = "Illegal result url.";
    public static final String ERROR_RETRIEVING_EXPORT_FORMATS = "An error occurred during retrieval of some export formats.";
    public static final String EXPORT_FORMAT_AND_RESULT_URLS_DO_NOT_MATCH = "The export format list does not match the result url list.";


    private ExceptionMsgs() {

    }
}
