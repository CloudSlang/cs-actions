/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.entities.aws.Scheme;

/**
 * Created by TusaM
 * 11/10/2016.
 */
public class LoadBalancerInputs {
    private final String loadBalancerArn;
    private final String loadBalancerName;
    private final String scheme;

    public String getLoadBalancerArn() {
        return loadBalancerArn;
    }

    public String getLoadBalancerName() {
        return loadBalancerName;
    }

    public String getScheme() {
        return scheme;
    }

    private LoadBalancerInputs(LoadBalancerInputs.Builder builder) {
        this.loadBalancerArn = builder.loadBalancerArn;
        this.loadBalancerName = builder.loadBalancerName;
        this.scheme = builder.scheme;
    }

    public static class Builder {
        private String loadBalancerArn;
        private String loadBalancerName;
        private String scheme;

        public LoadBalancerInputs build() {
            return new LoadBalancerInputs(this);
        }

        public LoadBalancerInputs.Builder withLoadBalancerArn(String inputValue) {
            loadBalancerArn = inputValue;
            return this;
        }

        public LoadBalancerInputs.Builder withLoadBalancerName(String inputValue) {
            loadBalancerName = inputValue;
            return this;
        }

        public LoadBalancerInputs.Builder withScheme(String inputValue) {
            scheme = Scheme.getValue(inputValue);
            return this;
        }
    }
}
