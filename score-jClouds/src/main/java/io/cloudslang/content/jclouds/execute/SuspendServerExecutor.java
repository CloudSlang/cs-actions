package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.actions.SuspendServerAction;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import io.cloudslang.content.jclouds.utils.OutputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by persdana on 6/22/2015.
 */
public class SuspendServerExecutor {
    private static final String SERVER_SUSPENDED = "server suspended";
    private static final String EMPTY = "";

    public Map<String, String> execute(CommonInputs inputs, CustomInputs customInputs) throws Exception {
        InputsUtil.validateInput(inputs.getEndpoint(), Inputs.CommonInputs.ENDPOINT);

        ComputeService cs = ComputeFactory.getComputeService(inputs, SuspendServerAction.class);
        String resultStr = cs.suspend(customInputs.getRegion(), customInputs.getServerId());

        return OutputsUtil.getResultsMap(StringUtils.isBlank(resultStr) ? SERVER_SUSPENDED : EMPTY);
    }
}