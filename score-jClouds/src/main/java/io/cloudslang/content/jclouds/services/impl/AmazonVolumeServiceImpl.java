package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import io.cloudslang.content.jclouds.services.VolumeService;
import io.cloudslang.content.jclouds.services.helpers.AmazonVolumeServiceHelper;
import io.cloudslang.content.jclouds.services.helpers.Utils;
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

    private String region;

    public AmazonVolumeServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    @Override
    public Attachment attachVolumeInRegion(String region, String volumeId, String instanceId, String device) {
        ElasticBlockStoreApi ebsApi = getEbsApi(region, true);

        return ebsApi.attachVolumeInRegion(region, volumeId, instanceId, device);
    }

    @Override
    public void detachVolumeInRegion(String region, String volumeId, String instanceId, String device, boolean force) {
        ElasticBlockStoreApi ebsApi = getEbsApi(region, true);

        DetachVolumeOptions detachVolumeOptions = new AmazonVolumeServiceHelper().getDetachVolumeOptions(instanceId, device);

        ebsApi.detachVolumeInRegion(region, volumeId, force, detachVolumeOptions);
    }

    @Override
    public Volume createVolumeInAvailabilityZone(String availabilityZone, String snapshotId, String volumeType,
                                                 int size, int iops, boolean encrypted) {
        ElasticBlockStoreApi ebsApi = getEbsApi(region, true);

        CreateVolumeOptions createVolumeOptions = new AmazonVolumeServiceHelper()
                .getCreateVolumeOptions(snapshotId, volumeType, size, iops, encrypted);

        return ebsApi.createVolumeInAvailabilityZone(availabilityZone, createVolumeOptions);
    }

    void init() {
        ContextBuilder contextBuilder = super.init(region, Constants.Apis.AMAZON_PROVIDER);
        ec2Api = new Utils().getApi(contextBuilder, EC2Api.class);
    }

    private ElasticBlockStoreApi getEbsApi(String region, boolean isForRegion) {
        lazyInit(region);

        return isForRegion ? ec2Api.getElasticBlockStoreApiForRegion(region).get() : ec2Api.getElasticBlockStoreApi().get();
    }

    private void lazyInit(String region) {
        if (this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init();
        } else if (ec2Api == null) {
            this.init();
        }
    }
}