package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/7/2016.
 */
public enum NetworkInterfaceFilters {
    NETWORK_INTERFACE_DESCRIPTION("network-interface.description"),
    NETWORK_INTERFACE_SUBNET_ID("network-interface.subnet-id"),
    NETWORK_INTERFACE_VPC_ID("network-interface.vpc-id"),
    NETWORK_INTERFACE_ID("network-interface.network-interface-id"),
    NETWORK_INTERFACE_OWNER_ID("network-interface.owner-id"),
    NETWORK_INTERFACE_AVAILABILITY_ZONE("network-interface.availability-zone"),
    NETWORK_INTERFACE_REQUESTER_ID("network-interface.requester-id"),
    NETWORK_INTERFACE_REQUESTER_MANAGED("network-interface.requester-managed"),
    NETWORK_INTERFACE_STATUS("network-interface.status"),
    NETWORK_INTERFACE_MAC_ADDRESS("network-interface.mac-address"),
    NETWORK_INTERFACE_PRIVATE_DNS_NAME("network-interface.private-dns-name"),
    NETWORK_INTERFACE_SOURCE_DESTINATION_CHECK("network-interface.source-dest-check"),
    NETWORK_INTERFACE_GROUP_ID("network-interface.group-id"),
    NETWORK_INTERFACE_GROUP_NAME("network-interface.group-name"),
    NETWORK_INTERFACE_ATTACHMENT_ID("network-interface.attachment.attachment-id"),
    NETWORK_INTERFACE_INSTANCE_ID("network-interface.attachment.instance-id"),
    NETWORK_INTERFACE_INSTANCE_OWNER_ID("network-interface.attachment.instance-owner-id"),
    NETWORK_INTERFACE_PRIVATE_IP_ADDRESS("network-interface.addresses.private-ip-address"),
    NETWORK_INTERFACE_DEVICE_INDEX("network-interface.attachment.device-index"),
    NETWORK_INTERFACE_ATTACHMENT_STATUS("network-interface.attachment.status"),
    NETWORK_INTERFACE_ATTACH_TIME("network-interface.attachment.attach-time"),
    NETWORK_INTERFACE_DELETE_ON_TERMINATION("network-interface.attachment.delete-on-termination"),
    NETWORK_INTERFACE_ADDRESSES_PRIMARY("network-interface.addresses.primary"),
    NETWORK_INTERFACE_PUBLIC_IP("network-interface.addresses.association.public-ip"),
    NETWORK_INTERFACE_IP_OWNER_ID("network-interface.addresses.association.ip-owner-id");

    private String value;

    NetworkInterfaceFilters(String input) {
        this.value = input;
    }

    public String getValue() {
        return value;
    }
}
