package io.cloudslang.content.jclouds.services.helpers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.cloudslang.content.jclouds.entities.InstanceFilters;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import org.apache.commons.lang3.StringUtils;
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

    public Multimap<String, String> getInstanceFilterMap(InstanceInputs instanceInputs) {
        Multimap<String, String> filtersMap = ArrayListMultimap.create();

        if (!Constants.Miscellaneous.EMPTY.equalsIgnoreCase(instanceInputs.getDeleteOnTermination())) {
            updateFiltersMap(filtersMap, InstanceFilters.BLOCK_DEVICE_MAPPING_DELETE_ON_TERMINATION.getValue(),
                    instanceInputs.getDeleteOnTermination());
        }

        updateFiltersMap(filtersMap, InstanceFilters.AFFINITY.getValue(), instanceInputs.getAffinity());
        updateFiltersMap(filtersMap, InstanceFilters.ARCHITECTURE.getValue(), instanceInputs.getArchitecture());
        updateFiltersMap(filtersMap, InstanceFilters.AVAILABILITY_ZONE.getValue(), instanceInputs.getAvailabilityZone());
        updateFiltersMap(filtersMap, InstanceFilters.BLOCK_DEVICE_MAPPING_ATTACH_TIME.getValue(), instanceInputs.getAttachTime());
        updateFiltersMap(filtersMap, InstanceFilters.BLOCK_DEVICE_MAPPING_DEVICE_NAME.getValue(), instanceInputs.getDeviceName());
        updateFiltersMap(filtersMap, InstanceFilters.BLOCK_DEVICE_MAPPING_STATUS.getValue(), instanceInputs.getStatus());
        updateFiltersMap(filtersMap, InstanceFilters.BLOCK_DEVICE_MAPPING_VOLUME_ID.getValue(),
                instanceInputs.getCustomInputs().getVolumeId());
        updateFiltersMap(filtersMap, InstanceFilters.CLIENT_TOKEN.getValue(), instanceInputs.getClientToken());
        updateFiltersMap(filtersMap, InstanceFilters.DNS_NAME.getValue(), instanceInputs.getDnsName());
        updateFiltersMap(filtersMap, InstanceFilters.GROUP_ID.getValue(), instanceInputs.getCustomInputs().getGroupId());
        updateFiltersMap(filtersMap, InstanceFilters.GROUP_NAME.getValue(), instanceInputs.getGroupName());



        return filtersMap;
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

    private void updateFiltersMap(Multimap<String, String> map, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            map.put(key, value);
        }
    }
}