package io.cloudslang.content.jclouds.execute.instances;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by persdana on 5/25/2015.
 */
public class StartServerExecutor {
    public Map<String, String> execute(CommonInputs inputs, CustomInputs customInputs) throws Exception {
        ComputeService cs = ComputeFactory.getComputeService(inputs);
        String resultStr = cs.start(customInputs.getRegion(), customInputs.getServerId());

        return OutputsUtil.getResultsMap((StringUtils.isBlank(resultStr)) ?
                Constants.Messages.SERVER_STARTED : Constants.Miscellaneous.EMPTY);
    }
}