package io.cloudslang.content.amazon.execute.instances;

import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.InstanceInputs;
import io.cloudslang.content.amazon.factory.ComputeFactory;
import io.cloudslang.content.amazon.services.ComputeService;
import io.cloudslang.content.amazon.utils.OutputsUtil;

import java.util.Map;
import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 6/1/2016.
 */
public class DescribeInstancesExecutor {
    public Map<String, String> execute(CommonInputs inputs, InstanceInputs instanceInputs) throws Exception {
        ComputeService cs = ComputeFactory.getComputeService(inputs);
        Set<String> instancesSet = cs.describeInstancesInRegion(inputs, instanceInputs);

        return OutputsUtil.getResultsMap(instancesSet.toString());
    }
}
