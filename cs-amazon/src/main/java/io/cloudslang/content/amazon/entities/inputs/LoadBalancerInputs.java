package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.entities.aws.Scheme;
import io.cloudslang.content.amazon.utils.InputsUtil;

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
            loadBalancerArn = InputsUtil.getValidArn(inputValue);
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