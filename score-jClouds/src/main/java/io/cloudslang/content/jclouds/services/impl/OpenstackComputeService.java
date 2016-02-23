package io.cloudslang.content.jclouds.services.impl;

import com.google.common.base.Optional;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import org.jclouds.ContextBuilder;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.PagedIterable;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.RebootType;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.extensions.ServerAdminApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by persdana on 5/27/2015.
 */
public class OpenstackComputeService extends JCloudsComputeService implements ComputeService {
    private static final String NOT_IMPLEMENTED_ERROR_MESSAGE = "Not implemented. Use 'amazon' provider in provider input.";
    private static final String OPENSTACK_PROVIDER = "openstack-nova";

    protected NovaApi novaApi = null;
    private String region;

    public void setRegion(String region) {
        this.region = region;
    }

    public OpenstackComputeService(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    protected void init() {
        ContextBuilder contextBuilder = super.init(region, OPENSTACK_PROVIDER);

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
        boolean isSuspended = serverAdminApi.suspend(serverId);

        if (isSuspended) {
            return "OpenStack instance is suspending";
        }
        return "Can't suspend instance";
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

    @Override
    public Reservation<? extends RunningInstance> createServer(CommonInputs commonInputs, CustomInputs customInputs) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    public String createServer(String region, String name, String imageRef, String flavorRef) {
        lazyInit(region);
        ServerApi serverApi = novaApi.getServerApi(region);

        ServerCreated serverCreated = serverApi.create(name, imageRef, flavorRef);
        return serverCreated.toString();
    }

}
