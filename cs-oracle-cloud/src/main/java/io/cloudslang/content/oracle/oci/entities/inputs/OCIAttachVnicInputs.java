package io.cloudslang.content.oracle.oci.entities.inputs;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class OCIAttachVnicInputs {
    private final String vnicAttachmentDisplayName;
    private final String nicIndex;
    private final String assignPublicIp;
    private final String definedTags;
    private final String vnicDisplayName;
    private final String freeformTags;
    private final String hostnameLabel;
    private final String nsgIds;
    private final String privateIp;
    private final String skipSourceDestCheck;
    private final String subnetId;
    private final OCICommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vnicAttachmentDisplayName", "nicIndex", "assignPublicIp", "definedTags", "vnicDisplayName", "freeformTags", "hostnameLabel", "nsgIds", "privateIp", "subnetId", "skipSourceDestCheck", "subnetId", "commonInputs"})
    public OCIAttachVnicInputs(String vnicAttachmentDisplayName, String nicIndex, String assignPublicIp, String definedTags, String vnicDisplayName, String freeformTags, String hostnameLabel, String nsgIds, String privateIp, String skipSourceDestCheck, String subnetId, OCICommonInputs commonInputs) {
        this.vnicAttachmentDisplayName = vnicAttachmentDisplayName;
        this.nicIndex = nicIndex;
        this.assignPublicIp = assignPublicIp;
        this.definedTags = definedTags;
        this.vnicDisplayName = vnicDisplayName;
        this.freeformTags = freeformTags;
        this.hostnameLabel = hostnameLabel;
        this.nsgIds = nsgIds;
        this.privateIp = privateIp;
        this.skipSourceDestCheck = skipSourceDestCheck;
        this.subnetId = subnetId;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static OCIAttachVnicInputs.OCIAttachVnicInputsBuilder builder() {
        return new OCIAttachVnicInputs.OCIAttachVnicInputsBuilder();
    }

    @NotNull
    public String getVnicAttachmentDisplayName() {
        return vnicAttachmentDisplayName;
    }

    @NotNull
    public String getNicIndex() {
        return nicIndex;
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
    public String getVnicDisplayName() {
        return vnicDisplayName;
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
    public String getNsgIds() {
        return nsgIds;
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
    public OCICommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static final class OCIAttachVnicInputsBuilder {
        private String vnicAttachmentDisplayName = EMPTY;
        private String nicIndex = EMPTY;
        private String assignPublicIp = EMPTY;
        private String definedTags = EMPTY;
        private String vnicDisplayName = EMPTY;
        private String freeformTags = EMPTY;
        private String hostnameLabel = EMPTY;
        private String nsgIds = EMPTY;
        private String privateIp = EMPTY;
        private String skipSourceDestCheck = EMPTY;
        private String subnetId = EMPTY;
        private OCICommonInputs commonInputs;

        private OCIAttachVnicInputsBuilder() {
        }


        public OCIAttachVnicInputsBuilder vnicAttachmentDisplayName(String vnicAttachmentDisplayName) {
            this.vnicAttachmentDisplayName = vnicAttachmentDisplayName;
            return this;
        }

        public OCIAttachVnicInputsBuilder nicIndex(String nicIndex) {
            this.nicIndex = nicIndex;
            return this;
        }

        public OCIAttachVnicInputsBuilder assignPublicIp(String assignPublicIp) {
            this.assignPublicIp = assignPublicIp;
            return this;
        }

        public OCIAttachVnicInputsBuilder definedTags(String definedTags) {
            this.definedTags = definedTags;
            return this;
        }

        public OCIAttachVnicInputsBuilder vnicDisplayName(String vnicDisplayName) {
            this.vnicDisplayName = vnicDisplayName;
            return this;
        }

        public OCIAttachVnicInputsBuilder freeformTags(String freeformTags) {
            this.freeformTags = freeformTags;
            return this;
        }

        public OCIAttachVnicInputsBuilder hostnameLabel(String hostnameLabel) {
            this.hostnameLabel = hostnameLabel;
            return this;
        }

        public OCIAttachVnicInputsBuilder nsgIds(String nsgIds) {
            this.nsgIds = nsgIds;
            return this;
        }

        public OCIAttachVnicInputsBuilder privateIp(String privateIp) {
            this.privateIp = privateIp;
            return this;
        }

        public OCIAttachVnicInputsBuilder skipSourceDestCheck(String skipSourceDestCheck) {
            this.skipSourceDestCheck = skipSourceDestCheck;
            return this;
        }

        public OCIAttachVnicInputsBuilder subnetId(String subnetId) {
            this.subnetId = subnetId;
            return this;
        }

        public OCIAttachVnicInputsBuilder commonInputs(OCICommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }


        public OCIAttachVnicInputs build() {
            return new OCIAttachVnicInputs(vnicAttachmentDisplayName, nicIndex, assignPublicIp, definedTags, vnicDisplayName, freeformTags, hostnameLabel, nsgIds, privateIp, skipSourceDestCheck, subnetId, commonInputs);

        }
    }
}
