package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.services.JCloudsService;
import io.cloudslang.content.jclouds.services.VolumeService;
import io.cloudslang.content.jclouds.services.helpers.AmazonVolumeServiceHelper;
import io.cloudslang.content.jclouds.services.helpers.FiltersHelper;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.features.ElasticBlockStoreApi;
import org.jclouds.ec2.options.DetachVolumeOptions;

/**
 * Created by Mihai Tusa.
 * 6/16/2016.
 */
public class AmazonVolumeServiceImpl extends JCloudsService implements VolumeService {
    EC2Api ec2Api = null;

    private String region = Constants.Miscellaneous.EMPTY;

    public AmazonVolumeServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    @Override
    public void deleteVolumeInRegion(String region, String volumeId, boolean isDebugMode) {
        getEbsApi(region, true, isDebugMode).deleteVolumeInRegion(region, volumeId);
    }

    @Override
    public void detachVolumeInRegion(String region, String volumeId, String instanceId, String device, boolean force,
                                     boolean isDebugMode) {
        DetachVolumeOptions[] detachVolumeOptions = new AmazonVolumeServiceHelper().getDetachVolumeOptions(instanceId, device);
        if (detachVolumeOptions != null && detachVolumeOptions.length > 0) {
            getEbsApi(region, true, isDebugMode).detachVolumeInRegion(region, volumeId, force, detachVolumeOptions);
        } else {
            getEbsApi(region, true, isDebugMode).detachVolumeInRegion(region, volumeId, force);
        }
    }

    void lazyInit(String region, boolean isDebugMode) {
        this.region = InputsUtil.getDefaultStringInput(region, Constants.AwsParams.DEFAULT_AMAZON_REGION);
        init(isDebugMode);
    }

    void init(boolean isDebugMode) {
        ContextBuilder contextBuilder = super.init(region, Constants.Apis.AMAZON_EC2_API, isDebugMode);
        ec2Api = new FiltersHelper().getEC2Api(contextBuilder);
    }

    private ElasticBlockStoreApi getEbsApi(String region, boolean isForRegion, boolean isDebugMode) {
        lazyInit(region, isDebugMode);

        return isForRegion ? ec2Api.getElasticBlockStoreApiForRegion(region).get() : ec2Api.getElasticBlockStoreApi().get();
    }
}