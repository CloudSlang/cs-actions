package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.actions.UpdateServerTypeAction;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 2/24/2016.
 */
public class UpdateServerTypeExecutor {
    public Map<String, String> execute(CommonInputs inputs, CustomInputs customInputs) throws Exception {
        InputsUtil.validateInput(inputs.getEndpoint(), Inputs.CommonInputs.ENDPOINT);

        ComputeService cs = ComputeFactory.getComputeService(inputs, UpdateServerTypeAction.class);
        String result = cs.updateInstanceType(customInputs);

        return OutputsUtil.getResultsMap(result);
    }
}