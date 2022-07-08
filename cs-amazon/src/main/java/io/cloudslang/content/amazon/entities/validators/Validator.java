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


package io.cloudslang.content.amazon.entities.validators;

import io.cloudslang.content.amazon.entities.constants.Constants;
import io.cloudslang.content.utils.NumberUtilities;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.amazon.entities.constants.Constants.GetTimeFormatConstants.EXCEPTION_EPOCH_TIME;
import static io.cloudslang.content.amazon.entities.constants.Constants.SchedulerTimeConstants.*;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.NumberUtilities.isValidInt;
import static io.cloudslang.content.utils.NumberUtilities.isValidLong;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.join;

public class Validator {
    private final List<String> errorList;

    public Validator() {
        errorList = new ArrayList<>();
    }

    public boolean hasErrors() {
        return !errorList.isEmpty();
    }

    public String getErrors() {
        return getErrors(System.lineSeparator());
    }

    public String getErrors(@NotNull final String delimiter) {
        return join(errorList, delimiter);
    }

    public Validator validatePort(@NotNull final String value, @NotNull final String inputName) {
        if (!isValidIpPort(value)) {
            errorList.add(format("[%s]: Invalid port value [%s]!", inputName, value));
        }
        return this;
    }

    public Validator validateInt(@NotNull final String intValue, @NotNull final String inputName) {
        if (!isValidInt(intValue)) {
            errorList.add(format("[%s]: Invalid integer value.", inputName));
        }
        return this;
    }

    public Validator validateBoolean(@NotNull final String booleanValue, @NotNull final String inputName) {
        if (!isValid(booleanValue)) {
            errorList.add(format("[%s]: Invalid boolean value!", inputName));
        }
        return this;

    }

    @NotNull
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
    private static List<String> addVerifyEpochTime(@NotNull List<String> exceptions, @NotNull final String input) {

        if(!isValidLong(input))
        {
            exceptions.add(String.format(EXCEPTION_EPOCH_TIME, Constants.GetTimeFormatConstants.EPOCH_TIME));
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
            if (input.equalsIgnoreCase(String.format("(UTC%s) %s", offset, id.toString()))) {
                flag = true;
                break;
            }

        }
        if (!flag)
            exceptions.add(String.format(EXCEPTION_SCHEDULER_TIMEZONE, Constants.SchedulerTimeConstants.TIME_ZONE));
        return exceptions;

    }
}
