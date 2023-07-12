

package io.cloudslang.content.oracle.oci.entities.inputs;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class OCICreateInstanceInputs {
    private final String isManagementDisabled;
    private final String isMonitoringDisabled;
    private final String assignPublicIp;
    private final String definedTags;
    private final String displayName;
    private final String freeformTags;
    private final String hostnameLabel;
    private final String networkSecurityGroupIds;
    private final String privateIp;
    private final String skipSourceDestCheck;
    private final String subnetId;
    private final String dedicatedVmHostId;
    private final String vnicDefinedTags;
    private final String vnicFreeformTags;
    private final String vnicDisplayName;
    private final String extendedMetadata;
    private final String faultDomain;
    private final String ipxeScript;
    private final String isPvEncryptionInTransitEnabled;
    private final String launchMode;
    private final String bootVolumeType;
    private final String firmware;
    private final String isConsistentVolumeNamingEnabled;
    private final String networkType;
    private final String remoteDataVolumeType;
    private final String shape;
    private final String sshAuthorizedKeys;
    private final String userdata;
    private final String ocpus;
    private final String bootVolumeSizeInGBs;
    private final String imageId;
    private final String kmsKeyId;
    private final String sourceType;
    private final String bootVolumeId;
    private final OCICommonInputs commonInputs;


    @java.beans.ConstructorProperties({"isManagementDisabled", "isMonitoringDisabled", "assignPublicIp", "definedTags", "displayName", "freeformTags", "hostnameLabel", "networkSecurityGroupIds", "privateIp", "skipSourceDestCheck", "subnetId", "dedicatedVmHostId", "vnicDefinedTags", "vnicFreeformTags", "vnicDisplayName", "extendedMetadata", "faultDomain", "ipxeScript", "isPvEncryptionInTransitEnabled", "launchMode", "bootVolumeType", "firmware", "isConsistentVolumeNamingEnabled", "networkType", "remoteDataVolumeType", "shape", "sshAuthorizedKeys", "userdata", "ocpus", "bootVolumeSizeInGBs", "imageId", "kmsKeyId", "sourceType", "bootVolumeId", "commonInputs"})
    public OCICreateInstanceInputs(String isManagementDisabled, String isMonitoringDisabled, String assignPublicIp, String definedTags, String displayName, String freeformTags, String hostnameLabel, String networkSecurityGroupIds, String privateIp, String skipSourceDestCheck, String subnetId, String dedicatedVmHostId, String vnicDefinedTags, String vnicFreeformTags, String vnicDisplayName, String extendedMetadata, String faultDomain, String ipxeScript, String isPvEncryptionInTransitEnabled, String launchMode, String bootVolumeType, String firmware, String isConsistentVolumeNamingEnabled, String networkType, String remoteDataVolumeType, String shape, String sshAuthorizedKeys, String userdata, String ocpus, String bootVolumeSizeInGBs, String imageId, String kmsKeyId, String sourceType, String bootVolumeId, OCICommonInputs commonInputs) {
        this.isManagementDisabled = isManagementDisabled;
        this.isMonitoringDisabled = isMonitoringDisabled;
        this.assignPublicIp = assignPublicIp;
        this.definedTags = definedTags;
        this.displayName = displayName;
        this.freeformTags = freeformTags;
        this.hostnameLabel = hostnameLabel;
        this.networkSecurityGroupIds = networkSecurityGroupIds;
        this.privateIp = privateIp;
        this.skipSourceDestCheck = skipSourceDestCheck;
        this.subnetId = subnetId;
        this.dedicatedVmHostId = dedicatedVmHostId;
        this.vnicDefinedTags = vnicDefinedTags;
        this.vnicFreeformTags = vnicFreeformTags;
        this.vnicDisplayName = vnicDisplayName;
        this.extendedMetadata = extendedMetadata;
        this.faultDomain = faultDomain;
        this.ipxeScript = ipxeScript;
        this.isPvEncryptionInTransitEnabled = isPvEncryptionInTransitEnabled;
        this.launchMode = launchMode;
        this.bootVolumeType = bootVolumeType;
        this.firmware = firmware;
        this.isConsistentVolumeNamingEnabled = isConsistentVolumeNamingEnabled;
        this.networkType = networkType;
        this.remoteDataVolumeType = remoteDataVolumeType;
        this.shape = shape;
        this.sshAuthorizedKeys = sshAuthorizedKeys;
        this.userdata = userdata;
        this.ocpus = ocpus;
        this.bootVolumeSizeInGBs = bootVolumeSizeInGBs;
        this.imageId = imageId;
        this.kmsKeyId = kmsKeyId;
        this.sourceType = sourceType;
        this.bootVolumeId = bootVolumeId;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static OCICreateInstanceInputsBuilder builder() {
        return new OCICreateInstanceInputsBuilder();
    }

    @NotNull
    public String getIsManagementDisabled() {
        return isManagementDisabled;
    }

    @NotNull
    public String getIsMonitoringDisabled() {
        return isMonitoringDisabled;
    }


    @NotNull
    public String getAssignPublicIp() {
        return assignPublicIp;
    }

    @NotNull
    public String getDefinedTags() {
        return definedTags;
    }

    @NotNull
    public String getDisplayName() {
        return displayName;
    }

    @NotNull
    public String getFreeformTags() {
        return freeformTags;
    }

    @NotNull
    public String getHostnameLabel() {
        return hostnameLabel;
    }

    @NotNull
    public String getNetworkSecurityGroupIds() {
        return networkSecurityGroupIds;
    }

    @NotNull
    public String getPrivateIp() {
        return privateIp;
    }

    @NotNull
    public String getSkipSourceDestCheck() {
        return skipSourceDestCheck;
    }

    @NotNull
    public String getSubnetId() {
        return subnetId;
    }

    @NotNull
    public String getDedicatedVmHostId() {
        return dedicatedVmHostId;
    }

    @NotNull
    public String getVnicDefinedTags() {
        return vnicDefinedTags;
    }

    @NotNull
    public String getVnicFreeformTags() {
        return vnicFreeformTags;
    }

    @NotNull
    public String getVnicDisplayName() {
        return vnicDisplayName;
    }

    @NotNull
    public String getExtendedMetadata() {
        return extendedMetadata;
    }

    @NotNull
    public String getFaultDomain() {
        return faultDomain;
    }

    @NotNull
    public String getIpxeScript() {
        return ipxeScript;
    }

    @NotNull
    public String getIsPvEncryptionInTransitEnabled() {
        return isPvEncryptionInTransitEnabled;
    }

    @NotNull
    public String getLaunchMode() {
        return launchMode;
    }

    @NotNull
    public String getBootVolumeType() {
        return bootVolumeType;
    }

    @NotNull
    public String getFirmware() {
        return firmware;
    }

    @NotNull
    public String getIsConsistentVolumeNamingEnabled() {
        return isConsistentVolumeNamingEnabled;
    }

    @NotNull
    public String getNetworkType() {
        return networkType;
    }

    @NotNull
    public String getRemoteDataVolumeType() {
        return remoteDataVolumeType;
    }

    @NotNull
    public String getShape() {
        return shape;
    }

    @NotNull
    public String getSSHAuthorizedKeys() {
        return sshAuthorizedKeys;
    }

    @NotNull
    public String getUserdata() {
        return userdata;
    }

    @NotNull
    public String getOcpus() {
        return ocpus;
    }

    @NotNull
    public String getBootVolumeSizeInGBs() {
        return bootVolumeSizeInGBs;
    }

    @NotNull
    public String getImageId() {
        return imageId;
    }

    @NotNull
    public String getKmsKeyId() {
        return kmsKeyId;
    }

    @NotNull
    public String getSourceType() {
        return sourceType;
    }

    @NotNull
    public String getBootVolumeId() {
        return bootVolumeId;
    }

    @NotNull
    public OCICommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static final class OCICreateInstanceInputsBuilder {
        private String isManagementDisabled = EMPTY;
        private String isMonitoringDisabled = EMPTY;
        private String assignPublicIp = EMPTY;
        private String definedTags = EMPTY;
        private String displayName = EMPTY;
        private String freeformTags = EMPTY;
        private String hostnameLabel = EMPTY;
        private String networkSecurityGroupIds = EMPTY;
        private String privateIp = EMPTY;
        private String skipSourceDestCheck = EMPTY;
        private String subnetId = EMPTY;
        private String dedicatedVmHostId = EMPTY;
        private String vnicDefinedTags = EMPTY;
        private String vnicFreeformTags = EMPTY;
        private String vnicDisplayName = EMPTY;
        private String extendedMetadata = EMPTY;
        private String faultDomain = EMPTY;
        private String ipxeScript = EMPTY;
        private String isPvEncryptionInTransitEnabled = EMPTY;
        private String launchMode = EMPTY;
        private String bootVolumeType = EMPTY;
        private String firmware = EMPTY;
        private String isConsistentVolumeNamingEnabled = EMPTY;
        private String networkType = EMPTY;
        private String remoteDataVolumeType = EMPTY;
        private String shape = EMPTY;
        private String sshAuthorizedKeys = EMPTY;
        private String userdata = EMPTY;
        private String ocpus = EMPTY;
        private String bootVolumeSizeInGBs = EMPTY;
        private String imageId = EMPTY;
        private String kmsKeyId = EMPTY;
        private String sourceType = EMPTY;
        private String bootVolumeId = EMPTY;
        private OCICommonInputs commonInputs;

        OCICreateInstanceInputsBuilder() {
        }


        @NotNull
        public OCICreateInstanceInputsBuilder isManagementDisabled(@NotNull final String isManagementDisabled) {
            this.isManagementDisabled = isManagementDisabled;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder isMonitoringDisabled(@NotNull final String isMonitoringDisabled) {
            this.isMonitoringDisabled = isMonitoringDisabled;
            return this;
        }


        @NotNull
        public OCICreateInstanceInputsBuilder assignPublicIp(@NotNull final String assignPublicIp) {
            this.assignPublicIp = assignPublicIp;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder definedTags(@NotNull final String definedTags) {
            this.definedTags = definedTags;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder displayName(@NotNull final String displayName) {
            this.displayName = displayName;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder freeformTags(@NotNull final String freeformTags) {
            this.freeformTags = freeformTags;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder hostnameLabel(@NotNull final String hostnameLabel) {
            this.hostnameLabel = hostnameLabel;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder networkSecurityGroupIds(@NotNull final String networkSecurityGroupIds) {
            this.networkSecurityGroupIds = networkSecurityGroupIds;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder privateIp(@NotNull final String privateIp) {
            this.privateIp = privateIp;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder skipSourceDestCheck(@NotNull final String skipSourceDestCheck) {
            this.skipSourceDestCheck = skipSourceDestCheck;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder subnetId(@NotNull final String subnetId) {
            this.subnetId = subnetId;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder dedicatedVmHostId(@NotNull final String dedicatedVmHostId) {
            this.dedicatedVmHostId = dedicatedVmHostId;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder vnicDefinedTags(@NotNull final String vnicDefinedTags) {
            this.vnicDefinedTags = vnicDefinedTags;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder vnicFreeformTags(@NotNull final String vnicFreeformTags) {
            this.vnicFreeformTags = vnicFreeformTags;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder vnicDisplayName(@NotNull final String vnicDisplayName) {
            this.vnicDisplayName = vnicDisplayName;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder extendedMetadata(@NotNull final String extendedMetadata) {
            this.extendedMetadata = extendedMetadata;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder faultDomain(@NotNull final String faultDomain) {
            this.faultDomain = faultDomain;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder ipxeScript(@NotNull final String ipxeScript) {
            this.ipxeScript = ipxeScript;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder isPvEncryptionInTransitEnabled(@NotNull final String isPvEncryptionInTransitEnabled) {
            this.isPvEncryptionInTransitEnabled = isPvEncryptionInTransitEnabled;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder launchMode(@NotNull final String launchMode) {
            this.launchMode = launchMode;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder bootVolumeType(@NotNull final String bootVolumeType) {
            this.bootVolumeType = bootVolumeType;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder firmware(@NotNull final String firmware) {
            this.firmware = firmware;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder isConsistentVolumeNamingEnabled(@NotNull final String isConsistentVolumeNamingEnabled) {
            this.isConsistentVolumeNamingEnabled = isConsistentVolumeNamingEnabled;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder networkType(@NotNull final String networkType) {
            this.networkType = networkType;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder remoteDataVolumeType(@NotNull final String remoteDataVolumeType) {
            this.remoteDataVolumeType = remoteDataVolumeType;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder shape(@NotNull final String shape) {
            this.shape = shape;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder sshAuthorizedKeys(@NotNull final String sshAuthorizedKeys) {
            this.sshAuthorizedKeys = sshAuthorizedKeys;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder userdata(@NotNull final String userdata) {
            this.userdata = userdata;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder ocpus(@NotNull final String ocpus) {
            this.ocpus = ocpus;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder bootVolumeSizeInGBs(@NotNull final String bootVolumeSizeInGBs) {
            this.bootVolumeSizeInGBs = bootVolumeSizeInGBs;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder imageId(@NotNull final String imageId) {
            this.imageId = imageId;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder kmsKeyId(@NotNull final String kmsKeyId) {
            this.kmsKeyId = kmsKeyId;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder sourceType(@NotNull final String sourceType) {
            this.sourceType = sourceType;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder bootVolumeId(@NotNull final String bootVolumeId) {
            this.bootVolumeId = bootVolumeId;
            return this;
        }

        @NotNull
        public OCICreateInstanceInputsBuilder commonInputs(@NotNull final OCICommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public OCICreateInstanceInputs build() {
            return new OCICreateInstanceInputs(isManagementDisabled, isMonitoringDisabled, assignPublicIp, definedTags, displayName, freeformTags, hostnameLabel, networkSecurityGroupIds, privateIp, skipSourceDestCheck, subnetId, dedicatedVmHostId, vnicDefinedTags, vnicFreeformTags, vnicDisplayName, extendedMetadata, faultDomain, ipxeScript, isPvEncryptionInTransitEnabled, launchMode, bootVolumeType, firmware, isConsistentVolumeNamingEnabled, networkType, remoteDataVolumeType, shape, sshAuthorizedKeys, userdata, ocpus, bootVolumeSizeInGBs, imageId, kmsKeyId, sourceType, bootVolumeId, commonInputs);

        }
    }
}
