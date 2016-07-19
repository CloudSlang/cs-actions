package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.jclouds.ec2.options.RunInstancesOptions;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.RebootType;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;

import java.util.Set;

/**
 * Created by persdana on 5/27/2015.
 */
public class OpenstackComputeServiceImpl extends JCloudsComputeService implements ComputeService {
    NovaApi novaApi = null;

    private static final String OPENSTACK_NOVA = "openstack-nova";
    private String region;

    public void setRegion(String region) {
        this.region = region;
    }

    public OpenstackComputeServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    protected void init() {
        ContextBuilder contextBuilder = super.init(region, OPENSTACK_NOVA);
        novaApi = contextBuilder.buildApi(NovaApi.class);
    }

    void lazyInit() {
        if (novaApi == null) {
            this.init();
        }
    }

    void lazyInit(String region) {
        if (this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init();
        } else if (novaApi == null) {
            this.init();
        }
    }

    @Override
    public String startInstances(String region, String serverId) {
        lazyInit(region);

        ServerApi serverApi = novaApi.getServerApi(region);

        serverApi.start(serverId);

        return "Server is Starting";
    }

    @Override
    public String stopInstances(String region, String serverId) {
        lazyInit(region);
        ServerApi serverApi = novaApi.getServerApi(region);
        serverApi.stop(serverId);

        return "The server is stopping";
    }

    @Override
    public void rebootInstances(String region, String serverId) {
        lazyInit(region);
        ServerApi serverApi = novaApi.getServerApi(region);
        serverApi.reboot(serverId, RebootType.SOFT);
    }

    @Override
    public String terminateInstances(String region, String serverId) {
        lazyInit(region);
        ServerApi serverApi = novaApi.getServerApi(region);
        serverApi.delete(serverId);

        return "Server deleted";
    }

    @Override
    public Set<String> describeRegions() {
        lazyInit();
        return novaApi.getConfiguredRegions();
    }

    @Override
    public Reservation<? extends RunningInstance> runInstancesInRegion(String region, String availabilityZone, String imageId,
                                                                       int minCount, int maxCount, RunInstancesOptions... options)
            throws Exception {
        throw new Exception(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }

    @Override
    public String updateInstanceType(String region, String serverId, String instanceType, long checkStateTimeout, long polingInterval)
            throws Exception {
        throw new Exception(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }

    @Override
    public Set<String> describeInstancesInRegion(CommonInputs commonInputs, InstanceInputs instanceInputs)
            throws Exception {
        throw new Exception(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }

    String createServer(String region, String name, String imageRef, String flavorRef) {
        lazyInit(region);
        ServerApi serverApi = novaApi.getServerApi(region);

        ServerCreated serverCreated = serverApi.create(name, imageRef, flavorRef);
        return serverCreated.toString();
    }
}
