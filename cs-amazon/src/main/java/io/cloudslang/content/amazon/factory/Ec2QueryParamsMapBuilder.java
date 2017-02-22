/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.factory;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.factory.helpers.*;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.*;
import static io.cloudslang.content.amazon.entities.constants.Constants.ErrorMessages.UNSUPPORTED_QUERY_API;

/**
 * Created by TusaM
 * 11/7/2016.
 */
class Ec2QueryParamsMapBuilder {
    private Ec2QueryParamsMapBuilder() {
        // prevent instantiation
    }

    static Map<String, String> getEc2QueryParamsMap(InputsWrapper wrapper) throws Exception {
        switch (wrapper.getCommonInputs().getAction()) {
            case ALLOCATE_ADDRESS:
                return new ElasticIpUtils().getAllocateAddressQueryParamsMap(wrapper);
            case ASSOCIATE_ADDRESS:
                return new NetworkUtils().getAssociateAddressQueryParamsMap(wrapper);
            case ATTACH_NETWORK_INTERFACE:
                return new NetworkUtils().getAttachNetworkInterfaceQueryParamsMap(wrapper);
            case ATTACH_VOLUME:
                return new VolumeUtils().getAttachVolumeQueryParamsMap(wrapper);
            case CREATE_IMAGE:
                return new ImageUtils().getCreateImageQueryParamsMap(wrapper);
            case CREATE_NETWORK_INTERFACE:
                return new NetworkUtils().getCreateNetworkInterfaceQueryParamsMap(wrapper);
            case CREATE_SNAPSHOT:
                return new SnapshotUtils().getCreateSnapshotQueryParamsMap(wrapper);
            case CREATE_SUBNET:
                return new NetworkUtils().getCreateSubnetQueryParamsMap(wrapper);
            case CREATE_TAGS:
                return new TagUtils().getCreateTagsQueryParamsMap(wrapper);
            case CREATE_VOLUME:
                return new VolumeUtils().getCreateVolumeQueryParamsMap(wrapper);
            case DELETE_NETWORK_INTERFACE:
                return new NetworkUtils().getDeleteNetworkInterfaceQueryParamsMap(wrapper);
            case DELETE_SNAPSHOT:
                return new SnapshotUtils().getDeleteSnapshotQueryParamsMap(wrapper);
            case DELETE_SUBNET:
                return new NetworkUtils().getDeleteSubnetQueryParamsMap(wrapper);
            case DELETE_VOLUME:
                return new VolumeUtils().getDeleteVolumeQueryParamsMap(wrapper);
            case DESCRIBE_AVAILABILITY_ZONES:
                return new RegionUtils().getDescribeAvailabilityZonesQueryParamsMap(wrapper);
            case DESCRIBE_IMAGES:
                return new ImageUtils().getDescribeImagesQueryParamsMap(wrapper);
            case DESCRIBE_IMAGE_ATTRIBUTE:
                return new ImageUtils().getDescribeImageAttributeQueryParamsMap(wrapper);
            case DESCRIBE_INSTANCES:
                return new InstanceUtils().getDescribeInstancesQueryParamsMap(wrapper);
            case DESCRIBE_NETWORK_INTERFACES:
                return new NetworkUtils().getDescribeNetworkInterfacesQueryParamsMap(wrapper);
            case DESCRIBE_REGIONS:
                return new RegionUtils().getDescribeRegionsQueryParamsMap(wrapper);
            case DESCRIBE_TAGS:
                return new TagUtils().getDescribeTagsQueryParamsMap(wrapper);
            case DESCRIBE_VOLUMES:
                return new VolumeUtils().getDescribeVolumesQueryParamsMap(wrapper);
            case DEREGISTER_IMAGE:
                return new ImageUtils().getDeregisterImageQueryParamsMap(wrapper);
            case DETACH_NETWORK_INTERFACE:
                return new NetworkUtils().getDetachNetworkInterfaceQueryParamsMap(wrapper);
            case DETACH_VOLUME:
                return new VolumeUtils().getDetachVolumeQueryParamsMap(wrapper);
            case DISASSOCIATE_ADDRESS:
                return new NetworkUtils().getDisassociateAddressQueryParamsMap(wrapper);
            case MODIFY_IMAGE_ATTRIBUTE:
                return new ImageUtils().getModifyImageAttributeQueryParamsMap(wrapper);
            case MODIFY_INSTANCE_ATTRIBUTE:
                return new InstanceUtils().getModifyInstanceAttributeQueryParamsMap(wrapper);
            case REBOOT_INSTANCES:
                return new InstanceUtils().getRebootInstancesQueryParamsMap(wrapper);
            case RELEASE_ADDRESS:
                return new ElasticIpUtils().getReleaseAddressQueryParamsMap(wrapper);
            case RESET_IMAGE_ATTRIBUTE:
                return new ImageUtils().getResetImageAttributeQueryParamsMap(wrapper);
            case RUN_INSTANCES:
                return new InstanceUtils().getRunInstancesQueryParamsMap(wrapper);
            case START_INSTANCES:
                return new InstanceUtils().getStartInstancesQueryParamsMap(wrapper);
            case STOP_INSTANCES:
                return new InstanceUtils().getStopInstancesQueryParamsMap(wrapper);
            case TERMINATE_INSTANCES:
                return new InstanceUtils().getTerminateInstancesQueryParamsMap(wrapper);
            default:
                throw new RuntimeException(UNSUPPORTED_QUERY_API);
        }
    }
}
