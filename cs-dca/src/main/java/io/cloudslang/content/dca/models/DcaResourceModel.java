/*
 * (c) Copyright 2018 Micro Focus, L.P.
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
package io.cloudslang.content.dca.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DcaResourceModel {
    private String typeUuid;
    private int deploySequence;

    private List<DcaBaseResourceModel> baseResources = new ArrayList<>();
    private List<DcaDeploymentParameterModel> deploymentParameters = new ArrayList<>();

    public DcaResourceModel() {
    }

    public DcaResourceModel(String typeUuid, int deploySequence) {
        this.typeUuid = typeUuid;
        this.deploySequence = deploySequence;
    }

    public void addBaseResource(@NotNull final DcaBaseResourceModel baseResource) {
        baseResources.add(baseResource);
    }

    public void addBaseResources(@NotNull final List<DcaBaseResourceModel> baseResourceList) {
        baseResources.addAll(baseResourceList);
    }

    public void addDeploymentParameter(@NotNull final DcaDeploymentParameterModel deploymentParameter) {
        deploymentParameters.add(deploymentParameter);
    }

    public void addDeploymentParameters(@NotNull final List<DcaDeploymentParameterModel> deploymentParameterList) {
        deploymentParameters.addAll(deploymentParameterList);
    }

    @JsonProperty("TypeUuid")
    public String getTypeUuid() {
        return typeUuid;
    }

    public void setTypeUuid(String typeUuid) {
        this.typeUuid = typeUuid;
    }

    public int getDeploySequence() {
        return deploySequence;
    }

    public void setDeploySequence(int deploySequence) {
        this.deploySequence = deploySequence;
    }

    public List<DcaBaseResourceModel> getBaseResources() {
        return baseResources;
    }

    public void setBaseResources(List<DcaBaseResourceModel> baseResources) {
        this.baseResources = baseResources;
    }

    public List<DcaDeploymentParameterModel> getDeploymentParameters() {
        return deploymentParameters;
    }

    public void setDeploymentParameters(List<DcaDeploymentParameterModel> deploymentParameters) {
        this.deploymentParameters = deploymentParameters;
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
