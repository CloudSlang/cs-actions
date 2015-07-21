package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.inputs.ListRegionsInputs;
import io.cloudslang.content.jclouds.entities.outputs.Outputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utilities.InputsValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by persdana on 6/23/2015.
 */
public class ListRegionsExecutor {
    public Map<String, String> execute(ListRegionsInputs listRegionsInputs) throws Exception {
        Map<String, String> result = new HashMap<>();
        InputsValidator.validateListRegionsInputs(listRegionsInputs);

        ComputeService cs = ComputeFactory.getComputeService(listRegionsInputs);
        Set<String> resultSet = cs.listRegions();

        StringBuilder resultBuf = new StringBuilder();
        for(String region : resultSet) {
            resultBuf.append(region);
            resultBuf.append(listRegionsInputs.getDelimiter());
        }
        String regions = resultBuf.substring(0, resultBuf.length() - listRegionsInputs.getDelimiter().length());

        result.put(Outputs.RETURN_CODE, Outputs.SUCCESS_RETURN_CODE);
        result.put(Outputs.RETURN_RESULT, regions);

        return  result;
    }
}
