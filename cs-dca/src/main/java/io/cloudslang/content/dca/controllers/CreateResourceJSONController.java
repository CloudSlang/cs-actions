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
