package io.cloudslang.content.jclouds.services.impl.imagesImpl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.services.ImageService;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.features.AMIApi;
import org.jclouds.ec2.options.CreateImageOptions;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public class AmazonImageService extends JCloudsComputeService implements ImageService {
    private String region;
    private EC2Api ec2Api;

    public AmazonImageService(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
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

    private AMIApi getAMIApi(String region, boolean isForRegion) {
        lazyInit(region);
        return isForRegion ? ec2Api.getAMIApiForRegion(region).get() : ec2Api.getAMIApi().get();
    }

    private void lazyInit(String region) {
        if (this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init();
        } else if (ec2Api == null) {
            this.init();
        }
    }

    private void init() {
        ContextBuilder contextBuilder = super.init(region, Constants.Apis.AMAZON_PROVIDER);
        ec2Api = contextBuilder.buildApi(EC2Api.class);
    }
}