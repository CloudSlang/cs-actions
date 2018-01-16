package io.cloudslang.content.utilities.entities;

/**
 * Created by pinteae on 1/10/2018.
 */
public class LocalPingInputs {
    private String targetHost;
    private String packetSize;
    private String packetCount;
    private String timeout;
    private String ipVersion;

    public LocalPingInputs(LocalPingInputsBuilder localPingInputsBuilder){
        this.targetHost = localPingInputsBuilder.targetHost;
        this.packetSize = localPingInputsBuilder.packetSize;
        this.packetCount = localPingInputsBuilder.packetCount;
        this.timeout = localPingInputsBuilder.timeout;
        this.ipVersion = localPingInputsBuilder.ipVersion;
    }

    public String getTargetHost() {
        return targetHost;
    }

    public String getPacketSize() {
        return packetSize;
    }

    public String getPacketCount() {
        return packetCount;
    }

    public String getTimeout() {
        return timeout;
    }

    public String getIpVersion() {
        return ipVersion;
    }

    public static class LocalPingInputsBuilder {
        private String targetHost;
        private String packetSize;
        private String packetCount;
        private String timeout;
        private String ipVersion;

        public LocalPingInputsBuilder targetHost(String targetHost) {
            this.targetHost = targetHost;
            return this;
        }

        public LocalPingInputsBuilder packetSize(String packetSize) {
            this.packetSize = packetSize;
            return this;
        }

        public LocalPingInputsBuilder packetCount(String packetCount) {
            this.packetCount = packetCount;
            return this;
        }

        public LocalPingInputsBuilder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }

        public LocalPingInputsBuilder ipVersion(String ipVersion) {
            this.ipVersion = ipVersion;
            return this;
        }

        public LocalPingInputs build(){
            return new LocalPingInputs(this);
        }
    }
}
