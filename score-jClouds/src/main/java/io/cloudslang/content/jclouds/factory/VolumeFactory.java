package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.services.VolumeService;
import io.cloudslang.content.jclouds.services.impl.AmazonVolumeServiceImpl;
import io.cloudslang.content.jclouds.services.impl.OpenstackVolumeServiceImpl;

/**
 * Created by Mihai Tusa.
 * 6/16/2016.
 */
public class VolumeFactory {
    public static VolumeService getVolumeService(CommonInputs commonInputs) throws Exception {
        VolumeService volumeService;
        switch (commonInputs.getProvider().toLowerCase()) {
            case Constants.Providers.OPENSTACK:
                volumeService = new OpenstackVolumeServiceImpl(
                        commonInputs.getEndpoint(),
                        commonInputs.getIdentity(),
                        commonInputs.getCredential(),
                        commonInputs.getProxyHost(),
                        commonInputs.getProxyPort());
                break;
            default:
                volumeService = new AmazonVolumeServiceImpl(
                        commonInputs.getEndpoint(),
                        commonInputs.getIdentity(),
                        commonInputs.getCredential(),
                        commonInputs.getProxyHost(),
                        commonInputs.getProxyPort());
        }

        return volumeService;
    }

}