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

public class EnableMonitorGroupInputs {
    private String fullPathToGroup;
    private String delimiter;
    private String enable;
    private String timePeriod;
    private String fromTime;
    private String toTime;
    private String description;
    private String identifier;
    private SiteScopeCommonInputs commonInputs;

    private EnableMonitorGroupInputs(String fullPathToGroup, String delimiter, String identifier, String enable,
                                     String timePeriod, String fromTime, String toTime, String description,
                                     SiteScopeCommonInputs commonInputs) {
        this.fullPathToGroup = fullPathToGroup;
        this.delimiter = delimiter;
        this.identifier = identifier;
        this.enable = enable;
        this.timePeriod = timePeriod;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.description = description;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static EnableMonitorGroupInputsBuilder builder() {
        return new EnableMonitorGroupInputsBuilder();
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
    public String getEnable() {
        return enable;
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

    public static class EnableMonitorGroupInputsBuilder {
        private String fullPathToGroup = EMPTY;
        private String delimiter = EMPTY;
        private String identifier = EMPTY;
        private String enable = EMPTY;
        private String timePeriod = EMPTY;
        private String fromTime = EMPTY;
        private String toTime = EMPTY;
        private String description = EMPTY;
        private SiteScopeCommonInputs commonInputs;

        public EnableMonitorGroupInputsBuilder() {

        }

        @NotNull
        public EnableMonitorGroupInputs.EnableMonitorGroupInputsBuilder fullPathToGroup(@NotNull final String fullPathToGroup) {
            this.fullPathToGroup = fullPathToGroup;
            return this;
        }

        @NotNull
        public EnableMonitorGroupInputs.EnableMonitorGroupInputsBuilder delimiter(@NotNull final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        @NotNull
        public EnableMonitorGroupInputs.EnableMonitorGroupInputsBuilder identifier(@NotNull final String identifier) {
            this.identifier = identifier;
            return this;
        }

        @NotNull
        public EnableMonitorGroupInputs.EnableMonitorGroupInputsBuilder enable(@NotNull final String enable) {
            this.enable = enable;
            return this;
        }

        @NotNull
        public EnableMonitorGroupInputs.EnableMonitorGroupInputsBuilder timePeriod(@NotNull final String timePeriod) {
            this.timePeriod = timePeriod;
            return this;
        }

        @NotNull
        public EnableMonitorGroupInputs.EnableMonitorGroupInputsBuilder fromTime(@NotNull final String fromTime) {
            this.fromTime = fromTime;
            return this;
        }

        @NotNull
        public EnableMonitorGroupInputs.EnableMonitorGroupInputsBuilder toTime(@NotNull final String toTime) {
            this.toTime = toTime;
            return this;
        }

        @NotNull
        public EnableMonitorGroupInputs.EnableMonitorGroupInputsBuilder description(@NotNull final String description) {
            this.description = description;
            return this;
        }

        @NotNull
        public EnableMonitorGroupInputs.EnableMonitorGroupInputsBuilder commonInputs(@NotNull final SiteScopeCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public EnableMonitorGroupInputs build() {
            return new EnableMonitorGroupInputs(fullPathToGroup, delimiter, identifier, enable, timePeriod, fromTime,
                    toTime, description, commonInputs);
        }
    }
}
