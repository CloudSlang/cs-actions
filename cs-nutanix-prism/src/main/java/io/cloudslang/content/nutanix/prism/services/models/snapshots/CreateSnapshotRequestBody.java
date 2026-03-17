package io.cloudslang.content.nutanix.prism.services.models.snapshots;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateSnapshotRequestBody {

    @JsonProperty("snapshot_specs")
    private SnapshotSpec[] snapshotSpecs;

    public SnapshotSpec[] getSnapshotSpecs() {
        return snapshotSpecs;
    }

    public void setSnapshotSpecs(SnapshotSpec[] snapshotSpecs) {
        this.snapshotSpecs = snapshotSpecs;
    }

    public static class SnapshotSpec {
        @JsonProperty("snapshot_name")
        private String snapshotName;

        @JsonProperty("uuid")
        private String uuid;

        @JsonProperty("vm_logical_timestamp")
        private long vmLogicalTimestamp;

        @JsonProperty("vm_uuid")
        private String vmUUID;

        public SnapshotSpec() {
        }

        public SnapshotSpec(String snapshotName, String uuid, long vmLogicalTimestamp, String vmUUID) {
            this.snapshotName = snapshotName;
            this.uuid = uuid;
            this.vmLogicalTimestamp = vmLogicalTimestamp;
            this.vmUUID = vmUUID;
        }

        public String getSnapshotName() {
            return snapshotName;
        }

        public void setSnapshotName(String snapshotName) {
            this.snapshotName = snapshotName;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public long getVmLogicalTimestamp() {
            return vmLogicalTimestamp;
        }

        public void setVmLogicalTimestamp(long vmLogicalTimestamp) {
            this.vmLogicalTimestamp = vmLogicalTimestamp;
        }

        public String getVmUUID() {
            return vmUUID;
        }

        public void setVmUUID(String vmUUID) {
            this.vmUUID = vmUUID;
        }
    }
}

