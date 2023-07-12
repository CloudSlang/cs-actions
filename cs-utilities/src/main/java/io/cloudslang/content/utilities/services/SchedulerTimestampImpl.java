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


package io.cloudslang.content.utilities.services;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.utilities.util.Constants.SchedulerTimeConstants.*;

public class SchedulerTimestampImpl {


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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        map.put(SCHEDULER_START_TIME, localDateTime.withSecond(seconds).withNano(66).format(formatter) + utcTime);
        map.put(TIME_ZONE, timeZone);
        map.put("returnCode", "0");
        return map;
    }
}
