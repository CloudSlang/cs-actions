package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.services.JCloudsService;
import io.cloudslang.content.jclouds.services.SnapshotService;
import io.cloudslang.content.jclouds.services.helpers.FiltersHelper;
import io.cloudslang.content.jclouds.utils.InputsUtil;
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
public class AmazonSnapshotServiceImpl extends JCloudsService implements SnapshotService {
    EC2Api ec2Api = null;

    private String region;

    public AmazonSnapshotServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    @Override
    public Snapshot createSnapshotInRegion(String region, String volumeId, String description, boolean isDebugMode) {
        if (StringUtils.isBlank(description)) {
            return getEbsApi(region, true, isDebugMode).createSnapshotInRegion(region, volumeId);
        }

        CreateSnapshotOptions snapshotOptions = new CreateSnapshotOptions().withDescription(description);

        return getEbsApi(region, true, isDebugMode).createSnapshotInRegion(region, volumeId, snapshotOptions);
    }

    @Override
    public void deleteSnapshotInRegion(String region, String snapshotId, boolean isDebugMode) {
        getEbsApi(region, true, isDebugMode).deleteSnapshotInRegion(region, snapshotId);
    }

    void init(boolean isDebugMode) {
        ContextBuilder contextBuilder = super.init(region, Constants.Apis.AMAZON_EC2_API, isDebugMode);
        ec2Api = new FiltersHelper().getEC2Api(contextBuilder);
    }

    void lazyInit(String region, boolean isDebugMode) {
        this.region = InputsUtil.getDefaultStringInput(region, Constants.AwsParams.DEFAULT_AMAZON_REGION);
        init(isDebugMode);
    }

    private ElasticBlockStoreApi getEbsApi(String region, boolean isForRegion, boolean isDebugMode) {
        lazyInit(region, isDebugMode);

        return isForRegion ? ec2Api.getElasticBlockStoreApiForRegion(region).get() : ec2Api.getElasticBlockStoreApi().get();
    }
}
