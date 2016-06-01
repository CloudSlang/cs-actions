package io.cloudslang.content.jclouds.execute.instances;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.jclouds.ec2.options.RunInstancesOptions;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class RunInstancesExecutor {
    public Map<String, String> execute(CommonInputs inputs, InstanceInputs instanceInputs) throws Exception {
        ComputeService cs = ComputeFactory.getComputeService(inputs);
        Reservation<? extends RunningInstance> result = cs.runInstancesInRegion(instanceInputs.getCustomInputs().getRegion(),
                instanceInputs.getAvailabilityZone(), instanceInputs.getCustomInputs().getImageId(), instanceInputs.getMinCount(),
                instanceInputs.getMaxCount(), RunInstancesOptions.NONE);

        return OutputsUtil.getResultsMap(result.toString());
    }
}