package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.JCloudsService;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeServiceContext;

import java.util.Set;

/**
 * Created by persdana on 6/5/2015.
 */
public class ComputeServiceImpl extends JCloudsService implements ComputeService {
    org.jclouds.compute.ComputeService computeService = null;

    private String provider;
    protected String region;

    public ComputeServiceImpl(String provider, String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
        this.provider = provider;
    }

    protected void init(boolean isDebugMode) {
        ContextBuilder contextBuilder = super.init(region, provider, isDebugMode);
        ComputeServiceContext context = contextBuilder.buildView(ComputeServiceContext.class);
        computeService = context.getComputeService();
    }

    void lazyInit(boolean isDebugMode) {
        if (computeService == null) {
            this.init(isDebugMode);
        }
    }

    void lazyInit(String region, boolean isDebugMode) {
        if (this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init(isDebugMode);
        } else if (computeService == null) {
            this.init(isDebugMode);
        }
    }

    @Override
    public Set<String> describeInstancesInRegion(CommonInputs commonInputs, InstanceInputs instanceInputs) throws Exception {
        throw new Exception(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }
}