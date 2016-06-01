package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.utils.InputsUtil;

/**
 * Created by Mihai Tusa.
 * 6/1/2016.
 */
public class InstanceInputs {
    private CustomInputs customInputs;

    private String instanceType;
    private String availabilityZone;

    private int minCount;
    private int maxCount;
    private long checkStateTimeout;
    private long polingInterval;

    public InstanceInputs(InstanceInputs.InstanceInputsBuilder builder) {
        this.customInputs = builder.customInputs;

        this.instanceType = builder.instanceType;
        this.availabilityZone = builder.availabilityZone;

        this.minCount = builder.minCount;
        this.maxCount = builder.maxCount;
        this.checkStateTimeout = builder.checkStateTimeout;
        this.polingInterval = builder.polingInterval;
    }

    public CustomInputs getCustomInputs() {
        return customInputs;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public int getMinCount() {
        return minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public long getCheckStateTimeout() {
        return checkStateTimeout;
    }

    public long getPolingInterval() {
        return polingInterval;
    }

    public static class InstanceInputsBuilder {
        private CustomInputs customInputs;

        private String instanceType;
        private String availabilityZone;

        private int minCount;
        private int maxCount;
        private long checkStateTimeout;
        private long polingInterval;

        public InstanceInputs build() {
            return new InstanceInputs(this);
        }

        public InstanceInputs.InstanceInputsBuilder withCustomInputs(CustomInputs inputs) {
            customInputs = inputs;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withInstanceType(String inputValue) {
            instanceType = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withAvailabilityZone(String inputValue) {
            availabilityZone = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withMinCount(String inputValue) {
            minCount = InputsUtil.getMinInstancesCount(inputValue);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withMaxCount(String inputValue) {
            maxCount = InputsUtil.getMaxInstancesCount(inputValue);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withCheckStateTimeout(String inputValue) {
            checkStateTimeout = InputsUtil.getValidLong(inputValue, Constants.Miscellaneous.DEFAULT_TIMING);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withPolingInterval(String inputValue) {
            polingInterval = InputsUtil.getValidLong(inputValue, Constants.Miscellaneous.DEFAULT_TIMING);
            return this;
        }
    }
}
