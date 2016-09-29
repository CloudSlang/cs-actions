package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.services.JCloudsService;
import io.cloudslang.content.jclouds.services.VolumeService;

/**
 * Created by Mihai Tusa.
 * 6/16/2016.
 */
public class OpenstackVolumeServiceImpl extends JCloudsService implements VolumeService {
    public OpenstackVolumeServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    @Override
    public void deleteVolumeInRegion(String region, String volumeId, boolean isDebugMode) {
        throw new RuntimeException(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }
}
