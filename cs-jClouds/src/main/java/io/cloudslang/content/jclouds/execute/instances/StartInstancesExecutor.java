package io.cloudslang.content.jclouds.execute.instances;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by persdana on 5/25/2015.
 */
public class StartInstancesExecutor {
    public Map<String, String> execute(CommonInputs commonInputs, CustomInputs customInputs) throws Exception {
        ComputeService cs = ComputeFactory.getComputeService(commonInputs);
        String resultStr = cs.startInstances(customInputs.getRegion(), customInputs.getInstanceId(), commonInputs.getWithExecutionLogs());

        return OutputsUtil.getResultsMap(resultStr);
    }
}
