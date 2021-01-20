/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.sitescope.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ChangeMonitorGroupStatusInputs {
    private String fullPathToGroup;
    private String delimiter;
    private String status;
    private String timePeriod;
    private String fromTime;
    private String toTime;
    private String description;
    private String identifier;
    private SiteScopeCommonInputs commonInputs;

    private ChangeMonitorGroupStatusInputs(String fullPathToGroup, String delimiter, String identifier, String status,
                                           String timePeriod, String fromTime, String toTime, String description,
                                           SiteScopeCommonInputs commonInputs) {
        this.fullPathToGroup = fullPathToGroup;
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
    public static ChangeMonitorGroupStatusInputs.ChangeMonitorGroupStatusInputsBuilder builder() {
        return new ChangeMonitorGroupStatusInputsBuilder();
    }

    @NotNull
    public String getFullPathToGroup() {
        return fullPathToGroup;
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
    public String getTimePeriod() { return timePeriod; }

    @NotNull
    public String getFromTime() { return fromTime; }

    @NotNull
    public String getToTime() { return toTime; }

    @NotNull
    public String getDescription() { return description; }

    @NotNull
    public SiteScopeCommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class ChangeMonitorGroupStatusInputsBuilder {
        private String fullPathToGroup = EMPTY;
        private String delimiter = EMPTY;
        private String identifier = EMPTY;
        private String status = EMPTY;
        private String timePeriod = EMPTY;
        private String fromTime = EMPTY;
        private String toTime = EMPTY;
        private String description = EMPTY;
        private SiteScopeCommonInputs commonInputs;

        public ChangeMonitorGroupStatusInputsBuilder() {

        }

        @NotNull
        public ChangeMonitorGroupStatusInputs.ChangeMonitorGroupStatusInputsBuilder fullPathToGroup(@NotNull final String fullPathToGroup) {
            this.fullPathToGroup = fullPathToGroup;
            return this;
        }

        @NotNull
        public ChangeMonitorGroupStatusInputs.ChangeMonitorGroupStatusInputsBuilder delimiter(@NotNull final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        @NotNull
        public ChangeMonitorGroupStatusInputs.ChangeMonitorGroupStatusInputsBuilder identifier(@NotNull final String identifier) {
            this.identifier = identifier;
            return this;
        }

        @NotNull
        public ChangeMonitorGroupStatusInputs.ChangeMonitorGroupStatusInputsBuilder status(@NotNull final String status) {
            this.status = status;
            return this;
        }

        @NotNull
        public ChangeMonitorGroupStatusInputs.ChangeMonitorGroupStatusInputsBuilder timePeriod(@NotNull final String timePeriod) {
            this.timePeriod = timePeriod;
            return this;
        }

        @NotNull
        public ChangeMonitorGroupStatusInputs.ChangeMonitorGroupStatusInputsBuilder fromTime(@NotNull final String fromTime) {
            this.fromTime = fromTime;
            return this;
        }

        @NotNull
        public ChangeMonitorGroupStatusInputs.ChangeMonitorGroupStatusInputsBuilder toTime(@NotNull final String toTime) {
            this.toTime = toTime;
            return this;
        }

        @NotNull
        public ChangeMonitorGroupStatusInputs.ChangeMonitorGroupStatusInputsBuilder description(@NotNull final String description) {
            this.description = description;
            return this;
        }

        @NotNull
        public ChangeMonitorGroupStatusInputs.ChangeMonitorGroupStatusInputsBuilder commonInputs(@NotNull final SiteScopeCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public ChangeMonitorGroupStatusInputs build() {
            return new ChangeMonitorGroupStatusInputs(fullPathToGroup, delimiter, identifier, status, timePeriod, fromTime,
                    toTime, description, commonInputs);
        }
    }
}
