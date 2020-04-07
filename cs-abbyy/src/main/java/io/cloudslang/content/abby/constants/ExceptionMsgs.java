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
package io.cloudslang.content.abby.constants;

public final class ExceptionMsgs {

    public static final String INVALID_PROFILE = "Invalid profile '%s'.";
    public static final String INVALID_TEXT_TYPE = "Invalid text type '%s'.";
    public static final String INVALID_IMAGE_SOURCE = "Invalid image source '%s'.";
    public static final String INVALID_EXPORT_FORMAT = "Invalid export format '%s'.";
    public static final String INVALID_WRITE_TAGS = "Invalid write tags value '%s'.";
    public static final String INVALID_MARKING_TYPE = "Invalid marking type '%s'.";
    public static final String INVALID_WRITING_STYLE = "Invalid writing style '%s'.";
    public static final String INVALID_LOCATION_ID = "Invalid location id '%s'.";
    public static final String INVALID_REGION = "Given coordinates do not represent a valid region.";
    public static final String UNKNOWN_TASK_STATUS = "Unknown task status '%s'.";
    public static final String INVALID_CREDITS = "Invalid number of credits received.";
    public static final String INVALID_RESPONSE = "Invalid response received.";
    public static final String EMPTY_ID = "Value of attribute 'id' was empty.";
    public static final String EMPTY_CREDITS = "Value of attribute 'credits' was empty.";
    public static final String EMPTY_STATUS = "Value of attribute 'status' was empty.";
    public static final String NOT_ENOUGH_CREDITS = "Not enough credits to complete task. Required nr of credits: %d";
    public static final String TASK_PROCESSING_FAILED = "Task processing failed. Reason: %s";
    public static final String TASK_DELETED = "Task was deleted prematurely.";
    public static final String PROCESSING_RESULT_COULD_NOT_BE_RETRIEVED = "Processing result could not be retrieved.";
    public static final String NULL_ARGUMENT = "Value of argument '%s' was null.";
    public static final String INVALID_ESTIMATED_PROCESSING_TIME = "Invalid number of milliseconds for estimated processing time was received.";
    public static final String OPERATION_TIMEOUT = "Processing of the document was not finished in the expected time.";
    public static final String TOO_MANY_EXPORT_FORMATS = "Maximum number of export formats exceeded.";
    public static final String UNEXPECTED_STATUS = "Unexpected status.";


    private ExceptionMsgs() {

    }
}
