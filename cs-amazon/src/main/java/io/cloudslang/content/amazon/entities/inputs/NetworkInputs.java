/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.utils.InputsUtil;

/**
 * Created by Mihai Tusa.
 * 6/7/2016.
 */
public class NetworkInputs {
    private static final int MINIMUM_PRIVATE_SECONDARY_IP_ADDRESSES_COUNT = 2;

    private final String cidrBlock;
    private final String deviceIndex;
    private final String networkInterfaceDescription;
    private final String networkInterfaceDeviceIndex;
    private final String networkInterfaceId;
    private final String networkInterfacePrivateIpAddress;
    private final String secondaryPrivateIpAddressCount;
    private final String networkInterfacesAssociatePublicIpAddressesString;
    private final String networkInterfaceDeleteOnTermination;
    private final String subnetIdsString;

    private final boolean forceDetach;

    private NetworkInputs(Builder builder) {
        this.cidrBlock = builder.cidrBlock;
        this.networkInterfaceDescription = builder.networkInterfaceDescription;
        this.networkInterfacePrivateIpAddress = builder.networkInterfacePrivateIpAddress;
        this.networkInterfaceDeviceIndex = builder.networkInterfaceDeviceIndex;
        this.networkInterfaceDeleteOnTermination = builder.networkInterfaceDeleteOnTermination;
        this.networkInterfaceId = builder.networkInterfaceId;
        this.deviceIndex = builder.deviceIndex;
        this.secondaryPrivateIpAddressCount = builder.secondaryPrivateIpAddressCount;
        this.networkInterfacesAssociatePublicIpAddressesString = builder.networkInterfacesAssociatePublicIpAddressesString;
        this.subnetIdsString = builder.subnetIdsString;

        this.forceDetach = builder.forceDetach;
    }

    public String getCidrBlock() {
        return cidrBlock;
    }

    public String getNetworkInterfaceDescription() {
        return networkInterfaceDescription;
    }

    public String getNetworkInterfaceId() {
        return networkInterfaceId;
    }

    public String getNetworkInterfacePrivateIpAddress() {
        return networkInterfacePrivateIpAddress;
    }

    public String getNetworkInterfaceDeviceIndex() {
        return networkInterfaceDeviceIndex;
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

    public String getSubnetIdsString() {
        return subnetIdsString;
    }

    public boolean isForceDetach() {
        return forceDetach;
    }

    public static class Builder {
        private String cidrBlock;
        private String deviceIndex;
        private String networkInterfacesAssociatePublicIpAddressesString;
        private String networkInterfaceDescription;
        private String networkInterfaceDeviceIndex;
        private String networkInterfaceId;
        private String secondaryPrivateIpAddressCount;
        private String networkInterfacePrivateIpAddress;
        private String networkInterfaceDeleteOnTermination;
        private String subnetIdsString;

        private boolean forceDetach;

        public NetworkInputs build() {
            return new NetworkInputs(this);
        }

        public Builder withCidrBlock(String inputValue) {
            cidrBlock = InputsUtil.getValidCidrNotation(inputValue);
            return this;
        }

        public Builder withNetworkInterfaceDescription(String inputValue) {
            networkInterfaceDescription = inputValue;
            return this;
        }

        public Builder withDeviceIndex(String inputValue) {
            deviceIndex = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceId(String inputValue) {
            networkInterfaceId = inputValue;
            return this;
        }

        public Builder withNetworkInterfacePrivateIpAddress(String inputValue) {
            networkInterfacePrivateIpAddress = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceDeviceIndex(String inputValue) {
            networkInterfaceDeviceIndex = inputValue;
            return this;
        }

        public Builder withNetworkInterfaceDeleteOnTermination(String inputValue) {
            networkInterfaceDeleteOnTermination = inputValue;
            return this;
        }

        public Builder withSecondaryPrivateIpAddressCount(String inputValue) {
            secondaryPrivateIpAddressCount = inputValue;
            return this;
        }

        public Builder withNetworkInterfacesAssociatePublicIpAddressesString(String inputValue) {
            networkInterfacesAssociatePublicIpAddressesString = inputValue;
            return this;
        }

        public Builder withSubnetIdsString(String inputValue) {
            subnetIdsString = inputValue;
            return this;
        }

        public Builder withForceDetach(String inputValue) {
            forceDetach = InputsUtil.getEnforcedBooleanCondition(inputValue, Boolean.FALSE);
            return this;
        }
    }
}