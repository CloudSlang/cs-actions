package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.domain.Location;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.jclouds.ec2.options.RunInstancesOptions;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by persdana on 6/5/2015.
 */
public class ComputeServiceImpl extends JCloudsComputeService implements ComputeService {
    public static final String NOT_IMPLEMENTED_ERROR_MESSAGE = "Not implemented. Use 'amazon\' or 'openstack' " +
            "providers in the provider input";

    org.jclouds.compute.ComputeService computeService = null;

    private String provider;
    protected String region;

    public ComputeServiceImpl(String provider, String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
        this.provider = provider;
    }

    protected void init() {
        ContextBuilder contextBuilder = super.init(region, provider);
        ComputeServiceContext context = contextBuilder.buildView(ComputeServiceContext.class);
        computeService = context.getComputeService();
    }

    void lazyInit() {
        if (computeService == null) {
            this.init();
        }
    }

    void lazyInit(String region) {
        if (this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init();
        } else if (computeService == null) {
            this.init();
        }
    }

    @Override
    public String terminateInstances(String region, String serverId) {
        lazyInit(region);
        computeService.destroyNode(serverId);
        return "Server Removed";
    }

    @Override
    public String startInstances(String region, String serverId) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    @Override
    public String stopInstances(String region, String serverId) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    public void rebootInstances(String region, String serverId) {
        reboot(region, serverId);
    }

    public Set<String> describeRegions() {
        lazyInit();
        Set<? extends Location> locations = computeService.listAssignableLocations();
        Set<String> res = new HashSet<>();
        for (Location l : locations) {
            res.add(l.getDescription());
        }

        return res;
    }

    @Override
    public String updateInstanceType(String region, String serverId, String instanceType, long checkStateTimeout, long polingInterval)
            throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    @Override
    public Reservation<? extends RunningInstance> runInstancesInRegion(String region, String availabilityZone, String imageId,
                                                                       int minCount, int maxCount, RunInstancesOptions... options)
            throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    @Override
    public Set<String> describeInstancesInRegion(CommonInputs commonInputs, InstanceInputs instanceInputs)
            throws Exception {
        throw new Exception(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }

    protected void reboot(String region, String serverId) {
        lazyInit(region);
        computeService.rebootNode(region + "/" + serverId);
    }
}