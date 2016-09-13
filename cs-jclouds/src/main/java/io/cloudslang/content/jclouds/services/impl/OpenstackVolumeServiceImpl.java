package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.services.JCloudsService;
import io.cloudslang.content.jclouds.services.VolumeService;
import org.jclouds.ec2.domain.Attachment;
import org.jclouds.ec2.domain.Volume;

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

    @Override
    public Attachment attachVolumeInRegion(String region, String volumeId, String instanceId, String device,
                                           boolean isDebugMode) throws Exception {
        throw new RuntimeException(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }

    @Override
    public void detachVolumeInRegion(String region, String volumeId, String instanceId, String device, boolean force,
                                     boolean isDebugMode) {
        throw new RuntimeException(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }
}
