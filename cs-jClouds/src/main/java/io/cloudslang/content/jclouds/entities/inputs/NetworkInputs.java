package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.aws.NetworkInterfaceAttachmentStatus;
import io.cloudslang.content.jclouds.entities.aws.NetworkInterfaceStatus;

/**
 * Created by Mihai Tusa.
 * 6/7/2016.
 */
public class NetworkInputs {
    private String networkInterfaceDescription;
    private String networkInterfaceSubnetId;
    private String networkInterfaceVpcId;
    private String networkInterfaceId;
    private String networkInterfaceOwnerId;
    private String networkInterfaceAvailabilityZone;
    private String networkInterfaceRequesterId;
    private String networkInterfaceRequesterManaged;
    private String networkInterfaceStatus;
    private String networkInterfaceMacAddress;
    private String networkInterfacePrivateDnsName;
    private String networkInterfaceSourceDestinationCheck;
    private String networkInterfaceGroupId;
    private String networkInterfaceGroupName;
    private String networkInterfaceAttachmentId;
    private String networkInterfaceInstanceId;
    private String networkInterfaceInstanceOwnerId;
    private String networkInterfacePrivateIpAddress;
    private String networkInterfaceDeviceIndex;
    private String networkInterfaceAttachmentStatus;
    private String networkInterfaceAttachTime;
    private String networkInterfaceDeleteOnTermination;
    private String networkInterfaceAddressesPrimary;
    private String networkInterfacePublicIp;
    private String networkInterfaceIpOwnerId;

    private NetworkInputs(NetworkInputs.NetworkInputsBuilder builder) {
        this.networkInterfaceDescription = builder.networkInterfaceDescription;
        this.networkInterfaceSubnetId = builder.networkInterfaceSubnetId;
        this.networkInterfaceVpcId = builder.networkInterfaceVpcId;
        this.networkInterfaceId = builder.networkInterfaceId;
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

    public String getNetworkInterfaceDeleteOnTermination() {
        return networkInterfaceDeleteOnTermination;
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

    public static class NetworkInputsBuilder {
        private String networkInterfaceDescription;
        private String networkInterfaceSubnetId;
        private String networkInterfaceVpcId;
        private String networkInterfaceId;
        private String networkInterfaceOwnerId;
        private String networkInterfaceAvailabilityZone;
        private String networkInterfaceRequesterId;
        private String networkInterfaceRequesterManaged;
        private String networkInterfaceStatus;
        private String networkInterfaceMacAddress;
        private String networkInterfacePrivateDnsName;
        private String networkInterfaceSourceDestinationCheck;
        private String networkInterfaceGroupId;
        private String networkInterfaceGroupName;
        private String networkInterfaceAttachmentId;
        private String networkInterfaceInstanceId;
        private String networkInterfaceInstanceOwnerId;
        private String networkInterfacePrivateIpAddress;
        private String networkInterfaceDeviceIndex;
        private String networkInterfaceAttachmentStatus;
        private String networkInterfaceAttachTime;
        private String networkInterfaceDeleteOnTermination;
        private String networkInterfaceAddressesPrimary;
        private String networkInterfacePublicIp;
        private String networkInterfaceIpOwnerId;

        public NetworkInputs build() {
            return new NetworkInputs(this);
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceDescription(String inputValue) {
            networkInterfaceDescription = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceSubnetId(String inputValue) {
            networkInterfaceSubnetId = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceVpcId(String inputValue) {
            networkInterfaceVpcId = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceId(String inputValue) {
            networkInterfaceId = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceOwnerId(String inputValue) {
            networkInterfaceOwnerId = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceAvailabilityZone(String inputValue) {
            networkInterfaceAvailabilityZone = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceRequesterId(String inputValue) {
            networkInterfaceRequesterId = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceRequesterManaged(String inputValue) {
            networkInterfaceRequesterManaged = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceStatus(String inputValue) {
            networkInterfaceStatus = NetworkInterfaceStatus.getValue(inputValue);
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceMacAddress(String inputValue) {
            networkInterfaceMacAddress = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfacePrivateDnsName(String inputValue) {
            networkInterfacePrivateDnsName = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceSourceDestinationCheck(String inputValue) {
            networkInterfaceSourceDestinationCheck = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceGroupId(String inputValue) {
            networkInterfaceGroupId = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceGroupName(String inputValue) {
            networkInterfaceGroupName = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceAttachmentId(String inputValue) {
            networkInterfaceAttachmentId = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceInstanceId(String inputValue) {
            networkInterfaceInstanceId = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceInstanceOwnerId(String inputValue) {
            networkInterfaceInstanceOwnerId = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfacePrivateIpAddress(String inputValue) {
            networkInterfacePrivateIpAddress = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceDeviceIndex(String inputValue) {
            networkInterfaceDeviceIndex = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceAttachmentStatus(String inputValue) throws Exception {
            networkInterfaceAttachmentStatus = NetworkInterfaceAttachmentStatus.getValue(inputValue);
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceAttachTime(String inputValue) throws Exception {
            networkInterfaceAttachTime = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceDeleteOnTermination(String inputValue) throws Exception {
            networkInterfaceDeleteOnTermination = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceAddressesPrimary(String inputValue) throws Exception {
            networkInterfaceAddressesPrimary = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfacePublicIp(String inputValue) throws Exception {
            networkInterfacePublicIp = inputValue;
            return this;
        }

        public NetworkInputs.NetworkInputsBuilder withNetworkInterfaceIpOwnerId(String inputValue) throws Exception {
            networkInterfaceIpOwnerId = inputValue;
            return this;
        }
    }
}
