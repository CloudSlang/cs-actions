package io.cloudslang.content.jclouds.entities.aws;

/**
 * Created by Mihai Tusa.
 * 8/12/2016.
 */
public enum AWSApiAction {
    ATTACH_NETWORK_INTERFACE("AttachNetworkInterface"),
    DETACH_NETWORK_INTERFACE("DetachNetworkInterface");

    private String value;

    AWSApiAction(String input) {
        this.value = input;
    }

    public String getValue() {
        return value;
    }
}