package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.entities.aws.Scheme;

/**
 * Created by TusaM
 * 11/10/2016.
 */
public class LoadBalancerInputs {
    private final String loadBalancerName;
    private final String scheme;

    public String getLoadBalancerName() {
        return loadBalancerName;
    }

    public String getScheme() {
        return scheme;
    }

    private LoadBalancerInputs(LoadBalancerInputs.Builder builder) {
        this.loadBalancerName = builder.loadBalancerName;
        this.scheme = builder.scheme;
    }

    public static class Builder {
        private String loadBalancerName;
        private String scheme;

        public LoadBalancerInputs build() {
            return new LoadBalancerInputs(this);
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