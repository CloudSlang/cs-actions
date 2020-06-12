package io.cloudslang.content.nutanix.prism.service.models.virtualmachines;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class AddNicRequestBody {

    @JsonProperty("spec_list")
    ArrayList spec_list;

    public ArrayList getSpec_list() {
        return spec_list;
    }

    public void setSpec_list(ArrayList spec_list) {
        this.spec_list = spec_list;
    }

    public class VMNics {
        boolean is_connected;
        String vlan_id;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String requested_ip_address;
        String network_uuid;

        public boolean isIs_connected() {
            return is_connected;
        }

        public void setIs_connected(boolean is_connected) {
            this.is_connected = is_connected;
        }

        public String getVlan_id() { return vlan_id; }

        public void setVlan_id(String vlan_id) { this.vlan_id = vlan_id; }

        public String getRequested_ip_address() {
            return requested_ip_address;
        }

        public void setRequested_ip_address(String requested_ip_address) { this.requested_ip_address = requested_ip_address; }

        public String getNetwork_uuid() { return network_uuid; }

        public void setNetwork_uuid(String network_uuid) { this.network_uuid = network_uuid; }

    }

    }

