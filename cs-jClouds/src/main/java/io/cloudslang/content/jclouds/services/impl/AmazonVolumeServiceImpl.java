package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import io.cloudslang.content.jclouds.services.VolumeService;
import io.cloudslang.content.jclouds.services.helpers.AmazonVolumeServiceHelper;
import io.cloudslang.content.jclouds.services.helpers.Utils;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.domain.Attachment;
import org.jclouds.ec2.domain.Volume;
import org.jclouds.ec2.features.ElasticBlockStoreApi;
import org.jclouds.ec2.options.CreateVolumeOptions;
import org.jclouds.ec2.options.DetachVolumeOptions;

/**
 * Created by Mihai Tusa.
 * 6/16/2016.
 */
public class AmazonVolumeServiceImpl extends JCloudsComputeService implements VolumeService {
    EC2Api ec2Api = null;

    private String region = Constants.Miscellaneous.EMPTY;

    public AmazonVolumeServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    @Override
    public Volume createVolumeInAvailabilityZone(String region, String availabilityZone, String snapshotId, String volumeType,
                                                 String size, String iops, boolean encrypted) {
        if (StringUtils.isBlank(snapshotId) && StringUtils.isBlank(volumeType)) {
            int validSize = new AmazonVolumeServiceHelper()
                    .getSize(Constants.Miscellaneous.STANDARD, Constants.ValidationValues.ONE,
                            Constants.ValidationValues.THOUSAND_AND_TWENTY_FOUR, size);
            return getEbsApi(region, true).createVolumeInAvailabilityZone(availabilityZone, validSize);
        }

        CreateVolumeOptions createVolumeOptions = new AmazonVolumeServiceHelper()
                .getCreateVolumeOptions(snapshotId, volumeType, size, iops, encrypted);

        return getEbsApi(region, true).createVolumeInAvailabilityZone(availabilityZone, createVolumeOptions);
    }

    @Override
    public void deleteVolumeInRegion(String region, String volumeId) {
        getEbsApi(region, true).deleteVolumeInRegion(region, volumeId);
    }

    @Override
    public Attachment attachVolumeInRegion(String region, String volumeId, String instanceId, String device) {
        return getEbsApi(region, true).attachVolumeInRegion(region, volumeId, instanceId, device);
    }

    @Override
    public void detachVolumeInRegion(String region, String volumeId, String instanceId, String device, boolean force) {
        DetachVolumeOptions[] detachVolumeOptions = new AmazonVolumeServiceHelper().getDetachVolumeOptions(instanceId, device);
        if (detachVolumeOptions != null && detachVolumeOptions.length > 0) {
            getEbsApi(region, true).detachVolumeInRegion(region, volumeId, force, detachVolumeOptions);
        } else {
            getEbsApi(region, true).detachVolumeInRegion(region, volumeId, force);
        }
    }

    void lazyInit(String region) {
        this.region = InputsUtil.getAmazonRegion(region);
        init();
    }

    void init() {
        ContextBuilder contextBuilder = super.init(region, Constants.Apis.AMAZON_EC2_API);
        ec2Api = new Utils().getEC2Api(contextBuilder);
    }

    private ElasticBlockStoreApi getEbsApi(String region, boolean isForRegion) {
        lazyInit(region);

        return isForRegion ? ec2Api.getElasticBlockStoreApiForRegion(region).get() : ec2Api.getElasticBlockStoreApi().get();
    }
}