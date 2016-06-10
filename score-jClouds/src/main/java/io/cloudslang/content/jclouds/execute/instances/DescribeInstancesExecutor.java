package io.cloudslang.content.jclouds.execute.instances;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;

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