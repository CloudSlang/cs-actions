package io.cloudslang.content.jclouds.services.impl;

import com.google.common.base.Optional;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.domain.Image;
import org.jclouds.ec2.domain.InstanceStateChange;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.jclouds.ec2.features.AMIApi;
import org.jclouds.ec2.features.InstanceApi;
import org.jclouds.ec2.options.RunInstancesOptions;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by persdana on 5/27/2015.
 */
public class AmazonComputeService extends JCloudsComputeService implements ComputeService {
    private static final String AMAZON_PROVIDER = "ec2";

    protected EC2Api ec2Api = null;
    protected String region;

    public AmazonComputeService(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    protected void init() {
        ContextBuilder contextBuilder = super.init(region, AMAZON_PROVIDER);
        ec2Api = contextBuilder.buildApi(EC2Api.class);
    }

    protected void lazyInit() {
        if (ec2Api == null) {
            this.init();
        }
    }

    protected void lazyInit(String region) {
        if (this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init();
        } else if (ec2Api == null) {
            this.init();
        }
    }

    @Override
    public String start(String region, String serverId) {
        lazyInit(region);
        Optional<? extends InstanceApi> optionalInstanceApi = ec2Api.getInstanceApiForRegion(region);
        InstanceApi instanceApi = optionalInstanceApi.get();

        Set<? extends InstanceStateChange> instanceChanged = instanceApi.startInstancesInRegion(region, serverId);

        return instanceChanged.toString();
    }

    @Override
    public String stop(String region, String serverId) {
        lazyInit(region);
        Optional<? extends InstanceApi> optionalInstanceApi = ec2Api.getInstanceApiForRegion(region);
        InstanceApi instanceApi = optionalInstanceApi.get();

        Set<? extends InstanceStateChange> instaceChanged = instanceApi.stopInstancesInRegion(region, false, serverId);

        return instaceChanged.toString();
    }

    @Override
    public void softReboot(String region, String serverId) {
        lazyInit(region);
        Optional<? extends InstanceApi> optionalInstanceApi = ec2Api.getInstanceApiForRegion(region);
        InstanceApi instanceApi = optionalInstanceApi.get();
        instanceApi.rebootInstancesInRegion(region, serverId);
    }

    @Override
    public void hardReboot(String region, String serverId) throws Exception {
        throw new Exception("Use soft reboot and if a Linux/UNIX instance does not cleanly shut down within four minutes, " +
                "Amazon EC2 will perform a hard reboot\n");
    }

    @Override
    public String suspend(String region, String serverId) throws Exception {
        throw new Exception("Use stop server operation to suspend an amazon Instance");
    }

    @Override
    public void resume(String region, String serverId) throws Exception {
        throw new Exception("Resume is not supported on Amazon. Use start server operation to resume an amazon Instance");
    }

    public String removeServer(String region, String serverId) {
        lazyInit(region);
        Optional<? extends InstanceApi> optionalInstanceApi = ec2Api.getInstanceApiForRegion(region);
        InstanceApi instanceApi = optionalInstanceApi.get();

        Set<? extends InstanceStateChange> instanceChanged = instanceApi.terminateInstancesInRegion(region, serverId);

        return instanceChanged.toString();
    }

    public Set<String> listRegions() {
        lazyInit();
        return ec2Api.getConfiguredRegions();
    }

    public Set<String> listNodes(String region) {
        lazyInit(region);
        Optional<? extends InstanceApi> optionalInstanceApi = ec2Api.getInstanceApi();
        InstanceApi instanceApi = optionalInstanceApi.get();

        Set<? extends Reservation<? extends RunningInstance>> instancesInRegion = instanceApi.describeInstancesInRegion(region);
        Set<String> nodesList = new HashSet<>();
        for (Reservation<? extends RunningInstance> reservation : instancesInRegion) {
            nodesList.add(reservation.toString());
        }

        return nodesList;
    }

    public Set<String> listImagesInRegion(String region) {
        lazyInit(region);
        Optional<? extends AMIApi> amiApi = ec2Api.getAMIApi();

        Set<? extends Image> imagesSet = amiApi.get().describeImagesInRegion(region);

        Set<String> imagesList = new HashSet<>();
        for (Image image : imagesSet) {
            imagesList.add(image.toString());
        }

        return imagesList;
    }

    @Override
    public Reservation<? extends RunningInstance> createServer(CommonInputs commonInputs, CustomInputs customInputs) {
        lazyInit(customInputs.getRegion());
        Optional<? extends InstanceApi> optionalInstanceApi = ec2Api.getInstanceApi();
        InstanceApi instanceApi = optionalInstanceApi.get();

        RunInstancesOptions runInstancesOptions = RunInstancesOptions.NONE;

        return instanceApi.runInstancesInRegion(region, customInputs.getAvailabilityZone(), customInputs.getImageRef(),
                customInputs.getMinCount(), customInputs.getMaxCount(), runInstancesOptions);
    }
}