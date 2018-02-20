/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.couchbase.entities.inputs;

import static io.cloudslang.content.couchbase.entities.couchbase.RecoveryType.getRecoveryTypeValue;
import static io.cloudslang.content.couchbase.validate.Validators.getValidInternalNodeIpAddress;

/**
 * Created by TusaM
 * 5/8/2017.
 */
public class NodeInputs {
    private final String internalNodeIpAddress;
    private final String recoveryType;

    private NodeInputs(NodeInputs.Builder builder) {
        this.internalNodeIpAddress = builder.internalNodeIpAddress;
        this.recoveryType = builder.recoveryType;
    }

    public String getInternalNodeIpAddress() {
        return internalNodeIpAddress;
    }

    public String getRecoveryType() {
        return recoveryType;
    }

    public static class Builder {
        private String internalNodeIpAddress;
        private String recoveryType;

        public NodeInputs build() {
            return new NodeInputs(this);
        }

        public NodeInputs.Builder withInternalNodeIpAddress(String inputValue) {
            internalNodeIpAddress = getValidInternalNodeIpAddress(inputValue);
            return this;
        }

        public NodeInputs.Builder withRecoveryType(String inputValue) {
            recoveryType = getRecoveryTypeValue(inputValue);
            return this;
        }
    }
}