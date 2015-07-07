package io.cloudslang.content.jclouds.services.impl;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import io.cloudslang.content.jclouds.services.ComputeService;
import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.domain.Location;
import org.jclouds.location.reference.LocationConstants;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by persdana on 6/5/2015.
 */
public class ComputeServiceImpl implements ComputeService{
    private static final String NOT_IMPLEMENTED_ERROR_MESSAGE = "Not implemented. Use 'amazon\' or 'openstack' providers in the provider input";

    org.jclouds.compute.ComputeService computeService = null;

    private String provider;
    private String endpoint;
    private String identity;
    private String credential;
    private String proxyHost;
    private String proxyPort;
    protected String region;

    public ComputeServiceImpl(String provider, String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        this.provider = provider;
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

        ContextBuilder contextBuilder = ContextBuilder.newBuilder(provider)
                .endpoint(endpoint)
                .credentials(identity, credential)
                .overrides(overrides)
                .modules(modules);

        ComputeServiceContext context = contextBuilder.buildView(ComputeServiceContext.class);
        computeService = context.getComputeService();
    }

    protected void lazyInit() {
        if(computeService == null) {
            this.init();
        }
    }

    protected void lazyInit(String region) {
        if(this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init();
        } else if(computeService == null) {
            this.init();
        }
    }

    @Override
    public void resume(String region, String serverId) {
        lazyInit(region);
        computeService.resumeNode(region + "/" + serverId);
    }

    @Override
    public String removeServer(String region, String serverId) {
        lazyInit(region);
        computeService.destroyNode(serverId);
        return "Server Removed";
    }

    @Override
    public String suspend(String region, String serverId) {
        lazyInit(region);
        computeService.suspendNode(region + "/" + serverId);

        return "";
    }


    protected void reboot(String region, String serverId) {
        lazyInit(region);
        computeService.rebootNode(region + "/" + serverId);
    }

    @Override
    public String start(String region, String serverId) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    @Override
    public String stop(String region, String serverId) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    public void softReboot(String region, String serverId) {
        reboot(region, serverId);
    }

    public void hardReboot(String region, String serverId) {
        reboot(region, serverId);
    }

    public Set<String> listRegions() {
        lazyInit();
        Set<? extends Location> locations = computeService.listAssignableLocations();
        Set<String> res = new HashSet<>();
        for(Location l : locations) {
            res.add(l.getDescription());
        }

        return res;
    }

    @Override
    public Set<String> listNodes(String region) {
        lazyInit(region);
        Set<? extends ComputeMetadata> nodes = computeService.listNodes();
        Set<String> result = new HashSet<>();
        for(ComputeMetadata cm: nodes) {
            result.add(cm.toString());
        }
        return result;
    }

    public Set<String> listNodes() {
        lazyInit();
        Set<? extends ComputeMetadata> locations = computeService.listNodes();
        Set<String> res = new HashSet<>();
        for(ComputeMetadata cm : locations) {
            res.add(cm.toString());
        }
        return res;
    }

    public String createServer(String region, String name, String imageRef, String flavorRef) throws Exception {

        throw new Exception("not implemented yet");
//        String res = null;
//        lazyInit(region);
//
//        Template template = computeService.templateBuilder().build();
//
//        template.getOptions().as(EC2TemplateOptions.class)
//                .authorizePublicKey("aaa");
//
//        computeService.createNodesInGroup(region, 1);
//
//        return res;
    }
}
