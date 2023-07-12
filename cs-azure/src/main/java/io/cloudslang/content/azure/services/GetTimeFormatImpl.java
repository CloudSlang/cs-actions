/*
 * Copyright 2023 Open Text
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




package io.cloudslang.content.azure.services;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.azure.utils.Constants.GetTimeFormatConstants.DATE_FORMAT;

public class GetTimeFormatImpl {
    @NotNull
    public static Map<String, String> getTimeFormat(String epochTime, String timeZone) {
        Map<String, String> map = new HashMap<>();
        map.put("returnCode", "0");
        map.put(DATE_FORMAT, LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(epochTime)),
                ZoneId.of(timeZone.split("\\) ")[1])) + ":00");
        return map;

    }
}
