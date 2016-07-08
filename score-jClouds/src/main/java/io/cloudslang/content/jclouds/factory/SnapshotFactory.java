package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.services.SnapshotService;
import io.cloudslang.content.jclouds.services.impl.AmazonSnapshotServiceImpl;
import io.cloudslang.content.jclouds.services.impl.OpenstackSnapshotServiceImpl;

/**
 * Created by Mihai Tusa.
 * 6/27/2016.
 */
public class SnapshotFactory {
    public static SnapshotService getSnapshotService(CommonInputs commonInputs) throws Exception {
        SnapshotService snapshotService;
        switch (commonInputs.getProvider().toLowerCase()) {
            case Constants.Providers.OPENSTACK:
                snapshotService = new OpenstackSnapshotServiceImpl(
                        commonInputs.getEndpoint(),
                        commonInputs.getIdentity(),
                        commonInputs.getCredential(),
                        commonInputs.getProxyHost(),
                        commonInputs.getProxyPort());
                break;
            default:
                snapshotService = new AmazonSnapshotServiceImpl(
                        commonInputs.getEndpoint(),
                        commonInputs.getIdentity(),
                        commonInputs.getCredential(),
                        commonInputs.getProxyHost(),
                        commonInputs.getProxyPort());
        }

        return snapshotService;
    }
}