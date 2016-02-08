package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.impl.AmazonComputeService;
import io.cloudslang.content.jclouds.services.impl.ComputeServiceImpl;
import io.cloudslang.content.jclouds.services.impl.OpenstackComputeService;
import io.cloudslang.content.jclouds.utilities.InputsValidator;

/**
 * Created by persdana on 5/27/2015.
 */
public class ComputeFactory {

    public static ComputeService getComputeService(CommonInputs commonInputs) throws Exception {
        InputsValidator.validateCommonInputs(commonInputs);
        ComputeService computeService = null;
        switch (commonInputs.getProvider()) {
            case OPENSTACK:
                computeService = new OpenstackComputeService(
                        commonInputs.getEndpoint(),
                        commonInputs.getIdentity(),
                        commonInputs.getCredential(),
                        commonInputs.getProxyHost(),
                        commonInputs.getProxyPort());
                break;
            case AMAZON:
                computeService = new AmazonComputeService(
                        commonInputs.getEndpoint(),
                        commonInputs.getIdentity(),
                        commonInputs.getCredential(),
                        commonInputs.getProxyHost(),
                        commonInputs.getProxyPort());
                break;
            case OTHER:
                computeService = new ComputeServiceImpl(
                        commonInputs.getProvider().getProviderStr(),
                        commonInputs.getEndpoint(),
                        commonInputs.getIdentity(),
                        commonInputs.getCredential(),
                        commonInputs.getProxyHost(),
                        commonInputs.getProxyPort());
            break;
        }

        return computeService;
    }
}
