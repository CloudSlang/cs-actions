/*
 * Copyright 2019-2023 Open Text
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




package io.cloudslang.content.datetime.utils;

/**
 * Created by stcu on 22.04.2016.
 */
public class Constants {
    public static class ErrorMessages {
        public static final String DATE_NULL_OR_EMPTY = "Date is either Null or Empty";
    }

    public static class Miscellaneous {
        public static final String GMT = "GMT";
        public static final String UNIX = "unix"; //seconds since January 1, 1970
        public static final String MILLISECONDS = "S"; //milliseconds since January 1, 1970; DateTimeFormat doesn't support it

        public static final int THOUSAND_MULTIPLIER = 1000;
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

        public static final String DEFAULT_TIME_VALUE = "00";
        public static final String DEFAULT_ZONE_REPLACEMENT_VALUE = "+00:00";
        public static final String DEFAULT_ZONE_CONST = "Z";

        public static final String HYPHEN = "-";

        public static final String EMPTY_STRING = "";
        public static final String RETURN_CODE = "returnCode";
        public static final String DEFAULT_RETURN_CODE = "0";


    }
}