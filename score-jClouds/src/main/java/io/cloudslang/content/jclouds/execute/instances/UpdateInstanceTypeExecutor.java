package io.cloudslang.content.jclouds.execute.instances;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 2/24/2016.
 */
public class UpdateInstanceTypeExecutor {
    public Map<String, String> execute(CommonInputs inputs, InstanceInputs instanceInputs) throws Exception {
        ComputeService cs = ComputeFactory.getComputeService(inputs);
        String result = cs.updateInstanceType(instanceInputs.getCustomInputs().getRegion(),
                instanceInputs.getCustomInputs().getServerId(), instanceInputs.getInstanceType(),
                instanceInputs.getCheckStateTimeout(), instanceInputs.getPolingInterval());

        return OutputsUtil.getResultsMap(result);
    }
}