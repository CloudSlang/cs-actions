/*
 * Copyright 2022-2023 Open Text
 * This program and the accompanying materials
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




package io.cloudslang.content.utilities.util;

public final class Constants {
    public static final String FILE_RETURN_MESSAGE = "File was successfully created at: ";
    public static final String NEW_LINE = "\n";

    public static final String ENCODE_RETURN_VALUE = "The encoded value is: ";
    public static final String RETURN_PATH = "returnPath";
    public static final String RETURN_VALUE = "returnValue";

    public static final String ENCODE_NO_FILE_EXCEPTION = "The specified file does not exist.";
    public static final String ENCODE_IS_NO_FILE_EXCEPTION = "The path provided doesn\'t contain a file.";
    public static final String ENCODE_EXCEPTION_MESSAGE = "An error occurred when encoding the value read from file.";
    public static final String EXCEPTION_EMPTY_CONTENT_BYTES = "The contentBytes input are required";
    public static final String EXCEPTION_EMPTY_PATH = "The setFilePath input is required";
    public static final String EXCEPTION_MESSAGE = "Error converting the bytes to file";
    public static final String EXCEPTION_VALID_PATH = "The setFilePath is not valid";

    public static final String DEFAULT_PASSWORD_LENGTH = "10";
    public static final String ONE = "1";
    static final String EXCEPTION_PASSWORD_LENGTH = "The passwordLength should be greater.";
    static final String EXCEPTION_LENGTH = "The input for any minimum number of a specific class " +
            "of characters should be a number greater or equal to 0.";
    static final String EXCEPTION_NUMBER_FORMAT = "Wrong input number provided.";

    public static final String DEFAULT_IGNORE_CASE = "false";

    public static class CounterConstants{
        public static final String RESULT_STRING="resultString";
        public static final String INCREMENT_BY_DEFAULT_VALUE="1";
        public static final String RESULT="result";
        public static final String HASMORE="has more";
        public static final String NOMORE="no more";
        public static final String FAILURE = "failure";
        public static final String EXCEPTION = "exception";
        public static final String COUNTER_OPERATION_NAME = "Counter";
        public static final String TO = "to";
        public static final String INDEX = "index";
        public static final String FROM = "from";
        public static final String INCREMENT_BY = "incrementBy";
        public static final String BOOLEAN_FALSE = "false";
        public static final String RESET = "reset";

    }

    public static class EpochTimeFormatConstants {
        public static final String CONVERT_EPOCH_TIME_OPERATION_NAME = "Convert Epoch Time";
        public static final String EPOCH_TIME = "epochTime";
        public static final String DATE_FORMAT = "dateFormat";
        public static final String EXCEPTION_EPOCH_TIME = "The %s is not a valid.";

        public static final String EPOCH_TIME_DIFFERENCE_OPERATION_NAME = "Epoch Time Difference";

        public static final String UTC_ZONE_OFFSET = "utcZoneOffset";

        public static final String TIME_DIFFERENCE = "timeDifference";

        public static final String TIME_ZONE = "timeZone";
    }

    public static class SchedulerTimeConstants {
        public static final String SCHEDULER_START_TIME = "schedulerStartTime";
        public static final String SCHEDULER_TIME_ZONE = "schedulerTimeZone";
        public static final String TIME_ZONE = "timeZone";
        public static final String TRIGGER_EXPRESSION = "triggerExpression";
        public static final String SCHEDULER_TIME = "schedulerTime";

        public static final String NUMBER_OF_HOURS = "numberOfHours";

        public static final String TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        public static final String COLON = ":";
        public static final String NEW_LINE = "\n";
        public static final String FAILURE = "failure";
        public static final String EXCEPTION = "exception";
        public static final String SCHEDULER_TIME_OPERATION_NAME = "Scheduler Time";

        public static final String EXCEPTION_SCHEDULER_TIME = "The %s format should be in HH:MM:SS format.";
        public static final String EXCEPTION_SCHEDULER_HOUR_TIME = "The %s format should be in between 0 to 23.";
        public static final String EXCEPTION_SCHEDULER_MINUTES_TIME = "The %s format should be in between 0 to 59.";
        public static final String EXCEPTION_SCHEDULER_TIMEZONE = "The %s is not a valid.";


    }
}
