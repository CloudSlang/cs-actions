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

/**
 * Created by TusaM
 * 5/11/2017.
 */
public class ClusterInputs {
    private final String ejectedNodes;
    private final String knownNodes;

    private ClusterInputs(ClusterInputs.Builder builder) {
        this.ejectedNodes = builder.ejectedNodes;
        this.knownNodes = builder.knownNodes;
    }

    public String getEjectedNodes() {
        return ejectedNodes;
    }

    public String getKnownNodes() {
        return knownNodes;
    }

    public static class Builder {
        private String ejectedNodes;
        private String knownNodes;

        public ClusterInputs build() {
            return new ClusterInputs(this);
        }

        public ClusterInputs.Builder withEjectedNodes(String inputValue) {
            ejectedNodes = inputValue;
            return this;
        }

        public ClusterInputs.Builder withKnownNodes(String inputValue) {
            knownNodes = inputValue;
            return this;
        }
    }
}