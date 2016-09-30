package io.cloudslang.content.jclouds.services;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import org.jclouds.ec2.domain.Image;

import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public interface ImageService {
    Set<? extends Image> describeImagesInRegion(CommonInputs commonInputs, ImageInputs imageInputs) throws Exception;

    String removeLaunchPermissionsFromImage(String region, Set<String> userIds, Set<String> userGroups, String imageId,
                                            boolean isDebugMode) throws Exception;

    String resetLaunchPermissionsOnImage(String region, String imageId, boolean isDebugMode) throws Exception;
}
