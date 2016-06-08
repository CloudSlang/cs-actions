package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.NetworkInterfaceStatus;

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
    }
}