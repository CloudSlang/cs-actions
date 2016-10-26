package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.entities.aws.Architecture;
import io.cloudslang.content.amazon.entities.aws.BlockDeviceMappingStatus;
import io.cloudslang.content.amazon.entities.aws.Domain;
import io.cloudslang.content.amazon.entities.aws.Hypervisor;
import io.cloudslang.content.amazon.entities.aws.InstanceType;
import io.cloudslang.content.amazon.entities.aws.Platform;
import io.cloudslang.content.amazon.entities.aws.ProductCodeType;
import io.cloudslang.content.amazon.entities.aws.RootDeviceType;
import io.cloudslang.content.amazon.entities.aws.VirtualizationType;
import io.cloudslang.content.amazon.entities.aws.VolumeType;
import io.cloudslang.content.amazon.utils.InputsUtil;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.DEFAULT_AMAZON_REGION;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class CustomInputs {
    private String operationType;
    private String attribute;
    private String region;
    private String instanceId;
    private String imageId;
    private String identityId;
    private String volumeId;
    private String groupId;
    private String hostId;
    private String kernelId;
    private String ownerId;
    private String ramdiskId;
    private String reservationId;
    private String subnetId;
    private String vpcId;
    private String allocationId;
    private String associationId;
    private String architecture;
    private String deleteOnTermination;
    private String blockMappingDeviceName;
    private String blockDeviceMappingSnapshotId;
    private String attachTime;
    private String blockDeviceMappingStatus;
    private String volumeSize;
    private String volumeType;
    private String hypervisor;
    private String ownerAlias;
    private String platform;
    private String productCode;
    private String productCodeType;
    private String rootDeviceName;
    private String rootDeviceType;
    private String stateReasonCode;
    private String stateReasonMessage;
    private String keyTagsString;
    private String valueTagsString;
    private String virtualizationType;
    private String availabilityZone;
    private String instanceType;
    private String resourceIdsString;
    private String kmsKeyId;
    private String attachmentId;
    private String domain;
    private String regionsString;
    private String keyFiltersString;
    private String valueFiltersString;

    private CustomInputs(Builder builder) {
        this.region = builder.region;
        this.instanceId = builder.instanceId;
        this.imageId = builder.imageId;
        this.identityId = builder.identityId;
        this.volumeId = builder.volumeId;
        this.groupId = builder.groupId;
        this.hostId = builder.hostId;
        this.kernelId = builder.kernelId;
        this.ownerId = builder.ownerId;
        this.ramdiskId = builder.ramdiskId;
        this.reservationId = builder.reservationId;
        this.subnetId = builder.subnetId;
        this.vpcId = builder.vpcId;
        this.allocationId = builder.allocationId;
        this.associationId = builder.associationId;
        this.architecture = builder.architecture;
        this.blockMappingDeviceName = builder.blockMappingDeviceName;
        this.deleteOnTermination = builder.deleteOnTermination;
        this.blockDeviceMappingSnapshotId = builder.blockDeviceMappingSnapshotId;
        this.attachTime = builder.attachTime;
        this.blockDeviceMappingStatus = builder.blockDeviceMappingStatus;
        this.volumeSize = builder.volumeSize;
        this.volumeType = builder.volumeType;
        this.hypervisor = builder.hypervisor;
        this.ownerAlias = builder.ownerAlias;
        this.platform = builder.platform;
        this.productCode = builder.productCode;
        this.productCodeType = builder.productCodeType;
        this.rootDeviceName = builder.rootDeviceName;
        this.rootDeviceType = builder.rootDeviceType;
        this.stateReasonCode = builder.stateReasonCode;
        this.stateReasonMessage = builder.stateReasonMessage;
        this.keyTagsString = builder.keyTagsString;
        this.valueTagsString = builder.valueTagsString;
        this.virtualizationType = builder.virtualizationType;
        this.availabilityZone = builder.availabilityZone;
        this.instanceType = builder.instanceType;
        this.resourceIdsString = builder.resourceIdsString;
        this.kmsKeyId = builder.kmsKeyId;
        this.attachmentId = builder.attachmentId;
        this.domain = builder.domain;
        this.attribute = builder.attribute;
        this.operationType = builder.operationType;
        this.regionsString = builder.regionsString;
        this.keyFiltersString = builder.keyFiltersString;
        this.valueFiltersString = builder.valueFiltersString;
    }

    public String getRegion() {
        return region;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getImageId() {
        return imageId;
    }

    public String getIdentityId() {
        return identityId;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getHostId() {
        return hostId;
    }

    public String getKernelId() {
        return kernelId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getRamdiskId() {
        return ramdiskId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public String getVpcId() {
        return vpcId;
    }

    public String getAllocationId() {
        return allocationId;
    }

    public String getAssociationId() {
        return associationId;
    }

    public String getArchitecture() {
        return architecture;
    }

    public String getBlockMappingDeviceName() {
        return blockMappingDeviceName;
    }

    public String getDeleteOnTermination() {
        return deleteOnTermination;
    }

    public String getBlockDeviceMappingSnapshotId() {
        return blockDeviceMappingSnapshotId;
    }

    public String getAttachTime() {
        return attachTime;
    }

    public String getBlockDeviceMappingStatus() {
        return blockDeviceMappingStatus;
    }

    public String getVolumeSize() {
        return volumeSize;
    }

    public String getVolumeType() {
        return volumeType;
    }

    public String getHypervisor() {
        return hypervisor;
    }

    public String getOwnerAlias() {
        return ownerAlias;
    }

    public String getPlatform() {
        return platform;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductCodeType() {
        return productCodeType;
    }

    public String getRootDeviceName() {
        return rootDeviceName;
    }

    public String getRootDeviceType() {
        return rootDeviceType;
    }

    public String getStateReasonCode() {
        return stateReasonCode;
    }

    public String getStateReasonMessage() {
        return stateReasonMessage;
    }

    public String getKeyTagsString() {
        return keyTagsString;
    }

    public String getValueTagsString() {
        return valueTagsString;
    }

    public String getVirtualizationType() {
        return virtualizationType;
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public String getResourceIdsString() {
        return resourceIdsString;
    }

    public String getKmsKeyId() {
        return kmsKeyId;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public String getDomain() {
        return domain;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getRegionsString() {
        return regionsString;
    }

    public String getKeyFiltersString() {
        return keyFiltersString;
    }

    public String getValueFiltersString() {
        return valueFiltersString;
    }

    public static class Builder {
        private String region;
        private String instanceId;
        private String imageId;
        private String identityId;
        private String volumeId;
        private String groupId;
        private String hostId;
        private String kernelId;
        private String ownerId;
        private String ramdiskId;
        private String reservationId;
        private String subnetId;
        private String vpcId;
        private String allocationId;
        private String associationId;
        private String architecture;
        private String blockMappingDeviceName;
        private String deleteOnTermination;
        private String blockDeviceMappingSnapshotId;
        private String attachTime;
        private String blockDeviceMappingStatus;
        private String volumeSize;
        private String volumeType;
        private String hypervisor;
        private String ownerAlias;
        private String platform;
        private String productCode;
        private String productCodeType;
        private String rootDeviceName;
        private String rootDeviceType;
        private String stateReasonCode;
        private String stateReasonMessage;
        private String keyTagsString;
        private String valueTagsString;
        private String virtualizationType;
        private String availabilityZone;
        private String instanceType;
        private String resourceIdsString;
        private String kmsKeyId;
        private String attachmentId;
        private String domain;
        private String attribute;
        private String operationType;
        private String regionsString;
        private String keyFiltersString;
        private String valueFiltersString;

        public CustomInputs build() {
            return new CustomInputs(this);
        }

        public Builder withRegion(String inputValue) {
            region = InputsUtil.getDefaultStringInput(inputValue, DEFAULT_AMAZON_REGION);
            return this;
        }

        public Builder withInstanceId(String inputValue) {
            instanceId = inputValue;
            return this;
        }

        public Builder withImageId(String inputValue) {
            imageId = inputValue;
            return this;
        }

        public Builder withIdentityId(String inputValue) {
            identityId = inputValue;
            return this;
        }

        public Builder withVolumeId(String inputValue) {
            volumeId = inputValue;
            return this;
        }

        public Builder withGroupId(String inputValue) {
            groupId = inputValue;
            return this;
        }

        public Builder withHostId(String inputValue) {
            hostId = inputValue;
            return this;
        }

        public Builder withKernelId(String inputValue) {
            kernelId = inputValue;
            return this;
        }

        public Builder withOwnerId(String inputValue) {
            ownerId = inputValue;
            return this;
        }

        public Builder withRamdiskId(String inputValue) {
            ramdiskId = inputValue;
            return this;
        }

        public Builder withReservationId(String inputValue) {
            reservationId = inputValue;
            return this;
        }

        public Builder withSubnetId(String inputValue) {
            subnetId = inputValue;
            return this;
        }

        public Builder withVpcId(String inputValue) {
            vpcId = inputValue;
            return this;
        }

        public Builder withAllocationId(String inputValue) {
            allocationId = inputValue;
            return this;
        }

        public Builder withAssociationId(String inputValue) {
            associationId = inputValue;
            return this;
        }

        public Builder withArchitecture(String inputValue) {
            architecture = Architecture.getValue(inputValue);
            return this;
        }

        public Builder withBlockMappingDeviceName(String inputValue) {
            blockMappingDeviceName = inputValue;
            return this;
        }

        public Builder withDeleteOnTermination(String inputValue) {
            deleteOnTermination = InputsUtil.getRelevantBooleanString(inputValue);
            return this;
        }

        public Builder withBlockDeviceMappingSnapshotId(String inputValue) {
            blockDeviceMappingSnapshotId = inputValue;
            return this;
        }

        public Builder withAttachTime(String inputValue) {
            attachTime = inputValue;
            return this;
        }

        public Builder withBlockDeviceMappingStatus(String inputValue) {
            blockDeviceMappingStatus = BlockDeviceMappingStatus.getValue(inputValue);
            return this;
        }

        public Builder withVolumeSize(String inputValue) {
            volumeSize = InputsUtil.getValidVolumeAmount(inputValue);
            return this;
        }

        public Builder withVolumeType(String inputValue) {
            volumeType = VolumeType.getValue(inputValue);
            return this;
        }

        public Builder withHypervisor(String inputValue) {
            hypervisor = Hypervisor.getValue(inputValue);
            return this;
        }

        public Builder withOwnerAlias(String inputValue) {
            ownerAlias = inputValue;
            return this;
        }

        public Builder withPlatform(String inputValue) {
            platform = Platform.getValue(inputValue);
            return this;
        }

        public Builder withProductCode(String inputValue) {
            productCode = inputValue;
            return this;
        }

        public Builder withProductCodeType(String inputValue) {
            productCodeType = ProductCodeType.getValue(inputValue);
            return this;
        }

        public Builder withRootDeviceName(String inputValue) {
            rootDeviceName = inputValue;
            return this;
        }

        public Builder withRootDeviceType(String inputValue) {
            rootDeviceType = RootDeviceType.getValue(inputValue);
            return this;
        }

        public Builder withStateReasonCode(String inputValue) {
            stateReasonCode = inputValue;
            return this;
        }

        public Builder withStateReasonMessage(String inputValue) {
            stateReasonMessage = inputValue;
            return this;
        }

        public Builder withKeyTagsString(String inputValue) {
            keyTagsString = inputValue;
            return this;
        }

        public Builder withValueTagsString(String inputValue) {
            valueTagsString = inputValue;
            return this;
        }

        public Builder withVirtualizationType(String inputValue) {
            virtualizationType = VirtualizationType.getValue(inputValue);
            return this;
        }

        public Builder withAvailabilityZone(String inputValue) {
            availabilityZone = inputValue;
            return this;
        }

        public Builder withInstanceType(String inputValue) {
            instanceType = InstanceType.getInstanceType(inputValue);
            return this;
        }

        public Builder withResourceIdsString(String inputValue) {
            resourceIdsString = inputValue;
            return this;
        }

        public Builder withKmsKeyId(String inputValue) {
            kmsKeyId = inputValue;
            return this;
        }

        public Builder withAttachmentId(String inputValue) {
            attachmentId = inputValue;
            return this;
        }

        public Builder withDomain(String inputValue) {
            domain = Domain.getValue(inputValue);
            return this;
        }

        public Builder withAttribute(String inputValue) {
            attribute = inputValue;
            return this;
        }

        public Builder withOperationType(String inputValue) {
            operationType = inputValue;
            return this;
        }

        public Builder withRegionsString(String inputValue) {
            regionsString = inputValue;
            return this;
        }

        public Builder withKeyFiltersString(String inputValue) {
            keyFiltersString = inputValue;
            return this;
        }

        public Builder withValueFiltersString(String inputValue) {
            valueFiltersString = inputValue;
            return this;
        }
    }
}