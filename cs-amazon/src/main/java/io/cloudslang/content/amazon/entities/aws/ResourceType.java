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
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Tirla Alin.
 * 6/7/2016.
 */
public enum ResourceType {
    CUSTOMER_GATEWAY("customer-gateway"),
    DHCP_OPTIONS("dhcp-options"),
    IMAGE("image"),
    INSTANCE("instance"),
    INTERNET_GATEWAY("internet-gateway"),
    NETWORK_ACL("network-acl"),
    NETWORK_INTERFACE("network-interface"),
    RESERVED_INSTANCES("reserved-instances"),
    ROUTE_TABLE("route-table"),
    SECURITY_GROUP("security-group"),
    SNAPSHOT("snapshot"),
    SPOT_INSTANCES_REQUEST("spot-instances-request"),
    SUBNET("subnet"),
    VOLUME("volume"),
    VPC("vpc"),
    VPN_CONNECTION("vpn-connection"),
    VPN_GATEWAY("vpn-gateway");
    

    private static final String UNRECOGNIZED_RESOURCE_TYPE = "Unrecognized resource type value: [%s]. Valid values are: " +
            "customer-gateway, dhcp-options, image, instance, internet-gateway, network-acl, network-interface, " +
            "reserved-instances, route-table, security-group, snapshot, spot-instances-request, subnet, volume, vpc, " +
            "vpn-connection, vpn-gateway";

    private final String value;

    ResourceType(String value) {
        this.value = value;
    }

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (ResourceType member : ResourceType.values()) {
            if (member.value.equalsIgnoreCase(input)) {
                return member.value;
            }
        }

        throw new RuntimeException(format(UNRECOGNIZED_RESOURCE_TYPE, input));
    }
}
