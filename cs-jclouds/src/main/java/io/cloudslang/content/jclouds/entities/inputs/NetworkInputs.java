package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.aws.NetworkInterfaceAttachmentStatus;
import io.cloudslang.content.jclouds.entities.aws.NetworkInterfaceStatus;
import io.cloudslang.content.jclouds.utils.InputsUtil;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Values.START_INDEX;

/**
 * Created by Mihai Tusa.
 * 6/7/2016.
 */
public class NetworkInputs {
    private static final int MINIMUM_PRIVATE_SECONDARY_IP_ADDRESSES_COUNT = 2;

    private String deviceIndex;
    private String networkInterfaceAddressesPrimary;
    private String networkInterfaceAttachmentId;
    private String networkInterfaceAttachmentStatus;
    private String networkInterfaceAttachTime;
    private String networkInterfaceAvailabilityZone;
    private String networkInterfaceDescription;
    private String networkInterfaceDeviceIndex;
    private String networkInterfaceGroupId;
    private String networkInterfaceGroupName;
    private String networkInterfaceId;
    private String networkInterfaceInstanceId;
    private String networkInterfaceInstanceOwnerId;
    private String networkInterfaceIpOwnerId;
    private String networkInterfaceMacAddress;
    private String networkInterfaceOwnerId;
    private String networkInterfacePrivateDnsName;
    private String networkInterfacePrivateIpAddress;
    private String networkInterfacePublicIp;
    private String networkInterfaceRequesterId;
    private String networkInterfaceRequesterManaged;
    private String networkInterfaceSourceDestinationCheck;
    private String networkInterfaceStatus;
    private String networkInterfaceSubnetId;
    private String networkInterfaceVpcId;
    private String secondaryPrivateIpAddressCount;
    private String networkInterfacesAssociatePublicIpAddressesString;
    private String networkInterfaceDeleteOnTermination;

    private boolean forceDetach;

    private NetworkInputs(Builder builder) {
        this.networkInterfaceDescription = builder.networkInterfaceDescription;
        this.networkInterfaceSubnetId = builder.networkInterfaceSubnetId;
        this.networkInterfaceVpcId = builder.networkInterfaceVpcId;
        this.networkInterfaceOwnerId = builder.networkInterfaceOwnerId;
        this.networkInterfaceAvailabilityZone = builder.networkInterfaceAvailabilityZone;
        this.networkInterfaceRequesterId = builder.networkInterfaceRequesterId;
        this.networkInterfaceRequesterManaged = builder.networkInterfaceRequesterManaged;
        this.networkInterfaceStatus = builder.networkInterfaceStatus;
        this.networkInterfaceMacAddress = builder.networkInterfaceMacAddress;
        this.networkInterfacePrivateDnsName = builder.networkInterfacePrivateDnsName;
        this.networkInterfaceSourceDestinationCheck = builder.networkInterfaceSourceDestinationCheck;
        this.networkInterfaceGroupId = builder.networkInterfaceGroupId;
        this.networkInterfaceGroupName = builder.networkInterfaceGroupName;
        this.networkInterfaceAttachmentId = builder.networkInterfaceAttachmentId;
        this.networkInterfaceInstanceId = builder.networkInterfaceInstanceId;
        this.networkInterfaceInstanceOwnerId = builder.networkInterfaceInstanceOwnerId;
        this.networkInterfacePrivateIpAddress = builder.networkInterfacePrivateIpAddress;
        this.networkInterfaceDeviceIndex = builder.networkInterfaceDeviceIndex;
        this.networkInterfaceAttachmentStatus = builder.networkInterfaceAttachmentStatus;
        this.networkInterfaceAttachTime = builder.networkInterfaceAttachTime;
        this.networkInterfaceDeleteOnTermination = builder.networkInterfaceDeleteOnTermination;
        this.networkInterfaceAddressesPrimary = builder.networkInterfaceAddressesPrimary;
        this.networkInterfacePublicIp = builder.networkInterfacePublicIp;
        this.networkInterfaceIpOwnerId = builder.networkInterfaceIpOwnerId;
        this.networkInterfaceId = builder.networkInterfaceId;
        this.deviceIndex = builder.deviceIndex;
        this.secondaryPrivateIpAddressCount = builder.secondaryPrivateIpAddressCount;
        this.networkInterfacesAssociatePublicIpAddressesString = builder.networkInterfacesAssociatePublicIpAddressesString;

        this.forceDetach = builder.forceDetach;
    }

    public String getNetworkInterfaceDescription() {
        return networkInterfaceDescription;
    }

    public String getNetworkInterfaceSubnetId() {
        return networkInterfaceSubnetId;
    }

    public String getNetworkInterfaceVpcId() {
        return networkInterfaceVpcId;
    }

    public String getNetworkInterfaceId() {
        return networkInterfaceId;
    }

    public String getNetworkInterfaceOwnerId() {
        return networkInterfaceOwnerId;
    }

    public String getNetworkInterfaceAvailabilityZone() {
        return networkInterfaceAvailabilityZone;
    }

    public String getNetworkInterfaceRequesterId() {
        return networkInterfaceRequesterId;
    }

    public String getNetworkInterfaceRequesterManaged() {
        return networkInterfaceRequesterManaged;
    }

    public String getNetworkInterfaceStatus() {
        return networkInterfaceStatus;
    }

    public String getNetworkInterfaceMacAddress() {
        return networkInterfaceMacAddress;
    }

    public String getNetworkInterfacePrivateDnsName() {
        return networkInterfacePrivateDnsName;
    }

    public String getNetworkInterfaceSourceDestinationCheck() {
        return networkInterfaceSourceDestinationCheck;
    }

    public String getNetworkInterfaceGroupId() {
        return networkInterfaceGroupId;
    }

    public String getNetworkInterfaceGroupName() {
        return networkInterfaceGroupName;
    }

    public String getNetworkInterfaceAttachmentId() {
        return networkInterfaceAttachmentId;
    }

    public String getNetworkInterfaceInstanceId() {
        return networkInterfaceInstanceId;
    }

    public String getNetworkInterfaceInstanceOwnerId() {
        return networkInterfaceInstanceOwnerId;
    }

    public String getNetworkInterfacePrivateIpAddress() {
        return networkInterfacePrivateIpAddress;
    }

    public String getNetworkInterfaceDeviceIndex() {
        return networkInterfaceDeviceIndex;
    }

    public String getNetworkInterfaceAttachmentStatus() {
        return networkInterfaceAttachmentStatus;
    }

    public String getNetworkInterfaceAttachTime() {
        return networkInterfaceAttachTime;
    }

    public String getNetworkInterfaceAddressesPrimary() {
        return networkInterfaceAddressesPrimary;
    }

    public String getNetworkInterfacePublicIp() {
        return networkInterfacePublicIp;
    }

    public String getNetworkInterfaceIpOwnerId() {
        return networkInterfaceIpOwnerId;
    }

    public String getDeviceIndex() {
        return deviceIndex;
    }

    public String getSecondaryPrivateIpAddressCount() {
        return secondaryPrivateIpAddressCount;
    }

    public String getNetworkInterfacesAssociatePublicIpAddressesString() {
        return networkInterfacesAssociatePublicIpAddressesString;
    }

    public String getNetworkInterfaceDeleteOnTermination() {
        return networkInterfaceDeleteOnTermination;
    }

    public boolean isForceDetach() {
        return forceDetach;
    }

    public static class Builder {
        private String deviceIndex;
        private String networkInterfaceAddressesPrimary;
        private String networkInterfacesAssociatePublicIpAddressesString;
        private String networkInterfaceAttachmentId;
        private String networkInterfaceAttachmentStatus;
        private String networkInterfaceAttachTime;
        private String networkInterfaceAvailabilityZone;
        private String networkInterfaceDescription;
        private String networkInterfaceDeviceIndex;
        private String networkInterfaceGroupId;
        private String networkInterfaceGroupName;
        private String networkInterfaceId;
        private String networkInterfaceInstanceOwnerId;
        private String secondaryPrivateIpAddressCount;
        private String networkInterfaceIpOwnerId;
        private String networkInterfaceInstanceId;
        private String networkInterfaceOwnerId;
        private String networkInterfaceMacAddress;
        private String networkInterfacePrivateDnsName;
        private String networkInterfacePrivateIpAddress;
        private String networkInterfacePublicIp;
        private String networkInterfaceRequesterId;
        private String networkInterfaceRequesterManaged;
        private String networkInterfaceSourceDestinationCheck;
        private String networkInterfaceStatus;
        private String networkInterfaceSubnetId;
        private String networkInterfaceVpcId;
        private String networkInterfaceDeleteOnTermination;

        private boolean forceDetach;

        public NetworkInputs build() {
            return new NetworkInputs(this);
        }

        public Builder withNetworkInterfaceDescription(String inputValue) {
            networkInterfaceDescription = inputValue;
            return this;
        }

        public Builder withDeviceIndex(String inputValue) {
            deviceIndex = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceSubnetId(String inputValue) {
            networkInterfaceSubnetId = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceVpcId(String inputValue) {
            networkInterfaceVpcId = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceId(String inputValue) {
            networkInterfaceId = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceOwnerId(String inputValue) {
            networkInterfaceOwnerId = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceAvailabilityZone(String inputValue) {
            networkInterfaceAvailabilityZone = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceRequesterId(String inputValue) {
            networkInterfaceRequesterId = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceRequesterManaged(String inputValue) {
            networkInterfaceRequesterManaged = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceStatus(String inputValue) {
            networkInterfaceStatus = NetworkInterfaceStatus.getValue(inputValue);
            return this;
        }

        public Builder withNetworkInterfaceMacAddress(String inputValue) {
            networkInterfaceMacAddress = inputValue;
            return this;
        }

        public Builder withNetworkInterfacePrivateDnsName(String inputValue) {
            networkInterfacePrivateDnsName = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceSourceDestinationCheck(String inputValue) {
            networkInterfaceSourceDestinationCheck = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceGroupId(String inputValue) {
            networkInterfaceGroupId = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceGroupName(String inputValue) {
            networkInterfaceGroupName = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceAttachmentId(String inputValue) {
            networkInterfaceAttachmentId = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceInstanceId(String inputValue) {
            networkInterfaceInstanceId = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceInstanceOwnerId(String inputValue) {
            networkInterfaceInstanceOwnerId = inputValue;
            return this;
        }

        public Builder withNetworkInterfacePrivateIpAddress(String inputValue) {
            networkInterfacePrivateIpAddress = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceDeviceIndex(String inputValue) {
            networkInterfaceDeviceIndex = InputsUtil
                    .getValidPositiveIntegerAsStringValue(inputValue, START_INDEX);
            return this;
        }

        public Builder withNetworkInterfaceAttachmentStatus(String inputValue) throws Exception {
            networkInterfaceAttachmentStatus = NetworkInterfaceAttachmentStatus.getValue(inputValue);
            return this;
        }

        public Builder withNetworkInterfaceAttachTime(String inputValue) {
            networkInterfaceAttachTime = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceDeleteOnTermination(String inputValue) {
            networkInterfaceDeleteOnTermination = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceAddressesPrimary(String inputValue) {
            networkInterfaceAddressesPrimary = inputValue;
            return this;
        }

        public Builder withNetworkInterfacePublicIp(String inputValue) {
            networkInterfacePublicIp = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceIpOwnerId(String inputValue) {
            networkInterfaceIpOwnerId = inputValue;
            return this;
        }

        public Builder withSecondaryPrivateIpAddressCount(String inputValue) {
            secondaryPrivateIpAddressCount = InputsUtil
                    .getValidPositiveIntegerAsStringValue(inputValue, MINIMUM_PRIVATE_SECONDARY_IP_ADDRESSES_COUNT);
            return this;
        }

        public Builder withNetworkInterfacesAssociatePublicIpAddressesString(String inputValue) {
            networkInterfacesAssociatePublicIpAddressesString = inputValue;
            return this;
        }

        public Builder withForceDetach(String inputValue) {
            forceDetach = InputsUtil.getEnforcedBooleanCondition(inputValue, Boolean.FALSE);
            return this;
        }
    }
}