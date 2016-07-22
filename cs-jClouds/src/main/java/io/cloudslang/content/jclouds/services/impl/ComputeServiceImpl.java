package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeServiceContext;
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
    private static final String NOT_IMPLEMENTED_ERROR_MESSAGE = "Not implemented. Use 'amazon\' or 'openstack' " +
            "providers in the provider input";

    org.jclouds.compute.ComputeService computeService = null;

    private String provider;
    protected String region;

    public ComputeServiceImpl(String provider, String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
        this.provider = provider;
    }

    protected void init(boolean withExecutionLogs) {
        ContextBuilder contextBuilder = super.init(region, provider, withExecutionLogs);
        ComputeServiceContext context = contextBuilder.buildView(ComputeServiceContext.class);
        computeService = context.getComputeService();
    }

    void lazyInit(boolean withExecutionLogs) {
        if (computeService == null) {
            this.init(withExecutionLogs);
        }
    }

    void lazyInit(String region, boolean withExecutionLogs) {
        if (this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init(withExecutionLogs);
        } else if (computeService == null) {
            this.init(withExecutionLogs);
        }
    }

    @Override
    public String terminateInstances(String region, String serverId, boolean withExecutionLogs) {
        lazyInit(region, withExecutionLogs);
        computeService.destroyNode(serverId);
        return "Server Removed";
    }

    @Override
    public String startInstances(String region, String serverId, boolean withExecutionLogs) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    @Override
    public String stopInstances(String region, String serverId, boolean withExecutionLogs) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    public void rebootInstances(String region, String serverId, boolean withExecutionLogs) {
        reboot(region, serverId, withExecutionLogs);
    }

    public Set<String> describeRegions(boolean withExecutionLogs) {
        lazyInit(withExecutionLogs);
        Set<? extends Location> locations = computeService.listAssignableLocations();
        Set<String> res = new HashSet<>();
        for (Location l : locations) {
            res.add(l.getDescription());
        }

        return res;
    }

    @Override
    public String updateInstanceType(String region, String serverId, String instanceType, long checkStateTimeout,
                                     long polingInterval, boolean withExecutionLogs) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    @Override
    public Reservation<? extends RunningInstance> runInstancesInRegion(String region, String availabilityZone,
                                                                       String imageId, int minCount, int maxCount,
                                                                       boolean withExecutionLogs,
                                                                       RunInstancesOptions... options) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    @Override
    public Set<String> describeInstancesInRegion(CommonInputs commonInputs, InstanceInputs instanceInputs) throws Exception {
        throw new Exception(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }

    protected void reboot(String region, String serverId, boolean withExecutionLogs) {
        lazyInit(region, withExecutionLogs);
        computeService.rebootNode(region + "/" + serverId);
    }
}
