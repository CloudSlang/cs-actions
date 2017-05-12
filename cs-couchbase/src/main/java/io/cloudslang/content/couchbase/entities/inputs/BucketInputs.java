/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.entities.inputs;

import io.cloudslang.content.couchbase.entities.couchbase.AuthType;
import io.cloudslang.content.couchbase.entities.couchbase.BucketType;
import io.cloudslang.content.couchbase.entities.couchbase.ConflictResolutionType;
import io.cloudslang.content.couchbase.entities.couchbase.EvictionPolicy;

import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.DEFAULT_REPLICA_NUMBER;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.DEFAULT_THREADS_NUMBER;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.INIT_INDEX;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.MAXIMUM_REPLICA_NUMBER;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.MINIMUM_RAM_QUOTA_AMOUNT;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.MAXIMUM_THREADS_NUMBER;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.MINIMUM_THREADS_NUMBER;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getEnforcedBooleanCondition;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getEnabledString;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getValidIntValue;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getValidPort;
import static java.lang.String.valueOf;

/**
 * Created by Mihai Tusa
 * 4/9/2017.
 */
public class BucketInputs {
    private final String authType;
    private final String bucketName;
    private final String bucketType;
    private final String conflictResolutionType;
    private final String evictionPolicy;
    private final String flushEnabled;
    private final String parallelDBAndViewCompaction;
    private final String replicaIndex;
    private final String saslPassword;

    private final Integer proxyPort;
    private final Integer ramQuotaMB;
    private final Integer replicaNumber;
    private final Integer threadsNumber;

    private BucketInputs(BucketInputs.Builder builder) {
        this.authType = builder.authType;
        this.bucketName = builder.bucketName;
        this.bucketType = builder.bucketType;
        this.conflictResolutionType = builder.conflictResolutionType;
        this.evictionPolicy = builder.evictionPolicy;
        this.flushEnabled = builder.flushEnabled;
        this.parallelDBAndViewCompaction = builder.parallelDBAndViewCompaction;
        this.replicaIndex = builder.replicaIndex;
        this.saslPassword = builder.saslPassword;

        this.proxyPort = builder.proxyPort;
        this.ramQuotaMB = builder.ramQuotaMB;
        this.replicaNumber = builder.replicaNumber;
        this.threadsNumber = builder.threadsNumber;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getAuthType() {
        return authType;
    }

    public String getBucketType() {
        return bucketType;
    }

    public String getConflictResolutionType() {
        return conflictResolutionType;
    }

    public String getEvictionPolicy() {
        return evictionPolicy;
    }

    public String getFlushEnabled() {
        return flushEnabled;
    }

    public String getParallelDBAndViewCompaction() {
        return parallelDBAndViewCompaction;
    }

    public String getReplicaIndex() {
        return replicaIndex;
    }

    public String getSaslPassword() {
        return saslPassword;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public Integer getRamQuotaMB() {
        return ramQuotaMB;
    }

    public Integer getReplicaNumber() {
        return replicaNumber;
    }

    public Integer getThreadsNumber() {
        return threadsNumber;
    }

    public static class Builder {
        private String authType;
        private String bucketName;
        private String bucketType;
        private String conflictResolutionType;
        private String evictionPolicy;
        private String flushEnabled;
        private String parallelDBAndViewCompaction;
        private String replicaIndex;
        private String saslPassword;

        private Integer proxyPort;
        private Integer ramQuotaMB;
        private Integer replicaNumber;
        private Integer threadsNumber;

        public BucketInputs build() {
            return new BucketInputs(this);
        }

        public BucketInputs.Builder withAuthType(String input) {
            authType = AuthType.getValue(input);
            return this;
        }

        public BucketInputs.Builder withBucketName(String input) {
            bucketName = input;
            return this;
        }

        public BucketInputs.Builder withBucketType(String input) {
            bucketType = BucketType.getValue(input);
            return this;
        }

        public BucketInputs.Builder withConflictResolutionType(String input) {
            conflictResolutionType = ConflictResolutionType.getValue(input);
            return this;
        }

        public BucketInputs.Builder withEvictionPolicy(String input) {
            evictionPolicy = EvictionPolicy.getValue(input);
            return this;
        }

        public BucketInputs.Builder withFlushEnabled(String input) {
            flushEnabled = getEnabledString(input, false);
            return this;
        }

        public BucketInputs.Builder withParallelDBAndViewCompaction(String input) {
            parallelDBAndViewCompaction = valueOf(getEnforcedBooleanCondition(input, Boolean.FALSE));
            return this;
        }

        public BucketInputs.Builder withReplicaIndex(String input) {
            replicaIndex = getEnabledString(input, true);
            return this;
        }

        public BucketInputs.Builder withProxyPort(String input) {
            proxyPort = getValidPort(input);
            return this;
        }

        public BucketInputs.Builder withRamQuotaMB(String input) {
            ramQuotaMB = getValidIntValue(input, MINIMUM_RAM_QUOTA_AMOUNT, null, MINIMUM_RAM_QUOTA_AMOUNT);
            return this;
        }

        public BucketInputs.Builder withReplicaNumber(String input) {
            replicaNumber = getValidIntValue(input, INIT_INDEX, MAXIMUM_REPLICA_NUMBER, DEFAULT_REPLICA_NUMBER);
            return this;
        }

        public BucketInputs.Builder withSaslPassword(String input) {
            saslPassword = input;
            return this;
        }

        public BucketInputs.Builder withThreadsNumber(String input) {
            threadsNumber = getValidIntValue(input, MINIMUM_THREADS_NUMBER, MAXIMUM_THREADS_NUMBER, DEFAULT_THREADS_NUMBER);
            return this;
        }
    }
}