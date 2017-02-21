/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.utils.InputsUtil;
import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.isBlank;

import static io.cloudslang.content.amazon.utils.InputsUtil.getEnforcedBooleanCondition;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
/**
 * Created by Mihai Tusa.
 * 6/22/2016.
 */
public class VolumeInputs {
    private final String snapshotId;
    private final String size;
    private final String iops;
    private final String deviceName;
    private final String description;
    private final String maxResults;
    private final String nextToken;
    private final String volumeIdsString;

    private final boolean encrypted;
    private final boolean force;

    private VolumeInputs(Builder builder) {
        this.snapshotId = builder.snapshotId;
        this.size = builder.size;
        this.iops = builder.iops;
        this.deviceName = builder.deviceName;
        this.description = builder.description;
        this.maxResults = builder.maxResults;
        this.nextToken = builder.nextToken;
        this.volumeIdsString = builder.volumeIdsString;

        this.encrypted = builder.encrypted;
        this.force = builder.force;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public String getSize() {
        return size;
    }

    public String getIops() {
        return iops;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public boolean isForce() {
        return force;
    }

    public String getMaxResults() {
        return maxResults;
    }

    public String getNextToken() {
        return nextToken;
    }

    public String getVolumeIdsString() {
        return volumeIdsString;
    }

    public static class Builder {
        private String snapshotId;
        private String size;
        private String iops;
        private String deviceName;
        private String description;
        private String maxResults;
        private String nextToken;
        private String volumeIdsString;

        private boolean encrypted;
        private boolean force;

        public VolumeInputs build() {
            return new VolumeInputs(this);
        }

        public Builder withSnapshotId(@NotNull final String inputValue) {
            snapshotId = inputValue;
            return this;
        }

        public Builder withSize(@NotNull final String inputValue) {
            size = inputValue;
            return this;
        }

        public Builder withIops(@NotNull final String inputValue) {
            iops = isBlank(inputValue) ? NOT_RELEVANT : inputValue;
            return this;
        }

        public Builder withDeviceName(@NotNull final String inputValue) {
            deviceName = inputValue;
            return this;
        }

        public Builder withDescription(@NotNull final String inputValue) {
            description = inputValue;
            return this;
        }

        public Builder withEncrypted(@NotNull final String inputValue) {
            encrypted = getEnforcedBooleanCondition(inputValue, false);
            return this;
        }

        public Builder withForce(@NotNull final String inputValue) {
            force = getEnforcedBooleanCondition(inputValue, false);
            return this;
        }

        public Builder withMaxResults(@NotNull final String inputValue) {
            maxResults = InputsUtil.getMaxResultsCount(inputValue);
            return this;
        }

        public Builder withNextToken(@NotNull final String inputValue) {
            nextToken = inputValue;
            return this;
        }

        public Builder withVolumeIdsString(@NotNull final String inputValue) {
            volumeIdsString = inputValue;
            return this;
        }
    }
}
