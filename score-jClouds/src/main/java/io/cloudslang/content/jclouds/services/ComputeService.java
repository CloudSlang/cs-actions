package io.cloudslang.content.jclouds.services;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;

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

    Reservation<? extends RunningInstance> createServer(CommonInputs commonInputs, CustomInputs customInputs) throws Exception;

    String updateInstanceType(CustomInputs customInputs) throws Exception;
}