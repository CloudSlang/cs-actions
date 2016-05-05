package io.cloudslang.content.jclouds.execute.regions;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;
import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public class ListRegionsExecutor {
    public Map<String, String> execute(CommonInputs inputs) throws Exception {
        ComputeService cs = ComputeFactory.getComputeService(inputs);
        Set<String> availableRegions = cs.listRegions();

        String regionsString = OutputsUtil.getElementsString(availableRegions, inputs.getDelimiter());

        return OutputsUtil.getResultsMap(regionsString);
    }
}