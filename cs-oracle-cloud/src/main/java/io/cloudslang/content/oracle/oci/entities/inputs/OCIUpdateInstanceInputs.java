package io.cloudslang.content.oracle.oci.entities.inputs;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class OCIUpdateInstanceInputs {

    private final String isManagementDisabled;
    private final String isMonitoringDisabled;
    private final String definedTags;
    private final String displayName;
    private final String freeformTags;
    private final String shape;
    private final String ocpus;
    private final OCICommonInputs commonInputs;


    @java.beans.ConstructorProperties({"isManagementDisabled", "isMonitoringDisabled", "definedTags", "displayName", "freeformTags", "shape", "ocpus", "commonInputs"})
    public OCIUpdateInstanceInputs(String isManagementDisabled, String isMonitoringDisabled, String definedTags, String displayName, String freeformTags, String shape, String ocpus, OCICommonInputs commonInputs) {
        this.isManagementDisabled = isManagementDisabled;
        this.isMonitoringDisabled = isMonitoringDisabled;
        this.definedTags = definedTags;
        this.displayName = displayName;
        this.freeformTags = freeformTags;
        this.shape = shape;
        this.ocpus = ocpus;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static OCIUpdateInstanceInputs.OCIUpdateInstanceInputsBuilder builder() {
        return new OCIUpdateInstanceInputs.OCIUpdateInstanceInputsBuilder();
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
    public String getShape() {
        return shape;
    }

    @NotNull
    public String getOcpus() {
        return ocpus;
    }

    @NotNull
    public OCICommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static final class OCIUpdateInstanceInputsBuilder {
        private String isManagementDisabled = EMPTY;
        private String isMonitoringDisabled = EMPTY;
        private String definedTags = EMPTY;
        private String displayName = EMPTY;
        private String freeformTags = EMPTY;
        private String shape = EMPTY;
        private String ocpus = EMPTY;
        private OCICommonInputs commonInputs;

        OCIUpdateInstanceInputsBuilder() {
        }


        @NotNull
        public OCIUpdateInstanceInputs.OCIUpdateInstanceInputsBuilder isManagementDisabled(@NotNull final String isManagementDisabled) {
            this.isManagementDisabled = isManagementDisabled;
            return this;
        }

        @NotNull
        public OCIUpdateInstanceInputs.OCIUpdateInstanceInputsBuilder isMonitoringDisabled(@NotNull final String isMonitoringDisabled) {
            this.isMonitoringDisabled = isMonitoringDisabled;
            return this;
        }

        @NotNull
        public OCIUpdateInstanceInputs.OCIUpdateInstanceInputsBuilder definedTags(@NotNull final String definedTags) {
            this.definedTags = definedTags;
            return this;
        }

        @NotNull
        public OCIUpdateInstanceInputs.OCIUpdateInstanceInputsBuilder displayName(@NotNull final String displayName) {
            this.displayName = displayName;
            return this;
        }

        @NotNull
        public OCIUpdateInstanceInputs.OCIUpdateInstanceInputsBuilder freeformTags(@NotNull final String freeformTags) {
            this.freeformTags = freeformTags;
            return this;
        }

        @NotNull
        public OCIUpdateInstanceInputs.OCIUpdateInstanceInputsBuilder shape(@NotNull final String shape) {
            this.shape = shape;
            return this;
        }

        @NotNull
        public OCIUpdateInstanceInputs.OCIUpdateInstanceInputsBuilder ocpus(@NotNull final String ocpus) {
            this.ocpus = ocpus;
            return this;
        }

        @NotNull
        public OCIUpdateInstanceInputs.OCIUpdateInstanceInputsBuilder commonInputs(@NotNull final OCICommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public OCIUpdateInstanceInputs build() {
            return new OCIUpdateInstanceInputs(isManagementDisabled, isMonitoringDisabled, definedTags, displayName, freeformTags, shape, ocpus, commonInputs);

        }
    }
}
