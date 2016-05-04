package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.jclouds.actions.CreateNodesInGroupAction;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.impl.AmazonComputeService;
import io.cloudslang.content.jclouds.services.impl.ComputeServiceImpl;
import io.cloudslang.content.jclouds.services.impl.OpenstackComputeService;

/**
 * Created by persdana on 5/27/2015.
 */
public class ComputeFactory {
    private static final String OPENSTACK = "openstack";
    private static final String AMAZON = "amazon";

    public static ComputeService getComputeService(CommonInputs commonInputs, Class<?> valueType) throws Exception {
        ComputeService computeService;
        switch (commonInputs.getProvider().toLowerCase()) {
            case OPENSTACK:
                computeService = new OpenstackComputeService(
                        commonInputs.getEndpoint(),
                        commonInputs.getIdentity(),
                        commonInputs.getCredential(),
                        commonInputs.getProxyHost(),
                        commonInputs.getProxyPort());
                break;
            case AMAZON:
                if (valueType == CreateNodesInGroupAction.class) {
                    computeService = new ComputeServiceImpl(
                            commonInputs.getProvider(),
                            commonInputs.getEndpoint(),
                            commonInputs.getIdentity(),
                            commonInputs.getCredential(),
                            commonInputs.getProxyHost(),
                            commonInputs.getProxyPort());
                } else {
                    computeService = new AmazonComputeService(
                            commonInputs.getEndpoint(),
                            commonInputs.getIdentity(),
                            commonInputs.getCredential(),
                            commonInputs.getProxyHost(),
                            commonInputs.getProxyPort());
                }
                break;
            default:
                computeService = new ComputeServiceImpl(
                        commonInputs.getProvider(),
                        commonInputs.getEndpoint(),
                        commonInputs.getIdentity(),
                        commonInputs.getCredential(),
                        commonInputs.getProxyHost(),
                        commonInputs.getProxyPort());
        }

        return computeService;
    }
}