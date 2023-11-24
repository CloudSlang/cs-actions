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
package io.cloudslang.content.datetime.services;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.datetime.utils.Constants.EpochTimeFormatConstants.*;
import static io.cloudslang.content.datetime.utils.Constants.EpochTimeFormatConstants.TIME_ZONE;
import static io.cloudslang.content.datetime.utils.Constants.SchedulerTimeConstants.*;
import static java.time.temporal.ChronoUnit.MINUTES;

public class SchedulerService {

    public static Map<String, String> getTimeFormat(String epochTime, String timeZone) {
        Map<String, String> map = new HashMap<>();
        map.put("returnCode", "0");
        map.put(DATE_FORMAT, LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(epochTime)),
                ZoneId.of(timeZone.split("\\) ")[1])) + ":00");
        return map;

    }

    @NotNull
    public static Map<String, String> getTimeDifference(String epochTime, String zoneId) {
        Map<String, String> map = new HashMap<>();
        ZoneId z = ZoneId.of(zoneId);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(epochTime)), z);
        map.put(DATE_FORMAT, localDateTime.toString());
        map.put(TIME_ZONE, z.toString());
        LocalDateTime currentLocalDateTime = LocalDateTime.now(z);
        StringBuffer timestamp = new StringBuffer();
        long hours, minutes;
        long days = currentLocalDateTime.until(localDateTime, ChronoUnit.DAYS);
        if (days > 0) {
            currentLocalDateTime = currentLocalDateTime.plusDays(days);

        }
        hours = currentLocalDateTime.until(localDateTime, ChronoUnit.HOURS);
        if (hours > 0) {
            currentLocalDateTime = currentLocalDateTime.plusHours(hours);
        }

        minutes = currentLocalDateTime.until(localDateTime, MINUTES);
        timestamp.append(days);
        if ((EMPTY_STRING + hours).contains(HYPHEN))
            timestamp.append(COLON + DEFAULT_TIME_VALUE + COLON);
        else
            timestamp.append(COLON).append(hours).append(COLON);

        if ((EMPTY_STRING + minutes).contains(HYPHEN))
            timestamp.append(DEFAULT_TIME_VALUE);
        else
            timestamp.append(minutes);

        timestamp.append(COLON + DEFAULT_TIME_VALUE);
        map.put(TIME_DIFFERENCE, timestamp.toString());
        map.put(UTC_ZONE_OFFSET, localDateTime.atZone(z).getOffset().getId().replaceAll(DEFAULT_ZONE_CONST, DEFAULT_ZONE_REPLACEMENT_VALUE));
        map.put(RETURN_CODE, DEFAULT_RETURN_CODE);
        return map;

    }

    @NotNull
    public static Map<String, String> getSchedulerTime(String time, String timeZone) {

        String[] timeZoneWithUTC = timeZone.split("\\) ");
        timeZone = timeZoneWithUTC[1];
        String utcTime = timeZoneWithUTC[0].split("\\(UTC")[1];
        String[] time1 = time.split(":");
        int hour = Integer.parseInt(time1[0]);
        int minutes = Integer.parseInt(time1[1]);
        int seconds = Integer.parseInt(time1[2]);
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of(timeZone)).withHour(hour).withMinute(minutes);
        String expression = seconds + " " + minutes + " " + hour + " ? * *";
        Map<String, String> map = new HashMap<>();
        map.put(TRIGGER_EXPRESSION, expression);
        LocalDateTime localDateAndTimeWithTimeZone = LocalDateTime.now(ZoneId.of(timeZone));
        if (localDateAndTimeWithTimeZone.compareTo(localDateTime) >= 1) {
            localDateTime = localDateTime.plusDays(1);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

        map.put(SCHEDULER_START_TIME, localDateTime.withSecond(seconds).withNano(66).format(formatter) + utcTime);
        map.put(TIME_ZONE, timeZone);
        map.put("returnCode", "0");
        return map;
    }

    @NotNull
    public  static Map<String, String> getSchedulerTimeByHours(String hours, String timeZone) {


        int hour = Integer.parseInt(hours);
        ZoneId z = ZoneId.of(timeZone);

        LocalDateTime localDateTime = LocalDateTime.now(z);
        int value = hour*60*60;
        String expression =   "*/"+value+"000";
        Map<String, String> map = new HashMap<>();
        map.put(TRIGGER_EXPRESSION, expression);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

        map.put(SCHEDULER_START_TIME, localDateTime.plusMinutes(10).withSecond(0).withNano(66).format(formatter) + localDateTime.atZone(z).getOffset().getId().replaceAll("Z", "+00:00"));
        map.put(TIME_ZONE, timeZone);
        map.put("returnCode", "0");
        return map;
    }
}
