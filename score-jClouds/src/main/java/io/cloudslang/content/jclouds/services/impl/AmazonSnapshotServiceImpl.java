package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import io.cloudslang.content.jclouds.services.SnapshotService;
import io.cloudslang.content.jclouds.services.helpers.Utils;
import org.apache.commons.lang3.StringUtils;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.domain.Snapshot;
import org.jclouds.ec2.features.ElasticBlockStoreApi;
import org.jclouds.ec2.options.CreateSnapshotOptions;

/**
 * Created by Mihai Tusa.
 * 6/27/2016.
 */
public class AmazonSnapshotServiceImpl extends JCloudsComputeService implements SnapshotService {
    EC2Api ec2Api = null;

    private String region;

    public AmazonSnapshotServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    @Override
    public Snapshot createSnapshotInRegion(String region, String volumeId, String description) {
        if (StringUtils.isBlank(description)) {
            return getEbsApi(region, true).createSnapshotInRegion(region, volumeId);
        }

        CreateSnapshotOptions snapshotOptions = new CreateSnapshotOptions().withDescription(description);

        return getEbsApi(region, true).createSnapshotInRegion(region, volumeId, snapshotOptions);
    }

    @Override
    public void deleteSnapshotInRegion(String region, String snapshotId){
        getEbsApi(region, true).deleteSnapshotInRegion(region, snapshotId);
    }

    void init() {
        ContextBuilder contextBuilder = super.init(region, Constants.Apis.AMAZON_PROVIDER);
        ec2Api = new Utils().getApi(contextBuilder, EC2Api.class);
    }

    void lazyInit(String region) {
        if (this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init();
        } else if (ec2Api == null) {
            this.init();
        }
    }

    private ElasticBlockStoreApi getEbsApi(String region, boolean isForRegion) {
        lazyInit(region);

        return isForRegion ? ec2Api.getElasticBlockStoreApiForRegion(region).get() : ec2Api.getElasticBlockStoreApi().get();
    }
}
