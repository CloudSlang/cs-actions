

package io.cloudslang.content.nutanix.prism.services.models.virtualmachines;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SetVMPowerStateRequestBody {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class SetPowerStateData {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String host_uuid;
        String transition;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String uuid;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        int vm_logical_timestamp;

        public String getHost_uuid() {
            return host_uuid;
        }

        public void setHost_uuid(String host_uuid) {
            this.host_uuid = host_uuid;
        }

        public String getTransition() {
            return transition;
        }

        public void setTransition(String transition) {
            this.transition = transition;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public int getVm_logical_timestamp() {
            return vm_logical_timestamp;
        }

        public void setVm_logical_timestamp(int vm_logical_timestamp) {
            this.vm_logical_timestamp = vm_logical_timestamp;
        }
    }
}
