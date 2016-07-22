package io.cloudslang.content.jclouds.execute.regions;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;
import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public class DescribeRegionsExecutor {
    public Map<String, String> execute(CommonInputs commonInputs) throws Exception {
        ComputeService cs = ComputeFactory.getComputeService(commonInputs);
        Set<String> availableRegions = cs.describeRegions(commonInputs.getWithExecutionLogs());

        String regionsString = OutputsUtil.getElementsString(availableRegions, commonInputs.getDelimiter());

        return OutputsUtil.getResultsMap(regionsString);
    }
}
