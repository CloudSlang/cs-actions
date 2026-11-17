/*
 * Copyright 2026-2027 Open Text
 * This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.nutanix.prism.services.models.snapshots;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateSnapshotRequestBody {
    @JsonProperty("snapshot_specs")
    private final List<SnapshotSpec> snapshotSpecs;

    public CreateSnapshotRequestBody(SnapshotSpec snapshotSpec) {
        this.snapshotSpecs = List.of(snapshotSpec);
    }

    public List<SnapshotSpec> getSnapshotSpecs() {
        return snapshotSpecs;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({"snapshot_name", "uuid", "vm_logical_timestamp", "vm_uuid"})
    public static class SnapshotSpec {
        @JsonProperty("snapshot_name")
        private final String snapshotName;
        private final String uuid;
        @JsonProperty("vm_logical_timestamp")
        private final Long vmLogicalTimestamp;
        @JsonProperty("vm_uuid")
        private final String vmUUID;

        public SnapshotSpec(String snapshotName, String uuid, Long vmLogicalTimestamp, String vmUUID) {
            this.snapshotName = snapshotName;
            this.uuid = uuid;
            this.vmLogicalTimestamp = vmLogicalTimestamp;
            this.vmUUID = vmUUID;
        }

        public String getSnapshotName() {
            return snapshotName;
        }

        public String getUuid() {
            return uuid;
        }

        public Long getVmLogicalTimestamp() {
            return vmLogicalTimestamp;
        }

        public String getVmUUID() {
            return vmUUID;
        }
    }
}
