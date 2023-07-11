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

import io.cloudslang.content.utils.NumberUtilities;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.utilities.util.Constants.EpochTimeFormatConstants.EXCEPTION_EPOCH_TIME;
import static io.cloudslang.content.utilities.util.Constants.SchedulerTimeConstants.*;
import static io.cloudslang.content.utils.NumberUtilities.isValidLong;


public final class InputsValidation {

    @NotNull
    public static List<String> verifyBase64DecoderToFileInputs(
            @Nullable final String filePath,
            @Nullable final String contentBytes) {

        return verifyBase64DecoderToFileBytesAndPath(filePath, contentBytes);
    }

    @NotNull
    private static List<String> verifyBase64DecoderToFileBytesAndPath(@Nullable final String filePath, @Nullable final String contentBytes) {
        final List<String> exceptionMessages = new ArrayList<>();

        if (StringUtils.isEmpty(contentBytes)) {
            exceptionMessages.add(Constants.EXCEPTION_EMPTY_CONTENT_BYTES);
        }
        if (StringUtils.isEmpty(filePath)) {
            exceptionMessages.add(Constants.EXCEPTION_EMPTY_PATH);
        } else if (!isValidPath(filePath)) {
            exceptionMessages.add(Constants.EXCEPTION_VALID_PATH);
        }
        return exceptionMessages;
    }

    /**
     * This method verifies the inputs for the encode operation
     *
     * @param filePath - the absolute filepath to the file that need to be encoded
     * @return - returns an error message in case of not meeting the requirements
     */
    @NotNull
    public static List<String> verifyBase64EncoderInputs(@Nullable final String filePath) {
        final List<String> exceptionMessages = new ArrayList<>();

        if (StringUtils.isEmpty(filePath)) {
            exceptionMessages.add(Constants.EXCEPTION_EMPTY_PATH);
        } else if (!isValidPath(filePath)) {
            exceptionMessages.add(Constants.EXCEPTION_VALID_PATH);
        }
        return exceptionMessages;
    }

    /**
     * This method verifies if the path is valid and if the specified file exists
     *
     * @param path - the absolute filepath to the file
     * @return - returns a boolean response
     */
    private static boolean isValidPath(@NotNull final String path) {
        final File file = new File(path);
        return new File(FilenameUtils.getFullPathNoEndSeparator(file.getAbsolutePath())).exists();
    }

    @NotNull
    public static List<String> verifyGenerateRandomPasswordInputs(@NotNull final String passwordLength, @NotNull final String numberOfLowerCaseCharacters,
                                                                  @NotNull final String numberOfUpperCaseCharacters, @NotNull final String numberOfNumericalCharacters,
                                                                  @NotNull final String numberOfSpecialCharacters) throws Exception {
        final List<String> exceptionMessages = new ArrayList<>();
        try {
            if (!isValidLength(passwordLength) || !isValidPasswordLength(passwordLength, numberOfLowerCaseCharacters,
                    numberOfUpperCaseCharacters, numberOfNumericalCharacters, numberOfSpecialCharacters)) {
                exceptionMessages.add(Constants.EXCEPTION_PASSWORD_LENGTH);
            }
            if (!isValidLength(numberOfLowerCaseCharacters)) {
                exceptionMessages.add(Constants.EXCEPTION_LENGTH);
            }
            if (!isValidLength(numberOfUpperCaseCharacters)) {
                exceptionMessages.add(Constants.EXCEPTION_LENGTH);
            }
            if (!isValidLength(numberOfNumericalCharacters)) {
                exceptionMessages.add(Constants.EXCEPTION_LENGTH);
            }
            if (!isValidLength(numberOfSpecialCharacters)) {
                exceptionMessages.add(Constants.EXCEPTION_LENGTH);
            }
            return exceptionMessages;
        } catch (Exception exception) {
            throw new Exception(Constants.EXCEPTION_NUMBER_FORMAT);
        }
    }

    private static boolean isValidLength(@NotNull final String passwordLength) {
        int length = Integer.parseInt(passwordLength);
        return length > -1;
    }

    private static boolean isValidPasswordLength(@NotNull final String passwordLength, @NotNull final String numberOfLowerCaseCharacters,
                                                 @NotNull final String numberOfUpperCaseCharacters, @NotNull final String numberOfNumericalCharacters,
                                                 @NotNull final String numberOfSpecialCharacters) {
        return Integer.parseInt(passwordLength) >= Integer.parseInt(numberOfLowerCaseCharacters) + Integer.parseInt(numberOfUpperCaseCharacters) + Integer.parseInt(numberOfNumericalCharacters)
                + Integer.parseInt(numberOfSpecialCharacters);
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

        if (!isValidLong(input)) {
            exceptions.add(String.format(EXCEPTION_EPOCH_TIME, Constants.EpochTimeFormatConstants.EPOCH_TIME));
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
}

