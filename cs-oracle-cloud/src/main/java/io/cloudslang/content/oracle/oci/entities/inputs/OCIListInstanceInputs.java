package io.cloudslang.content.oracle.oci.entities.inputs;

import org.jetbrains.annotations.NotNull;

public class OCIListInstanceInputs {

    private final String displayName;
    private final String lifecycleState;
    private final String sortBy;
    private final String sortOrder;
    private final OCICommonInputs commonInputs;

    @java.beans.ConstructorProperties({"displayName", "lifecycleState", "sortBy", "sortOrder", "commonInputs"})
    private OCIListInstanceInputs(String displayName, String lifecycleState, String sortBy, String sortOrder, OCICommonInputs commonInputs) {
        this.displayName = displayName;
        this.lifecycleState = lifecycleState;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static OCIListInstanceInputs.OCIListInstanceInputsBuilder builder() {
        return new OCIListInstanceInputs.OCIListInstanceInputsBuilder();
    }

    @NotNull
    public String getDisplayName() {
        return displayName;
    }

    @NotNull
    public String getLifecycleState() {
        return lifecycleState;
    }

    @NotNull
    public String getSortBy() {
        return sortBy;
    }

    @NotNull
    public String getSortOrder() {
        return sortOrder;
    }

    @NotNull
    public OCICommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static final class OCIListInstanceInputsBuilder {
        private String displayName;
        private String lifecycleState;
        private String sortBy;
        private String sortOrder;
        private OCICommonInputs commonInputs;

        private OCIListInstanceInputsBuilder() {
        }


        public OCIListInstanceInputsBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public OCIListInstanceInputsBuilder lifecycleState(String lifecycleState) {
            this.lifecycleState = lifecycleState;
            return this;
        }

        public OCIListInstanceInputsBuilder sortBy(String sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        public OCIListInstanceInputsBuilder sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public OCIListInstanceInputsBuilder commonInputs(OCICommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public OCIListInstanceInputs build() {
            return new OCIListInstanceInputs(displayName, lifecycleState, sortBy, sortOrder, commonInputs);

        }
    }
}
