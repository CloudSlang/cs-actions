package io.cloudslang.content.sitescope.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class EnableMonitorInputs {

    private String fullPathToMonitor;
    private String monitorId;
    private String delimiter;
    private String enable;
    private String timePeriod;
    private String fromTime;
    private String toTime;
    private String description;
    private String identifier;
    private SiteScopeCommonInputs commonInputs;

    private EnableMonitorInputs(String fullPathToMonitor, String monitorId, String delimiter, String identifier, String enable,
                                String timePeriod, String fromTime, String toTime, String description,
                                SiteScopeCommonInputs commonInputs) {
        this.fullPathToMonitor = fullPathToMonitor;
        this.monitorId = monitorId;
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
    public static EnableMonitorInputsBuilder builder() {
        return new EnableMonitorInputsBuilder();
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
    public String getEnable() {
        return enable;
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

    public static class EnableMonitorInputsBuilder {
        private String fullPathToMonitor = EMPTY;
        private String monitorId = EMPTY;
        private String delimiter = EMPTY;
        private String identifier = EMPTY;
        private String enable = EMPTY;
        private String timePeriod = EMPTY;
        private String fromTime = EMPTY;
        private String toTime = EMPTY;
        private String description = EMPTY;
        private SiteScopeCommonInputs commonInputs;

        public EnableMonitorInputsBuilder() {

        }

        @NotNull
        public EnableMonitorInputsBuilder fullPathToMonitor(@NotNull final String fullPathToMonitor) {
            this.fullPathToMonitor = fullPathToMonitor;
            return this;
        }

        @NotNull
        public EnableMonitorInputsBuilder monitorId(@NotNull final String monitorId) {
            this.monitorId = monitorId;
            return this;
        }

        @NotNull
        public EnableMonitorInputsBuilder delimiter(@NotNull final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        @NotNull
        public EnableMonitorInputsBuilder identifier(@NotNull final String identifier) {
            this.identifier = identifier;
            return this;
        }

        @NotNull
        public EnableMonitorInputsBuilder enable(@NotNull final String enable) {
            this.enable = enable;
            return this;
        }

        @NotNull
        public EnableMonitorInputsBuilder timePeriod(@NotNull final String timePeriod) {
            this.timePeriod = timePeriod;
            return this;
        }

        @NotNull
        public EnableMonitorInputsBuilder fromTime(@NotNull final String fromTime) {
            this.fromTime = fromTime;
            return this;
        }

        @NotNull
        public EnableMonitorInputsBuilder toTime(@NotNull final String toTime) {
            this.toTime = toTime;
            return this;
        }

        @NotNull
        public EnableMonitorInputsBuilder description(@NotNull final String description) {
            this.description = description;
            return this;
        }

        @NotNull
        public EnableMonitorInputsBuilder commonInputs(@NotNull final SiteScopeCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public EnableMonitorInputs build() {
            return new EnableMonitorInputs(fullPathToMonitor, monitorId, delimiter, identifier, enable, timePeriod, fromTime,
                    toTime, description, commonInputs);
        }
    }
}

