package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.*;
import io.cloudslang.content.jclouds.utils.InputsUtil;

/**
 * Created by Mihai Tusa.
 * 6/1/2016.
 */
public class InstanceInputs {
    private static final long DEFAULT_CHECK_STATE_TIMEOUT = 20000;
    private static final long DEFAULT_POLING_INTERVAL = 20000;

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
    private String instanceGroupName;
    private String ipAddress;
    private String keyName;
    private String launchIndex;
    private String launchTime;
    private String monitoringState;
    private String placementGroupName;
    private String platform;
    private String privateDnsName;
    private String privateIpAddress;
    private String productCode;
    private String productCodeType;
    private String reason;
    private String requesterId;
    private String rootDeviceName;
    private String rootDeviceType;
    private String sourceDestinationCheck;
    private String spotInstanceRequestId;
    private String stateReasonCode;
    private String stateReasonMessage;
    private String tenancy;
    private String virtualizationType;

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
        this.instanceGroupName = builder.instanceGroupName;
        this.ipAddress = builder.ipAddress;
        this.keyName = builder.keyName;
        this.launchIndex = builder.launchIndex;
        this.launchTime = builder.launchTime;
        this.monitoringState = builder.monitoringState;
        this.placementGroupName = builder.placementGroupName;
        this.platform = builder.platform;
        this.privateDnsName = builder.privateDnsName;
        this.privateIpAddress = builder.privateIpAddress;
        this.productCode = builder.productCode;
        this.productCodeType = builder.productCodeType;
        this.reason = builder.reason;
        this.requesterId = builder.requesterId;
        this.rootDeviceName = builder.rootDeviceName;
        this.rootDeviceType = builder.rootDeviceType;
        this.sourceDestinationCheck = builder.sourceDestinationCheck;
        this.spotInstanceRequestId = builder.spotInstanceRequestId;
        this.stateReasonCode = builder.stateReasonCode;
        this.stateReasonMessage = builder.stateReasonMessage;
        this.tenancy = builder.tenancy;
        this.virtualizationType = builder.virtualizationType;

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

    public String getInstanceGroupName() {
        return instanceGroupName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getKeyName() {
        return keyName;
    }

    public String getLaunchIndex() {
        return launchIndex;
    }

    public String getLaunchTime() {
        return launchTime;
    }

    public String getMonitoringState() {
        return monitoringState;
    }

    public String getPlacementGroupName() {
        return placementGroupName;
    }

    public String getPlatform() {
        return platform;
    }

    public String getPrivateDnsName() {
        return privateDnsName;
    }

    public String getPrivateIpAddress() {
        return privateIpAddress;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductCodeType() {
        return productCodeType;
    }

    public String getReason() {
        return reason;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public String getRootDeviceName() {
        return rootDeviceName;
    }

    public String getRootDeviceType() {
        return rootDeviceType;
    }

    public String getSourceDestinationCheck() {
        return sourceDestinationCheck;
    }

    public String getSpotInstanceRequestId() {
        return spotInstanceRequestId;
    }

    public String getStateReasonCode() {
        return stateReasonCode;
    }

    public String getStateReasonMessage() {
        return stateReasonMessage;
    }

    public String getTenancy() {
        return tenancy;
    }

    public String getVirtualizationType() {
        return virtualizationType;
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
        private String instanceGroupName;
        private String ipAddress;
        private String keyName;
        private String launchIndex;
        private String launchTime;
        private String monitoringState;
        private String placementGroupName;
        private String platform;
        private String privateDnsName;
        private String privateIpAddress;
        private String productCode;
        private String productCodeType;
        private String reason;
        private String requesterId;
        private String rootDeviceName;
        private String rootDeviceType;
        private String sourceDestinationCheck;
        private String spotInstanceRequestId;
        private String stateReasonCode;
        private String stateReasonMessage;
        private String tenancy;
        private String virtualizationType;

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
            instanceGroupId = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withInstanceGroupName(String inputValue) {
            instanceGroupName = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withIpAddress(String inputValue) {
            ipAddress = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withKeyName(String inputValue) {
            keyName = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withLaunchIndex(String inputValue) {
            launchIndex = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withLaunchTime(String inputValue) {
            launchTime = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withMonitoringState(String inputValue) throws Exception {
            monitoringState = MonitoringState.getValue(inputValue);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withPlacementGroupName(String inputValue) throws Exception {
            placementGroupName = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withPlatform(String inputValue) throws Exception {
            platform = Platform.getValue(inputValue);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withPrivateDnsName(String inputValue) throws Exception {
            privateDnsName = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withPrivateIpAddress(String inputValue) throws Exception {
            privateIpAddress = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withProductCode(String inputValue) throws Exception {
            productCode = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withProductCodeType(String inputValue) throws Exception {
            productCodeType = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withReason(String inputValue) throws Exception {
            reason = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withRequesterId(String inputValue) throws Exception {
            requesterId = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withRootDeviceName(String inputValue) throws Exception {
            rootDeviceName = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withRootDeviceType(String inputValue) throws Exception {
            rootDeviceType = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withSourceDestinationCheck(String inputValue) throws Exception {
            sourceDestinationCheck = InputsUtil.getRelevantBooleanString(inputValue);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withSpotInstanceRequestId(String inputValue) throws Exception {
            spotInstanceRequestId = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withStateReasonCode(String inputValue) throws Exception {
            stateReasonCode = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withStateReasonMessage(String inputValue) throws Exception {
            stateReasonMessage = inputValue;
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withTenancy(String inputValue) throws Exception {
            tenancy = Tenancy.getValue(inputValue);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withVirtualizationType(String inputValue) throws Exception {
            virtualizationType = VirtualizationType.getValue(inputValue);
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
            checkStateTimeout = InputsUtil.getValidLong(inputValue, DEFAULT_CHECK_STATE_TIMEOUT);
            return this;
        }

        public InstanceInputs.InstanceInputsBuilder withPolingInterval(String inputValue) {
            polingInterval = InputsUtil.getValidLong(inputValue, DEFAULT_POLING_INTERVAL);
            return this;
        }
    }
}