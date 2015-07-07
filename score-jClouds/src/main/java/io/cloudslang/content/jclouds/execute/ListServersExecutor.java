package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.inputs.ListServersInputs;
import io.cloudslang.content.jclouds.entities.outputs.Outputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by persdana on 6/23/2015.
 */
public class ListServersExecutor {
    private static final String DEFAULT_DELIMITER = ";\n";
    protected String region;
    protected String delimiter;

    public Map<String, String> execute(ListServersInputs listServersInputs) throws Exception {
        Map<String, String> result = new HashMap<>();

        try {
            processInputs(listServersInputs);

            ComputeService cs = ComputeFactory.getComputeService(listServersInputs);
            Set<String> resultSet = cs.listNodes(region);
            StringBuilder resultBuf = new StringBuilder();
            Iterator iterator = resultSet.iterator();
            while(iterator.hasNext()) {
                resultBuf.append(iterator.next());
                if(iterator.hasNext()) {
                    resultBuf.append(delimiter);
                }
            }

            result.put(Outputs.RETURN_CODE, Outputs.SUCCESS_RETURN_CODE);
            result.put(Outputs.RETURN_RESULT, resultBuf.toString());
        } catch (Exception e) {
            throw e;
        }

        return  result;
    }

    protected void processInputs(ListServersInputs listServerInputs) throws Exception {
        region = listServerInputs.getRegion();
        delimiter = listServerInputs.getDelimiter();
        if(delimiter == null || delimiter.isEmpty())
            delimiter = DEFAULT_DELIMITER;
    }
}
