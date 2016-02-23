package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;
import java.util.Set;

/**
 * Created by persdana on 6/23/2015.
 */
public class ListRegionsExecutor {
    public Map<String, String> execute(CommonInputs inputs) throws Exception {
        InputsUtil.validateInput(inputs.getEndpoint(), Inputs.ENDPOINT);

        ComputeService cs = ComputeFactory.getComputeService(inputs);
        Set<String> availableRegions = cs.listRegions();

        int index = 0;
        StringBuilder sb = new StringBuilder();
        for (String region : availableRegions) {
            sb.append(region);
            if (index < availableRegions.size() - 1) {
                sb.append(inputs.getDelimiter());
            }
            index++;
        }

        return OutputsUtil.getResultsMap(sb.toString());
    }
}