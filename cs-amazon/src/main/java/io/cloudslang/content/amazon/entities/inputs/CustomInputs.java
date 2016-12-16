/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.entities.aws.Architecture;
import io.cloudslang.content.amazon.entities.aws.Domain;
import io.cloudslang.content.amazon.entities.aws.Hypervisor;
import io.cloudslang.content.amazon.entities.aws.InstanceType;
import io.cloudslang.content.amazon.entities.aws.Platform;
import io.cloudslang.content.amazon.entities.aws.ProductCodeType;
import io.cloudslang.content.amazon.entities.aws.RootDeviceType;
import io.cloudslang.content.amazon.entities.aws.VirtualizationType;
import io.cloudslang.content.amazon.entities.aws.VolumeType;
import io.cloudslang.content.amazon.utils.InputsUtil;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class CustomInputs {
    private final String operationType;
    private final String attribute;
    private final String instanceId;
    private final String imageId;
    private final String identityId;
    private final String volumeId;
    private final String hostId;
    private final String kernelId;
    private final String ownerId;
    private final String ramdiskId;
    private final String subnetId;
    private final String allocationId;
    private final String associationId;
    private final String architecture;
    private final String deleteOnTermination;
    private final String blockMappingDeviceName;
    private final String blockDeviceMappingSnapshotId;
    private final String volumeSize;
    private final String volumeType;
    private final String hypervisor;
    private final String ownerAlias;
    private final String platform;
    private final String productCode;
    private final String productCodeType;
    private final String rootDeviceName;
    private final String rootDeviceType;
    private final String stateReasonCode;
    private final String stateReasonMessage;
    private final String keyTagsString;
    private final String valueTagsString;
    private final String virtualizationType;
    private final String availabilityZone;
    private final String instanceType;
    private final String resourceIdsString;
    private final String kmsKeyId;
    private final String attachmentId;
    private final String domain;
    private final String regionsString;
    private final String keyFiltersString;
    private final String valueFiltersString;
    private final String availabilityZonesString;
    private final String vpcId;

    private CustomInputs(Builder builder) {
        this.instanceId = builder.instanceId;
        this.imageId = builder.imageId;
        this.identityId = builder.identityId;
        this.volumeId = builder.volumeId;
        this.hostId = builder.hostId;
        this.kernelId = builder.kernelId;
        this.ownerId = builder.ownerId;
        this.ramdiskId = builder.ramdiskId;
        this.subnetId = builder.subnetId;
        this.allocationId = builder.allocationId;
        this.associationId = builder.associationId;
        this.architecture = builder.architecture;
        this.blockMappingDeviceName = builder.blockMappingDeviceName;
        this.deleteOnTermination = builder.deleteOnTermination;
        this.blockDeviceMappingSnapshotId = builder.blockDeviceMappingSnapshotId;
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
        this.availabilityZonesString = builder.availabilityZonesString;
        this.vpcId = builder.vpcId;
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

    public String getSubnetId() {
        return subnetId;
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

    public String getAvailabilityZonesString() {
        return availabilityZonesString;
    }

    public String getVpcId() {
        return vpcId;
    }

    public static class Builder {
        private String instanceId;
        private String imageId;
        private String identityId;
        private String volumeId;
        private String hostId;
        private String kernelId;
        private String ownerId;
        private String ramdiskId;
        private String subnetId;
        private String allocationId;
        private String associationId;
        private String architecture;
        private String blockMappingDeviceName;
        private String deleteOnTermination;
        private String blockDeviceMappingSnapshotId;
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
        private String availabilityZonesString;
        private String vpcId;

        public CustomInputs build() {
            return new CustomInputs(this);
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

        public Builder withSubnetId(String inputValue) {
            subnetId = inputValue;
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

        public Builder withAvailabilityZonesString(String inputValue) {
            availabilityZonesString = inputValue;
            return this;
        }

        public Builder withVpcId(String inputValue) {
            vpcId = inputValue;
            return this;
        }
    }
}
