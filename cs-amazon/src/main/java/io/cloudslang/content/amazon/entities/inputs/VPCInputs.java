/*
 * (c) Copyright 2022 Micro Focus, L.P.
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

package io.cloudslang.content.amazon.entities.inputs;

public class VPCInputs {
    private final String vpcIds;

    private VPCInputs(VPCInputs.Builder builder) {
        this.vpcIds = builder.vpcIds;
    }

    public String getVpcIds() {
        return vpcIds;
    }

    public static class Builder {
        private String vpcIds;

        public VPCInputs build() {
            return new VPCInputs(this);
        }

        public VPCInputs.Builder withVpcIds(String inputValue) {
            vpcIds = inputValue;
            return this;
        }
    }
}