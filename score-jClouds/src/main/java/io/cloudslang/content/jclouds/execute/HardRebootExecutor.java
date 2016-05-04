package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.actions.HardRebootAction;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by persdana on 6/22/2015.
 */
public class HardRebootExecutor {
    private static final String REBOOT_SUCCESS = "Hard Reboot started successfully";

    public Map<String, String> execute(CommonInputs inputs, CustomInputs customInputs) throws Exception {
        InputsUtil.validateInput(inputs.getEndpoint(), Inputs.CommonInputs.ENDPOINT);

        ComputeService cs = ComputeFactory.getComputeService(inputs, HardRebootAction.class);
        cs.hardReboot(customInputs.getRegion(), customInputs.getServerId());

        return OutputsUtil.getResultsMap(REBOOT_SUCCESS);
    }
}