package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import io.cloudslang.content.jclouds.services.helpers.AmazonComputeServiceHelper;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.domain.InstanceState;
import org.jclouds.ec2.domain.InstanceStateChange;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.jclouds.ec2.features.InstanceApi;
import org.jclouds.ec2.options.RunInstancesOptions;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by persdana on 5/27/2015.
 */
public class AmazonComputeServiceImpl extends JCloudsComputeService implements ComputeService {
    EC2Api ec2Api = null;

    protected String region;

    public AmazonComputeServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    protected void init() {
        ContextBuilder contextBuilder = super.init(region, Constants.Apis.AMAZON_PROVIDER);
        ec2Api = contextBuilder.buildApi(EC2Api.class);
    }

    void lazyInit() {
        if (ec2Api == null) {
            this.init();
        }
    }

    void lazyInit(String region) {
        if (this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init();
        } else if (ec2Api == null) {
            this.init();
        }
    }

    @Override
    public Set<String> listNodes(String region) {
        InstanceApi instanceApi = getEC2InstanceApi(region, false);
        Set<? extends Reservation<? extends RunningInstance>> instancesInRegion = instanceApi.describeInstancesInRegion(region);

        Set<String> nodesList = new HashSet<>();
        for (Reservation<? extends RunningInstance> reservation : instancesInRegion) {
            nodesList.add(reservation.toString());
        }

        return nodesList;
    }

    @Override
    public String start(String region, String serverId) {
        InstanceApi instanceApi = getEC2InstanceApi(region, true);
        Set<? extends InstanceStateChange> instanceChanged = instanceApi.startInstancesInRegion(region, serverId);

        return instanceChanged.toString();
    }

    @Override
    public String stop(String region, String serverId) {
        InstanceApi instanceApi = getEC2InstanceApi(region, true);
        Set<? extends InstanceStateChange> instanceChanged = instanceApi.stopInstancesInRegion(region, false, serverId);

        return instanceChanged.toString();
    }

    @Override
    public String removeServer(String region, String serverId) {
        InstanceApi instanceApi = getEC2InstanceApi(region, true);
        Set<? extends InstanceStateChange> instanceChanged = instanceApi.terminateInstancesInRegion(region, serverId);

        return instanceChanged.toString();
    }

    @Override
    public Set<String> listRegions() {
        lazyInit();
        return ec2Api.getConfiguredRegions();
    }

    @Override
    public Reservation<? extends RunningInstance> runInstancesInRegion(String region, String availabilityZone, String imageId,
                                                                       int minCount, int maxCount, RunInstancesOptions... options)
            throws Exception {
        InstanceApi instanceApi = getEC2InstanceApi(region, false);
        RunInstancesOptions runInstancesOptions = RunInstancesOptions.NONE;

        return instanceApi.runInstancesInRegion(region, availabilityZone, imageId, minCount, maxCount, runInstancesOptions);
    }

    @Override
    public String updateInstanceType(String region, String serverId, String instanceType, long checkStateTimeout, long polingInterval)
            throws Exception {
        InstanceApi instanceApi = getEC2InstanceApi(region, false);

        AmazonComputeServiceHelper helper = new AmazonComputeServiceHelper();
        InstanceState previousState = helper.getInstanceState(instanceApi, region, serverId);
        helper.stopAndWaitToStopInstance(instanceApi, previousState, region, serverId, checkStateTimeout, polingInterval);

        instanceApi.setInstanceTypeForInstanceInRegion(region, serverId, instanceType);

        if (InstanceState.RUNNING.equals(previousState)) {
            Set<? extends InstanceStateChange> instanceChanged = instanceApi.startInstancesInRegion(region, serverId);
            return instanceChanged.toString();
        }

        return Constants.Messages.SERVER_UPDATED;
    }

    @Override
    public void softReboot(String region, String serverId) {
        InstanceApi instanceApi = getEC2InstanceApi(region, true);
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

    private InstanceApi getEC2InstanceApi(String region, boolean isForRegion) {
        lazyInit(region);
        return isForRegion ? ec2Api.getInstanceApiForRegion(region).get() : ec2Api.getInstanceApi().get();
    }
}