package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.utils.InputsUtil;

/**
 * Created by Mihai Tusa.
 * 9/14/2016.
 */
public class ElasticIpInputs {
    private String publicIp;
    private String privateIpAddress;
    private String privateIpAddressesString;

    private boolean allowReassociation;

    private ElasticIpInputs(ElasticIpInputsBuilder builder) {
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

    public static class ElasticIpInputsBuilder {
        private String publicIp;
        private String privateIpAddress;
        private String privateIpAddressesString;

        private boolean allowReassociation;

        public ElasticIpInputs build() {
            return new ElasticIpInputs(this);
        }

        public ElasticIpInputs.ElasticIpInputsBuilder withPublicIp(String inputValue) {
            publicIp = InputsUtil.getValidIPv4Address(inputValue);
            return this;
        }

        public ElasticIpInputs.ElasticIpInputsBuilder withPrivateIpAddress(String inputValue) {
            privateIpAddress = InputsUtil.getValidIPv4Address(inputValue);
            return this;
        }

        public ElasticIpInputs.ElasticIpInputsBuilder withPrivateIpAddressesString(String inputValue) {
            privateIpAddressesString = InputsUtil.getDefaultStringInput(inputValue, Constants.Miscellaneous.EMPTY);
            return this;
        }

        public ElasticIpInputs.ElasticIpInputsBuilder withAllowReassociation(String inputValue) {
            allowReassociation = Boolean.parseBoolean(inputValue);
            return this;
        }
    }
}