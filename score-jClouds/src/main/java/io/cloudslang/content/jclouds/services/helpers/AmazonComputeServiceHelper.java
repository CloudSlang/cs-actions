package io.cloudslang.content.jclouds.services.helpers;

import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import org.jclouds.ec2.domain.InstanceState;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.jclouds.ec2.features.InstanceApi;

import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 2/23/2016.
 */
public class AmazonComputeServiceHelper {
    private static final String SERVER_NOT_FOUND = "Server not found.";

    public InstanceState getInstanceState(InstanceApi instanceApi, CustomInputs customInputs) throws Exception {
        Set<? extends Reservation<? extends RunningInstance>> reservationSet = instanceApi
                .describeInstancesInRegion(customInputs.getRegion(), customInputs.getServerId());
        if (reservationSet.iterator().hasNext()) {
            Reservation<? extends RunningInstance> reservation = reservationSet.iterator().next();
            if (reservation.iterator().hasNext()) {
                RunningInstance runningInstance = reservation.iterator().next();
                return runningInstance.getInstanceState();
            }
        }
        throw new Exception(SERVER_NOT_FOUND);
    }

    public void stopAndWaitToStopInstance(InstanceApi instanceApi, InstanceState instanceState, CustomInputs customInputs)
            throws Exception {
        if (!InstanceState.STOPPED.equals(instanceState)) {
            instanceApi.stopInstancesInRegion(customInputs.getRegion(), true, customInputs.getServerId());
            waitLoop(instanceApi, instanceState, customInputs);
        }
    }

    private void waitLoop(InstanceApi instanceApi, InstanceState instanceState, CustomInputs customInputs)
            throws Exception {
        long waitTime = 0;
        while (!InstanceState.STOPPED.equals(instanceState) && waitTime <= customInputs.getCheckStateTimeout()) {
            Thread.sleep(customInputs.getPolingInterval());
            waitTime += 4000;
            instanceState = getInstanceState(instanceApi, customInputs);
        }
    }
}