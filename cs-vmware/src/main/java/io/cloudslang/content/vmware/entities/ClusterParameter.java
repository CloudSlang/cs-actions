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

package io.cloudslang.content.vmware.entities;

/**
 * Created by giloan on 8/31/2016.
 */
public enum ClusterParameter {
    COMPUTE_RESOURCE("ComputeResource"),
    CLUSTER_COMPUTE_RESOURCE("ClusterComputeResource"),
    CLUSTER_PROFILE("ClusterProfile"),
    CLUSTER_PROFILE_MANAGER("ClusterProfileManager"),
    CONFIGURATION_EX("configurationEx");

    private final String parameter;

    /**
     * Instantiates a VM parameter.
     *
     * @param input the parameter
     */
    ClusterParameter(String input) {
        this.parameter = input;
    }

    /**
     * Gets the parameter.
     *
     * @return the parameter
     */
    public String getValue() {
        return parameter;
    }
}
