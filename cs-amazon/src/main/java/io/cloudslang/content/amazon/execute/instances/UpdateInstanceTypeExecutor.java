package io.cloudslang.content.amazon.execute.instances;

import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.InstanceInputs;
import io.cloudslang.content.amazon.factory.ComputeFactory;
import io.cloudslang.content.amazon.services.ComputeService;
import io.cloudslang.content.amazon.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 2/24/2016.
 */
public class UpdateInstanceTypeExecutor {
    public Map<String, String> execute(CommonInputs commonInputs, InstanceInputs instanceInputs) throws Exception {
        ComputeService cs = ComputeFactory.getComputeService(commonInputs);
        String result = cs.updateInstanceType(instanceInputs.getCustomInputs().getRegion(),
                instanceInputs.getCustomInputs().getInstanceId(), instanceInputs.getCustomInputs().getInstanceType(),
                instanceInputs.getCheckStateTimeout(), instanceInputs.getPolingInterval(), commonInputs.isDebugMode());

        return OutputsUtil.getResultsMap(result);
    }
}