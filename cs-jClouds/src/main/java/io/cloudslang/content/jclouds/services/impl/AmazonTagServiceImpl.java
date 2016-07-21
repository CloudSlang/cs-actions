package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
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
public class AmazonTagServiceImpl extends JCloudsComputeService implements TagService {
    EC2Api ec2Api = null;

    private String region;

    public AmazonTagServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    public void applyToResources(CustomInputs customInputs, String delimiter) {
        Utils utils = new Utils();

        Map<String, String> tagsMap = utils.getTagsMap(customInputs, delimiter);
        Set<String> resourcesIdsList = utils.getResourcesIdsList(customInputs.getResourceIdsString(), delimiter);

        getTagApi(customInputs.getRegion(), true).applyToResources(tagsMap, resourcesIdsList);
    }

    void init() {
        ContextBuilder contextBuilder = super.init(region, Constants.Apis.AMAZON_EC2_API);
        ec2Api = new Utils().getEC2Api(contextBuilder);
    }

    void lazyInit(String region) {
        this.region = InputsUtil.getAmazonRegion(region);
        init();
    }

    private TagApi getTagApi(String region, boolean isForRegion) {
        lazyInit(region);

        return isForRegion ? ec2Api.getTagApiForRegion(region).get() : ec2Api.getTagApi().get();
    }
}