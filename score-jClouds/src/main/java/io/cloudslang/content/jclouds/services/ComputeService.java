package io.cloudslang.content.jclouds.services;

import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.jclouds.ec2.options.RunInstancesOptions;

import java.util.Set;

/**
 * Created by persdana on 5/27/2015.
 */
public interface ComputeService {

    String start(String region, String serverId) throws Exception;

    String stop(String region, String serverId) throws Exception;

    void softReboot(String region, String serverId);

    void hardReboot(String region, String serverId) throws Exception;

    String suspend(String region, String serverId) throws Exception;

    void resume(String region, String serverId) throws Exception;

    String removeServer(String region, String serverId);

    Set<String> listRegions();

    Set<String> listNodes(String region);

    Reservation<? extends RunningInstance> runInstancesInRegion(String region, String availabilityZone, String imageId,
                                                                int minCount, int maxCount, RunInstancesOptions... options)
            throws Exception;

    String updateInstanceType(String region, String instanceId, String instanceType, long checkStateTimeout, long polingInterval)
            throws Exception;
}