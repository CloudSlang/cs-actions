package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.services.ImageService;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import io.cloudslang.content.jclouds.services.helpers.AmazonImageServiceHelper;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.domain.Image;
import org.jclouds.ec2.domain.Permission;
import org.jclouds.ec2.features.AMIApi;
import org.jclouds.ec2.options.CreateImageOptions;
import org.jclouds.ec2.options.DescribeImagesOptions;

import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public class AmazonImageServiceImpl extends JCloudsComputeService implements ImageService {
    EC2Api ec2Api;
    private String region;

    public AmazonImageServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    void init() {
        ContextBuilder contextBuilder = super.init(region, Constants.Apis.AMAZON_PROVIDER);
        ec2Api = contextBuilder.buildApi(EC2Api.class);
    }

    @Override
    public String createImageInRegion(String region, String name, String serverId, String imageDescription, boolean imageNoReboot) {
        AMIApi amiApi = getAMIApi(region, true);

        CreateImageOptions options = new CreateImageOptions().withDescription(imageDescription);
        if (imageNoReboot) {
            options.noReboot();
        }

        return amiApi.createImageInRegion(region, name, serverId, options);
    }

    @Override
    public String deregisterImageInRegion(String region, String imageId) {
        AMIApi amiApi = getAMIApi(region, true);

        amiApi.deregisterImageInRegion(region, imageId);

        return Constants.Messages.IMAGE_SUCCESSFULLY_DEREGISTER;
    }

    @Override
    public Set<? extends Image> describeImagesInRegion(String region, String identityId, String[] imageIds, String[] owners) {
        AMIApi amiApi = getAMIApi(region, true);
        DescribeImagesOptions options = new AmazonImageServiceHelper().getDescribeImagesOptions(identityId, imageIds, owners);

        return amiApi.describeImagesInRegion(region, options);
    }

    @Override
    public Permission getLaunchPermissionForImage(String region, String imageId) {
        AMIApi amiApi = getAMIApi(region, true);

        return amiApi.getLaunchPermissionForImageInRegion(region, imageId);
    }

    @Override
    public String addLaunchPermissionsToImage(String region, Set<String> userIds, Set<String> userGroups, String imageId) {
        AMIApi amiApi = getAMIApi(region, true);

        amiApi.addLaunchPermissionsToImageInRegion(region, userIds, userGroups, imageId);

        return Constants.Messages.LAUNCH_PERMISSIONS_SUCCESSFULLY_ADDED;
    }

    @Override
    public String removeLaunchPermissionsFromImage(String region, Set<String> userIds, Set<String> userGroups, String imageId) {
        AMIApi amiApi = getAMIApi(region, true);

        amiApi.removeLaunchPermissionsFromImageInRegion(region, userIds, userGroups, imageId);

        return Constants.Messages.LAUNCH_PERMISSIONS_SUCCESSFULLY_REMOVED;
    }

    @Override
    public String resetLaunchPermissionsOnImage(String region, String imageId) {
        AMIApi amiApi = getAMIApi(region, true);

        amiApi.resetLaunchPermissionsOnImageInRegion(region, imageId);

        return Constants.Messages.LAUNCH_PERMISSIONS_SUCCESSFULLY_RESET;
    }

    private AMIApi getAMIApi(String region, boolean isForRegion) {
        lazyInit(region);
        return isForRegion ? ec2Api.getAMIApiForRegion(region).get() : ec2Api.getAMIApi().get();
    }

    void lazyInit(String region) {
        if (this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init();
        } else if (ec2Api == null) {
            this.init();
        }
    }
}