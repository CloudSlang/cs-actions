package io.cloudslang.content.amazon.entities.inputs;

/**
 * Created by TusaM
 * 11/10/2016.
 */
public class LoadBalancerInputs {
    private final String listenerInstancePortsString;
    private final String listenerInstanceProtocolsString;

    public String getListenerInstanceProtocolsString() {
        return listenerInstanceProtocolsString;
    }

    public String getListenerInstancePortsString() {
        return listenerInstancePortsString;
    }

    private LoadBalancerInputs(LoadBalancerInputs.Builder builder) {
        this.listenerInstancePortsString = builder.listenerInstancePortsString;
        this.listenerInstanceProtocolsString = builder.listenerInstanceProtocolsString;
    }

    public static class Builder {
        private String listenerInstancePortsString;
        private String listenerInstanceProtocolsString;

        public LoadBalancerInputs build() {
            return new LoadBalancerInputs(this);
        }

        public LoadBalancerInputs.Builder withListenerInstancePortsString(String inputValue) {
            listenerInstancePortsString = inputValue;
            return this;
        }

        public LoadBalancerInputs.Builder withListenerInstanceProtocolsString(String inputValue) {
            listenerInstanceProtocolsString = inputValue;
            return this;
        }
    }
}