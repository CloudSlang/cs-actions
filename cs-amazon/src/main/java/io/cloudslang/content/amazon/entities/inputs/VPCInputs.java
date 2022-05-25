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

import static io.cloudslang.content.amazon.utils.InputsUtil.getMaxResultsCount;

public class VPCInputs {
    private final String vpcIds;
    private final String maxResults;
    private final String nextToken;

    private VPCInputs(VPCInputs.Builder builder) {

        this.vpcIds = builder.vpcIds;
        this.maxResults = builder.maxResults;
        this.nextToken = builder.nextToken;
    }

    public String getVpcIds() {
        return vpcIds;
    }

    public String getMaxResults() {
        return maxResults;
    }

    public String getNextToken() {
        return nextToken;
    }

    public static class Builder {
        private String vpcIds;
        private String maxResults;
        private String nextToken;

        public VPCInputs build() {
            return new VPCInputs(this);
        }

        public VPCInputs.Builder withVpcIds(String inputValue) {
            vpcIds = inputValue;
            return this;
        }

        public VPCInputs.Builder withMaxResults(String inputValue) {
            maxResults = getMaxResultsCount(inputValue);
            return this;
        }

        public VPCInputs.Builder withNextToken(String inputValue) {
            nextToken = inputValue;
            return this;
        }
    }
}
