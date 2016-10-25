package io.cloudslang.content.amazon.services.impl;

import io.cloudslang.content.amazon.entities.constants.Constants;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.InstanceInputs;
import io.cloudslang.content.amazon.services.ComputeService;
import io.cloudslang.content.amazon.services.JCloudsService;

import java.util.Set;

/**
 * Created by persdana on 5/27/2015.
 */
public class OpenstackComputeServiceImpl extends JCloudsService implements ComputeService {
    public OpenstackComputeServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    @Override
    public Set<String> describeInstancesInRegion(CommonInputs commonInputs, InstanceInputs instanceInputs) throws Exception {
        throw new Exception(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }
}