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

public enum NetworkFilter {

    ADDRESSES_PRIVATE_IP_ADDRESS("addresses.private-ip-address"),
    ADDRESSES_PRIMARY("addresses.primary"),
    ADDRESSES_ASSOCIATION_PUBLIC_IP("addresses.association.public-ip"),
    ADDRESSES_ASSOCIATION_OWNER_ID("addresses.association.owner-id"),
    ASSOCIATION_ASSOCIATION_ID("association.association-id"),
    ASSOCIATION_ALLOCATION_ID("association.allocation-id"),
    ASSOCIATION_IP_OWNER_ID("association.ip-owner-id"),
    ASSOCIATION_PUBLIC_IP("association.public-ip"),
    ASSOCIATION_PUBLIC_DNS_NAME("association.public-dns-name"),
    ATTACHMENT_ATTACHMENT_ID("attachment.attachment-id"),
    ATTACHMENT_ATTACH_TIME("attachment.attach.time"),
    ATTACHMENT_DELETE_ON_TERMINATION("attachment.delete-on-termination"),
    ATTACHMENT_DEVICE_INDEX("attachment.device-index"),
    ATTACHMENT_INSTANCE_ID("attachment.instance-id"),
    ATTACHMENT_INSTANCE_OWNER_ID("attachment.instance-owner-id"),
    ATTACHMENT_NAT_GATEWAY_ID("attachment.nat-gateway-id"),
    ATTACHMENT_STATUS("attachment.status"),
    AVAILABILITY_ZONE("availability-zone"),
    DESCRIPTION("description"),
    GROUP_ID("group-id"),
    GROUP_NAME("group-name"),
    IPV6_ADDRESSES_IPV6_ADDRESS("ipv6-addresses.ipv6-address"),
    MACC_ADDRESS("mac-address"),
    NETWORK_INTERFACE_ID("network-interface-id"),
    OWNER_ID("owner-id"),
    PRIVATE_IP_ADDRESS("private-ip-address"),
    PRIVATE_DNS_NAME("private-dns-name"),
    REQUESTER_ID("requester-id"),
    REQUESTER_MANAGED("requester-managed"),
    SOURCE_DESK_CHECK("source-desk-check"),
    STATUS("status"),
    SUBNET_ID("subnet-id"),
    TAG("tag"),
    TAG_KEY("tag-key"),
    TAG_VALUE("tag-value"),
    VPC_ID("vpc-id");

    private final String value;

    NetworkFilter(String value) {
        this.value = value;
    }

    private String getValue() {
        return value;
    }

    public static String getNetworkFilter(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (NetworkFilter instanceFilter : NetworkFilter.values()) {
            if (instanceFilter.getValue().equalsIgnoreCase(input)) {
                return instanceFilter.getValue();
            }
        }

        throw new IllegalArgumentException("Invalid filter value: [" + input + "]. Valid values are: addresses.private-ip-address | " +
                "addresses.primary | addresses.association.public-ip | addresses.association.owner-id | " +
                "association.association-id | association.allocation-id | association.ip-owner-id | association.public-ip | " +
                "association.public-dns-name | attachment.attachment-id | attachment.attach.time | attachment.delete-on-termination | " +
                "attachment.device-index | attachment.instance-id | attachment.instance-owner-id | attachment.nat-gateway-id | " +
                "attachment.status | availability-zone | description | group-id | group-name | ipv6-addresses.ipv6-address | " +
                "mac-address | network-interface-id | owner-id | private-ip-address | private-dns-name | requester-id | " +
                "requester-managed | source-desk-check | status | subnet-id | tag | tag-key | tag-value | vpc-id");
    }
}
