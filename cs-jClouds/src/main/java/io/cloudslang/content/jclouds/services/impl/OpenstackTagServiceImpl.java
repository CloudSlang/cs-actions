package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import io.cloudslang.content.jclouds.services.TagService;

/**
 * Created by Mihai Tusa.
 * 7/20/2016.
 */
public class OpenstackTagServiceImpl extends JCloudsComputeService implements TagService {
    public OpenstackTagServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    @Override
    public void applyToResources(CustomInputs imageInputs, String delimiter) {
        throw new RuntimeException(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }
}