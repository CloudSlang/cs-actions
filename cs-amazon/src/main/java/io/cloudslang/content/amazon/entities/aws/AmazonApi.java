/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.amazon.entities.aws;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 8/10/2016.
 */
public enum AmazonApi {
    EC2("ec2"),
    ELASTIC_LOAD_BALANCING("elasticloadbalancing"),
    S3("s3");

    private final String value;

    AmazonApi(String value) {
        this.value = value;
    }

    public static String getApiValue(String input) throws RuntimeException {
        if (isBlank(input)) {
            return EC2.getValue();
        }

        for (AmazonApi api : AmazonApi.values()) {
            if (api.getValue().equalsIgnoreCase(input)) {
                return api.getValue();
            }
        }

        throw new RuntimeException("Invalid Amazon API service value: [" + input + "]. Valid values: ec2, elasticloadbalancing, s3.");
    }

    private String getValue() {
        return value;
    }
}
