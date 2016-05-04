package io.cloudslang.content.jclouds.execute.instances;

import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by persdana on 6/23/2015.
 */
public class ResumeServerExecutor {
    private static final String SERVER_RESUMED = "server resumed";

    public Map<String, String> execute(CommonInputs inputs, CustomInputs customInputs) throws Exception {
        InputsUtil.validateInput(inputs.getEndpoint(), Inputs.CommonInputs.ENDPOINT);

        ComputeService cs = ComputeFactory.getComputeService(inputs);
        cs.resume(customInputs.getRegion(), customInputs.getServerId());

        return OutputsUtil.getResultsMap(SERVER_RESUMED);
    }
}