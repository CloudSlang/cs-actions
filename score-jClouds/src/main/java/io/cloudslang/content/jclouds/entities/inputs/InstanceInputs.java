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
    private String affinity;
    private String architecture;
    private String attachTime;
    private String deleteOnTermination;
    private String deviceName;
    private String status;
    private String clientToken;
    private String dnsName;
    private String groupName;

    private int minCount;
    private int maxCount;
    private long checkStateTimeout;
    private long polingInterval;

    public InstanceInputs(InstanceInputs.InstanceInputsBuilder builder) {
        this.customInputs = builder.customInputs;

        this.instanceType = builder.instanceType;
        this.availabilityZone = builder.availabilityZone;
        this.affinity = builder.affinity;
        this.architecture = builder.architecture;
        this.attachTime = builder.attachTime;
        this.deviceName = builder.deviceName;
        this.deleteOnTermination = builder.deleteOnTermination;
        this.status = builder.status;
        this.clientToken = builder.clientToken;
        this.dnsName = builder.dnsName;
        this.groupName = builder.groupName;

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

    public String getAffinity() {
        return affinity;
    }

    public String getArchitecture() {
        return architecture;
    }

    public String getAttachTime() {
        return attachTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeleteOnTermination() {
        return deleteOnTermination;
    }

    public String getStatus() {
        return status;
    }

    public String getClientToken() {
        return clientToken;
    }

    public String getDnsName() {
        return dnsName;
    }

    public String getGroupName() {
        return groupName;
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
        private String affinity;
        private String architecture;
        private String attachTime;
        private String deviceName;
        private String deleteOnTermination;
        private String status;
        private String clientToken;
        private String dnsName;
        private String groupName;

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

        public InstanceInputs.InstanceInputsBuilder withAffinity(String inputValue) {
            affinity = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withArchitecture(String inputValue) {
            architecture = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withAttachTime(String inputValue) {
            attachTime = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withDeviceName(String inputValue) {
            deviceName = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withDeleteOnTermination(String inputValue) {
            deleteOnTermination = InputsUtil.getValidBooleanString(inputValue);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withStatus(String inputValue) {
            status = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withClientToken(String inputValue) {
            clientToken = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withDnsName(String inputValue) {
            dnsName = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withGroupName(String inputValue) {
            groupName = inputValue;
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
