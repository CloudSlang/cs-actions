package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.inputs.ListServersInputs;
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
public class ListServersExecutor {
    public Map<String, String> execute(ListServersInputs listServersInputs) throws Exception {
        Map<String, String> result = new HashMap<>();
        InputsValidator.validateListRegionsInputs(listServersInputs);

        ComputeService cs = ComputeFactory.getComputeService(listServersInputs);
        Set<String> resultSet = cs.listNodes(listServersInputs.getRegion());

        StringBuilder resultBuf = new StringBuilder();
        for(String serve : resultSet) {
            resultBuf.append(serve);
            resultBuf.append(listServersInputs.getDelimiter());
        }
        String servers = resultBuf.substring(0, resultBuf.length() - listServersInputs.getDelimiter().length());

        result.put(Outputs.RETURN_CODE, Outputs.SUCCESS_RETURN_CODE);
        result.put(Outputs.RETURN_RESULT, servers);

        return  result;
    }
}
