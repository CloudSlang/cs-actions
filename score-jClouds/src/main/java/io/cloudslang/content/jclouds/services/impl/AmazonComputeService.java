package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CreateServerCustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import io.cloudslang.content.jclouds.services.helpers.AmazonComputeServiceHelper;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.domain.NodeMetadata;
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
public class AmazonComputeService extends JCloudsComputeService implements ComputeService {
    private static final String AMAZON_PROVIDER = "ec2";
    private static final String SERVER_UPDATED = "Server updated successfully.";
    private static final String NOT_IMPLEMENTED_ERROR_MESSAGE = "Not implemented.";

    EC2Api ec2Api = null;

    protected String region;

    public AmazonComputeService(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    protected void init() {
        ContextBuilder contextBuilder = super.init(region, AMAZON_PROVIDER);
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
    public Reservation<? extends RunningInstance> runServer(CommonInputs commonInputs, CustomInputs customInputs) throws Exception {
        InstanceApi instanceApi = getEC2InstanceApi(customInputs.getRegion(), false);
        RunInstancesOptions runInstancesOptions = RunInstancesOptions.NONE;

        return instanceApi.runInstancesInRegion(customInputs.getRegion(), customInputs.getAvailabilityZone(),
                customInputs.getImageRef(), customInputs.getMinCount(), customInputs.getMaxCount(), runInstancesOptions);
    }

    @Override
    public String updateInstanceType(CustomInputs customInputs) throws Exception {
        InstanceApi instanceApi = getEC2InstanceApi(customInputs.getRegion(), false);

        AmazonComputeServiceHelper helper = new AmazonComputeServiceHelper();
        InstanceState previousState = helper.getInstanceState(instanceApi, customInputs);
        helper.stopAndWaitToStopInstance(instanceApi, previousState, customInputs);

        instanceApi.setInstanceTypeForInstanceInRegion(customInputs.getRegion(), customInputs.getServerId(),
                customInputs.getInstanceType());

        if (InstanceState.RUNNING.equals(previousState)) {
            Set<? extends InstanceStateChange> instanceChanged = instanceApi
                    .startInstancesInRegion(customInputs.getRegion(), customInputs.getServerId());
            return instanceChanged.toString();
        }

        return SERVER_UPDATED;
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

    @Override
    public Set<? extends NodeMetadata> createNodesInGroup(CommonInputs commonInputs, CreateServerCustomInputs createServerInputs) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    private InstanceApi getEC2InstanceApi(String region, boolean isForRegion) {
        lazyInit(region);
        return isForRegion ? ec2Api.getInstanceApiForRegion(region).get() : ec2Api.getInstanceApi().get();
    }
}