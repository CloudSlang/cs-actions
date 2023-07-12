/*
 * Copyright 2020-2023 Open Text
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

package io.cloudslang.content.sitescope.entities;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static io.cloudslang.content.sitescope.constants.Constants.BOOLEAN_FALSE;
import static io.cloudslang.content.sitescope.constants.Constants.BOOLEAN_TRUE;
import static io.cloudslang.content.sitescope.constants.Constants.DISABLED;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ChangeMonitorStatusInputs {

    private String fullPathToMonitor;
    private String monitorId;
    private String delimiter;
    private String status;
    private String timePeriod;
    private String fromTime;
    private String toTime;
    private String description;
    private String identifier;
    private SiteScopeCommonInputs commonInputs;

    private ChangeMonitorStatusInputs(String fullPathToMonitor, String monitorId, String delimiter, String identifier,
                                      String status, String timePeriod, String fromTime, String toTime, String description,
                                      SiteScopeCommonInputs commonInputs) {
        this.fullPathToMonitor = fullPathToMonitor;
        this.monitorId = monitorId;
        this.delimiter = delimiter;
        this.identifier = identifier;
        this.status = status;
        this.timePeriod = timePeriod;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.description = description;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static ChangeMonitorStatusInputsBuilder builder() {
        return new ChangeMonitorStatusInputsBuilder();
    }

    @NotNull
    public String getFullPathToMonitor() {
        return fullPathToMonitor;
    }

    @NotNull
    public String getMonitorId() {
        return monitorId;
    }

    @NotNull
    public String getDelimiter() {
        return delimiter;
    }

    @NotNull
    public String getIdentifier() {
        return identifier;
    }

    @NotNull
    public String getStatus() {
        return status;
    }

    @NotNull
    public String getTimePeriod() {
        return timePeriod;
    }

    @NotNull
    public String getFromTime() {
        return fromTime;
    }

    @NotNull
    public String getToTime() {
        return toTime;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    @NotNull
    public SiteScopeCommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class ChangeMonitorStatusInputsBuilder {
        private String fullPathToMonitor = EMPTY;
        private String monitorId = EMPTY;
        private String delimiter = EMPTY;
        private String identifier = EMPTY;
        private String status = EMPTY;
        private String timePeriod = EMPTY;
        private String fromTime = EMPTY;
        private String toTime = EMPTY;
        private String description = EMPTY;
        private SiteScopeCommonInputs commonInputs;

        public ChangeMonitorStatusInputsBuilder() {

        }

        @NotNull
        public ChangeMonitorStatusInputsBuilder fullPathToMonitor(@NotNull final String fullPathToMonitor) {
            this.fullPathToMonitor = fullPathToMonitor;
            return this;
        }

        @NotNull
        public ChangeMonitorStatusInputsBuilder monitorId(@NotNull final String monitorId) {
            this.monitorId = monitorId;
            return this;
        }

        @NotNull
        public ChangeMonitorStatusInputsBuilder delimiter(@NotNull final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        @NotNull
        public ChangeMonitorStatusInputsBuilder identifier(@NotNull final String identifier) {
            this.identifier = identifier;
            return this;
        }

        @NotNull
        public ChangeMonitorStatusInputsBuilder status(@NotNull final String status) {
            if(Objects.equals(status.replaceAll("\\s+","").toLowerCase(),DISABLED))
                this.status = BOOLEAN_FALSE;
            else
                this.status = BOOLEAN_TRUE;
            return this;
        }

        @NotNull
        public ChangeMonitorStatusInputsBuilder timePeriod(@NotNull final String timePeriod) {
            this.timePeriod = timePeriod;
            return this;
        }

        @NotNull
        public ChangeMonitorStatusInputsBuilder fromTime(@NotNull final String fromTime) {
            this.fromTime = fromTime;
            return this;
        }

        @NotNull
        public ChangeMonitorStatusInputsBuilder toTime(@NotNull final String toTime) {
            this.toTime = toTime;
            return this;
        }

        @NotNull
        public ChangeMonitorStatusInputsBuilder description(@NotNull final String description) {
            this.description = description;
            return this;
        }

        @NotNull
        public ChangeMonitorStatusInputsBuilder commonInputs(@NotNull final SiteScopeCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public ChangeMonitorStatusInputs build() {
            return new ChangeMonitorStatusInputs(fullPathToMonitor, monitorId, delimiter, identifier, status, timePeriod, fromTime,
                    toTime, description, commonInputs);
        }
    }
}

