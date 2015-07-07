package io.cloudslang.content.jclouds.services.impl;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import io.cloudslang.content.jclouds.services.ComputeService;
import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.PagedIterable;
import org.jclouds.location.reference.LocationConstants;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.RebootType;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.extensions.ServerAdminApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by persdana on 5/27/2015.
 */
public class OpenstackComputeService implements ComputeService {
    private static final String OPENSTACK_PROVIDER = "openstack-nova";

    protected NovaApi novaApi = null;

    private String endpoint;
    private String identity;
    private String credential;
    private String proxyHost;
    private String proxyPort;
    protected String region;
    private SLF4JLoggingModule loggingModule;

    public OpenstackComputeService(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        this.endpoint = endpoint;
        this.identity = identity;
        this.credential = credential;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    protected void init() {
        loggingModule =  new SLF4JLoggingModule();
        Iterable<Module> modules = ImmutableSet.<Module>of(loggingModule);

        Properties overrides = new Properties();
        if (proxyHost != null && !proxyHost.isEmpty()) {
            overrides.setProperty(Constants.PROPERTY_PROXY_HOST, proxyHost);
            overrides.setProperty(Constants.PROPERTY_PROXY_PORT, proxyPort);
        }
        if(region != null && !region.isEmpty()) {
            overrides.setProperty(LocationConstants.PROPERTY_REGIONS, region);
        }

        ContextBuilder contextBuilder = ContextBuilder.newBuilder(OPENSTACK_PROVIDER)
                .endpoint(endpoint)
                .credentials(identity, credential)
                .overrides(overrides)
                .modules(modules);

        novaApi = contextBuilder
                .buildApi(NovaApi.class);
    }

    protected void lazyInit() {
        if(null == novaApi) {
            this.init();
        }
    }

    protected void lazyInit(String region) {
        if(this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init();
        } else if(novaApi == null) {
            this.init();
        }
    }

    @Override
    public String start(String region, String serverId) {
        lazyInit(region);

        ServerApi serverApi = novaApi.getServerApi(region);

        serverApi.start(serverId);

        return "Server is Starting";
    }

    @Override
    public String stop(String region, String serverId) {
        lazyInit(region);
        ServerApi serverApi = novaApi.getServerApi(region);
        serverApi.stop(serverId);

        return "The server is stopping";
    }

    @Override
    public void softReboot(String region, String serverId) {
        lazyInit(region);
        ServerApi serverApi = novaApi.getServerApi(region);
        serverApi.reboot(serverId, RebootType.SOFT);
    }

    @Override
    public void hardReboot(String region, String serverId) {
        lazyInit(region);
        ServerApi serverApi = novaApi.getServerApi(region);
        serverApi.reboot(serverId, RebootType.HARD);
    }

    @Override
    public String suspend(String region, String serverId) {
        lazyInit(region);
        Optional<ServerAdminApi> optionalServerAdminApi = novaApi.getServerAdminApi(region);
        ServerAdminApi serverAdminApi = optionalServerAdminApi.get();
        Boolean b = serverAdminApi.suspend(serverId);

        if(b)
            return "Openstack instance is suspending";
        else
            return "Something went wrong";
    }

    @Override
    public void resume(String region, String serverId) {
        lazyInit(region);
        Optional<ServerAdminApi> optionalServerAdminApi = novaApi.getServerAdminApi(region);
        ServerAdminApi serverAdminApi = optionalServerAdminApi.get();
        serverAdminApi.resume(serverId);
    }

    @Override
    public String removeServer(String region, String serverId) {
        lazyInit(region);
        ServerApi serverApi = novaApi.getServerApi(region);
        serverApi.delete(serverId);

        return "Server deleted";
    }

    @Override
    public Set<String> listRegions() {
        lazyInit();
        return novaApi.getConfiguredRegions();
    }

    @Override
    public Set<String> listNodes(String region) {
        lazyInit(region);
        ServerApi serverApi = novaApi.getServerApi(region);
        PagedIterable<Server> servers = serverApi.listInDetail();
        Set<String> res = new HashSet<>();

        for(IterableWithMarker<Server> iterableWithMarker : servers) {
            for(Server s : iterableWithMarker) {
                res.add(s.toString());
            }
        }
        return res;
    }

    public String createServer(String region, String name, String imageRef, String flavorRef) {
        lazyInit(region);
        ServerApi serverApi = novaApi.getServerApi(region);

        ServerCreated serverCreated = serverApi.create(name, imageRef, flavorRef);
        return serverCreated.toString();
    }

}
