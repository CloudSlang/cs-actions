package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.actions.CreateNodesInGroupAction;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CreateServerCustomInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import io.cloudslang.content.jclouds.utils.OutputsUtil;
import org.jclouds.compute.domain.NodeMetadata;

import java.util.Map;
import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 4/28/2016.
 */
public class CreateNodesInGroupExecutor {
    public Map<String, String> execute(CommonInputs inputs, CreateServerCustomInputs serverCustomInputs) throws Exception {
        InputsUtil.validateInput(inputs.getEndpoint(), Inputs.CommonInputs.ENDPOINT);

        ComputeService cs = ComputeFactory.getComputeService(inputs, CreateNodesInGroupAction.class);
        Set<? extends NodeMetadata> result = cs.createNodesInGroup(inputs, serverCustomInputs);

        return OutputsUtil.getResultsMap(result.toString());
    }
}
