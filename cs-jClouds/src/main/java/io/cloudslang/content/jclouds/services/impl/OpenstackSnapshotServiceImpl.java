package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import io.cloudslang.content.jclouds.services.SnapshotService;
import org.jclouds.ec2.domain.Snapshot;

/**
 * Created by Mihai Tusa.
 * 6/27/2016.
 */
public class OpenstackSnapshotServiceImpl extends JCloudsComputeService implements SnapshotService {
    public OpenstackSnapshotServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    @Override
    public Snapshot createSnapshotInRegion(String region, String volumeId, String description, boolean withExecutionLogs) {
        throw new RuntimeException(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }

    @Override
    public void deleteSnapshotInRegion(String region, String snapshotId, boolean withExecutionLogs) {
        throw new RuntimeException(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }
}
