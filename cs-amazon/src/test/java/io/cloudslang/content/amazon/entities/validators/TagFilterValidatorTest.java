/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.amazon.entities.validators;

import io.cloudslang.content.amazon.entities.aws.TagFilter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Tirla Alin
 * 2/16/2017.
 */
public class TagFilterValidatorTest {
    private static final String CUSTOMER_GATEWAY = "customer-gateway";
    private static final String DHCP_OPTIONS = "dhcp-options";
    private static final String IMAGE = "image";
    private static final String INSTANCE = "instance";
    private static final String INTERNET_GATEWAY = "internet-gateway";
    private static final String NETWORK_ACL = "network-acl";
    private static final String NETWORK_INTERFACE = "network-interface";
    private static final String RESERVED_INSTANCES = "reserved-instances";
    private static final String ROUTE_TABLE = "route-table";
    private static final String SECURITY_GROUP = "security-group";
    private static final String SNAPSHOT = "snapshot";
    private static final String SPOT_INSTANCES_REQUEST = "spot-instances-request";
    private static final String SUBNET = "subnet";
    private static final String VOLUME = "volume";
    private static final String VPC = "vpc";
    private static final String VPN_CONNECTION = "vpn-connection";
    private static final String VPN_GATEWAY = "vpn-gateway";
    private static final String WRONG_VALUE = "WRONGy";
    private TagFilterValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new TagFilterValidator();
    }

    @Test
    public void testGoodValues() {
        assertEquals(CUSTOMER_GATEWAY, validator.getFilterValue(TagFilter.RESOURCE_TYPE, CUSTOMER_GATEWAY));
        assertEquals(DHCP_OPTIONS, validator.getFilterValue(TagFilter.RESOURCE_TYPE, DHCP_OPTIONS));
        assertEquals(IMAGE, validator.getFilterValue(TagFilter.RESOURCE_TYPE, IMAGE));
        assertEquals(INSTANCE, validator.getFilterValue(TagFilter.RESOURCE_TYPE, INSTANCE));
        assertEquals(INTERNET_GATEWAY, validator.getFilterValue(TagFilter.RESOURCE_TYPE, INTERNET_GATEWAY));
        assertEquals(NETWORK_ACL, validator.getFilterValue(TagFilter.RESOURCE_TYPE, NETWORK_ACL));
        assertEquals(NETWORK_INTERFACE, validator.getFilterValue(TagFilter.RESOURCE_TYPE, NETWORK_INTERFACE));
        assertEquals(RESERVED_INSTANCES, validator.getFilterValue(TagFilter.RESOURCE_TYPE, RESERVED_INSTANCES));
        assertEquals(ROUTE_TABLE, validator.getFilterValue(TagFilter.RESOURCE_TYPE, ROUTE_TABLE));
        assertEquals(SECURITY_GROUP, validator.getFilterValue(TagFilter.RESOURCE_TYPE, SECURITY_GROUP));
        assertEquals(SNAPSHOT, validator.getFilterValue(TagFilter.RESOURCE_TYPE, SNAPSHOT));
        assertEquals(SPOT_INSTANCES_REQUEST, validator.getFilterValue(TagFilter.RESOURCE_TYPE, SPOT_INSTANCES_REQUEST));
        assertEquals(SUBNET, validator.getFilterValue(TagFilter.RESOURCE_TYPE, SUBNET));
        assertEquals(VOLUME, validator.getFilterValue(TagFilter.RESOURCE_TYPE, VOLUME));
        assertEquals(VPC, validator.getFilterValue(TagFilter.RESOURCE_TYPE, VPC));
        assertEquals(VPN_CONNECTION, validator.getFilterValue(TagFilter.RESOURCE_TYPE, VPN_CONNECTION));
        assertEquals(VPN_GATEWAY, validator.getFilterValue(TagFilter.RESOURCE_TYPE, VPN_GATEWAY));

    }

    @Test(expected = RuntimeException.class)
    public void testBadValues() {
        validator.getFilterValue(TagFilter.RESOURCE_TYPE, WRONG_VALUE);
    }
}
