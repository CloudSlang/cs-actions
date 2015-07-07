package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.constants.ErrorConstants;
import io.cloudslang.content.jclouds.entities.inputs.ServerIdentificationInputs;
import io.cloudslang.content.jclouds.entities.outputs.Outputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by persdana on 6/23/2015.
 */
public class RemoveServerExecutor {
    protected String serverId;
    protected String region;


    public Map<String, String> execute(ServerIdentificationInputs serverIdentificationInputs) throws Exception {
        Map<String, String> result = new HashMap<>();

        try {
            processInputs(serverIdentificationInputs);

            ComputeService cs = ComputeFactory.getComputeService(serverIdentificationInputs);
            String resultStr = cs.removeServer(region, serverId);

            result.put(Outputs.RETURN_CODE, Outputs.SUCCESS_RETURN_CODE);
            result.put(Outputs.RETURN_RESULT, resultStr);
        } catch (Exception e) {
            throw e;
        }

        return  result;
    }

    protected void processInputs(ServerIdentificationInputs serverIdentificationInputs) throws Exception {
        serverId = serverIdentificationInputs.getServerId();
        if (null == serverId || serverId.equals("")) {
            throw new Exception(ErrorConstants.SERVER_ID_NOT_SPECIFIED);
        } else {
            serverId = serverId.trim();
        }

        region = serverIdentificationInputs.getRegion();
    }
}
