package io.cloudslang.content.jclouds.entities.aws;

/**
 * Created by Mihai Tusa.
 * 6/7/2016.
 */
public enum NetworkInterfaceFilter {
    DESCRIPTION("network-interface.description"),
    SUBNET_ID("network-interface.subnet-id"),
    VPC_ID("network-interface.vpc-id"),
    INTERFACE_ID("network-interface.network-interface-id"),
    OWNER_ID("network-interface.owner-id"),
    AVAILABILITY_ZONE("network-interface.availability-zone"),
    REQUESTER_ID("network-interface.requester-id"),
    REQUESTER_MANAGED("network-interface.requester-managed"),
    STATUS("network-interface.status"),
    MAC_ADDRESS("network-interface.mac-address"),
    PRIVATE_DNS_NAME("network-interface.private-dns-name"),
    SOURCE_DEST_CHECK("network-interface.source-dest-check"),
    GROUP_ID("network-interface.group-id"),
    GROUP_NAME("network-interface.group-name"),

    ATTACHMENT_ATTACHMENT_ID("network-interface.attachment.attachment-id"),
    ATTACHMENT_INSTANCE_ID("network-interface.attachment.instance-id"),
    ATTACHMENT_INSTANCE_OWNER_ID("network-interface.attachment.instance-owner-id"),
    ATTACHMENT_DEVICE_INDEX("network-interface.attachment.device-index"),
    ATTACHMENT_STATUS("network-interface.attachment.status"),
    ATTACHMENT_ATTACH_TIME("network-interface.attachment.attach-time"),
    ATTACHMENT_DELETE_ON_TERMINATION("network-interface.attachment.delete-on-termination"),

    ADDRESSES_PRIMARY("network-interface.addresses.primary"),
    ADDRESSES_PRIVATE_IP_ADDRESS("network-interface.addresses.private-ip-address"),
    ADDRESSES_ASSOCIATION_PUBLIC_IP("network-interface.addresses.association.public-ip"),
    ADDRESSES_ASSOCIATION_IP_OWNER_ID("network-interface.addresses.association.ip-owner-id");

    private String value;

    NetworkInterfaceFilter(String input) {
        this.value = input;
    }

    public String getValue() {
        return value;
    }
}
