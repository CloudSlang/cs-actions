package io.cloudslang.content.jclouds.execute.instances;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by persdana on 6/22/2015.
 */
public class SoftRebootExecutor {
    public Map<String, String> execute(CommonInputs inputs, CustomInputs customInputs) throws Exception {
        ComputeService cs = ComputeFactory.getComputeService(inputs);
        cs.softReboot(customInputs.getRegion(), customInputs.getServerId());

        return OutputsUtil.getResultsMap(Constants.Messages.SOFT_REBOOT_SUCCESS);
    }
}