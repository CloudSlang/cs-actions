/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.aws;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 6/2/2016.
 */
public enum InstanceFilter {
    AFFINITY("affinity"),
    ARCHITECTURE("architecture"),
    ASSOCIATION_PUBLIC_IP("association.public-ip"),
    ASSOCIATION_IP_OWNER_ID("association.ip-owner-id"),
    ASSOCIATION_ALLOCATION_ID("association.allocation-id"),
    ASSOCIATION_ASSOCIATION_ID("association.association-id"),
    AVAILABILITY_ZONE("availability-zone"),
    BLOCK_DEVICE_MAPPING_ATTACH_TIME("block-device-mapping.attach-time"),
    BLOCK_DEVICE_MAPPING_DELETE_ON_TERMINATION("block-device-mapping.delete-on-termination"),
    BLOCK_DEVICE_MAPPING_DEVICE_NAME("block-device-mapping.device-name"),
    BLOCK_DEVICE_MAPPING_STATUS("block-device-mapping.status"),
    BLOCK_DEVICE_MAPPING_VOLUME_ID("block-device-mapping.volume-id"),
    CLIENT_TOKEN("client-token"),
    DNS_NAME("dns-name"),
    GROUP_ID("group-id"),
    GROUP_NAME("group-name"),
    HOST_ID("host-Id"),
    HYPERVISOR("hypervisor"),
    IAM_INSTANCE_PROFILE_ARN("iam-instance-profile.arn"),
    IMAGE_ID("image-id"),
    INSTANCE_ID("instance-id"),
    INSTANCE_LIFECYCLE("instance-lifecycle"),
    INSTANCE_STATE_CODE("instance-state-code"),
    INSTANCE_STATE_NAME("instance-state-name"),
    INSTANCE_TYPE("instance-type"),
    INSTANCE_GROUP_ID("instance.group-id"),
    INSTANCE_GROUP_NAME("instance.group-name"),
    IP_ADDRESS("ip-address"),
    KERNEL_ID("kernel-id"),
    KEY_NAME("key-name"),
    LAUNCH_INDEX("launch-index"),
    LAUNCH_TIME("launch-time"),
    MONITORING_STATE("monitoring-state"),
    NETWORK_ADDRESSES_ASSOCIATION_IP_OWNER_ID("network-interface.addresses.association.ip-owner-id"),
    NETWORK_ADDRESSES_ASSOCIATION_PUBLIC_IP("network-interface.addresses.association.public-ip"),
    NETWORK_ADDRESSES_PRIMARY("network-interface.addresses.primary"),
    NETWORK_ADDRESSES_PRIVATE_IP_ADDRESS("network-interface.addresses.private-ip-address"),
    NETWORK_ATTACHMENT_ATTACH_TIME("network-interface.attachment.attach-time"),
    NETWORK_ATTACHMENT_DELETE_ON_TERMINATION("network-interface.attachment.delete-on-termination"),
    NETWORK_ATTACHMENT_DEVICE_INDEX("network-interface.attachment.device-index"),
    NETWORK_ATTACHMENT_ID("network-interface.attachment.attachment-id"),
    NETWORK_ATTACHMENT_INSTANCE_ID("network-interface.attachment.instance-id"),
    NETWORK_ATTACHMENT_INSTANCE_OWNER_ID("network-interface.attachment.instance-owner-id"),
    NETWORK_ATTACHMENT_STATUS("network-interface.attachment.status"),
    NETWORK_AVAILABILITY_ZONE("network-interface.availability-zone"),
    NETWORK_DESCRIPTION("network-interface.description"),
    NETWORK_GROUP_ID("network-interface.group-id"),
    NETWORK_GROUP_NAME("network-interface.group-name"),
    NETWORK_INTERFACE_ID("network-interface.network-interface-id"),
    NETWORK_MAC_ADDRESS("network-interface.mac-address"),
    NETWORK_OWNER_ID("network-interface.owner-id"),
    NETWORK_PRIVATE_DNS_NAME("network-interface.private-dns-name"),
    NETWORK_REQUESTER_ID("network-interface.requester-id"),
    NETWORK_REQUESTER_MANAGED("network-interface.requester-managed"),
    NETWORK_SOURCE_DEST_CHECK("network-interface.source-dest-check"),
    NETWORK_STATUS("network-interface.status"),
    NETWORK_SUBNET_ID("network-interface.subnet-id"),
    NETWORK_VPC_ID("network-interface.vpc-id"),
    OWNER_ID("owner-id"),
    PLACEMENT_GROUP_NAME("placement-group-name"),
    PLATFORM("platform"),
    PRODUCT_CODE("product-code"),
    PRODUCT_CODE_TYPE("product-code.type"),
    RAMDISK_ID("ramdisk-id"),
    REASON("reason"),
    REQUESTER_ID("requester-id"),
    RESERVATION_ID("reservation-id"),
    ROOT_DEVICE_NAME("root-device-name"),
    ROOT_DEVICE_TYPE("root-device-type"),
    SOURCE_DEST_CHECK("source-dest-check"),
    SPOT_INSTANCE_REQUEST_ID("spot-instance-request-id"),
    STATE_REASON_CODE("state-reason-code"),
    STATE_REASON_MESSAGE("state-reason-message"),
    SUBNET_ID("subnet-id"),
    TAG("tag"),
    TAG_KEY("tag-key"),
    TAG_VALUE("tag-value"),
    TENANCY("tenancy"),
    VIRTUALIZATION_TYPE("virtualization-type"),
    VPC_ID("vpc-id");

    private final String value;

    InstanceFilter(String value) {
        this.value = value;
    }

    private String getValue() {
        return value;
    }

    public static String getInstanceFilter(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (InstanceFilter instanceFilter : InstanceFilter.values()) {
            if (instanceFilter.getValue().equalsIgnoreCase(input)) {
                return instanceFilter.getValue();
            }
        }

        throw new IllegalArgumentException("Invalid filter value: [" + input + "]. Valid values are: affinity | architecture | " +
                "availability-zone | block-device-mapping.attach-time | block-device-mapping.delete-on-termination | " +
                "block-device-mapping.device-name | block-device-mapping.status | block-device-mapping.volume-id | " +
                "client-token | dns-name | group-id | group-name | host-id | hypervisor | iam-instance-profile.arn | " +
                "image-id | instance-id | instance-lifecycle | instance-state-code | instance-state-name | instance-type | " +
                "instance.group-id | instance.group-name | ip-address | kernel-id | key-name | launch-index | launch-time | " +
                "monitoring-state | owner-id | placement-group-name | platform | private-dns-name | private-ip-address | " +
                "product-code | product-code.type | ramdisk-id | reason | requester-id | reservation-id | root-device-name | " +
                "root-device-type | source-dest-check | spot-instance-request-id | state-reason-code | state-reason-message | " +
                "subnet-id | tag | tag-key | tag-value | tenancy | virtualization-type | vpc-id | network-interface.description | " +
                "network-interface.subnet-id | network-interface.vpc-id | network-interface.network-interface-id | " +
                "network-interface.owner-id | network-interface.availability-zone | network-interface.requester-id | " +
                "network-interface.requester-managed | network-interface.status | network-interface.mac-address | " +
                "network-interface.private-dns-name | network-interface.source-dest-check | network-interface.group-id | " +
                "network-interface.group-name | network-interface.attachment.attachment-id | network-interface.attachment.instance-id | " +
                "network-interface.attachment.instance-owner-id | network-interface.addresses.private-ip-address | " +
                "network-interface.attachment.device-index | network-interface.attachment.status | " +
                "network-interface.attachment.attach-time | network-interface.attachment.delete-on-termination | " +
                "network-interface.addresses.primary | network-interface.addresses.association.public-ip | " +
                "network-interface.addresses.association.ip-owner-id | association.public-ip | association.ip-owner-id | " +
                "association.allocation-id | association.association-id");
    }
}