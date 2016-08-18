package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.services.JCloudsService;
import io.cloudslang.content.jclouds.services.TagService;
import io.cloudslang.content.jclouds.services.helpers.Utils;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.features.TagApi;

import java.util.Map;
import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 7/20/2016.
 */
public class AmazonTagServiceImpl extends JCloudsService implements TagService {
    EC2Api ec2Api = null;

    private String region;

    public AmazonTagServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    public void applyToResources(CommonInputs commonInputs, CustomInputs customInputs) {
        Utils utils = new Utils();

        Map<String, String> tagsMap = utils.getTagsMap(customInputs, commonInputs.getDelimiter());
        Set<String> resourcesIdsList = utils.getResourcesIdsList(customInputs.getResourceIdsString(), commonInputs.getDelimiter());

        getTagApi(customInputs.getRegion(), true, commonInputs.isDebugMode()).applyToResources(tagsMap, resourcesIdsList);
    }

    void init(boolean isDebugMode) {
        ContextBuilder contextBuilder = super.init(region, Constants.Apis.AMAZON_EC2_API, isDebugMode);
        ec2Api = new Utils().getEC2Api(contextBuilder);
    }

    void lazyInit(String region, boolean isDebugMode) {
        this.region = InputsUtil.getDefaultStringInput(region, Constants.Miscellaneous.DEFAULT_AMAZON_REGION);
        init(isDebugMode);
    }

    private TagApi getTagApi(String region, boolean isForRegion, boolean isDebugMode) {
        lazyInit(region, isDebugMode);

        return isForRegion ? ec2Api.getTagApiForRegion(region).get() : ec2Api.getTagApi().get();
    }
}