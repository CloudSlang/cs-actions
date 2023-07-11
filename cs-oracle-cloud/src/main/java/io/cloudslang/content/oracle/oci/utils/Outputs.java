

package io.cloudslang.content.oracle.oci.utils;

import io.cloudslang.content.constants.OutputNames;

public class Outputs extends OutputNames {
    public static class CommonOutputs {
        public static final String DOCUMENT = "document";

    }

    public static class CreateInstanceOutputs {
        public static final String INSTANCE_ID = "instance_id";
        public static final String INSTANCE_NAME = "instance_name";
    }

    public static class ListInstancesOutputs {
        public static final String INSTANCE_NAME_LIST = "instance_name_list";
    }

    public static class GetInstanceDetailsOutputs {
        public static final String INSTANCE_STATE = "instance_state";
    }

    public static class ListVnicAttachmentsOutputs {
        public static final String VNIC_LIST = "vnic_list";
        public static final String VNIC_ATTACHMENTS_LIST = "vnic_attachments_list";
    }

    public static class AttachVnicOutputs {
        public static final String VNIC_ID = "vnicId";
        public static final String VNIC_ATTACHMENT_ID = "vnicAttachmentId";
        public static final String VNIC_ATTACHMENT_STATE = "vnicAttachmentState";
    }

    public static class AttachVolumeOutputs {
        public static final String VOLUME_ATTACHMENT_STATE = "volumeAttachmentState";
    }

    public static class GetVnicDetailsOutputs {
        public static final String PRIVATE_IP = "private_ip";
        public static final String PUBLIC_IP = "public_ip";
        public static final String VNIC_NAME = "vnic_name";
        public static final String VNIC_HOSTNAME = "vnic_hostname";
        public static final String VNIC_STATE = "vnic_state";
        public static final String MAC_ADDRESS = "mac_address";
    }

    public static class GetInstanceDefaultCredentialsOutputs {
        public static final String INSTANCE_USERNAME = "instance_username";
        public static final String INSTANCE_PASSWORD = "instance_password";
    }

}
