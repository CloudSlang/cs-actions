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

public class Descriptions {

    public static class SchedulerTime {
        public static final String SCHEDULER_TIME_OPERATION_DESC = "Converts time with specified timezone and return the scheduler time.";

        public static final String SCHEDULER_TIME_BY_HOURS_OPERATION_DESC = "Converts time with specified timezone and return the scheduler time.";
        public static final String SCHEDULER_START_TIME_DESC = "Scheduler start time.";
        public static final String TIME_ZONE_DESC = "Scheduler timeZone.";
        public static final String TRIGGER_EXPRESSION_DESC = "Scheduler trigger expression.";
        public static final String SCHEDULER_TIME_DESC = "Scheduler time in HH:MM:SS format";
        public static final String NUMBER_OF_HOURS_DESC = "Specify the number of hours scheduler should start.";
        public static final String EXCEPTION_DESC = "Exception if there was an error when executing, empty otherwise.";
    }

    public static class GetTimeFormat {

        public static final String FAILURE_DESC = "There was an error while executing the request.";
        public static final String SUCCESS_DESC = "The request was successfully executed.";
        public static final String CONVERT_EPOCH_TIME_OPERATION_DESC = "This operation converts the unix time into given format.";
        public static final String EPOCH_TIME_DESC = "Epoch time.";
        public static final String DATE_FORMAT_DESC = "Date format.";

        public static final String EPOCH_TIME_DIFFERENCE_OPERATION_DESC = "This operation converts the unix time into given format and gives the time difference.";
    }
}
