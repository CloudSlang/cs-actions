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

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;

/**
 * Created by Mihai Tusa.
 * 9/14/2016.
 */
public class ElasticIpInputs {
    private final String publicIp;
    private final String privateIpAddress;
    private final String privateIpAddressesString;

    private final boolean allowReassociation;

    private ElasticIpInputs(Builder builder) {
        this.publicIp = builder.publicIp;
        this.privateIpAddress = builder.privateIpAddress;
        this.privateIpAddressesString = builder.privateIpAddressesString;

        this.allowReassociation = builder.allowReassociation;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public String getPrivateIpAddress() {
        return privateIpAddress;
    }

    public String getPrivateIpAddressesString() {
        return privateIpAddressesString;
    }

    public boolean isAllowReassociation() {
        return allowReassociation;
    }

    public static class Builder {
        private String publicIp;
        private String privateIpAddress;
        private String privateIpAddressesString;

        private boolean allowReassociation;

        public ElasticIpInputs build() {
            return new ElasticIpInputs(this);
        }

        public Builder withPublicIp(String inputValue) {
            publicIp = InputsUtil.getValidIPv4Address(inputValue);
            return this;
        }

        public Builder withPrivateIpAddress(String inputValue) {
            privateIpAddress = InputsUtil.getValidIPv4Address(inputValue);
            return this;
        }

        public Builder withPrivateIpAddressesString(String inputValue) {
            privateIpAddressesString = InputsUtil.getDefaultStringInput(inputValue, EMPTY);
            return this;
        }

        public Builder withAllowReassociation(String inputValue) {
            allowReassociation = Boolean.parseBoolean(inputValue);
            return this;
        }
    }
}