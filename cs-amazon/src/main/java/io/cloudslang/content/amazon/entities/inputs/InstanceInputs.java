package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.entities.aws.Attribute;
import io.cloudslang.content.amazon.entities.aws.InstanceInitiatedShutdownBehavior;
import io.cloudslang.content.amazon.entities.aws.Tenancy;
import io.cloudslang.content.amazon.utils.InputsUtil;

import static java.lang.String.valueOf;

/**
 * Created by Mihai Tusa.
 * 6/1/2016.
 */
public class InstanceInputs {
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
    private String instanceIdsString;
    private String maxResults;

    private int maxCount;
    private int minCount;

    private boolean disableApiTermination;
    private boolean enaSupport;
    private boolean forceStop;
    private boolean monitoring;

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
        ;
        this.maxResults = builder.maxResults;
        ;

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

    public String getInstanceIdsString() {
        return instanceIdsString;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public String getMaxResults() {
        return maxResults;
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
        private String instanceIdsString;
        private String maxResults;

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

        public Builder withInstanceIdsString(String inputValue) {
            instanceIdsString = inputValue;
            return this;
        }

        public Builder withMaxCount(String inputValue) {
            maxCount = InputsUtil.getValidInstancesCount(inputValue);
            return this;
        }

        public Builder withMaxResults(String inputValue) {
            maxResults = InputsUtil.getMaxResultsCount(inputValue);
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