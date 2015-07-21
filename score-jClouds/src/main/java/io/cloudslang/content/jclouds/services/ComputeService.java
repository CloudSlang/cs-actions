package io.cloudslang.content.jclouds.services;

import java.util.Set;

/**
 * Created by persdana on 5/27/2015.
 */
public interface ComputeService {

    public String start(String region, String serverId) throws Exception;

    public String stop(String region, String serverId) throws Exception;

    public void softReboot(String region, String serverId);

    public void hardReboot(String region, String serverId) throws Exception;

    public String suspend(String region, String serverId) throws Exception;

    public void resume(String region, String serverId) throws Exception;

    public String removeServer(String region, String serverId);

    public Set<String> listRegions();

    public Set<String> listNodes(String region);

}
