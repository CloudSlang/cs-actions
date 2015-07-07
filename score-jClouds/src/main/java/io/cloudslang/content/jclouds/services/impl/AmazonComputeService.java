package io.cloudslang.content.jclouds.services.impl;


import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import io.cloudslang.content.jclouds.services.ComputeService;
import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.domain.Image;
import org.jclouds.ec2.domain.InstanceStateChange;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.jclouds.ec2.features.AMIApi;
import org.jclouds.ec2.features.InstanceApi;
import org.jclouds.ec2.options.RunInstancesOptions;
import org.jclouds.location.reference.LocationConstants;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by persdana on 5/27/2015.
 */
public class AmazonComputeService implements ComputeService {
    private static final String AMAZON_PROVIDER = "ec2";

    protected EC2Api ec2Api = null;

    private String endpoint;
    private String identity;
    private String credential;
    private String proxyHost;
    private String proxyPort;
    protected String region;

    public AmazonComputeService(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        this.endpoint = endpoint;
        this.identity = identity;
        this.credential = credential;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    protected void init() {
        Iterable<Module> modules = ImmutableSet.<Module>of(new SLF4JLoggingModule());

        Properties overrides = new Properties();
        if (proxyHost != null && !proxyHost.isEmpty()) {
            overrides.setProperty(Constants.PROPERTY_PROXY_HOST, proxyHost);
            overrides.setProperty(Constants.PROPERTY_PROXY_PORT, proxyPort);
        }
        if(region != null && !region.isEmpty()) {
            overrides.setProperty(LocationConstants.PROPERTY_REGIONS, region);
        }

        ContextBuilder contextBuilder = ContextBuilder.newBuilder(AMAZON_PROVIDER)
                .endpoint(endpoint)
                .credentials(identity, credential)
                .overrides(overrides)
                .modules(modules);
        ec2Api = contextBuilder.buildApi(EC2Api.class);

    }

    protected void lazyInit() {
        if(ec2Api == null) {
            this.init();
        }
    }

    protected void lazyInit(String region) {
        if(this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init();
        } else if(ec2Api == null) {
            this.init();
        }
    }


    @Override
    public String start(String region, String serverId) {
        lazyInit(region);
        Optional<? extends InstanceApi> optionalInstanceApi = ec2Api.getInstanceApiForRegion(region);

        InstanceApi instanceApi = optionalInstanceApi.get();

        Set<? extends InstanceStateChange> instaceChanged = instanceApi.startInstancesInRegion(region, serverId);

        return instaceChanged.toString();
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
        throw new Exception("Use soft reboot and if a Linux/UNIX instance does not cleanly shut down within four minutes, Amazon EC2 will perform a hard reboot\n");
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
        java.util.Set<? extends org.jclouds.ec2.domain.Reservation<? extends org.jclouds.ec2.domain.RunningInstance>> instancesInRegion = instanceApi.describeInstancesInRegion(region);
        Set<String> res = new HashSet<>();
        for(org.jclouds.ec2.domain.Reservation<? extends org.jclouds.ec2.domain.RunningInstance> reservation : instancesInRegion) {
            res.add(reservation.toString());
        }
        return res;
    }

    public String createServer(String region, String name, String imageRef, String flavorRef) {
        lazyInit(region);
        Optional<? extends InstanceApi> optionalInstanceApi = ec2Api.getInstanceApiForRegion(region);
        InstanceApi instanceApi = optionalInstanceApi.get();

        RunInstancesOptions runInstancesOptions = RunInstancesOptions.NONE;

        Reservation<? extends RunningInstance> reservation = instanceApi.runInstancesInRegion(region, null, imageRef, 1, 1, runInstancesOptions);
        return reservation.toString();
    }

    public Set<String> listImagesInRegion(String region) {
        lazyInit(region);
        Optional<? extends AMIApi> amiApi = ec2Api.getAMIApi();
        Set<? extends Image> imagesSet = amiApi.get().describeImagesInRegion(region);

        Set<String> result = new HashSet<>();

        for(Image i : imagesSet) {
            result.add(i.toString());
        }
        return result;
    }

}
