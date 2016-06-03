package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.Architecture;
import io.cloudslang.content.jclouds.entities.BlockDeviceMappingStatus;
import io.cloudslang.content.jclouds.entities.Hypervisor;
import io.cloudslang.content.jclouds.entities.InstanceState;
import io.cloudslang.content.jclouds.utils.InputsUtil;

/**
 * Created by Mihai Tusa.
 * 6/1/2016.
 */
public class InstanceInputs {
    private static final long DEFAULT_TIMING = 20000;

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
    private String hypervisor;
    private String iamArn;
    private String instanceLifecycle;
    private String instanceStateCode;
    private String instanceStateName;
    private String instanceGroupId;

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
        this.hypervisor = builder.hypervisor;
        this.iamArn = builder.iamArn;
        this.instanceLifecycle = builder.instanceLifecycle;
        this.instanceStateCode = builder.instanceStateCode;
        this.instanceStateName = builder.instanceStateName;
        this.instanceGroupId = builder.instanceGroupId;

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

    public String getHypervisor() {
        return hypervisor;
    }

    public String getIamArn() {
        return iamArn;
    }

    public String getInstanceLifecycle() {
        return instanceLifecycle;
    }

    public String getInstanceStateCode() {
        return instanceStateCode;
    }

    public String getInstanceStateName() {
        return instanceStateName;
    }

    public String getInstanceGroupId() {
        return instanceGroupId;
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
        private String hypervisor;
        private String iamArn;
        private String instanceLifecycle;
        private String instanceStateCode;
        private String instanceStateName;
        private String instanceGroupId;

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
            architecture = Architecture.getValue(inputValue);
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
            deleteOnTermination = InputsUtil.getRelevantBooleanString(inputValue);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withStatus(String inputValue) {
            status = BlockDeviceMappingStatus.getValue(inputValue);
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

        public InstanceInputs.InstanceInputsBuilder withHypervisor(String inputValue) {
            hypervisor = Hypervisor.getValue(inputValue);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withGroupName(String inputValue) {
            groupName = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withIamArn(String inputValue) {
            iamArn = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withInstanceLifecycle(String inputValue) {
            instanceLifecycle = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withInstanceStateCode(String inputValue) {
            instanceStateCode = String.valueOf(InputsUtil.getValidInstanceStateCode(inputValue));
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withInstanceStateName(String inputValue) {
            instanceStateName = InstanceState.getValue(inputValue);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withInstanceGroupId(String inputValue) {
            instanceGroupId = InstanceState.getValue(inputValue);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withMinCount(String inputValue) {
            minCount = InputsUtil.getValidInstancesCount(inputValue);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withMaxCount(String inputValue) {
            maxCount = InputsUtil.getValidInstancesCount(inputValue);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withCheckStateTimeout(String inputValue) {
            checkStateTimeout = InputsUtil.getValidLong(inputValue, DEFAULT_TIMING);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withPolingInterval(String inputValue) {
            polingInterval = InputsUtil.getValidLong(inputValue, DEFAULT_TIMING);
            return this;
        }
    }
}
