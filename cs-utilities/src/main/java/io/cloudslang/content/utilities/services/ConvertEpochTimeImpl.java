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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.utilities.util.Constants.EpochTimeFormatConstants.*;
import static io.cloudslang.content.utilities.util.Constants.EpochTimeFormatConstants.UTC_ZONE_OFFSET;
import static java.time.temporal.ChronoUnit.DAYS;

public class ConvertEpochTimeImpl {
    @NotNull
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
        LocalDateTime localDateTime1 = LocalDateTime.now(z);
        StringBuffer timestamp = new StringBuffer();
        int hours = localDateTime.getHour() - localDateTime1.getHour();
        int minutes = localDateTime.getMinute() - localDateTime1.getMinute();
        timestamp.append(DAYS.between(localDateTime,localDateTime1));
        if (("" + hours).contains("-"))
            timestamp.append(":00:");
        else
            timestamp.append(":").append(hours).append(":");

        if (("" + minutes).contains("-"))
            timestamp.append("00");
        else
            timestamp.append(minutes);

        timestamp.append(":00");
        map.put(TIME_DIFFERENCE, timestamp.toString());
        map.put(UTC_ZONE_OFFSET, localDateTime.atZone(z).getOffset().getId().replaceAll("Z", "+00:00"));
        map.put("returnCode", "0");
        return map;

    }
}
