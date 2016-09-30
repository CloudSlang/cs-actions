package io.cloudslang.content.jclouds.services.impl;

import com.google.common.collect.Multimap;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.services.ImageService;
import io.cloudslang.content.jclouds.services.JCloudsService;
import io.cloudslang.content.jclouds.services.helpers.AmazonImageServiceHelper;
import io.cloudslang.content.jclouds.services.helpers.FiltersHelper;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.domain.Image;
import org.jclouds.ec2.features.AMIApi;
import org.jclouds.ec2.options.DescribeImagesOptions;

import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public class AmazonImageServiceImpl extends JCloudsService implements ImageService {
    EC2Api ec2Api;

    private String region;

    private static final String IMAGE_SUCCESSFULLY_DEREGISTER = "The image was successfully deregister.";
    private static final String LAUNCH_PERMISSIONS_SUCCESSFULLY_ADDED = "Launch permissions were successfully added.";
    private static final String LAUNCH_PERMISSIONS_SUCCESSFULLY_REMOVED = "Launch permissions were successfully removed.";
    private static final String LAUNCH_PERMISSIONS_SUCCESSFULLY_RESET = "Launch permissions were successfully reset.";
    private static final String IMAGE_NAME_INPUT_REQUIRED = "The value provided for [name] input is required. " +
            "Please provide an image name.";

    public AmazonImageServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    @Override
    public Set<? extends Image> describeImagesInRegion(CommonInputs commonInputs, ImageInputs imageInputs) {
        AmazonImageServiceHelper helper = new AmazonImageServiceHelper();
        DescribeImagesOptions options = helper.getDescribeImagesOptions(imageInputs, commonInputs.getDelimiter());
        Multimap<String, String> filtersMap = helper.getImageFiltersMap(imageInputs, commonInputs.getDelimiter());

        if (filtersMap.isEmpty()) {
            return getAMIApi(imageInputs.getCustomInputs().getRegion(), true, commonInputs.isDebugMode())
                    .describeImagesInRegion(imageInputs.getCustomInputs().getRegion(), options);
        }

        return getAMIApi(imageInputs.getCustomInputs().getRegion(), true, commonInputs.isDebugMode())
                .describeImagesInRegionWithFilter(imageInputs.getCustomInputs().getRegion(), filtersMap, options);
    }

    @Override
    public String removeLaunchPermissionsFromImage(String region, Set<String> userIds, Set<String> userGroups,
                                                   String imageId, boolean isDebugMode) {
        getAMIApi(region, true, isDebugMode)
                .removeLaunchPermissionsFromImageInRegion(region, userIds, userGroups, imageId);

        return LAUNCH_PERMISSIONS_SUCCESSFULLY_REMOVED;
    }

    @Override
    public String resetLaunchPermissionsOnImage(String region, String imageId, boolean isDebugMode) {
        getAMIApi(region, true, isDebugMode).resetLaunchPermissionsOnImageInRegion(region, imageId);

        return LAUNCH_PERMISSIONS_SUCCESSFULLY_RESET;
    }

    void lazyInit(String region, boolean isDebugMode) {
        this.region = InputsUtil.getDefaultStringInput(region, Constants.AwsParams.DEFAULT_AMAZON_REGION);
        init(isDebugMode);
    }

    void init(boolean isDebugMode) {
        ContextBuilder contextBuilder = super.init(region, Constants.Apis.AMAZON_EC2_API, isDebugMode);
        ec2Api = new FiltersHelper().getEC2Api(contextBuilder);
    }

    private AMIApi getAMIApi(String region, boolean isForRegion, boolean isDebugMode) {
        lazyInit(region, isDebugMode);

        return isForRegion ? ec2Api.getAMIApiForRegion(region).get() : ec2Api.getAMIApi().get();
    }
}
