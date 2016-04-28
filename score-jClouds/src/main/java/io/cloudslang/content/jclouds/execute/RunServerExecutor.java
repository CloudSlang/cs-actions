package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import io.cloudslang.content.jclouds.utils.OutputsUtil;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class RunServerExecutor {
    public Map<String, String> execute(CommonInputs inputs, CustomInputs customInputs) throws Exception {
        InputsUtil.validateInput(inputs.getEndpoint(), Inputs.ENDPOINT);

        ComputeService cs = ComputeFactory.getComputeService(inputs);
        Reservation<? extends RunningInstance> result = cs.createServer(inputs, customInputs);

        return OutputsUtil.getResultsMap(result.toString());
    }
}