package io.cloudslang.content.jclouds.services;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import org.jclouds.ec2.domain.Image;
import org.jclouds.ec2.domain.Permission;

import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public interface ImageService {
    String createImageInRegion(String region, String name, String serverId, String imageDescription, boolean imageNoReboot) throws Exception;

    String deregisterImageInRegion(String region, String imageId) throws Exception;

    Set<? extends Image> describeImagesInRegion(CommonInputs commonInputs, ImageInputs imageInputs) throws Exception;

    Permission getLaunchPermissionForImage(String region, String imageId) throws Exception;

    String addLaunchPermissionsToImage(String region, Set<String> userIds, Set<String> userGroups, String imageId) throws Exception;

    String removeLaunchPermissionsFromImage(String region, Set<String> userIds, Set<String> userGroups, String imageId) throws Exception;

    String resetLaunchPermissionsOnImage(String region, String imageId) throws Exception;
}