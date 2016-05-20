package io.cloudslang.content.jclouds.execute.instances;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 2/24/2016.
 */
public class UpdateServerTypeExecutor {
    public Map<String, String> execute(CommonInputs inputs, CustomInputs customInputs) throws Exception {
        ComputeService cs = ComputeFactory.getComputeService(inputs);
        String result = cs.updateInstanceType(customInputs.getRegion(), customInputs.getServerId(),
                customInputs.getInstanceType(), customInputs.getCheckStateTimeout(), customInputs.getPolingInterval());

        return OutputsUtil.getResultsMap(result);
    }
}