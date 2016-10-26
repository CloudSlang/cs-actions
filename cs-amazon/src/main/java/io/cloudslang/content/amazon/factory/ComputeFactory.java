package io.cloudslang.content.amazon.factory;

import io.cloudslang.content.amazon.entities.constants.Constants;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.services.ComputeService;
import io.cloudslang.content.amazon.services.impl.AmazonComputeServiceImpl;
import io.cloudslang.content.amazon.services.impl.ComputeServiceImpl;
import io.cloudslang.content.amazon.services.impl.OpenstackComputeServiceImpl;

/**
 * Created by persdana on 5/27/2015.
 */
public class ComputeFactory {
    private static final String AMAZON = "amazon";

    public static ComputeService getComputeService(CommonInputs commonInputs) {
        ComputeService computeService;
        switch (commonInputs.getProvider().toLowerCase()) {
            case AMAZON:
                computeService = new AmazonComputeServiceImpl(
                        commonInputs.getEndpoint(),
                        commonInputs.getIdentity(),
                        commonInputs.getCredential(),
                        commonInputs.getProxyHost(),
                        commonInputs.getProxyPort());
                break;
            case Constants.Providers.OPENSTACK:
                computeService = new OpenstackComputeServiceImpl(
                        commonInputs.getEndpoint(),
                        commonInputs.getIdentity(),
                        commonInputs.getCredential(),
                        commonInputs.getProxyHost(),
                        commonInputs.getProxyPort());
                break;
            default:
                computeService = new ComputeServiceImpl(
                        commonInputs.getEndpoint(),
                        commonInputs.getIdentity(),
                        commonInputs.getCredential(),
                        commonInputs.getProxyHost(),
                        commonInputs.getProxyPort());
        }

        return computeService;
    }
}
