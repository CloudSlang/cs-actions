/*
 * Copyright 2019-2024 Open Text
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

import io.cloudslang.content.utils.NumberUtilities;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.datetime.utils.Constants.EpochTimeFormatConstants.EXCEPTION_EPOCH_TIME;
import static io.cloudslang.content.datetime.utils.Constants.SchedulerTimeConstants.*;
import static io.cloudslang.content.utils.NumberUtilities.isValidInt;
import static io.cloudslang.content.utils.NumberUtilities.isValidLong;

public class InputsValidation {

    public static List<String> verifySchedulerInputs(@NotNull final String schedulerTime, @NotNull final String schedulerTimeZone) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifySchedulerTime(exceptionMessages, schedulerTime);
        addVerifySchedulerTimezone(exceptionMessages, schedulerTimeZone);
        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyTimeFormatInputs(@NotNull final String epochTime, @NotNull final String timeZone) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyEpochTime(exceptionMessages, epochTime);
        addVerifySchedulerTimezone(exceptionMessages, timeZone);
        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyEpochTimeInputs(@NotNull final String epochTime, @NotNull final String timeZone) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyEpochTime(exceptionMessages, epochTime);
        addVerifyEpochTimezone(exceptionMessages, timeZone);
        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifySchedulerTimeByHoursInputs(@NotNull final String hours, @NotNull final String timeZone) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifySchedulerNumberOfHours(exceptionMessages,hours);
        addVerifyEpochTimezone(exceptionMessages, timeZone);
        return exceptionMessages;
    }

    @NotNull
    private static List<String> addVerifyEpochTime(@NotNull List<String> exceptions, @NotNull final String input) {

        if (!isValidLong(input)) {
            exceptions.add(String.format(EXCEPTION_EPOCH_TIME, Constants.EpochTimeFormatConstants.EPOCH_TIME));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifySchedulerNumberOfHours(@NotNull List<String> exceptions, @NotNull final String input) {

        if (!isValidInt(input)) {
            exceptions.add(String.format(EXCEPTION_EPOCH_TIME, Constants.SchedulerTimeConstants.NUMBER_OF_HOURS));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifySchedulerTime(@NotNull List<String> exceptions, @NotNull final String input) {
        String[] timeFormat = input.split(COLON);
        if (timeFormat.length != 3) {
            exceptions.add(String.format(EXCEPTION_SCHEDULER_TIME, Constants.SchedulerTimeConstants.SCHEDULER_TIME));
        } else {
            try {
                int hour = Integer.parseInt(timeFormat[0]);
                int minutes = Integer.parseInt(timeFormat[1]);
                int seconds = Integer.parseInt(timeFormat[2]);
                if (!NumberUtilities.isValidInt(String.valueOf(hour), 0, 24)) {
                    exceptions.add(String.format(EXCEPTION_SCHEDULER_HOUR_TIME, "Hour"));
                } else if (!NumberUtilities.isValidInt(String.valueOf(minutes), 0, 60)) {
                    exceptions.add(String.format(EXCEPTION_SCHEDULER_MINUTES_TIME, "Minutes"));
                } else if (!NumberUtilities.isValidInt(String.valueOf(seconds), 0, 60)) {
                    exceptions.add(String.format(EXCEPTION_SCHEDULER_MINUTES_TIME, "Seconds"));
                }
            } catch (Exception e) {
                exceptions.add(String.format(EXCEPTION_SCHEDULER_TIME, Constants.SchedulerTimeConstants.SCHEDULER_TIME));
            }
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifySchedulerTimezone(@NotNull List<String> exceptions, @NotNull final String input) {
        boolean flag = false;
        LocalDateTime localDateTime = LocalDateTime.now();
        for (String zoneId : ZoneId.getAvailableZoneIds()) {

            ZoneId id = ZoneId.of(zoneId);
            ZonedDateTime zonedDateTime = localDateTime.atZone(id);
            ZoneOffset zoneOffset = zonedDateTime.getOffset();

            String offset = zoneOffset.getId().replaceAll("Z", "+00:00");
            if (input.equalsIgnoreCase(String.format("(UTC%s) %s", offset, id))) {
                flag = true;
                break;
            }
        }
        if (!flag)
            exceptions.add(String.format(EXCEPTION_SCHEDULER_TIMEZONE, Constants.SchedulerTimeConstants.TIME_ZONE));
        return exceptions;

    }

    @NotNull
    private static List<String> addVerifyEpochTimezone(@NotNull List<String> exceptions, @NotNull final String input) {
        boolean flag = false;
        LocalDateTime localDateTime = LocalDateTime.now();
        for (String zoneId : ZoneId.getAvailableZoneIds()) {

            ZoneId id = ZoneId.of(zoneId);
            ZonedDateTime zonedDateTime = localDateTime.atZone(id);
            ZoneOffset zoneOffset = zonedDateTime.getOffset();

            String offset = zoneOffset.getId().replaceAll("Z", "+00:00");
            if (input.equalsIgnoreCase(id.toString())) {
                flag = true;
                break;
            }
        }
        if (!flag)
            exceptions.add(String.format(EXCEPTION_SCHEDULER_TIMEZONE, Constants.SchedulerTimeConstants.TIME_ZONE));
        return exceptions;

    }
}
