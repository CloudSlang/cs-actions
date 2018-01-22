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
package io.cloudslang.content.dca.controllers;

import io.cloudslang.content.dca.models.DcaBaseResourceModel;
import io.cloudslang.content.dca.models.DcaDeploymentParameterModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CreateResourceJSONController {
    @NotNull
    public static List<DcaBaseResourceModel> getDcaBaseResourceModels(@NotNull final List<String> baseResourceUuids,
                                                                      @NotNull final List<String> baseResourceCiTypes,
                                                                      @NotNull final List<String> baseResourceTypeUuids) {
        final Iterator<String> baseResourceUuidsIter = baseResourceUuids.iterator();
        final Iterator<String> baseResourceCiTypesIter = baseResourceCiTypes.iterator();
        final Iterator<String> baseResourceTypeUuidsIter = baseResourceTypeUuids.iterator();
        final List<DcaBaseResourceModel> dcaBaseResources = new ArrayList<>();
        while (baseResourceUuidsIter.hasNext() && baseResourceCiTypesIter.hasNext() && baseResourceTypeUuidsIter.hasNext()) {
            dcaBaseResources.add(new DcaBaseResourceModel(baseResourceUuidsIter.next(), baseResourceCiTypesIter.next(), baseResourceTypeUuidsIter.next()));
        }
        return dcaBaseResources;
    }

    @NotNull
    public static List<DcaDeploymentParameterModel> getDcaDeploymentParameterModels(
            @NotNull final List<String> deploymentParameterNames,
            @NotNull final List<String> deploymentParameterValues) {
        final Iterator<String> deploymentParameterNamesIter = deploymentParameterNames.iterator();
        final Iterator<String> deploymentParameterValuesIter = deploymentParameterValues.iterator();

        final List<DcaDeploymentParameterModel> deploymentParameters = new ArrayList<>();
        while (deploymentParameterNamesIter.hasNext() && deploymentParameterValuesIter.hasNext()) {
            deploymentParameters.add(new DcaDeploymentParameterModel(deploymentParameterNamesIter.next(), deploymentParameterValuesIter.next()));
        }
        return deploymentParameters;
    }
}
