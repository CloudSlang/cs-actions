package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.inputs.ServerIdentificationInputs;
import io.cloudslang.content.jclouds.entities.outputs.Outputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utilities.InputsValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by persdana on 6/22/2015.
 */
public class HardRebootExecutor {
    private static final String REBOOT_SUCCESS = "Hard Reboot started successfully";

    public Map<String, String> execute(ServerIdentificationInputs serverIdentificationInputs) throws Exception {
        Map<String, String> result = new HashMap<>();
        InputsValidator.validateServerIdentificationInputs(serverIdentificationInputs);

        ComputeService cs = ComputeFactory.getComputeService(serverIdentificationInputs);
        cs.hardReboot(serverIdentificationInputs.getRegion(), serverIdentificationInputs.getServerId());

        result.put(Outputs.RETURN_CODE, Outputs.SUCCESS_RETURN_CODE);
        result.put(Outputs.RETURN_RESULT, REBOOT_SUCCESS);

        return  result;
    }
}
