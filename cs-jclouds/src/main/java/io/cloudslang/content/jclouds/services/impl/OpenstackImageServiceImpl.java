package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.services.ImageService;
import io.cloudslang.content.jclouds.services.JCloudsService;
import org.jclouds.ec2.domain.Image;

import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public class OpenstackImageServiceImpl extends JCloudsService implements ImageService {
    public OpenstackImageServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    @Override
    public Set<? extends Image> describeImagesInRegion(CommonInputs commonInputs, ImageInputs imageInputs) throws Exception {
        throw new Exception(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }

    @Override
    public String removeLaunchPermissionsFromImage(String region, Set<String> userIds, Set<String> userGroups,
                                                   String imageId, boolean isDebugMode) throws Exception {
        throw new Exception(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }

    @Override
    public String resetLaunchPermissionsOnImage(String region, String imageId, boolean isDebugMode) throws Exception {
        throw new Exception(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }
}