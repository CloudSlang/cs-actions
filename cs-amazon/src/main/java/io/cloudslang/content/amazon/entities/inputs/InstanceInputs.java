/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.entities.aws.Attribute;
import io.cloudslang.content.amazon.entities.aws.InstanceInitiatedShutdownBehavior;
import io.cloudslang.content.amazon.entities.aws.Tenancy;
import io.cloudslang.content.amazon.utils.InputsUtil;

/**
 * Created by Mihai Tusa.
 * 6/1/2016.
 */
public class InstanceInputs {
    private final String affinity;
    private final String attribute;
    private final String attributeValue;
    private final String clientToken;
    private final String placementGroupName;
    private final String sourceDestinationCheck;
    private final String tenancy;
    private final String instanceInitiatedShutdownBehavior;
    private final String userData;
    private final String kernel;
    private final String ramdisk;
    private final String sriovNetSupport;
    private final String filterNamesString;
    private final String filterValuesString;
    private final String instanceIdsString;
    private final String maxResults;
    private final String nextToken;

    private final int maxCount;
    private final int minCount;

    private final boolean disableApiTermination;
    private final boolean enaSupport;
    private final boolean forceStop;
    private final boolean monitoring;

    private InstanceInputs(Builder builder) {
        this.affinity = builder.affinity;
        this.attribute = builder.attribute;
        this.attributeValue = builder.attributeValue;
        this.clientToken = builder.clientToken;
        this.placementGroupName = builder.placementGroupName;
        this.sourceDestinationCheck = builder.sourceDestinationCheck;
        this.tenancy = builder.tenancy;
        this.instanceInitiatedShutdownBehavior = builder.instanceInitiatedShutdownBehavior;
        this.userData = builder.userData;
        this.kernel = builder.kernel;
        this.ramdisk = builder.ramdisk;
        this.sriovNetSupport = builder.sriovNetSupport;
        this.instanceIdsString = builder.instanceIdsString;
        this.maxResults = builder.maxResults;
        this.filterNamesString = builder.filterNamesString;
        this.filterValuesString = builder.filterValuesString;
        this.nextToken = builder.nextToken;

        this.minCount = builder.minCount;
        this.maxCount = builder.maxCount;

        this.disableApiTermination = builder.disableApiTermination;
        this.enaSupport = builder.enaSupport;
        this.monitoring = builder.monitoring;
        this.forceStop = builder.forceStop;
    }

    public String getAffinity() {
        return affinity;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public String getClientToken() {
        return clientToken;
    }

    public String getPlacementGroupName() {
        return placementGroupName;
    }

    public String getSourceDestinationCheck() {
        return sourceDestinationCheck;
    }

    public String getTenancy() {
        return tenancy;
    }

    public String getInstanceInitiatedShutdownBehavior() {
        return instanceInitiatedShutdownBehavior;
    }

    public String getUserData() {
        return userData;
    }

    public String getKernel() {
        return kernel;
    }

    public String getRamdisk() {
        return ramdisk;
    }

    public String getSriovNetSupport() {
        return sriovNetSupport;
    }

    public String getFilterNamesString() {
        return filterNamesString;
    }

    public String getFilterValuesString() {
        return filterValuesString;
    }

    public String getInstanceIdsString() {
        return instanceIdsString;
    }

    public String getMaxResults() {
        return maxResults;
    }

    public String getNextToken() {
        return nextToken;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getMinCount() {
        return minCount;
    }

    public boolean isDisableApiTermination() {
        return disableApiTermination;
    }

    public boolean isEnaSupport() {
        return enaSupport;
    }

    public boolean isMonitoring() {
        return monitoring;
    }

    public boolean isForceStop() {
        return forceStop;
    }

    public static class Builder {
        private String affinity;
        private String attribute;
        private String attributeValue;
        private String clientToken;
        private String placementGroupName;
        private String sourceDestinationCheck;
        private String tenancy;
        private String instanceInitiatedShutdownBehavior;
        private String userData;
        private String kernel;
        private String ramdisk;
        private String sriovNetSupport;
        private String filterNamesString;
        private String filterValuesString;
        private String instanceIdsString;
        private String maxResults;
        private String nextToken;

        private int maxCount;
        private int minCount;

        private boolean disableApiTermination;
        private boolean enaSupport;
        private boolean monitoring;
        private boolean forceStop;

        public InstanceInputs build() {
            return new InstanceInputs(this);
        }

        public Builder withAffinity(String inputValue) {
            affinity = inputValue;
            return this;
        }

        public Builder withAttribute(String inputValue) {
            attribute = Attribute.getAttribute(inputValue);
            return this;
        }

        public Builder withAttributeValue(String inputValue) {
            attributeValue = inputValue;
            return this;
        }

        public Builder withClientToken(String inputValue) {
            clientToken = inputValue;
            return this;
        }

        public Builder withPlacementGroupName(String inputValue) {
            placementGroupName = inputValue;
            return this;
        }

        public Builder withSourceDestinationCheck(String inputValue) {
            sourceDestinationCheck = InputsUtil.getRelevantBooleanString(inputValue);
            return this;
        }

        public Builder withTenancy(String inputValue) {
            tenancy = Tenancy.getValue(inputValue);
            return this;
        }

        public Builder withInstanceInitiatedShutdownBehavior(String inputValue) {
            instanceInitiatedShutdownBehavior = InstanceInitiatedShutdownBehavior.getValue(inputValue);
            return this;
        }

        public Builder withUserData(String inputValue) {
            userData = inputValue;
            return this;
        }

        public Builder withKernel(String inputValue) {
            kernel = inputValue;
            return this;
        }

        public Builder withRamdisk(String inputValue) {
            ramdisk = inputValue;
            return this;
        }

        public Builder withSriovNetSupport(String inputValue) {
            sriovNetSupport = inputValue;
            return this;
        }

        public Builder withFilterNamesString(String inputValue) {
            filterNamesString = inputValue;
            return this;
        }

        public Builder withFilterValuesString(String inputValue) {
            filterValuesString = inputValue;
            return this;
        }

        public Builder withInstanceIdsString(String inputValue) {
            instanceIdsString = inputValue;
            return this;
        }

        public Builder withMaxResults(String inputValue) {
            maxResults = InputsUtil.getMaxResultsCount(inputValue);
            return this;
        }

        public Builder withNextToken(String inputValue) {
            nextToken = inputValue;
            return this;
        }

        public Builder withMaxCount(String inputValue) {
            maxCount = InputsUtil.getValidInstancesCount(inputValue);
            return this;
        }

        public Builder withMinCount(String inputValue) {
            minCount = InputsUtil.getValidInstancesCount(inputValue);
            return this;
        }

        public Builder withDisableApiTermination(String inputValue) {
            disableApiTermination = InputsUtil.getEnforcedBooleanCondition(inputValue, false);
            return this;
        }

        public Builder withEnaSupport(String inputValue) {
            enaSupport = InputsUtil.getEnforcedBooleanCondition(inputValue, false);
            return this;
        }

        public Builder withMonitoring(String inputValue) {
            monitoring = InputsUtil.getEnforcedBooleanCondition(inputValue, false);
            return this;
        }

        public Builder withForceStop(String inputValue) {
            forceStop = InputsUtil.getEnforcedBooleanCondition(inputValue, false);
            return this;
        }
    }
}
