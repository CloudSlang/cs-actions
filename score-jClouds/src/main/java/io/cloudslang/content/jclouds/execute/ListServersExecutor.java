package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
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
public class ListServersExecutor {
    public Map<String, String> execute(CommonInputs inputs, CustomInputs customInputs) throws Exception {
        InputsUtil.validateInput(inputs.getEndpoint(), Inputs.ENDPOINT);

        ComputeService cs = ComputeFactory.getComputeService(inputs);
        Set<String> nodesInRegion = cs.listNodes(customInputs.getRegion());

        int index = 0;
        StringBuilder sb = new StringBuilder();
        for (String server : nodesInRegion) {
            sb.append(server);
            if (index < nodesInRegion.size() - 1) {
                sb.append(inputs.getDelimiter());
            }
            index++;
        }

        return OutputsUtil.getResultsMap(sb.toString());
    }
}
