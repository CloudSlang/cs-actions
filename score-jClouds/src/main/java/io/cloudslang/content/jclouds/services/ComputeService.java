package io.cloudslang.content.jclouds.services;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.jclouds.ec2.options.RunInstancesOptions;

import java.util.Set;

/**
 * Created by persdana on 5/27/2015.
 */
public interface ComputeService {

    String startInstances(String region, String serverId) throws Exception;

    String stopInstances(String region, String serverId) throws Exception;

    void rebootInstances(String region, String serverId);

    String terminateInstances(String region, String serverId);

    Set<String> describeRegions();

    Set<String> listNodes(String region);

    Set<? extends Reservation<? extends RunningInstance>> describeInstancesInRegion(InstanceInputs instanceInputs) throws Exception;

    Reservation<? extends RunningInstance> runInstancesInRegion(String region, String availabilityZone, String imageId,
                                                                int minCount, int maxCount, RunInstancesOptions... options)
            throws Exception;

    String updateInstanceType(String region, String instanceId, String instanceType, long checkStateTimeout, long polingInterval)
            throws Exception;
}