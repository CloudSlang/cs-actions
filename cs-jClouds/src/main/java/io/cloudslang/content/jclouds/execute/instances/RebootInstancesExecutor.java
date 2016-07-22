package io.cloudslang.content.jclouds.execute.instances;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by persdana on 6/22/2015.
 */
public class RebootInstancesExecutor {
    private static final String SOFT_REBOOT_SUCCESS = "Soft reboot started successfully.";

    public Map<String, String> execute(CommonInputs commonInputs, CustomInputs customInputs) throws Exception {
        ComputeService cs = ComputeFactory.getComputeService(commonInputs);
        cs.rebootInstances(customInputs.getRegion(), customInputs.getInstanceId(), commonInputs.getWithExecutionLogs());

        return OutputsUtil.getResultsMap(SOFT_REBOOT_SUCCESS);
    }
}