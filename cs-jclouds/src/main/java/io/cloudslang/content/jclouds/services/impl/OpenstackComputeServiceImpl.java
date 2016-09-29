package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.JCloudsService;
import org.jclouds.ContextBuilder;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;

import java.util.Set;

/**
 * Created by persdana on 5/27/2015.
 */
public class OpenstackComputeServiceImpl extends JCloudsService implements ComputeService {
    private static final String OPENSTACK_NOVA = "openstack-nova";

    private String region;

    NovaApi novaApi = null;

    public void setRegion(String region) {
        this.region = region;
    }

    public OpenstackComputeServiceImpl(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    protected void init(boolean isDebugMode) {
        ContextBuilder contextBuilder = super.init(region, OPENSTACK_NOVA, isDebugMode);
        novaApi = contextBuilder.buildApi(NovaApi.class);
    }

    void lazyInit(boolean isDebugMode) {
        if (novaApi == null) {
            this.init(isDebugMode);
        }
    }

    void lazyInit(String region, boolean isDebugMode) {
        if (this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init(isDebugMode);
        } else if (novaApi == null) {
            this.init(isDebugMode);
        }
    }

    @Override
    public Set<String> describeRegions(boolean isDebugMode) {
        lazyInit(isDebugMode);
        return novaApi.getConfiguredRegions();
    }

    @Override
    public String updateInstanceType(String region, String serverId, String instanceType, long checkStateTimeout,
                                     long polingInterval, boolean isDebugMode) throws Exception {
        throw new Exception(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }

    @Override
    public Set<String> describeInstancesInRegion(CommonInputs commonInputs, InstanceInputs instanceInputs) throws Exception {
        throw new Exception(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }

    String createServer(String region, String name, String imageRef, String flavorRef, boolean isDebugMode) {
        lazyInit(region, isDebugMode);
        ServerApi serverApi = novaApi.getServerApi(region);

        ServerCreated serverCreated = serverApi.create(name, imageRef, flavorRef);
        return serverCreated.toString();
    }
}