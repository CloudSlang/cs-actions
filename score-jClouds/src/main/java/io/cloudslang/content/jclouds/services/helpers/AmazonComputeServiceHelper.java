package io.cloudslang.content.jclouds.services.helpers;

import io.cloudslang.content.jclouds.entities.constants.Constants;
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
    public InstanceState getInstanceState(InstanceApi instanceApi, String region, String serverId) throws Exception {
        Set<? extends Reservation<? extends RunningInstance>> reservationSet = instanceApi
                .describeInstancesInRegion(region, serverId);
        if (reservationSet.iterator().hasNext()) {
            Reservation<? extends RunningInstance> reservation = reservationSet.iterator().next();
            if (reservation.iterator().hasNext()) {
                RunningInstance runningInstance = reservation.iterator().next();
                return runningInstance.getInstanceState();
            }
        }
        throw new Exception(Constants.ErrorMessages.SERVER_NOT_FOUND);
    }

    public void stopAndWaitToStopInstance(InstanceApi instanceApi, InstanceState instanceState, String region,
                                          String serverId, long checkStateTimeout, long polingInterval) throws Exception {
        if (!InstanceState.STOPPED.equals(instanceState)) {
            instanceApi.stopInstancesInRegion(region, true, serverId);
            waitLoop(instanceApi, instanceState, region, serverId, checkStateTimeout, polingInterval);
        }
    }

    private void waitLoop(InstanceApi instanceApi, InstanceState instanceState, String region,
                          String serverId, long checkStateTimeout, long polingInterval) throws Exception {
        long waitTime = 0;
        while (!InstanceState.STOPPED.equals(instanceState) && waitTime <= checkStateTimeout) {
            Thread.sleep(polingInterval);
            waitTime += 4000;
            instanceState = getInstanceState(instanceApi, region, serverId);
        }
    }
}