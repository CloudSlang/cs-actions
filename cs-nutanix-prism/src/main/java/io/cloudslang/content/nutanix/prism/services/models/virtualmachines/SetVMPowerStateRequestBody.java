/*
 * (c) Copyright 2020 Micro Focus, L.P.
 * All rights reserved. This program and the accompanying materials
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
